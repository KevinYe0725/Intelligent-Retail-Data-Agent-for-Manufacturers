package com.kevinye.pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodDataDTO {
    private Integer marketId;
    private Integer goodId;
    private Integer initialGoods;
    private  Integer noonGoods;
    private  Integer afternoonGoods;
    private  Integer nightGoods;
    private LocalDate date;
}
