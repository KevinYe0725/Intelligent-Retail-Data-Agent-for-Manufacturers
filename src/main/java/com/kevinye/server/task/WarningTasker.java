package com.kevinye.server.task;

import com.kevinye.pojo.Entity.Market;
import com.kevinye.server.mapper.DataMapper;
import com.kevinye.server.service.DataService;
import com.kevinye.server.service.serviceImpl.DataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class WarningTasker implements Runnable {
    @Autowired
    private DataService dataService;
    @Override
    public void run() {
        List<Integer> marketIdList = dataService.getAllMarkets(null).stream().map(Market::getId).toList();
        marketIdList.forEach(marketId -> {dataService.getProblemData4Market(LocalDate.now(),marketId);});
    }
}
