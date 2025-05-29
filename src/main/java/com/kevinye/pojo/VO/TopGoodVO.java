package com.kevinye.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopGoodVO {
    private Integer goodId;
    private String goodName;
    private Double totalSales;
}
