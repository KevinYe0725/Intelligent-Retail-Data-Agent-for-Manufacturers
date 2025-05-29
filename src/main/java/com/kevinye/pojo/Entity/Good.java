package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Good {
    private Integer GoodId;
    private String goodName;
    private String image;
    private Double price;
    public Good(String goodName,String image,Double price) {
        this.goodName = goodName;
        this.image = image;
        this.price = price;
    }
}
