package com.kevinye.server.service;

import com.kevinye.pojo.DTO.GoodDataDTO;
import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.GoodData;
import com.kevinye.pojo.Entity.GoodForMarket;
import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.VO.GoodVO;
import com.kevinye.pojo.VO.GoodsVO;
import com.kevinye.pojo.VO.MarketVO;

import java.time.LocalDate;
import java.util.List;

public interface MarketService {
    MarketVO getMarketByAuditorId(Integer auditorId);

    List<GoodVO> getGoods4Market(Integer marketId, Integer status, Integer period);

    List<Market> getAllMarket();

    List<GoodData> getAllGoods4Market(Integer marketId, LocalDate localDate);

    void addNewGood4Market(GoodForMarket goodformarket);

    void updateGoodInformation(GoodDataDTO goodDataDTO);

    void deleteGoodInformationFromMarket(Integer marketId, Integer goodId, LocalDate date);

    List<GoodsVO> getAllGoods(String goodName);
}
