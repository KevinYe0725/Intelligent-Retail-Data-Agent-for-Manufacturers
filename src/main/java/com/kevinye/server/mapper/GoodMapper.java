package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.GoodInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface GoodMapper {

    @Select("select g.id as good_id ,g.good_name,g.image ,count(*) as total_market,sum(s.initial_goods) as total_number  from goods g ,storage s where s.date = #{date} and s.good_id = g.id group by g.id")
    List<GoodInfo> getAllGoodInformation(LocalDate date);

    @Insert("insert into goods (id,good_name, image) values (#{goodId},#{goodName},#{image});")
    void insertNewGood(Good good);

    @Delete("delete from goods where id = #{goodId}")
    void deleteGoodById(Integer goodId);

    void insertGoodList(List<Good> goodList);

    @Select("select id as goodId, good_name, image from goods where good_name = #{goodName}")
    Good getGoodByName(String goodName);
}
