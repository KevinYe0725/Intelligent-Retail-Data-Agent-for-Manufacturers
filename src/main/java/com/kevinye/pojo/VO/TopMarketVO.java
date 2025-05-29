package com.kevinye.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopMarketVO {
    private Integer marketId;
    private String marketName;
    private Integer totalSales;
}
