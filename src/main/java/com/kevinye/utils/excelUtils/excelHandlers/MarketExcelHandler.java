package com.kevinye.utils.excelUtils.excelHandlers;

import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.constant.MarketConstant;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.utils.excelUtils.ExcelHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarketExcelHandler implements ExcelHandler {
    private final MarketMapper marketMapper;
    public MarketExcelHandler(MarketMapper marketMapper) {
        this.marketMapper = marketMapper;
    }
    @Override
    public void handleSheet(List<List<String>> sheet) {
        List<String> rowFirst = sheet.getFirst();
        int indexOfMarketName = rowFirst.indexOf(MarketConstant.MARKET_NAME);
        int indexOfAddress = rowFirst.indexOf(MarketConstant.ADDRESS);
        int indexOfPhone = rowFirst.indexOf(MarketConstant.PHONE);
        int indexOfEmail = rowFirst.indexOf(MarketConstant.EMAIL);
        int indexOfPerson = rowFirst.indexOf(MarketConstant.PERSON);
        List<Market> marketList = new ArrayList<>();
        for (int i = 1; i < sheet.size(); i++) {
            List<String> row = sheet.get(i);
            Market market = new Market(
                    row.get(indexOfMarketName),
                    row.get(indexOfPhone),
                    row.get(indexOfAddress),
                    row.get(indexOfEmail),
                    row.get(indexOfPerson)
            );
            marketList.add(market);
        }
        marketMapper.importNewMarkets(marketList);
    }
}
