package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketInfo {
    private Integer id;
    private String marketName;
    private String phone;
    private String image;
    private String address;
    private String person;
    private String email;
    private Integer warningStatus;
    private Integer problemStatus;
}
