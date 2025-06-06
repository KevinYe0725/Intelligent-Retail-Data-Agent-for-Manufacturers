package com.kevinye.utils.excelUtils.excelHandlers;

import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.Entity.Storage;
import com.kevinye.pojo.constant.MarketDataConstant;
import com.kevinye.server.mapper.GoodMapper;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.utils.excelUtils.ExcelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class StorageExcelHandler implements ExcelHandler {
    private final MarketMapper marketMapper;
    private final GoodMapper goodMapper;

    public StorageExcelHandler(MarketMapper marketMapper, GoodMapper goodMapper) {
        this.marketMapper = marketMapper;
        this.goodMapper = goodMapper;
    }
    @Override
    public void handleSheet(List<List<String>> sheet) {
        List<String> rowFirst = sheet.getFirst();
        List<Integer> goodIds = new ArrayList<>();
        int indexOfDate = rowFirst.indexOf(MarketDataConstant.DATE);
        List<String> rowSecond = sheet.get(1);
        String s = rowSecond.get(indexOfDate);
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy.M.d");
        LocalDate date = LocalDate.parse(s, parser);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date = LocalDate.parse(date.format(formatter), formatter);
        for(int i = 1; i < rowFirst.size()-1; i++) {
            String GoodName = rowFirst.get(i);
            Good good = goodMapper.getGoodByName(GoodName);
            if(good == null) {
                throw new RuntimeException("存在不存在商品");
            }
            goodIds.add(good.getGoodId());
        }
        for(int i = 1; i < sheet.size(); i++) {
            List<String> row = sheet.get(i);
            String MarketName = row.getFirst();
            List<Market> market = marketMapper.getMarketByName(MarketName);
            Market marketFirst = market.getFirst();
            if(market.isEmpty()) {
                throw new RuntimeException(marketFirst.getMarketName()+"不存在");
            }
            List<Storage> storage4Market = marketMapper.getStorage4Market(market.getFirst().getId(), date);

            List<Storage> storageList4Market = new ArrayList<>();
            for (int j = 1; j < row.size()-2; j++) {
                if(row.get(j) == null|| row.get(j).isEmpty()) {
                    continue;
                }
                int initialGoods = Integer.parseInt(row.get(j));
                Integer goodId = goodIds.get(j);
                Storage storage = new Storage(marketFirst.getId(),goodId,initialGoods,date);
                int flag = 0;
                if(!storage4Market.isEmpty()) {
                    for (Storage storage1 : storage4Market) {
                        log.info("storage1:{},storage:{} ",storage1,storage);
                        if (storage1.getGoodId().equals(storage.getGoodId())) {
                            storage.setId(storage1.getId());
                            if (!storage1.getInitialGoods().equals(storage.getInitialGoods())) {
                                marketMapper.updateInitialGoods(storage);
                            }
                            flag = 1;
                        }
                    }
                }
                if(flag == 1) {
                    continue;
                }
                storageList4Market.add(storage);
            }

            if(!storageList4Market.isEmpty()) {
                marketMapper.importNewStorage(storageList4Market);
            }

        }
    }
}
