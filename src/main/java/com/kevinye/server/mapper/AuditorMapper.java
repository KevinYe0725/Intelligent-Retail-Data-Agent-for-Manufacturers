package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.Auditor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuditorMapper {


    List<Auditor> getAllAuditors(String name, List<Integer> marketIds);

    @Insert("insert into auditor (id,auditor_name, username, phone, email, market_id, password) VALUES (#{auditorId},#{auditorName},#{username},#{phone},#{email},#{marketId},#{password})")
    void addAuditor(Auditor auditor);

    @Delete("delete from auditor where id = #{auditorId}")
    void deleteAuditorById(Integer auditorId);


    void importAuditors(List<Auditor> auditors);
}
