package com.kevinye.utils.excelUtils.excelHandlers;

import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.Entity.Storage;
import com.kevinye.pojo.constant.MarketDataConstant;
import com.kevinye.server.mapper.DataMapper;
import com.kevinye.server.mapper.GoodMapper;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.utils.excelUtils.ExcelHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class SingleMarketDataHandler implements ExcelHandler {
    private final MarketMapper marketMapper;
    private final DataMapper dataMapper;
    private final GoodMapper goodMapper;
    public SingleMarketDataHandler(MarketMapper marketMapper,GoodMapper goodMapper,DataMapper dataMapper) {
        this.marketMapper = marketMapper;
        this.goodMapper = goodMapper;
        this.dataMapper = dataMapper;
    }
    @Override
    public void handleSheet(List<List<String>> sheet) {
        List<String> rowFirst = sheet.getFirst();
        String MarketName = sheet.get(1).getFirst();
        List<Market> market = marketMapper.getMarketByName(MarketName);
        Integer marketId = market.getFirst().getId();
        int indexOfDate = rowFirst.indexOf(MarketDataConstant.DATE);
        int indexOfInitialGoods = rowFirst.indexOf(MarketDataConstant.INITIAL_GOODS);
        int indexOfGoodName = rowFirst.indexOf(MarketDataConstant.Good_NAME);
        List<String> rowSecond = sheet.get(1);
        String s = rowSecond.get(indexOfDate);
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy.M.d");
        LocalDate date = LocalDate.parse(s, parser);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date = LocalDate.parse(date.format(formatter), formatter);
        if(date.isAfter(LocalDate.now().plusDays(1))) {
            throw new RuntimeException("日期错误");
        }
        List<Storage> storage4Market = marketMapper.getStorage4Market(marketId, date);
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
            int flag = 0;
            if (!storage4Market.isEmpty()) {
                for (Storage storage1 : storage4Market) {
                    if (storage1.getGoodId().equals(storage.getGoodId())) {
                        storage.setId(storage1.getId());
                        if (!Objects.equals(storage.getInitialGoods(), storage1.getInitialGoods())) {
                            marketMapper.updateInitialGoods(storage);
                        }

                        flag = 1;
                        break;
                    }
                }
            }
            if (flag == 1) {
                continue;
            }
            storageList.add(storage);
        }
        if(!storageList.isEmpty()) {
            marketMapper.importNewStorage(storageList);
        }
    }
}
