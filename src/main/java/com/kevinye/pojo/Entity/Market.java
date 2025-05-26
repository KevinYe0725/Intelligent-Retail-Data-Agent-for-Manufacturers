package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Market {
    private Integer id;
    private String marketName;
    private String phone;
    private String image;
    private String address;
    private String person;
    private String email;

    public Market(String marketName, String phone, String address, String email, String person) {
        this.marketName = marketName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.person = person;
    }
}
