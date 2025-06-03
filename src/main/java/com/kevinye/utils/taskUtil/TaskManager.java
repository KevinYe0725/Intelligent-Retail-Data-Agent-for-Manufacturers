package com.kevinye.utils.taskUtil;

import com.kevinye.pojo.Entity.PeriodSetting;
import com.kevinye.server.mapper.TimeMapper;
import com.kevinye.server.service.TimeService;
import com.kevinye.server.task.RecommendTasker;
import com.kevinye.server.task.WarningTasker;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class TaskManager {
    private final RecommendTasker recommendTasker;
    private final TaskUtil taskUtil;
    private final TimeMapper timeMapper;
    private final Tasker tasker;
    private final WarningTasker warningTasker;
    public TaskManager(RecommendTasker recommendTasker ,TaskUtil taskUtil,TimeMapper timeMapper,Tasker tasker,WarningTasker warningTasker) {
        this.recommendTasker = recommendTasker;
        this.taskUtil = taskUtil;
        this.timeMapper = timeMapper;
        this.tasker = tasker;
        this.warningTasker= warningTasker;
    }
    @PostConstruct
    public void initRecommender(){
        PeriodSetting timeSetting = timeMapper.getPeriodSetting();
        LocalTime endNightTime = timeSetting.getEndNightTime();
        String cron = taskUtil.parseToCron(endNightTime);
        tasker.startTask("PeriodSetting",recommendTasker,cron);
    }

    @PostConstruct
    public void initNoonWarning(){
        PeriodSetting timeSetting = timeMapper.getPeriodSetting();
        LocalTime endNoonTime = timeSetting.getEndNoonTime();
        String cron = taskUtil.parseToCron(endNoonTime);
        tasker.startTask("NoonWarning", warningTasker,cron);
    }
    @PostConstruct
    public void initAfternoonWarning(){
        PeriodSetting timeSetting = timeMapper.getPeriodSetting();
        LocalTime endAfternoonTime = timeSetting.getEndAfternoonTime();
        String cron = taskUtil.parseToCron(endAfternoonTime);
        tasker.startTask("AfternoonWarning", warningTasker,cron);
    }

    @PostConstruct
    public void initNightWarning(){
        PeriodSetting timeSetting = timeMapper.getPeriodSetting();
        LocalTime endNightTime = timeSetting.getEndNightTime();
        String cron = taskUtil.parseToCron(endNightTime);
        tasker.startTask("NightWarning", warningTasker,cron);
    }

}
