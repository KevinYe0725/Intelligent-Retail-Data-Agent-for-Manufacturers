package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodData {
    private Integer goodId;;
    private String goodName;
    private Integer initialGoods;
    private  Integer noonGoods;
    private  Integer afternoonGoods;
    private  Integer nightGoods;
}
