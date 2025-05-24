package com.kevinye.utils.algorithm;

import com.kevinye.pojo.Entity.GoodData;
import com.kevinye.pojo.VO.RecommendVO;
import com.kevinye.server.mapper.DataMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
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
        LocalDate beginDate = endDate.minusDays(6);
        List<GoodData> goodDataList = dataMapper.selectAllData4Market(marketId,beginDate,endDate);
        List<GoodData> nowDayData = dataMapper.getData4Market(marketId,endDate);
        Map<Integer,List<GoodData>>goods = new HashMap<>();
        initialMap(goodDataList, goods);
        List<RecommendVO> answer = new ArrayList<>();
        goods.forEach((goodId,goodsData)->{
            double oldFactor = dataMapper.getFactor(endDate.minusDays(1),marketId,goodId);
            double factor = SingleFactorParam(goodsData,oldFactor,marketId,endDate,goodId);
            GoodData nowDayDatum = goodsData.getLast();
            int recommendGoods = Integer.parseInt(String.valueOf(nowDayDatum.getInitialGoods()/oldFactor*factor).split("\\.")[0]);
            RecommendVO recommendVO = new RecommendVO(nowDayDatum.getGoodId(),nowDayDatum.getGoodName(),nowDayDatum.getInitialGoods(), recommendGoods);
            answer.add(recommendVO);
        });

        //获得前一天的factor

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
        extracted(goodDataList, stock, i);
        double factor = oldFactor;
        double weightSum = 0;
        double[] weight = {1.0 ,0.6 ,0.2,0.5};

        for (int[] goods : stock) {
            weightSum+=goods[1]==0?weight[0]:0;
            weightSum+=goods[2]==0?weight[1]:0;
            weightSum+=goods[3]==0?weight[2]:0;
            weightSum-=((double) (goods[0] - goods[3]) /goods[0])>=(factor-1)?weight[3]:0;
        }
        double weightedOutOfStockRate = weightSum / 7;

        if (weightedOutOfStockRate >= 0.6){
            factor+=0.1;
        }else if (weightedOutOfStockRate >= 0.2){
            factor+=0.05;
        } else if (weightedOutOfStockRate <= 0) {
            factor-=0.1;
        }
        dataMapper.updateFactor(factor,marketId,now,goodId);
        return factor;
    }

    /**
     * 更新明天的警戒线
     * @param marketId 商店Id
     * @param now 选择数据结束
     */
    public void updateWarningLine(int marketId,LocalDate now){
        LocalDate begin = now.minusDays(30);
        Map<Integer,List<GoodData>>goods = new HashMap<>();

        List<GoodData> goodDataList = dataMapper.selectAllData4Market(marketId, begin, now);
        initialMap(goodDataList, goods);

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
    private void updateSingleOne(int marketId, LocalDate now,int[][] stocks ,int goodId) {
        double initialGoods = 0;
        double noonGoods = 0;
        double afternoonGoods = 0;
        double noonWarningLine = 0;
        double afternoonWarningLine = 0;
        double nightWarningLine = 0;
        int count = 0;
        for (int[] goods : stocks) {
            initialGoods += goods[0];
            afternoonGoods += goods[2];
            noonGoods += goods[1];
            if (goods[3] == 0) {
                noonWarningLine += goods[1];
                afternoonWarningLine += goods[2];
                count++;
            }
            nightWarningLine += goods[3];
        }
        if (count != 0) {
            noonWarningLine /= count * 1.3;
            afternoonWarningLine /= count * 1.2;
        } else {
            initialGoods /= 31;
            nightWarningLine /= 31;
            double averageOver = initialGoods - nightWarningLine;
            double rate = averageOver / initialGoods;
            noonWarningLine = noonGoods / rate * 1.3;
            afternoonWarningLine = afternoonGoods / rate * 1.2;
        }

        //将原本前一天开始，现在加入当前日期
        now = now.plusDays(1);
        if (dataMapper.getFactor(now, marketId, goodId) == null) {
            dataMapper.insertWarningLine(noonWarningLine, afternoonWarningLine, nightWarningLine, now, marketId,goodId);
        } else {
            dataMapper.updateWarningLine(noonWarningLine, afternoonWarningLine, nightWarningLine, now, marketId,goodId);
        }
    }

    /**
     * 将List数据注入到stock中
     * @param goodDataList 货品数据
     * @param stock 需求数组
     * @param i 计数器
     */
    private static void extracted(List<GoodData> goodDataList, int[][] stock, int i) {
        for (GoodData goodData : goodDataList) {
            stock[i][0] = goodData.getInitialGoods();
            stock[i][1] = goodData.getNoonGoods();
            stock[i][2] = goodData.getAfternoonGoods();
            stock[i++][3] = goodData.getNightGoods();
        }
    }

}
