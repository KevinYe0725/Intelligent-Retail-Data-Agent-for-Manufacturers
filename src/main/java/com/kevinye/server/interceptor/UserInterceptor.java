package com.kevinye.server.interceptor;

import com.kevinye.pojo.constant.JwtConstant;
import com.kevinye.utils.Jwt.JwtProperties;
import com.kevinye.utils.Jwt.JwtUtils;
import com.kevinye.utils.context.BaseContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    public UserInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判定是否为Controller层的方法，如果不是直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader(jwtProperties.getUserTokenName());
        try{
            log.info("开始验证token:{}",token);
            Claims claims = JwtUtils.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtConstant.AUDITOR_ID).toString());
            BaseContext.setCurrentId(userId);
            log.info("验证通过id:{}",userId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log.info("验证不通过");
            response.setStatus(401);
            return false;
        }
    }
}
