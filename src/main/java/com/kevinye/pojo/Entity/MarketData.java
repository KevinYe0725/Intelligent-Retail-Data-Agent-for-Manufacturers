package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketData {
    private Integer goodId;;
    private String goodName;
    private Integer initialGoods;
    private  Integer noonGoods;
    private  Integer afternoonGoods;
    private  Integer nightGoods;
    private String auditorName;
}
