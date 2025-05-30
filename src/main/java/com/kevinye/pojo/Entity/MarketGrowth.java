package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketGrowth {
    private Integer marketId;
    private String marketName;
    private Double monthSales;
    private Double monthGrowth;
}
