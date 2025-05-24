package com.kevinye.server.service;

import com.kevinye.pojo.login.LoginDTO;
import com.kevinye.pojo.login.LoginVO;

public interface LoginService {
    LoginVO login(LoginDTO loginDTO);

    LoginVO Adminlogin(LoginDTO loginDTO);
}
