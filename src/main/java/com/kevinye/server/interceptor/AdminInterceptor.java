package com.kevinye.server.interceptor;

import com.kevinye.pojo.constant.JwtConstant;
import com.kevinye.utils.Jwt.JwtProperties;
import com.kevinye.utils.Jwt.JwtUtils;
import com.kevinye.utils.context.BaseContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {
    private final JwtProperties jwtProperties;
    public AdminInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getParameter(jwtProperties.getAdminTokenName());
        try{
            log.info("开始验证token:{}",token);
            Claims claims = JwtUtils.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long adminId = Long.valueOf(claims.get(JwtConstant.ADMIN_ID).toString());
            BaseContext.setCurrentId(adminId);
            log.info("token验证成功:{}",token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log.info("验证不通过");
            response.setStatus(401);
            return false;
        }

    }
}
