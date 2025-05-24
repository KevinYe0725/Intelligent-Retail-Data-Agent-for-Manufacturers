package com.kevinye.utils.taskUtil;

import com.kevinye.pojo.Entity.PeriodSetting;
import com.kevinye.server.service.TimeService;
import com.kevinye.server.task.RecommendTasker;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class TaskManager {
    private final RecommendTasker recommendTasker;
    private final TaskUtil taskUtil;
    private final TimeService timeService;
    private final Tasker tasker;
    public TaskManager(RecommendTasker recommendTasker,TaskUtil taskUtil,TimeService timeService,Tasker tasker) {
        this.recommendTasker = recommendTasker;
        this.taskUtil = taskUtil;
        this.timeService = timeService;
        this.tasker = tasker;
    }
    @PostConstruct
    public void initRecommender(){
        PeriodSetting timeSetting = timeService.getTimeSetting();
        LocalTime endNightTime = timeSetting.getEndNightTime();
        String cron = taskUtil.parseToCron(endNightTime);
        tasker.startTask("PeriodSetting",recommendTasker,cron);
    }


}
