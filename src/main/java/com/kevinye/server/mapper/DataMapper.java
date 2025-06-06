package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DataMapper {

    @Select(" select s.good_id,g.good_name,initial_goods,noon_goods,afternoon_goods,night_goods from storage s inner join goods g on s.good_id = g.id\n" +
            "        where date = #{date} and market_id = #{marketId}")
    List<GoodData> getData4Market(Integer marketId, LocalDate date);

    @Select("select s.good_id,g.good_name,initial_goods,noon_goods,afternoon_goods,night_goods from storage s inner join goods g on s.good_id = g.id where market_id = #{marketId} and date between #{beginDate} and #{endDate} order by s.date  ")
    List<GoodData> selectAllData4Market(Integer marketId, LocalDate beginDate, LocalDate endDate);

    @Select("select recommend_factor from recommendation where date = #{date} and market_id = #{marketId} and good_id = #{goodId}")
    Double getFactor(LocalDate date, Integer marketId, Integer goodId);

    @Insert("insert into recommendation ( noon_warning_line, afternoon_warning_line, night_warning_line, date,market_id,good_id) VALUES " +
            "(#{noonWarningLine},#{afternoonWarningLine},#{nightWarningLine},#{now},#{marketId},#{goodId})")
    void insertWarningLine(double noonWarningLine, double afternoonWarningLine, double nightWarningLine, LocalDate now, Integer marketId, Integer goodId);

    @Select("select good_id, noon_warning_line, afternoon_warning_line, night_warning_line from recommendation where date = #{date} and market_id = #{marketId}")
    List<WarningLine> getProblemLine(Integer marketId, LocalDate date);

    @Update("update recommendation set  noon_warning_line = #{noonWarningLine} ,afternoon_warning_line = #{afternoonWarningLine} ,night_warning_line = #{nightWarningLine} ,date = #{date} where market_id = #{marketId} and good_id = #{goodId}")
    void updateWarningLine( double noonWarningLine, double afternoonWarningLine, double nightWarningLine, LocalDate date, int marketId, Integer goodId);

    @Update("update recommendation set recommend_factor = #{factor} where good_id = #{goodId} and date = #{date} and market_id = #{marketId}")
    void updateFactor(double factor, int marketId, LocalDate date, Integer goodId);


    void updateWarningStatus(List<GoodData> problems, LocalDate date, Integer marketId);

    @Update("update storage set status = 0 where market_id = #{marketId} and date = #{date}")
    void refreshWarningStatus(Integer marketId, LocalDate date);

    @Update("update supermarket set image = #{image}," +
                                   "market_name = #{marketName}," +
                                   "address = #{address}," +
                                   "person = #{person}," +
                                   "phone = #{phone}," +
                                   "email = #{email} " +
                                "where id = #{id}")
    void updateMarket(Market market);

    List<MarketInfo> getAllMarketsInfo(LocalDate date);

    @Select("select id, recommend_factor, noon_warning_line, afternoon_warning_line, night_warning_line, date, market_id, good_id from recommendation ")
    List<Factor> selectAllFactors4Markets(Integer marketId, LocalDate localDate);

    @Select("select  noon_warning_line, afternoon_warning_line, night_warning_line from recommendation where date =#{nextDay} and market_id = #{marketId} and good_id = #{goodId}")
    WarningLine getWarningLine(LocalDate nextDay, int marketId, int goodId);
}
