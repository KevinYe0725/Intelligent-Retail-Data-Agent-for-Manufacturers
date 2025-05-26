package com.kevinye.server.service;

import com.kevinye.pojo.Entity.GoodData;
import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.VO.RecommendVO;

import java.time.LocalDate;
import java.util.List;

public interface DataService {
    List<GoodData> getData4Market(Integer marketId, LocalDate date);


    List<GoodData> getProblemData4Market(LocalDate date, Integer marketId);

    List<RecommendVO> getRecommendsByIds(List<Integer> ids, LocalDate date, Integer marketId);

    List<Market> getAllMarkets(String marketName);

    Market getMarketById(Integer id);

    void addNewMarket(Market market);

    void deleteMarkets(List<Integer> ids);

    void updateMarket(Market market);
}
