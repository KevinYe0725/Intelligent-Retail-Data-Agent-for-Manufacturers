package com.kevinye.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditorVO {
    private Integer auditorId;
    private Integer marketId;
    private String marketName;
    private String auditorName;
    private String phone;
    private String email;
    private String username;
    private String password;
}
