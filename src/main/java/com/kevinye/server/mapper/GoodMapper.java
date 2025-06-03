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

    @Select("SELECT\n" +
            "  g.id             AS good_id,\n" +
            "  g.good_name,\n" +
            "  g.image,\n" +
            "  g.price,\n" +
            "  COUNT(s.market_id)                 AS total_market,\n" +
            "  COALESCE(SUM(s.initial_goods*g.price), 0)  AS total_number\n" +
            "FROM goods g\n" +
            "LEFT JOIN storage s\n" +
            "  ON s.good_id = g.id\n" +
            "  AND s.date = #{date} \n" +
            "GROUP BY\n" +
            "  g.id,\n" +
            "  g.good_name,\n" +
            "  g.image,\n" +
            "  g.price;\n")
    List<GoodInfo> getAllGoodInformation(LocalDate date);

    @Insert("insert into goods (id,good_name, image,price) values (#{goodId},#{goodName},#{image},#{price});")
    void insertNewGood(Good good);

    @Delete("delete from goods where id = #{goodId}")
    void deleteGoodById(Integer goodId);

    void insertGoodList(List<Good> goodList);

    @Select("select id as goodId, good_name, image, price from goods where good_name = #{goodName}")
    Good getGoodByName(String goodName);
}
