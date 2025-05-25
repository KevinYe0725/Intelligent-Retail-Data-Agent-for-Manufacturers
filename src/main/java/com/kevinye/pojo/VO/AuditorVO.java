package com.kevinye.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditorVO {
    private Integer id;
    private String marketName;
    private String auditorName;
    private String phone;
    private String email;
    private String username;
    private String password;
}
