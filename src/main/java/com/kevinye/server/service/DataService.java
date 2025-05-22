package com.kevinye.server.service;

import com.kevinye.pojo.Entity.GoodData;

import java.time.LocalDate;
import java.util.List;

public interface DataService {
    List<GoodData> getData4Market(Integer marketId, LocalDate date);
}
