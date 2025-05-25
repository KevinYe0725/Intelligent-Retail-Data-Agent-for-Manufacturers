package com.kevinye.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarningVO {
    private Integer goodId;
    private String goodName;
    private Integer initialGoods;
    private Double remainingRate;
    private String time;
    private String auditorName;
    private Integer recentTimes;
    private  Integer assignmentStatus;
}
