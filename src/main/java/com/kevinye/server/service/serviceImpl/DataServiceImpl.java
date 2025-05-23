package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.GoodData;
import com.kevinye.pojo.Entity.PeriodSetting;
import com.kevinye.pojo.Entity.WarningLine;
import com.kevinye.pojo.VO.RecommendVO;
import com.kevinye.server.mapper.DataMapper;
import com.kevinye.server.service.DataService;
import com.kevinye.server.service.TimeService;
import com.kevinye.utils.algorithm.MovingAverageUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {
    private  final DataMapper dataMapper;
    private  final TimeService timeService;
    private  final MovingAverageUtil movingAverageUtil;
    public DataServiceImpl(DataMapper dataMapper,TimeService timeService,MovingAverageUtil movingAverageUtil) {
        this.dataMapper = dataMapper;
        this.timeService = timeService;
        this.movingAverageUtil = movingAverageUtil;
    }

    @Override
    public List<GoodData> getData4Market(Integer marketId, LocalDate date) {
        return dataMapper.getData4Market(marketId, date);
    }

    @Override
    public List<GoodData> getProblemData4Market(LocalDate date, Integer marketId) {
        WarningLine problemLines = dataMapper.getProblemLine(marketId, date);
        if (problemLines == null) return Collections.emptyList();
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
        if(period.equals(-1)){
            if(nowTime.isAfter(endNoonTime)&&nowTime.isBefore(endAfternoonTime)){
                for (GoodData goodData : goodDataList) {
                    if (goodData.getNoonGoods()<=noonWarningLine ) {
                        problemList.add(goodData);
                    }
                }
            }else if (nowTime.isAfter(endAfternoonTime) && nowTime.isBefore(endNightTime)) {
                for (GoodData goodData : goodDataList) {
                    if (goodData.getAfternoonGoods()<=afternoonWarningLine ) {
                        problemList.add(goodData);
                    }
                }
            }else{
                for (GoodData goodData : goodDataList) {
                    if (goodData.getNightGoods()<=nightWarningLine ) {
                        problemList.add(goodData);
                    }
                }
            }
            return problemList;
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
}

