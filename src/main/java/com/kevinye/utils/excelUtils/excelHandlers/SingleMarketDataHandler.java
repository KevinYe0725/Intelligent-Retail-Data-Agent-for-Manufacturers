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
public class SingleMarketDataHandler implements ExcelHandler {
    private final MarketMapper marketMapper;
    private final GoodMapper goodMapper;
    public SingleMarketDataHandler(MarketMapper marketMapper,GoodMapper goodMapper) {
        this.marketMapper = marketMapper;
        this.goodMapper = goodMapper;
    }
    @Override
    public void handleSheet(List<List<String>> sheet) {
        List<String> rowFirst = sheet.getFirst();
        String MarketName = rowFirst.getFirst();
        List<Market> market = marketMapper.getMarketByName(MarketName);
        Integer marketId = market.getFirst().getId();
        int indexOfDate = rowFirst.indexOf(MarketDataConstant.DATE);
        int indexOfInitialGoods = rowFirst.indexOf(MarketDataConstant.INITIAL_GOODS);
        int indexOfGoodName = rowFirst.indexOf(MarketDataConstant.Good_NAME);
        List<String> rowSecond = sheet.get(1);
        String s = rowSecond.get(indexOfDate);
        LocalDate date = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        date = LocalDate.parse(date.format(formatter), formatter);
        List<Storage> storageList = new ArrayList<>();
        for(int i = 1; i < sheet.size(); i++ ) {
            List<String> row = sheet.get(i);
            String goodName = row.get(indexOfGoodName);
            Good good = goodMapper.getGoodByName(goodName);
            if ( good== null) {
                throw new RuntimeException("存在菜品不存在");
            }
            Integer initialGoods = Integer.parseInt(row.get(indexOfInitialGoods));
            Storage storage = new Storage(
                    marketId,
                    good.getGoodId(),
                    initialGoods,
                    date
            );
            storageList.add(storage);
        }
        marketMapper.importNewStorage(storageList);
    }
}
