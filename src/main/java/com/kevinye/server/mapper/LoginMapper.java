package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.Auditor;
import com.kevinye.pojo.login.LoginDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    @Select("select * from auditor where username = #{username} ")
    Auditor getAuditor(String username);
}
