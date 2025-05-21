package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.PeriodSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface TimeMapper {

    @Select("SELECT start_noon_time, end_noon_time, start_afternoon_time, end_afternoon_time, start_night_time, end_night_time FROM period_setting WHERE id = 1")
    PeriodSetting getPeriodSetting(); // ✅ 返回一行记录对应的对象

}
