package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.Admin;
import com.kevinye.pojo.Entity.Auditor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    @Select("select id as auditorId, auditor_name, username, phone, email, market_id, password from auditor where username = #{username} ")
    Auditor getAuditor(String username);
    @Select("select * from admin where username = #{username}")
    Admin getAdmin(String username);

    @Insert("insert into admin (username, password) VALUE (#{username} ,#{password})")
    void newAdmin(String username, String password);
}
