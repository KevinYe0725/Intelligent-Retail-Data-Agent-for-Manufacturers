package com.kevinye.server.mapper;

import com.kevinye.pojo.DTO.GoodDataDTO;
import com.kevinye.pojo.Entity.*;
import com.kevinye.pojo.VO.GoodsVO;
import com.kevinye.pojo.VO.WarningVO;
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

    /**
     * 获得所有的商店
     * @return 商家list
     */
    @Select("select * from supermarket")
    List<Market> getAllMarket();

    /**
     * 根据名字获取marketList
     * @param marketName 商家部分字段
     * @return 有关商家列表
     */
    List<Market> getMarketByName(String marketName);

    /**
     * 初始化加入库存
     * @param goodByName 货品相关信息
     */
    @Insert("insert storage (market_id, good_id, initial_goods, date) values (#{marketId},#{goodId},#{initialGoods},#{date})")
    void insertGood(GoodForMarket goodByName);

    /**
     *  更新某天的货品数据
     * @param goodDataDTO 货品数据
     */
    @Update("update storage set initial_goods = #{initialGoods},noon_goods = #{noonGoods},afternoon_goods = #{afternoonGoods},night_goods = #{nightGoods} where market_id = #{marketId} and date = #{date} and good_id = #{goodId}")
    void updateGoodInformation(GoodDataDTO goodDataDTO);

    /**
     * 删除某天某条商家货品数据
     * @param marketId 商店ID
     * @param goodId 货品ID
     * @param date 日期
     */
    @Delete("delete from storage where good_id = #{goodId} and market_id = #{marketId} and date = #{date}")
    void deleteGoodInformationFromMarket(Integer marketId, Integer goodId, LocalDate date);

    /**
     *  根据name获取货品类别列表
     * @param goodName 货品名字部分字段
     * @return 货品基础信息
     */

    List<GoodsVO> getAllGoodChoice(String goodName);

    /**
     * 获得该商店的所有问题（包括历史信息）
     * @param marketId 商店
     * @return 历史问题
     */
    @Select("SELECT " +
            "  p.id AS problemId, " +
            "  p.market_id, " +
            "  p.auditor_id, " +
            "  a.auditor_name AS auditorName, " +   // 新增：审计员名称
            "  p.content, " +
            "  p.datetime, " +
            "  p.image " +
            "FROM problem p " +
            "INNER JOIN auditor a ON p.auditor_id = a.id " +
            "WHERE p.market_id = #{marketId} " +
            "order by datetime desc"

)
    List<Problem> getProblemStorage4Market(Integer marketId);

    /**
     * 获得某一个商店的信息
     * @param marketId 商店的ID
     * @return 单个商店类
     */
    @Select("select * from supermarket where id = #{id}")
    Market getMarketById(Integer marketId);

    /**
     * 获取某天某商店的商店信息
     * @param marketId 商店ID
     * @param date 日期
     * @return 库存警告的相关库存信息
     */
    @Select("SELECT" +
            "    s.good_id,\n" +
            "    g.good_name,\n" +
            "    s.initial_goods,\n" +
            "    a.auditor_name,\n" +
            "    s.assignment_status,\n" +
            "    s.id as storageId \n" +
            "FROM\n" +
            "    storage s,\n" +
            "    goods g,\n" +
            "    auditor_storage_date asd,\n" +
            "    auditor a\n" +
            "WHERE\n" +
            "    s.market_id = #{marketId}\n" +
            "    AND s.date = #{date}\n" +
            "    AND s.status = 1\n" +
            "    AND s.good_id = g.id\n" +
            "    AND asd.storageId = s.id\n" +
            "    AND a.id = asd.auditorId\n ")
    List<WarningVO> getWarningGoods(Integer marketId, LocalDate date);

    /**
     *  获得某店近段时间有问题的库存次数
     * @param goodId 货品ID
     * @param beginDate 起始日子
     * @param endDate 结束日子
     * @param marketId 商店ID
     * @return  告警次数
     */
    @Select("select count(*) from storage where date between #{beginDate} and #{endDate} and good_id = #{goodId} and market_id = #{marketId} and status = 1")
    Integer getCount(Integer goodId,LocalDate beginDate, LocalDate endDate, Integer marketId);

    /**
     * 获得中午库存
     * @param marketId 商店ID
     * @param date 日期
     * @return 中午库存
     */
    @Select("select noon_goods from storage  where id = market_id and date = #{date}")
    Integer getNoonRemaining(Integer marketId, LocalDate date);

    /**
     * 获得下午库存
     * @param marketId 商店ID
     * @param date 日期
     * @return 下午库存
     */
    @Select("select afternoon_goods from storage  where id = market_id and date = #{date}")
    Integer getAfterNoonRemaining(Integer marketId, LocalDate date);

    /**
     * 获得晚上剩余库存
     * @param marketId 商店ID
     * @param date 日期
     * @return 晚上剩余库存
     */
    @Select("select night_goods from storage  where id = market_id and date = #{date}")
    Integer getNightRemaining(Integer marketId, LocalDate date);


    List<Storage> getStorageByIds(List<Integer> storageIds);

    List<MarketData> getMarketDataOfDate(Integer marketId, LocalDate date);

    @Insert("insert into supermarket (id, market_name, image, address, phone, person, email) VALUE (#{id},#{marketName},#{image},#{address},#{phone},#{person},#{email})")
    void addNewMarket(Market market);

    List<Integer> getInformation4Market(List<Integer> ids);

    void deleteMarkets(List<Integer> ids);

    void importNewStorage(List<Storage> storageList);

    void importNewMarkets(List<Market> marketList);

    List<DayData> getMonthData(LocalDate beginDate, LocalDate endDate, Integer marketId, Integer goodId);
}
