package com.kevinye.server.mapper;

import com.kevinye.pojo.DTO.UploadDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UploadMapper {
    @Update("update storage set status = 1, noon_goods = if(#{period} = 1,#{remaining},noon_goods) ,afternoon_goods = if(#{period} = 2,#{remaining},afternoon_goods),night_goods = if(#{period} = 3,#{remaining},night_goods) where market_id =#{marketId} and good_id = #{goodId} and date =#{date}")
    void uploadRemainingByPeriod(UploadDTO uploadDTO);

    @Select("select id from storage where market_id = #{marketId} and good_id = #{goodId} and date = #{date}")
    Integer selectStorageIdByIds(UploadDTO uploadDTO);

    @Insert("insert into auditor_storage_date (auditorId, storageId, period) values (#{auditorId},#{storageId},#{period})")
    void uploadAuditor(UploadDTO uploadDTO);

    @Select("select auditorId from auditor_storage_date where period= #{period} and storageId = #{storageId}")
    Integer selectAuditorId(UploadDTO uploadDTO);
}
