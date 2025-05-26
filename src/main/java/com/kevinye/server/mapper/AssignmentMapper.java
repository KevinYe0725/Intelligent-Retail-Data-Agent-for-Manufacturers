package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.Assignment;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AssignmentMapper {
    /**
     * 将需要的任务批量或者单个添加进任务列表
     * @param assignmentList 任务列表
     */
    void addAssignmentList(List<Assignment> assignmentList);


    List<Assignment> getAllAssignmentByIds(LocalDate date, List<Integer> ids);

    @Select("select * from assignment where date =#{date}")
    List<Assignment> getAllAssignments(LocalDate date);

    @Delete("delete from assignment where id = #{assignmentId}")
    void deleteAssignmentById(Integer assignmentId);

    @Update("update assignment set status = #{status} where id = #{assignmentId}")
    void updateStatus(Integer assignmentId, Integer status);
}
