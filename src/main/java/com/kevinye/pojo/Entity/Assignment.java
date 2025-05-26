package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignment {
    private Integer assignmentId;
    private String content;
    private Integer marketId;
    private  Integer goodId;
    private Integer status;
    private LocalDate date;
    public Assignment( String content, Integer marketId, Integer goodId, LocalDate date) {
        this.content = content;
        this.marketId = marketId;
        this.goodId = goodId;
        this.date = date;
    }

}
