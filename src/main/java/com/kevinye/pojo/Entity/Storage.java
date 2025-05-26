package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Storage {
    private Integer id;
    private Integer marketId;
    private String MarketName;
    private Integer goodId;
    private String goodName;
    private Integer initialGoods;
    private Integer noonGoods;
    private Integer afternoonGoods;
    private Integer nightGoods;
    private LocalDate date;
    private Integer period;
    private Integer status;
    private String image;
    private Integer assignmentStatus;

    public Storage(Integer marketId, Integer goodId, Integer initialGoods, LocalDate date) {
        this.marketId = marketId;
        this.goodId = goodId;
        this.initialGoods = initialGoods;
        this.date = date;
    }
}
