package com.kevinye.utils.algorithm;

import com.kevinye.pojo.Entity.GoodData;
import com.kevinye.pojo.VO.RecommendVO;
import com.kevinye.pojo.constant.RecommendConstant;
import com.kevinye.pojo.constant.WarningLineConstant;
import com.kevinye.server.mapper.DataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class MovingAverageUtil {
    private final DataMapper dataMapper;
    public MovingAverageUtil(DataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    /**
     * 获取到推荐的订单
     * @param marketId 商店ID
     * @param endDate 选择查看的日期（即为计算的结束日期）
     * @return 选择日期的recommend订单量，内包含对应品类预估值
     */
    public  List<RecommendVO> recommendOrders(Integer marketId, LocalDate endDate) {
        //获取前七天的货品数据
        //可不可能出现七天货品都为空的状态？
        //只有可能是初始化的时候，不是，你连货品都没有，要什么推荐？
        LocalDate beginDate = endDate.minusDays(6);
        List<GoodData> goodDataList = dataMapper.selectAllData4Market(marketId,beginDate,endDate);
        List<GoodData> nowDayData = dataMapper.getData4Market(marketId,endDate);
        Map<Integer,List<GoodData>>goods = new HashMap<>();
        initialMap(goodDataList, goods);
        List<RecommendVO> answer = new ArrayList<>();
        //遍历这个商店里所有的Good
        if(goods.isEmpty()){
            return answer;
        }
        goods.forEach((goodId,goodsData)->{
            //假如说这是一个加的货品或者前一天有货品没有更新factor，保证其健壮性
            Double oldFactor = dataMapper.getFactor(endDate.minusDays(1),marketId,goodId);
            if(oldFactor==null){
                oldFactor= RecommendConstant.DEFAULT_FACTOR;
            }
            double factor = SingleFactorParam(goodsData,oldFactor,marketId,endDate,goodId);
            GoodData nowDayDatum = null;
            for (GoodData nowDayGood : nowDayData) {
                if(nowDayGood.getGoodId().equals(goodId)){
                    nowDayDatum = nowDayGood;
                }
            }
            if(nowDayDatum!=null){
                double rawValue = nowDayDatum.getInitialGoods() / oldFactor * factor;
                int recommendGoods = (int) Math.floor(rawValue);
                RecommendVO recommendVO = new RecommendVO(nowDayDatum.getGoodId(),nowDayDatum.getGoodName(),nowDayDatum.getInitialGoods(), recommendGoods);
                answer.add(recommendVO);
            }
        });
//        //前七天的数据都为空
//        if(goods.isEmpty()){
//            dataMapper.selectAllFactors4Markets(marketId,endDate.minusDays(1));
//            dataMapper.updateAllFactors4Market(marketId,endDate);
//        }


        return answer;
    }

    /**
     * 初始化Map，key = goodId value = 对应ID的goodData
     * @param goodDataList 货品原始数据
     * @param goods  Map容器
     */
    private void initialMap(List<GoodData> goodDataList, Map<Integer, List<GoodData>> goods) {
        for (GoodData goodData : goodDataList) {
            if(goods.containsKey(goodData.getGoodId())){
                goods.get(goodData.getGoodId()).add(goodData);
            }else {
                goods.put(goodData.getGoodId(),new ArrayList<>());
                goods.get(goodData.getGoodId()).add(goodData);
            }
        }
    }

    /**
     * 获取到因数参数（同时也会把它存到数据库实现持久化）
     * @param goodDataList 货品数据list
     * @param oldFactor 上一次factor
     * @param marketId 商店ID
     * @param now 当前选择时间
     * @return 推荐的安全系数计算
     */
    public  Double SingleFactorParam(List<GoodData> goodDataList,double oldFactor,int marketId,LocalDate now,Integer goodId) {
        int [][]stock = new int[7][4];
        int i = 0;
        log.error("goodDataList:{}",goodDataList);
        extracted(goodDataList, stock, i);
        double factor = oldFactor;
        double weightSum = 0;
        double[] weight = {1.0 ,0.6 ,0.2,0.5};
        int invalidCount = 0;
        for (int[] goods : stock) {
            if(goods[1]==-1||goods[2]==-1||goods[3]==-1){
                invalidCount++;
                continue;
            }
            weightSum+=goods[1]==0?weight[0]:0;
            weightSum+=goods[2]==0?weight[1]:0;
            weightSum+=goods[3]==0?weight[2]:0;
            weightSum-=((double) (goods[0] - goods[3]) /goods[0])>=(factor-1)?weight[3]:0;
        }
         now = now.plusDays(1);
        log.error("更新了factors"+weightSum);
        if(invalidCount<7){
            double weightedOutOfStockRate = weightSum / (7-invalidCount);

            if (weightedOutOfStockRate >= 0.6){
                factor+=0.05;
            }else if (weightedOutOfStockRate >= 0.2){
                factor+=0.02;
            } else if (weightedOutOfStockRate <= 0) {
                factor-=0.1;
            }

            dataMapper.updateFactor(factor,marketId,now,goodId);
            return factor;
        }else {
            factor = RecommendConstant.DEFAULT_FACTOR;
            dataMapper.updateFactor(factor,marketId,now,goodId);
            return factor;
        }

    }

    /**
     * 更新明天的警戒线
     * @param marketId 商店Id
     * @param now 选择数据结束
     */
    public void updateWarningLine(int marketId,LocalDate now){
        LocalDate begin = now.minusDays(29);
        Map<Integer,List<GoodData>>goods = new HashMap<>();
        //获取前三十天的货品数据
        List<GoodData> goodDataList = dataMapper.selectAllData4Market(marketId, begin, now);
        //initialMap将过去30天所有的品类都放进去，都会被更新
        initialMap(goodDataList, goods);
        //将对该商店里的每一个商品的警戒线进行更新
        goods.forEach((goodId,goodsList) ->{
            int size = goodsList.size();
            int[][] stocks = new int[size][4];
            extracted(goodsList,stocks,0);
            updateSingleOne(marketId, now, stocks,goodId);
        });
    }

    /**
     * 更新单个id的line 是第二天的
     * @param marketId 商店ID ，用于定位
     * @param now 数据结束时间，用于定位（+1天）
     * @param stocks 单个ID所有的数据
     * @param goodId goodId
     */

    //更新某商店单品类某一天的警戒线
    //定义有效库存：在当日最后一次统计之后，从initialGoods到nightGoods
    private void updateSingleOne(int marketId, LocalDate now,int[][] stocks ,int goodId) {
        //这个货品前三十天无数据（在这里不可能出现，没有该货品不会加到stocks）
        if (stocks == null || stocks.length == 0) {
            return;
        }

        // 1. 累加过去几天的数据
        double initialSum    = 0;
        double noonSum       = 0;
        double afternoonSum  = 0;
        double nightSum      = 0;
        double noonWarnAccum = 0;
        double afterWarnAccum= 0;
        int    countZeroNight= 0;
        int  invalidCount = 0;
        for (int[] goods : stocks) {
            // goods = {initialGoods, noonGoods, afternoonGoods, nightGoods}
            //假如有库存不存在，那么就跳过这一组
            if(goods[0]==-1||goods[1]==-1||goods[2]==-1||goods[3]==-1){
                invalidCount++;
                continue;
            }
            initialSum    += goods[0];
            noonSum       += goods[1];
            afternoonSum  += goods[2];
            nightSum      += goods[3];

            if (goods[3] == 0) {
                // 如果某天夜间就已经缺货，累计那天的午间和下午库存
                noonWarnAccum     += goods[1];
                afterWarnAccum    += goods[2];
                countZeroNight++;
            }
        }

        // 2. 先算出用于计算百分比的基准：这里示例用“最后一天”库存的 initialGoods
        //    如果 stocks 中最后一个元素就是“今天”的库存，那它的 goods[0] 就是 todayInitial
        int lastIndex = stocks.length - 1;
        double todayInitial = (stocks[lastIndex][0] > 0 ? stocks[lastIndex][0] : 1.0);
        // 如果你要用“过去 days 天平均初始库存”做基准：
        // double avgInitial = initialSum / stocks.length;
        // double todayInitial = (avgInitial > 0 ? avgInitial : 1.0);

        // 3. 计算绝对值警戒线（根据是否有库存零）
        double noonWarningLineAbs;
        double afterWarningLineAbs;
        double noonWarningRateAbs;      // 0 ~ +∞ 小数
        double afterWarningRateAbs;
        double nightWarningRateAbs;

        if (countZeroNight > 0) {
            noonWarningLineAbs  = noonWarnAccum / countZeroNight;
            afterWarningLineAbs = afterWarnAccum / countZeroNight;
            noonWarningRateAbs = noonWarningLineAbs / todayInitial;        // 0 ~ +∞ 小数
            afterWarningRateAbs= afterWarningLineAbs / todayInitial;
        } else {
            // 如果过去天里没一次夜间缺货，就用均值法算绝对警戒线
            //days-invalidCount =   真实有效的天数
            double days = stocks.length-invalidCount;
            if (days > 0) {
                //initialSum:有效库存的总和
                double avgInitial = initialSum / days;
                double avgNight   = nightSum / days;
                //其实没必要每次结束后更新，最好是每天结束后更新，保证这个晚上剩余存在
                //计算方式：平均初始库存-平均晚上库存
                //计算每日平均消费比例
                double averageOver = avgInitial - avgNight;
                double rate = (averageOver > 0 ? (averageOver / avgInitial) : 1.0);
                //此为假设
                noonWarningLineAbs  = noonSum / rate ;
                afterWarningLineAbs = afternoonSum / rate ;
                noonWarningRateAbs = noonWarningLineAbs / todayInitial;        // 0 ~ +∞ 小数
                afterWarningRateAbs= afterWarningLineAbs / todayInitial;

            }else{
                noonWarningRateAbs= WarningLineConstant.NOON_WARNING_LINE;
                afterWarningRateAbs= WarningLineConstant.AFTER_WARNING_LINE;
            }

        }
        // 夜间警戒线：可以直接取当日夜间均值，或者和以上逻辑保持一致
        // 4. **把绝对值警戒线转成“百分比”**
        //    比如 “午间警戒率” = noonWarningLineAbs / todayInitial
        //    如果想要存储成 0~1 之间的小数，就直接存这个值；
        //    如果想要存成 0~100 之间的百分数，就 ×100。
         nightWarningRateAbs=WarningLineConstant.NIGHT_WARNING_LINE;
        // 5. 将日期挪到“明天”
        LocalDate nextDay = now.plusDays(1);

        // 6. 持久化“百分比”警戒线
        //    这里假设数据库中 warning_line 表的三个字段类型都是 DOUBLE，
        //    并且我们想存“百分比”数值（如 20.5 代表 20.5%），
        //    那就把上面三个变量直接传给 Mapper：
        if (dataMapper.getWarningLine(nextDay, marketId, goodId) == null) {
            // 新增一行，插入百分比
            dataMapper.insertWarningLine(
                    noonWarningRateAbs,
                    afterWarningRateAbs,
                    nightWarningRateAbs,
                    nextDay,
                    marketId,
                    goodId
            );
        } else {
            // 更新已有行，保存新的百分比
            dataMapper.updateWarningLine(
                    noonWarningRateAbs,
                    afterWarningRateAbs,
                    nightWarningRateAbs,
                    nextDay,
                    marketId,
                    goodId
            );
        }
    }

    /**
     * 将List数据注入到stock中
     * @param goodDataList 货品数据
     * @param stock 需求数组
     * @param i 计数器
     */
    private static void extracted(List<GoodData> goodDataList, int[][] stock, int i) {
        for (int[] ints : stock) {
            Arrays.fill(ints, -1);
        }
        for (GoodData goodData : goodDataList) {
            stock[i][0] = goodData.getInitialGoods()==null?-1:goodData.getInitialGoods();
            stock[i][1] = goodData.getNoonGoods()==null?-1:goodData.getNoonGoods();
            stock[i][2] = goodData.getAfternoonGoods()==null?-1:goodData.getAfternoonGoods();
            stock[i++][3] = goodData.getNightGoods()==null?-1:goodData.getNightGoods();
        }
        log.error("STOCK：{}",Arrays.deepToString(stock));
    }

}
