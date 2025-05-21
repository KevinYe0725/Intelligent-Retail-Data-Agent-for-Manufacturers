package com.kevinye.server.controller.userController;

import com.kevinye.pojo.login.LoginDTO;
import com.kevinye.pojo.login.LoginVO;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("AdminLoginController")
@RequestMapping("/user")
public class LoginController {
    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }
    @PostMapping("/login")
    public Result<LoginVO> login( LoginDTO loginDTO) {
        log.info("用户开始登陆");
        LoginVO loginVO = loginService.login(loginDTO);
        return Result.success(loginVO);
    }


}
