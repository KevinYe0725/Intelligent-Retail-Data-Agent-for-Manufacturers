package com.kevinye.server.service;

import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.GoodInfo;

import java.time.LocalDate;
import java.util.List;

public interface GoodService {
    List<GoodInfo> getGoodInformation(LocalDate date);

    void addNewGood(Good good);

    void deleteGoodById(Integer id);

    void updateGood(Good good);
}
