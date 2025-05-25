package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.Entity.PeriodSetting;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

}
