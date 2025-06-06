package com.kevinye.utils.excelUtils.excelHandlers;

import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.GoodInfo;
import com.kevinye.pojo.constant.GoodConstant;
import com.kevinye.server.mapper.GoodMapper;
import com.kevinye.utils.excelUtils.ExcelHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        int indexOfPrice = rowFirst.indexOf(GoodConstant.PRICE);
        if(indexOfName == -1 ) {
            throw new IllegalArgumentException("Excel表头缺少必要字段！");
        }
        List<GoodInfo> allGoodInformation = goodMapper.getAllGoodInformation(LocalDate.now());
        List<Good> goodList = new ArrayList<>();
        for (int i = 1; i < sheet.size(); i++) {
            List<String> row = sheet.get(i);
            Good good = new Good(row.get(indexOfName),Double.valueOf(row.get(indexOfPrice)));
            int flag = 0;
            for (GoodInfo goodInfo : allGoodInformation) {
                if (goodInfo.getGoodName().equals(good.getGoodName())) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                continue;
            }
            goodList.add(good);
        }
        goodMapper.insertGoodList(goodList);
    }
}
