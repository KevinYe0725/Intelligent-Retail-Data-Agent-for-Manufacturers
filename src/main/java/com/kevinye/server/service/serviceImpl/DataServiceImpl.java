package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.*;
import com.kevinye.pojo.VO.RecommendVO;
import com.kevinye.pojo.constant.WarningLineConstant;
import com.kevinye.server.mapper.DataMapper;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.server.service.DataService;
import com.kevinye.server.service.TimeService;
import com.kevinye.utils.algorithm.MovingAverageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private TimeService timeService;
    @Autowired
    private MovingAverageUtil movingAverageUtil;
    @Autowired
    private MarketMapper marketMapper;


    @Override
    public List<GoodData> getData4Market(Integer marketId, LocalDate date) {
        return dataMapper.getData4Market(marketId, date);
    }

    @Override
    public List<GoodData> getProblemData4Market(LocalDate date, Integer marketId) {
        WarningLine problemLines = dataMapper.getProblemLine(marketId, date);
        if (problemLines==null||problemLines.getNoonWarningLine()==null||problemLines.getAfternoonWarningLine()==null||problemLines.getNightWaringLine()==null) {
            problemLines = new WarningLine(WarningLineConstant.NOON_WARNING_LINE,WarningLineConstant.AFTER_WARNING_LINE,WarningLineConstant.NOON_WARNING_LINE);
        }
        LocalDateTime now = LocalDateTime.now();
        LocalTime nowTime = now.toLocalTime();
        double noonWarningLine =  problemLines.getNoonWarningLine();
        double afternoonWarningLine = problemLines.getAfternoonWarningLine();
        double nightWarningLine = problemLines.getNightWaringLine();
        PeriodSetting timeSetting = timeService.getTimeSetting();
        LocalTime endNoonTime = timeSetting.getEndNoonTime();
        LocalTime endAfternoonTime = timeSetting.getEndAfternoonTime();
        LocalTime endNightTime = timeSetting.getEndNightTime();
        Integer period = timeService.getTimePeriod(now);
        List<GoodData> problemList = new ArrayList<>();
        List<GoodData> goodDataList = dataMapper.getData4Market(marketId, date);
        if(date.equals(LocalDate.now())){
            //假如晚于中午结束，早于下午结束，就使用中午警告
            if(nowTime.isAfter(endNoonTime)&&nowTime.isBefore(endAfternoonTime)){
                for (GoodData goodData : goodDataList) {
                    if (goodData.getNoonGoods()<=goodData.getInitialGoods()*noonWarningLine ) {
                        problemList.add(goodData);
                    }
                }
                //到下午结束之后，使用下午警告
            }else if (nowTime.isAfter(endAfternoonTime) && nowTime.isBefore(endNightTime)) {
                for (GoodData goodData : goodDataList) {
                    if (goodData.getAfternoonGoods()<=goodData.getInitialGoods()*afternoonWarningLine ) {
                        problemList.add(goodData);
                    }
                }
                //晚上结束之后，使用晚上警告
            }else if(nowTime.isAfter(endNightTime)) {
                for (GoodData goodData : goodDataList) {
                    if (goodData.getNightGoods()<=goodData.getInitialGoods()*nightWarningLine ) {
                        problemList.add(goodData);
                    }
                }
            }
        }else {
            for (GoodData goodData : goodDataList) {
                if (goodData.getNightGoods()<=goodData.getInitialGoods()*nightWarningLine ) {
                    problemList.add(goodData);
                }
            }
        }
        dataMapper.refreshWarningStatus(marketId,date);
            if(!problemList.isEmpty()){

                dataMapper.updateWarningStatus(problemList,date,marketId);
            }
            return problemList;
    }

    @Override
    public List<RecommendVO> getRecommendsByIds(List<Integer> ids, LocalDate date, Integer marketId) {
        List<RecommendVO> recommendVOS = movingAverageUtil.recommendOrders(marketId, date);
        List<RecommendVO> result = new ArrayList<>();
        for (RecommendVO recommendVO : recommendVOS) {
            if (ids.contains(recommendVO.getGoodId())){
                result.add(recommendVO);
            }
        }
        return result;
    }

    @Override
    public List<Market> getAllMarkets(String marketName) {
        return marketMapper.getMarketByName(marketName);
    }

    @Override
    public Market getMarketById(Integer id) {
        return marketMapper.getMarketById(id);
    }

    @Override
    public void addNewMarket(Market market) {
        marketMapper.addNewMarket(market);
    }

    @Override
    public void deleteMarkets(List<Integer> ids) {
        List<Integer> information4Market = marketMapper.getInformation4Market(ids);
        for (Integer number : information4Market) {
            if(number != 0){
                throw new RuntimeException("有历史数据的market不可删除");
            }
        }
        marketMapper.deleteMarkets(ids);
    }

    @Override
    public void updateMarket(Market market) {
        dataMapper.updateMarket(market);
    }

    @Override
    public List<MarketInfo> getAllMarketsInfo(LocalDate date) {
        List<MarketInfo> allMarketsInfo = dataMapper.getAllMarketsInfo(date);
        allMarketsInfo.sort((o1, o2) -> (o2.getProblemStatus()+o2.getWarningStatus())-(o1.getProblemStatus()+o1.getWarningStatus()));
        allMarketsInfo.forEach(o -> {
            o.setProblemStatus(o.getProblemStatus()==0?0:1);
            o.setWarningStatus(o.getWarningStatus()==0?0:1);
        });
        return allMarketsInfo;
    }
}

