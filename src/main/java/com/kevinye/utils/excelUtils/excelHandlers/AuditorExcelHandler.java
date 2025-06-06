package com.kevinye.utils.excelUtils.excelHandlers;

import com.kevinye.pojo.Entity.Auditor;
import com.kevinye.pojo.constant.AuditorConstant;
import com.kevinye.server.mapper.AuditorMapper;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.server.service.MarketService;
import com.kevinye.utils.excelUtils.ExcelHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuditorExcelHandler implements ExcelHandler {
    private final MarketMapper marketMapper;
    private final AuditorMapper auditorMapper;
    public AuditorExcelHandler(MarketMapper marketMapper, AuditorMapper auditorMapper) {
        this.marketMapper = marketMapper;
        this.auditorMapper = auditorMapper;
    }
    @Override
    public void handleSheet(List<List<String>> sheet) {
        List<String> firstRow = sheet.getFirst();
        int indexOfName = firstRow.indexOf(AuditorConstant.AUDITOR_NAME);
        int indexOfMarketName = firstRow.indexOf(AuditorConstant.MARKET_NAME);
        int indexOfEmail = firstRow.indexOf(AuditorConstant.EMAIL);
        int indexOfPassword = firstRow.indexOf(AuditorConstant.PASSWORD);
        int indexOfPhone = firstRow.indexOf(AuditorConstant.PHONE);
        int indexOfUsername = firstRow.indexOf(AuditorConstant.USR_NAME);
        List<Auditor> auditors = new ArrayList<>();
        List<Auditor> allAuditors = auditorMapper.getAllAuditors(null, null);
        for(int i=1;i<sheet.size();i++){
            List<String> row = sheet.get(i);
            Integer marketId = marketMapper.getMarketByName(row.get(indexOfMarketName)).getFirst().getId();
            if(marketId==null||marketId==0){
                throw new RuntimeException("不存在该商店");
            }
            Auditor auditor = new Auditor(
                    marketId,
                    row.get(indexOfName),
                    row.get(indexOfPhone),
                    row.get(indexOfEmail),
                    row.get(indexOfUsername),
                    DigestUtils.md5DigestAsHex(row.get(indexOfPassword).getBytes())
            );
            int flag = 0;
            for (Auditor allAuditor : allAuditors) {
                if(allAuditor.getAuditorName().equals(auditor.getAuditorName())&&allAuditor.getMarketId().equals(auditor.getMarketId())){
                    flag = 1;
                    break;
                }
            }
            if(flag==1){
                continue;
            }
            auditors.add(auditor);
        }
        if(!auditors.isEmpty()){
            auditorMapper.importAuditors(auditors);
        }
    }
}