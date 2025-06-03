package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.Entity.PeriodSetting;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController("adminTimeController")
@RequestMapping("/admin/time")
@Slf4j
public class TimeController {
    private final TimeService timeService;
    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PutMapping
    public Result<String> updateTime(@RequestBody PeriodSetting periodSetting) {

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        periodSetting.setStartNoonTime(LocalTime.parse(periodSetting.getStartNoonTime().format(formatter)).withSecond(0));
//        periodSetting.setEndNoonTime(LocalTime.parse(periodSetting.getEndNoonTime().format(formatter)).withSecond(0));
//        periodSetting.setStartNightTime(LocalTime.parse(periodSetting.getStartNightTime().format(formatter)).withSecond(0));
//        periodSetting.setEndNightTime(LocalTime.parse(periodSetting.getEndNightTime().format(formatter)).withSecond(0));
//        periodSetting.setStartAfternoonTime(LocalTime.parse(periodSetting.getStartAfternoonTime().format(formatter)).withSecond(0));
//        periodSetting.setEndAfternoonTime(LocalTime.parse(periodSetting.getEndAfternoonTime().format(formatter)).withSecond(0));
        log.info("periodSetting={}", periodSetting);
        timeService.updatePeriodSetting(periodSetting);
        return Result.success("上传成功");
    }

    @GetMapping
    public Result<Integer> getPeriod(LocalDateTime dateTime){
        if(dateTime == null){
            dateTime = LocalDateTime.now();
        }
        Integer period = timeService.getTimePeriod(dateTime);
        return Result.success(period);
    }

    @GetMapping("/setting")
    public Result<PeriodSetting> getPeriodSetting(){
        PeriodSetting timeSetting = timeService.getTimeSetting();
        return Result.success(timeSetting);
    }
}
