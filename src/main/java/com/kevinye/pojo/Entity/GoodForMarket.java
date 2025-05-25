package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodForMarket {
    private Integer goodId;
    private Integer marketId;
    private String goodName;
    private LocalDate date;
    private Integer initialGoods;
    private String image;
}
