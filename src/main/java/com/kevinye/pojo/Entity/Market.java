package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Market {
    private Integer id;
    private Integer auditorId;
    private String marketName;
    private String phone;
    private String address;
    private String person;
    private String email;

}
