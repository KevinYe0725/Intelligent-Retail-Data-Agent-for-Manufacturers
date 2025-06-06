package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodGrowth {
    private Integer goodId;
    private String goodName;
    private Double goodSales;
    private Double growth;
}
