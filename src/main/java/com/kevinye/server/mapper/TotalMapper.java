package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.DayData;
import com.kevinye.pojo.Entity.MonthData;
import com.kevinye.pojo.VO.TopGoodVO;
import com.kevinye.pojo.VO.TopMarketVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Mapper
public interface TotalMapper {


    List<TopGoodVO> getTopGoodList(LocalDate beginDate, LocalDate endDate);

    List<TopMarketVO> getTopMarketList(LocalDate beginDate, LocalDate endDate);

    List<DayData> getMonthData(LocalDate beginDate,LocalDate endDate, Integer goodId);

    List<MonthData> getYearData(LocalDate beginDate,LocalDate endDate, Integer goodId);
}
