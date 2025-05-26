package com.kevinye.utils.excelUtils.excelHandlers;

import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.Entity.Storage;
import com.kevinye.pojo.constant.MarketDataConstant;
import com.kevinye.server.mapper.GoodMapper;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.utils.excelUtils.ExcelHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
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
        LocalDate date = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
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
                throw new RuntimeException("存在商家不存在");
            }
            List<Storage> storageList4Market = new ArrayList<>();
            for (int j = 1; j < row.size()-1; j++) {
                if(row.get(j) == null|| row.get(j).isEmpty()) {
                    continue;
                }
                int initialGoods = Integer.parseInt(row.get(j));
                Integer goodId = goodIds.get(j);
                Storage storage = new Storage(marketFirst.getId(),goodId,initialGoods,date);
                storageList4Market.add(storage);
            }
            marketMapper.importNewStorage(storageList4Market);
        }
    }
}
