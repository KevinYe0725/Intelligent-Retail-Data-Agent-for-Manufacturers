package com.kevinye.utils.taskUtil;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class Tasker {
    private final TaskScheduler scheduler;
    public Tasker(@Qualifier("taskScheduler") TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    private final Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

    //新增或重启任务
    public void startTask(String taskId,Runnable task,String cron) {
        stopTask(taskId);
        ScheduledFuture<?> future = scheduler.schedule(task,new CronTrigger(cron));
        futures.put(taskId,future);
    }

    public void stopTask(String taskId) {
        ScheduledFuture<?> scheduledFuture = futures.get(taskId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        futures.remove(taskId);
    }

    public void stopAll() {
        for (String key : futures.keySet()) {
            stopTask(key);
        }
    }

}
