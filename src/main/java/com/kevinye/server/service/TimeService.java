package com.kevinye.server.service;

import com.kevinye.pojo.Entity.PeriodSetting;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface TimeService {
    Integer getTimePeriod(LocalDateTime now);

    PeriodSetting getTimeSetting();

    void updatePeriodSetting(PeriodSetting periodSetting);


}
