package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeriodSetting {
    private LocalTime startNoonTime;
    private LocalTime endNoonTime;
    private LocalTime startAfternoonTime;
    private LocalTime endAfternoonTime;
    private LocalTime startNightTime;
    private LocalTime endNightTime;
}
