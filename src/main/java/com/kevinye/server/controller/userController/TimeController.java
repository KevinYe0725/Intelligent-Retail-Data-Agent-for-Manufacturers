package com.kevinye.server.controller.userController;

import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.TimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    public  class Time {
        private Integer time;
        public Time(Integer period) {
            this.time = period;
        }
        public  Integer getTime() {
            return time;
        }
        public void setTime(Integer period) {
            this.time = period;
        }
    }

    /**
     * 获取当前时间段
     * @return
     */
    @GetMapping("/time")
    public Result<Time> getPeriod() {
        Time period  =new Time(timeService.getTimePeriod(LocalDateTime.now()));
        return Result.success(period);
    }


}
