package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    private Integer id;
    private Integer auditorId;
    private Integer marketId;
    private String content;
    private String image;
    private LocalDateTime datetime;
}
