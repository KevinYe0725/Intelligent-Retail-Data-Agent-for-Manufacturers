package com.kevinye.server.service.serviceImpl;

import com.aliyun.oss.ServiceException;
import com.kevinye.pojo.Entity.Admin;
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
    private final LoginMapper loginMapper;
    private final JwtProperties jwtProperties;
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

        String inputPassword = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes());
        String password = auditor.getPassword();
        if(!password.equals(inputPassword)) {
            throw new LoginException("密码错误");
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtConstant.AUDITOR_ID, auditor.getAuditorId());
        String token = JwtUtils.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        return new LoginVO(token,auditor.getAuditorId(),auditor.getMarketId());
    }

    @Override
    public LoginVO AdminLogin(LoginDTO loginDTO) {
        if(loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            throw new LoginException("输入不得为空");
        }
        String username = loginDTO.getUsername();
        Admin admin = loginMapper.getAdmin(username);
        if(admin == null){
            throw new LoginException("账号不存在");
        }
       String inputPassword = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes());
        String password = admin.getPassword();
        if(!password.equals(inputPassword)) {
            throw new LoginException("密码错误");
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put(JwtConstant.ADMIN_ID,admin.getId());
        String token = JwtUtils.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
        return new LoginVO(token,admin.getId());
    }

    @Override
    public void AdminLogin(Admin admin) {
        if(admin.getUsername() == null || admin.getPassword() == null) {
            throw new LoginException("存在数据为空");
        }
        String password = admin.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        loginMapper.newAdmin(admin.getUsername(),password);

    }
}
