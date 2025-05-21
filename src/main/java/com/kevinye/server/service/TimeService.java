package com.kevinye.server.service;

import java.time.LocalDateTime;

public interface TimeService {
    Integer getTimePeriod(LocalDateTime now);
}
