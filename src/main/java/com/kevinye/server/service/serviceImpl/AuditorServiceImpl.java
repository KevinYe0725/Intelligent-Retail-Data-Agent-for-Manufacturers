package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.Auditor;
import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.VO.AuditorVO;
import com.kevinye.server.mapper.AuditorMapper;
import com.kevinye.server.mapper.MarketMapper;
import com.kevinye.server.service.AuditorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuditorServiceImpl implements AuditorService {
    private final MarketMapper marketMapper;
    private final AuditorMapper auditorMapper;
    public AuditorServiceImpl(AuditorMapper auditorMapper, MarketMapper marketMapper) {
        this.auditorMapper = auditorMapper;
        this.marketMapper = marketMapper;
    }

    @Override
    public List<AuditorVO> getAllAuditors(String name, String marketName) {
        List<AuditorVO> auditorVOS = new ArrayList<>();
        if (marketName != null && !marketName.isEmpty() ) {
            List<Market> markets = marketMapper.getMarketByName(marketName);
            List<Integer> marketIds = markets.stream().map(Market::getId).toList();
            List<Auditor> allAuditors = auditorMapper.getAllAuditors(name, marketIds);

            for (Auditor auditor : allAuditors) {
                AuditorVO auditorVO = new AuditorVO(auditor.getAuditorId(),auditor.getMarketId(),marketName,auditor.getAuditorName(),auditor.getPhone(),auditor.getEmail(),auditor.getUsername(),auditor.getPassword() );
                auditorVOS.add(auditorVO);
            }
        }else {
            List<Market> markets = marketMapper.getAllMarket();
            List<Integer> marketIds = markets.stream().map(Market::getId).toList();
            List<Auditor> allAuditors = auditorMapper.getAllAuditors(name, marketIds);
            Map<Integer,String> matcher = new HashMap<>();
            markets.forEach(market -> matcher.put(market.getId(),market.getMarketName()));
            for (Auditor auditor : allAuditors) {
                AuditorVO auditorVO = new AuditorVO(auditor.getAuditorId(),auditor.getMarketId(),matcher.get(auditor.getMarketId()),auditor.getAuditorName(),auditor.getPhone(),auditor.getEmail(),auditor.getUsername(),auditor.getPassword() );
                auditorVOS.add(auditorVO);
            }
        }
        log.info("AuditorVOS:{} ", auditorVOS);
        return  auditorVOS;
    }

    @Override
    public boolean addAuditor(Auditor auditor) {
        if (auditor == null) {
            return false;
        }
        auditorMapper.addAuditor(auditor);
        return true;
    }
    @Transactional
    @Override
    public boolean updateAuditor(Auditor auditor) {
        if (auditor == null) {
            return false;
        }
        log.info("员工信息: {}", auditor);
        deleteAuditorById(auditor.getAuditorId());
        addAuditor(auditor);
        return true;
    }

    @Transactional
    @Override
    public boolean deleteAuditorById(Integer auditorId) {
        if (auditorId == null) {
            return false;
        }
        auditorMapper.deleteAuditorById(auditorId);
        return true;
    }
}
