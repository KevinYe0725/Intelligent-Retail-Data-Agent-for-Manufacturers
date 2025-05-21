package com.kevinye.server.service.serviceImpl;

import com.aliyun.oss.ServiceException;
import com.kevinye.pojo.Entity.Auditor;
import com.kevinye.pojo.Exception.LoginException;
import com.kevinye.pojo.constant.JwtConstant;
import com.kevinye.pojo.login.LoginDTO;
import com.kevinye.pojo.login.LoginVO;
import com.kevinye.server.mapper.LoginMapper;
import com.kevinye.server.service.LoginService;
import com.kevinye.utils.Jwt.JwtProperties;
import com.kevinye.utils.Jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    private LoginMapper loginMapper;
    private JwtProperties jwtProperties;
    public LoginServiceImpl(LoginMapper loginMapper,JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.loginMapper = loginMapper;
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        if(loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            throw new LoginException("输入不得为空");
        }
        String username = loginDTO.getUsername();
        Auditor auditor = loginMapper.getAuditor(username);
        if(auditor == null){
            throw new LoginException("账号不存在");
        }
        //测试先不使用加密
//        String password = DigestUtils.md5DigestAsHex(auditor.getPassword().getBytes());
        String password = auditor.getPassword();
        if(!password.equals(loginDTO.getPassword())) {
            throw new LoginException("密码错误");
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtConstant.AUDITOR_ID, auditor.getId());
        String token = JwtUtils.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        LoginVO loginVO = new LoginVO(token,auditor.getId());
        return  loginVO;
    }
}
