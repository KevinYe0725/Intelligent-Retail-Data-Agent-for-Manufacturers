package com.kevinye.pojo.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVO {
    private String token;
    private Integer auditorId;
    private Integer marketId;
    public LoginVO(String token, Integer auditorId) {
        this.token = token;
        this.auditorId = auditorId;
    }
}
