package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auditor {
    private Integer auditorId;
    private Integer marketId;
    private String auditorName;
    private String phone;
    private String email;
    private String username;
    private String password;
    public Auditor( Integer marketId, String auditorName, String phone, String email, String username, String password) {
        this.marketId = marketId;
        this.auditorName = auditorName;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
