package com.kevinye.server.service;

import com.kevinye.pojo.Entity.Admin;
import com.kevinye.pojo.login.LoginDTO;
import com.kevinye.pojo.login.LoginVO;

public interface LoginService {
    LoginVO login(LoginDTO loginDTO);

    LoginVO AdminLogin(LoginDTO loginDTO);

    void AdminLogin(Admin admin);
}
