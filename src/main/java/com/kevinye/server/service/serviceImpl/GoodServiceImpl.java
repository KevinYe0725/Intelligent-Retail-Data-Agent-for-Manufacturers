package com.kevinye.server.service.serviceImpl;

import com.aliyun.oss.ServiceException;
import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.GoodInfo;
import com.kevinye.server.mapper.GoodMapper;
import com.kevinye.server.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class GoodServiceImpl implements GoodService {

    private final GoodMapper goodMapper;
    public GoodServiceImpl(GoodMapper goodMapper) {
        this.goodMapper = goodMapper;
    }

    @Override
    public List<GoodInfo> getGoodInformation(LocalDate date) {
        return goodMapper.getAllGoodInformation(date);
    }

    @Override
    public void addNewGood(Good good) {
        goodMapper.insertNewGood(good);
    }

    @Transactional
    @Override
    public void deleteGoodById(Integer goodId) {
        if(goodId == null){
            throw new ServiceException("id不得为空");
        }
        goodMapper.deleteGoodById(goodId);
    }

    @Transactional
    @Override
    public void updateGood(Good good) {
        goodMapper.deleteGoodById(good.getGoodId());
        goodMapper.insertNewGood(good);
    }
}
