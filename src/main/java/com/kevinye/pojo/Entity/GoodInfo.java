package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodInfo {
    private Integer goodId;
    private String goodName;
    private String image;
    private Integer totalNumber;
    private Integer totalMarket;
}
