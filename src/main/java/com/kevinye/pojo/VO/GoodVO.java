package com.kevinye.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodVO {
    private Integer goodId;
    private String goodName;
    private Integer remaining;
    private Integer status;
    private String image;
}
