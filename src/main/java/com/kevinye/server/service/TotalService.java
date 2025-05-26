package com.kevinye.server.service;

import com.kevinye.pojo.VO.TopGoodVO;
import com.kevinye.pojo.VO.TopMarketVO;

import java.time.LocalDate;
import java.util.List;

public interface TotalService {
    List<TopGoodVO> getSortedGoodByNumber(LocalDate date);

    List<TopMarketVO> getSortedMarketByNumber(LocalDate date);
}
