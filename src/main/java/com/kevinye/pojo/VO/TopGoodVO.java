package com.kevinye.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopGoodVO {
    Integer goodId;
    String goodName;
    Integer totalNumber;
}
