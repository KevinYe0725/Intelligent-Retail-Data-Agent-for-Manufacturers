package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.PeriodSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TimeMapper {

    @Select("SELECT start_noon_time, end_noon_time, start_afternoon_time, end_afternoon_time, start_night_time, end_night_time FROM period_setting WHERE id = 1")
    PeriodSetting getPeriodSetting(); // ✅ 返回一行记录对应的对象

    @Update("update period_setting set start_noon_time = #{startNoonTime} , start_afternoon_time = #{startAfternoonTime} ,start_night_time = #{startNightTime},end_noon_time = #{endNoonTime} ,end_afternoon_time = #{endAfternoonTime} ,end_night_time = #{endNightTime} where id = 1")
    void updateSetting(PeriodSetting periodSetting);
}
