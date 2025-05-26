package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.VO.TopGoodVO;
import com.kevinye.pojo.VO.TopMarketVO;
import com.kevinye.server.mapper.TotalMapper;
import com.kevinye.server.service.TotalService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TotalServiceImpl implements TotalService {
    private final TotalMapper totalMapper;
    public TotalServiceImpl(TotalMapper totalMapper) {
        this.totalMapper = totalMapper;
    }

    @Override
    public List<TopGoodVO> getSortedGoodByNumber(LocalDate date) {
        LocalDate beginDate = date.minusDays(30);
        return totalMapper.getTopGoodList(beginDate,date);
    }

    @Override
    public List<TopMarketVO> getSortedMarketByNumber(LocalDate date) {
        LocalDate beginDate = date.minusDays(30);
        return totalMapper.getTopMarketList(beginDate,date);
    }
}
