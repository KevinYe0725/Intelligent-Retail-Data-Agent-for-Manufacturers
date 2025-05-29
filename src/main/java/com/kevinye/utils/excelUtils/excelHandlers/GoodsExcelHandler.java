package com.kevinye.utils.excelUtils.excelHandlers;

import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.constant.GoodConstant;
import com.kevinye.server.mapper.GoodMapper;
import com.kevinye.utils.excelUtils.ExcelHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GoodsExcelHandler implements ExcelHandler {
    private final GoodMapper goodMapper;
    public GoodsExcelHandler(GoodMapper goodMapper) {
        this.goodMapper = goodMapper;
    }

    @Override
    public void handleSheet(List<List<String>> sheet) {
        List<String> rowFirst = sheet.getFirst();
        int indexOfName = rowFirst.indexOf(GoodConstant.GOOD_NAME);
        int indexOfImage = rowFirst.indexOf(GoodConstant.IMAGE);
        int indexOfPrice = rowFirst.indexOf(GoodConstant.PRICE);
        if(indexOfName != -1 && indexOfImage != -1) {
            throw new IllegalArgumentException("Excel表头缺少必要字段！");
        }
        List<Good> goodList = new ArrayList<>();
        for (int i = 1; i < sheet.size(); i++) {
            List<String> row = sheet.get(i);
            Good good = new Good(row.get(indexOfName),row.get(indexOfImage),Double.valueOf(row.get(indexOfPrice)));
            goodList.add(good);
        }
        goodMapper.insertGoodList(goodList);
    }
}
