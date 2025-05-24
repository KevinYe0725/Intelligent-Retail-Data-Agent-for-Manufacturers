package com.kevinye.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendVO {
    private Integer goodId;;
    private String goodName;
    private Integer initialGoods;
    private  Integer recommendGoods;
}
