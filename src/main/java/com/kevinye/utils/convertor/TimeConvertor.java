package com.kevinye.utils.convertor;


import com.kevinye.pojo.Exception.TimeException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TimeConvertor {
    //转化DateTime成为时间Map
    public Map<String, Integer> dateTime2Number(String dateTime) {
        int year;
        int month;
        int day ;
        int hour ;
        int minute ;
        int second ;
        try {
            String[] ts = dateTime.split("T");
            String[] dateArray = ts[0].split("-");
            String[] timeArray = ts[1].split(":");
            extracted(dateArray);
            extracted(timeArray);
            year = Integer.parseInt(dateArray[0]);
            month = Integer.parseInt(dateArray[1]);
            day = Integer.parseInt(dateArray[2]);
            hour = Integer.parseInt(timeArray[0]);
            minute = Integer.parseInt(timeArray[1]);
            second = Integer.parseInt(timeArray[2].split("\\.")[0]);
        } catch (NumberFormatException e) {
            throw new TimeException("时间格式有误，请用YYYY-MM-DD HH:MM:SS",e);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        map.put("day", day);
        map.put("hour", hour);
        map.put("minute", minute);
        map.put("second", second);
        return map;
    }

    public Map<String,Integer> Time2Number(String time){
        int hour ;
        int minute ;
        int second ;
        try {
            String[] timeArray = time.split(":");
            extracted(timeArray);
            hour = Integer.parseInt(timeArray[0]);
            minute = Integer.parseInt(timeArray[1]);
            second = Integer.parseInt(timeArray[2]);
        }catch (NumberFormatException e) {
            throw new TimeException("时间格式有误，请用HH:MM:SS",e);
        }
        Map<String,Integer>timeMap = new HashMap<>();
        timeMap.put("hour",hour);
        timeMap.put("minute",minute);
        timeMap.put("second",second);
        return timeMap;
    }
    //去除其他无关元素
    private static void extracted(String[] timeArray) {
        for (int i = 0; i < timeArray.length; i++) {
            timeArray[i] = timeArray[i].trim();
        }
    }

}
