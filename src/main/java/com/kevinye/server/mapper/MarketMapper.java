package com.kevinye.server.mapper;

import com.kevinye.pojo.DTO.GoodDataDTO;
import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.GoodForMarket;
import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.Entity.Storage;
import com.kevinye.pojo.VO.GoodsVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MarketMapper {
    @Select(" select * from supermarket where id  = (select market_id from auditor where id = #{auditorId} )")
    Market getMarketByAuditorId(Integer auditorId);

    /**
     * 获取当前未完成状态货品
     * @param marketId 商店Id
     * @param status 状态
     * @param date 日期
     * @return 所有未完成库存数据
     */
    @Select("select s.id, s.market_id, s.good_id, s.initial_goods, s.noon_goods, s.afternoon_goods, s.night_goods, s.date, s.status, g.good_name, g.image  from storage s inner join goods g on s.good_id = g.id where s.market_id = #{marketId} and s.date = #{date} and status = #{status}")
    List<Storage> getAllUnfinishedGoods4Market(Integer marketId, Integer status, LocalDate date);

    /**
     * @param marketId 商店id
     * @param date 日期
     * @return 该商店当日所有数据
     */
    @Select("select s.id, s.market_id, s.good_id, s.initial_goods, s.noon_goods, s.afternoon_goods, s.night_goods, s.date, s.status, g.good_name, g.image  from storage s inner join goods g on s.good_id = g.id where s.market_id = #{marketId} and s.date = #{date}")
    List<Storage> getAllGoods4Market(Integer marketId, LocalDate date);

    @Select("select * from supermarket")
    List<Market> getAllMarket();


    List<Market> getMarketByName(String marketName);

    @Insert("insert storage (market_id, good_id, initial_goods, date) values (#{marketId},#{goodId},#{initialGoods},#{date})")
    void insertGood(GoodForMarket goodByName);

    @Update("update storage set initial_goods = #{initialGoods},noon_goods = #{noonGoods},afternoon_goods = #{afternoonGoods},night_goods = #{nightGoods} where market_id = #{marketId} and date = #{date} and good_id = #{goodId}")
    void updateGoodInformation(GoodDataDTO goodDataDTO);

    @Delete("delete from storage where good_id = #{goodId} and market_id = #{marketId} and date = #{date}")
    void deleteGoodInformationFromMarket(Integer marketId, Integer goodId, LocalDate date);

    @Select("select good_name,id as goodId from goods where good_name like concat('%',#{goodName},'%')")
    List<GoodsVO> getAllGoodChoice(String goodName);
}
