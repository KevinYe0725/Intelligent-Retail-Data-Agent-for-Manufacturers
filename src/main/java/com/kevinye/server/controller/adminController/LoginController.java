package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.Entity.Admin;
import com.kevinye.pojo.login.LoginDTO;
import com.kevinye.pojo.login.LoginVO;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("AdminLoginController")
@Slf4j
@RequestMapping("/admin")
public class LoginController {
    private final LoginService loginService;
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(loginService.AdminLogin(loginDTO));
    }

    @PostMapping("/new")
    public Result<String> newAdmin(@RequestBody Admin admin){
        loginService.AdminLogin(admin);
        return Result.success("新建成功");
    }
}
