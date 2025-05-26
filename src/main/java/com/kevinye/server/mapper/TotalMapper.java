package com.kevinye.server.mapper;

import com.kevinye.pojo.VO.TopGoodVO;
import com.kevinye.pojo.VO.TopMarketVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TotalMapper {


    List<TopGoodVO> getTopGoodList(LocalDate beginDate, LocalDate endDate);

    List<TopMarketVO> getTopMarketList(LocalDate beginDate, LocalDate endDate);
}
