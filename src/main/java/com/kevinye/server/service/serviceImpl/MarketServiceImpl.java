package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.DTO.GoodDataDTO;
import com.kevinye.pojo.Entity.*;
import com.kevinye.pojo.VO.GoodVO;
import com.kevinye.pojo.VO.GoodsVO;
import com.kevinye.pojo.VO.MarketVO;
import com.kevinye.pojo.VO.WarningVO;
import com.kevinye.pojo.constant.PeriodConstant;
import com.kevinye.server.mapper.GoodMapper;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.server.mapper.TimeMapper;
import com.kevinye.server.service.DataService;
import com.kevinye.server.service.MarketService;
import com.kevinye.server.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MarketServiceImpl implements MarketService {

    @Autowired
    private  MarketMapper marketMapper;
    @Autowired
    private  GoodMapper goodMapper;
    @Autowired
    private  TimeMapper timeMapper;
    @Autowired
    private DataService dataService;

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
                if (storage.getNoonGoods() == null) {
                    GoodVO goodVO = new GoodVO(storage.getGoodId(),storage.getGoodName(),storage.getInitialGoods(),storage.getStatus(),storage.getImage());
                    goodVOList.add(goodVO);
                }
                GoodVO goodVO = new GoodVO(storage.getGoodId(),storage.getGoodName(),storage.getNoonGoods(),storage.getStatus(),storage.getImage());
                goodVOList.add(goodVO);
            }
        }else if(period.equals(PeriodConstant.AFTERNOON)){
            for (Storage storage : storages) {
                if (storage.getAfternoonGoods() == null) {
                    GoodVO goodVO = new GoodVO(storage.getGoodId(),storage.getGoodName(),storage.getNoonGoods(),storage.getStatus(),storage.getImage());
                    goodVOList.add(goodVO);
                }
                GoodVO goodVO = new GoodVO(storage.getGoodId(),storage.getGoodName(),storage.getAfternoonGoods(),storage.getStatus(),storage.getImage());
                goodVOList.add(goodVO);
            }
        }else if(period.equals(PeriodConstant.NIGHT)){
            for (Storage storage : storages) {
                if(storage.getNightGoods()==null) {
                    GoodVO goodVO = new GoodVO(storage.getGoodId(),storage.getGoodName(),storage.getAfternoonGoods(),storage.getStatus(),storage.getImage());
                    goodVOList.add(goodVO);
                }
                GoodVO goodVO = new GoodVO(storage.getGoodId(),storage.getGoodName(),storage.getNightGoods(),storage.getStatus(),storage.getImage());
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
            throw new RuntimeException("该商品不存在，请去商品管理界面添加");
        }
    }

    @Override
    public void updateGoodInformation(GoodDataDTO goodDataDTO) {
        if(goodDataDTO.getInitialGoods()==null){
            throw new RuntimeException("订单不可为空");
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

    @Override
    public List<Problem> getProblems(Integer marketId) {
        return marketMapper.getProblemStorage4Market(marketId);
    }

    @Override
    public Market getMarketById(Integer marketId) {

        return marketMapper.getMarketById(marketId);
    }

    @Override
    public List<WarningVO> getWarningList(Integer marketId, LocalDate date) {
        //flag检测是否为当天
        int flag = 0;
        List<GoodData> problemData4Market = dataService.getProblemData4Market(date, marketId);
        log.error("problemData4Market={}",problemData4Market);

        if(date.equals(LocalDate.now()) ){
            flag = 1;

        }
        LocalTime now = LocalTime.now();
        PeriodSetting periodSetting = timeMapper.getPeriodSetting();
        List<WarningVO> warningGoods = marketMapper.getWarningGoods(marketId, date);
        if(warningGoods.isEmpty()){
            return new ArrayList<>();
        }
        for (WarningVO warningGood : warningGoods) {
            LocalDate beginDate = date.minusDays(6);
            Integer recentTimes = marketMapper.getCount(warningGood.getGoodId(), beginDate, date, marketId);
            warningGood.setRecentTimes(recentTimes);
            Integer remaining = null;
            String per = null;
            //是当天
            if(flag == 1){
                if(now.isBefore(periodSetting.getEndNoonTime())){
                    break;
                }else if (now.isBefore(periodSetting.getStartAfternoonTime() )) {
                    remaining = marketMapper.getNoonRemaining(warningGood.getStorageId());
                    per = "中午";
                } else if (now.isBefore(periodSetting.getStartNightTime() )) {
                    remaining = marketMapper.getAfterNoonRemaining(warningGood.getStorageId());
                    per = "下午";
                    if(remaining == null){
                        remaining = marketMapper.getNoonRemaining(warningGood.getStorageId());
                    }
                } else{
                    remaining = marketMapper.getNightRemaining(warningGood.getStorageId());
                    per = "";
                    if(remaining == null){
                        remaining = marketMapper.getAfterNoonRemaining(warningGood.getStorageId());
                    }
                    if(remaining == null){
                        remaining = marketMapper.getNoonRemaining(warningGood.getStorageId());
                    }
                }
            }else {
                    remaining = marketMapper.getNightRemaining(warningGood.getStorageId());
                    per = "";
                    if(remaining == null){
                        remaining = marketMapper.getAfterNoonRemaining(warningGood.getStorageId());

                    }
                    if(remaining == null){
                        remaining = marketMapper.getNoonRemaining(warningGood.getStorageId());
                    }

            }
            Double rate = (double)remaining/(double)warningGood.getInitialGoods();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

            String time = date.format(formatter)+" "+per;
            warningGood.setTime(time);
            warningGood.setRemainingRate(rate);
        }
        return warningGoods;

    }

    @Override
    public List<MarketData> getMarketDataOfDate(Integer marketId, LocalDate date) {
        if (marketId == null||date==null) {
            throw new RuntimeException("不得有数据为空");
        }
        return marketMapper.getMarketDataOfDate(marketId, date);
    }

    @Override
    public List<DayData> getMonthData(Integer marketId, LocalDate date, Integer goodId) {

        LocalDate beginDate = date.minusMonths(1).withDayOfMonth(1);
        return marketMapper.getMonthData(beginDate,date,marketId,goodId);
    }

    public Integer getRemainingPeriod(LocalTime now) {
        PeriodSetting periodSetting = timeMapper.getPeriodSetting();
        if (now.isAfter(periodSetting.getEndNightTime())){
            return PeriodConstant.NIGHT;
        }else if (now.isBefore(periodSetting.getEndAfternoonTime())){
            return PeriodConstant.AFTERNOON;
        }else if (now.isBefore(periodSetting.getEndNoonTime())){
            return PeriodConstant.NOON;
        }
        return -1;
    }

}
