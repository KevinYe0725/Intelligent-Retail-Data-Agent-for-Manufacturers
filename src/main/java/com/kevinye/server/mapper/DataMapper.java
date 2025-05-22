package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.GoodData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DataMapper {
    @Select(" select s.good_id,g.good_name,initial_goods,noon_goods,afternoon_goods,night_goods from storage s inner join goods g on s.good_id = g.id\n" +
            "        where date = #{date} and market_id = #{marketId}")
    List<GoodData> getData4Market(Integer marketId, LocalDate date);
}
