package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.DTO.GoodDataDTO;
import com.kevinye.pojo.Entity.*;
import com.kevinye.pojo.VO.GoodVO;
import com.kevinye.pojo.VO.GoodsVO;
import com.kevinye.pojo.VO.MarketVO;
import com.kevinye.pojo.constant.PeriodConstant;
import com.kevinye.server.mapper.GoodMapper;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.server.service.MarketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarketServiceImpl implements MarketService {

    private final MarketMapper marketMapper;
    private final GoodMapper goodMapper;
    public MarketServiceImpl(MarketMapper marketMapper, GoodMapper goodMapper) {
        this.marketMapper = marketMapper;
        this.goodMapper = goodMapper;
    }

    @Override
    public MarketVO getMarketByAuditorId(Integer auditorId) {
        if(auditorId == null) {
            throw new RuntimeException("auditorId 不可为空");
        }
        Market market = marketMapper.getMarketByAuditorId(auditorId);
        return new MarketVO(market.getMarketName(),market.getId());
    }

    @Override
    public List<GoodVO> getGoods4Market(Integer marketId, Integer status, Integer period) {
        LocalDate date = LocalDate.now();
        List<Storage> storages;
        if(status == 0) {
            storages = marketMapper.getAllUnfinishedGoods4Market(marketId, status,date);
        }else {
             storages = marketMapper.getAllGoods4Market(marketId, date);
        }
        List<GoodVO> goodVOList = new ArrayList<>();
        if(period.equals(PeriodConstant.NOON)){
            for (Storage storage : storages) {
                GoodVO goodVO = new GoodVO(storage.getGoodId(),storage.getGoodName(),storage.getInitialGoods(),storage.getStatus(),storage.getImage());
                goodVOList.add(goodVO);
            }
        }else if(period.equals(PeriodConstant.AFTERNOON)){
            for (Storage storage : storages) {
                GoodVO goodVO = new GoodVO(storage.getGoodId(),storage.getGoodName(),storage.getNoonGoods(),storage.getStatus(),storage.getImage());
                goodVOList.add(goodVO);
            }
        }else if(period.equals(PeriodConstant.NIGHT)){
            for (Storage storage : storages) {
                GoodVO goodVO = new GoodVO(storage.getGoodId(),storage.getGoodName(),storage.getAfternoonGoods(),storage.getStatus(),storage.getImage());
                goodVOList.add(goodVO);
            }
        }
        return goodVOList;
    }

    /**
     * 获得所有的Market数据
     * @return Market数据
     */
    @Override
    public List<Market> getAllMarket() {
        return marketMapper.getAllMarket();
    }

    @Override
    public List<GoodData> getAllGoods4Market(Integer marketId, LocalDate localDate) {

        List<Storage> allGoods4Market = marketMapper.getAllGoods4Market(marketId, localDate);
        List<GoodData> goodDataList = new ArrayList<>();
        for (Storage storage : allGoods4Market) {
            GoodData goodData = new GoodData(
                    storage.getGoodId(),
                    storage.getGoodName(),
                    storage.getInitialGoods(),
                    storage.getNoonGoods(),
                    storage.getAfternoonGoods(),
                    storage.getNightGoods()
            );
            goodDataList.add(goodData);
        }
        return goodDataList;
    }

    @Override
    public void addNewGood4Market(GoodForMarket goodformarket) {
        if(goodformarket.getInitialGoods()==null){
            throw new RuntimeException("订单量不可为空");
        }
        Good goodByName = goodMapper.getGoodByName(goodformarket.getGoodName());
        if(goodByName != null) {
            goodformarket.setGoodId(goodByName.getGoodId());
            marketMapper.insertGood(goodformarket);
        }else {
            if (goodformarket.getImage()==null){
                throw new RuntimeException("image不可为空");
            }else{
                Good good = new Good(goodformarket.getGoodName(), goodformarket.getImage());
                goodMapper.insertNewGood(good);
                goodByName = goodMapper.getGoodByName(goodformarket.getGoodName());
                goodformarket.setGoodId(goodByName.getGoodId());
                marketMapper.insertGood(goodformarket);
            }
        }
    }

    @Override
    public void updateGoodInformation(GoodDataDTO goodDataDTO) {
        if(goodDataDTO.getInitialGoods()==null){
            throw new RuntimeException("goodDataDTO不可为空");
        }
        marketMapper.updateGoodInformation(goodDataDTO);
    }

    @Transactional
    @Override
    public void deleteGoodInformationFromMarket(Integer marketId, Integer goodId, LocalDate date) {
        if (marketId == null||goodId == null||date==null) {
            throw new RuntimeException("存在数据为空");
        }
        marketMapper.deleteGoodInformationFromMarket(marketId,goodId,date);
    }

    @Override
    public List<GoodsVO> getAllGoods(String goodName) {
        return marketMapper.getAllGoodChoice(goodName);
    }

}
