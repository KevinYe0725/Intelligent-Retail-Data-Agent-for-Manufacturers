package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.DayData;
import com.kevinye.pojo.Entity.MonthData;
import com.kevinye.pojo.VO.TopGoodVO;
import com.kevinye.pojo.VO.TopMarketVO;
import com.kevinye.server.mapper.TotalMapper;
import com.kevinye.server.service.TotalService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
public class TotalServiceImpl implements TotalService {
    private final TotalMapper totalMapper;
    public TotalServiceImpl(TotalMapper totalMapper) {
        this.totalMapper = totalMapper;
    }

    @Override
    public List<TopGoodVO> getSortedGoodByNumber(LocalDate date) {
        LocalDate beginDate = date.minusDays(30);
        return totalMapper.getTopGoodList(beginDate, date);
    }

    @Override
    public List<TopMarketVO> getSortedMarketByNumber(LocalDate date) {
        LocalDate beginDate = date.minusDays(30);
        return totalMapper.getTopMarketList(beginDate,date);
    }

    @Override
    public List<DayData> getMonthData(LocalDate date, Integer goodId) {
        LocalDate twoMonthAgoFirstDay = date.minusMonths(1).withDayOfMonth(1);
        return totalMapper.getMonthData(twoMonthAgoFirstDay, date, goodId);
    }

    @Override
    public List<MonthData> getYearData(LocalDate date, Integer goodId) {
        LocalDate twoYearAgoDate = date.minusYears(2);

        return totalMapper.getYearData(twoYearAgoDate,date,goodId);
    }
}
