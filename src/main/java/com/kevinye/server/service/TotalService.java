package com.kevinye.server.service;

import com.kevinye.pojo.Entity.DayData;
import com.kevinye.pojo.Entity.GoodGrowth;
import com.kevinye.pojo.Entity.MarketGrowth;
import com.kevinye.pojo.Entity.MonthData;
import com.kevinye.pojo.VO.TopGoodVO;
import com.kevinye.pojo.VO.TopMarketVO;

import java.time.LocalDate;
import java.util.List;

public interface TotalService {
    List<TopGoodVO> getSortedGoodByNumber(LocalDate date);

    List<TopMarketVO> getSortedMarketByNumber(LocalDate date);

    List<DayData> getMonthData(LocalDate date, Integer goodId);

    List<MonthData> getYearData(LocalDate date, Integer goodId);

    List<MarketGrowth> getGrowthOfMarkets(LocalDate date);

    List<GoodGrowth> getGrowthOfGoods(LocalDate date);
}
