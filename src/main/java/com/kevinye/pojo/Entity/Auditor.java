package com.kevinye.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auditor {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String username;
    private String password;

}
