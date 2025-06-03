package com.kevinye.utils.taskUtil;

import com.kevinye.pojo.Entity.PeriodSetting;
import com.kevinye.server.mapper.TimeMapper;
import com.kevinye.server.service.TimeService;
import com.kevinye.utils.convertor.TimeConvertor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class TaskUtil {
    private final TimeMapper timeMapper;
    public TaskUtil(TimeMapper timeMapper, Tasker tasker, TimeConvertor timeConvertor) {
        this.timeMapper = timeMapper;
    }
    public String parseToCron(Integer period){
        PeriodSetting timeSetting = timeMapper.getPeriodSetting();
        LocalTime Time = timeSetting.getEndNightTime();
        Time = switch (period) {
            case 1 -> timeSetting.getEndNoonTime();
            case 2 -> timeSetting.getEndAfternoonTime();
            default -> Time;
        };
        return String.format("%d %d %d * * ?",Time.getSecond(),Time.getMinute(),Time.getHour());

    }
    public String parseToCron(LocalTime Time){
        return String.format("%d %d %d * * ?",Time.getSecond(),Time.getMinute(),Time.getHour());
    }
}
