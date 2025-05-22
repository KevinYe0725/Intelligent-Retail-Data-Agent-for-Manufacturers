package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.Problem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProblemMapper {

    @Insert("insert into problem (market_id, auditor_id, content, datetime, image) values (#{marketId},#{auditorId},#{content},#{datetime},#{image})")
    void uploadProblem(Problem problem);
}
