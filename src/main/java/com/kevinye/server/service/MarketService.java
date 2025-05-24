package com.kevinye.server.service;

import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.VO.GoodVO;
import com.kevinye.pojo.VO.MarketVO;

import java.util.List;

public interface MarketService {
    MarketVO getMarketByAuditorId(Integer auditorId);

    List<GoodVO> getGoods4Market(Integer marketId, Integer status, Integer period);

    List<Market> getAllMarket();
}
