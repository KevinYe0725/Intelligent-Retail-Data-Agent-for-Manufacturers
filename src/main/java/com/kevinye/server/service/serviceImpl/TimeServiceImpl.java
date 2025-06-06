package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.PeriodSetting;
import com.kevinye.pojo.constant.PeriodConstant;
import com.kevinye.pojo.constant.TimeConstant;
import com.kevinye.server.mapper.TimeMapper;
import com.kevinye.server.service.TimeService;
import com.kevinye.server.task.RecommendTasker;
import com.kevinye.server.task.WarningTasker;
import com.kevinye.utils.convertor.TimeConvertor;
import com.kevinye.utils.taskUtil.TaskUtil;
import com.kevinye.utils.taskUtil.Tasker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class TimeServiceImpl implements TimeService {
    @Autowired
    private  TimeMapper timeMapper;
    @Autowired
    private  TimeConvertor timeConvertor;
    @Autowired
    private  Tasker tasker;
    @Autowired
    private  RecommendTasker recommendTasker;
    @Autowired
    private  WarningTasker warningTasker;
    @Autowired
    private  TaskUtil taskUtil;

    @Override
    public Integer getTimePeriod(LocalDateTime now) {
        Map<String, Integer> nowTime = timeConvertor.dateTime2Number(String.valueOf(now));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        PeriodSetting periodSets = timeMapper.getPeriodSetting();
        PeriodSetting ps = timeMapper.getPeriodSetting();
        List<LocalTime> periodSetting = List.of(
                ps.getStartNoonTime(),
                ps.getEndNoonTime(),
                ps.getStartAfternoonTime(),
                ps.getEndAfternoonTime(),
                ps.getStartNightTime(),
                ps.getEndNightTime()
        );

        Map<String, Integer> begin_noon = timeConvertor.Time2Number(periodSetting.get(0).format(formatter));
        Map<String, Integer> end_noon = timeConvertor.Time2Number(periodSetting.get(1).format(formatter));
        Map<String, Integer> begin_afternoon = timeConvertor.Time2Number(periodSetting.get(2).format(formatter));
        Map<String, Integer> end_afternoon = timeConvertor.Time2Number(periodSetting.get(3).format(formatter));
        Map<String, Integer> begin_night = timeConvertor.Time2Number(periodSetting.get(4).format(formatter));
        Map<String, Integer> end_night = timeConvertor.Time2Number(periodSetting.get(5).format(formatter));
        if (IsInThePeriod(nowTime, begin_noon, end_noon)){
            return PeriodConstant.NOON;
        }
        if(IsInThePeriod(nowTime, begin_afternoon, end_afternoon)){
            return PeriodConstant.AFTERNOON;
        }
        if (IsInThePeriod(nowTime, begin_night, end_night)) {
            return PeriodConstant.NIGHT;
        }
        return -1;
    }

    @Override
    public PeriodSetting getTimeSetting() {
        return timeMapper.getPeriodSetting();
    }

    @Override
    public void updatePeriodSetting(PeriodSetting periodSetting) {
        timeMapper.updateSetting(periodSetting);
        LocalTime endNightTime = periodSetting.getEndNightTime();
        String cron = taskUtil.parseToCron(endNightTime);
        tasker.stopTask("PeriodSetting");
        tasker.stopTask("NoonWarning");
        tasker.stopTask("AfternoonWarning");
        tasker.stopTask("NightWarning");

        String cronNoon = taskUtil.parseToCron(periodSetting.getStartAfternoonTime());
        tasker.startTask("NoonWarning", warningTasker, cronNoon);
        String cronAfternoon = taskUtil.parseToCron(periodSetting.getEndAfternoonTime());
        tasker.startTask("AfternoonWarning", warningTasker, cronAfternoon);
        String cronNight = taskUtil.parseToCron(periodSetting.getEndNightTime());
        tasker.startTask("NightWarning", warningTasker, cronNight);
        tasker.startTask("PeriodSetting",recommendTasker,cronNight);

    }




    protected static boolean IsInThePeriod(Map<String, Integer> nowTime,
                                           Map<String, Integer> beginTime,
                                           Map<String, Integer> endTime) {
        // 构造当前时间的“秒数”总和
        int nowSeconds = nowTime.get(TimeConstant.HOUR) * 3600
                + nowTime.get(TimeConstant.MINUTE) * 60
                + nowTime.get(TimeConstant.SECOND);

        int beginSeconds = beginTime.get(TimeConstant.HOUR) * 3600
                + beginTime.get(TimeConstant.MINUTE) * 60
                + beginTime.get(TimeConstant.SECOND);

        int endSeconds = endTime.get(TimeConstant.HOUR) * 3600
                + endTime.get(TimeConstant.MINUTE) * 60
                + endTime.get(TimeConstant.SECOND);

        return nowSeconds >= beginSeconds && nowSeconds < endSeconds;
    }



}
