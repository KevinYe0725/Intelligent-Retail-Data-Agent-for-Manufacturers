package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.Entity.PeriodSetting;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
