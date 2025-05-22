package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.GoodData;
import com.kevinye.server.mapper.DataMapper;
import com.kevinye.server.service.DataService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {
    private  final DataMapper dataMapper;
    public DataServiceImpl(DataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    @Override
    public List<GoodData> getData4Market(Integer marketId, LocalDate date) {
        return dataMapper.getData4Market(marketId, date);
    }
}
