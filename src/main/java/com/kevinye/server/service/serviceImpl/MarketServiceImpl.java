package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.Entity.Storage;
import com.kevinye.pojo.VO.GoodVO;
import com.kevinye.pojo.VO.MarketVO;
import com.kevinye.pojo.constant.PeriodConstant;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.server.service.MarketService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarketServiceImpl implements MarketService {

    private final MarketMapper marketMapper;
    public MarketServiceImpl(MarketMapper marketMapper) {
        this.marketMapper = marketMapper;
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
}
