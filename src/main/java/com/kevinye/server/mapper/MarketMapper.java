package com.kevinye.server.mapper;

import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.Entity.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    @Select("select s.id, s.market_id, s.good_id, s.initial_good, s.noon_goods, s.afternoon_goods, s.night_goods, s.date, s.status, g.good_name, g.image  from storage s inner join goods g on s.good_id = g.id where s.market_id = #{marketId} and s.date = #{date} and status = #{status}")
    List<Storage> getAllUnfinishedGoods4Market(Integer marketId, Integer status, LocalDate date);

    /**
     * @param marketId 商店id
     * @param date 日期
     * @return 该商店当日所有数据
     */
    @Select("select s.id, s.market_id, s.good_id, s.initial_good, s.noon_goods, s.afternoon_goods, s.night_goods, s.date, s.status, g.good_name, g.image  from storage s inner join goods g on s.good_id = g.id where s.market_id = #{marketId} and s.date = #{date}")
    List<Storage> getAllGoods4Market(Integer marketId, LocalDate date);
}
