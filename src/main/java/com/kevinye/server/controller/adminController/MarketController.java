package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.DTO.GoodDataDTO;
import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.GoodData;
import com.kevinye.pojo.Entity.GoodForMarket;
import com.kevinye.pojo.Entity.GoodInfo;
import com.kevinye.pojo.VO.GoodsVO;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.MarketService;
import com.kevinye.server.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController("AdminMarketController")
@RequestMapping("/admin/market")
@Slf4j
public class MarketController {
    private final MarketService marketService;
    private final TimeService timeService;
    public MarketController(MarketService marketService, TimeService timeService) {
        this.marketService = marketService;
        this.timeService = timeService;
    }

    /**
     * 获取该商店中某天的所有品类数据
     * @param date 日期
     * @param marketId 商店ID
     * @return 货品数据list
     */
    @GetMapping("/good")
    public Result<List<GoodData>> getGoods4Market(LocalDate date,Integer marketId){
        if(marketId == null||date==null){
            throw new RuntimeException("数据不得为空");
        }
        List<GoodData> allGoods4Market = marketService.getAllGoods4Market(marketId, date);
        return Result.success(allGoods4Market);
    }

    @PostMapping("/good/add")
    public Result<String> addGoods(@RequestBody GoodForMarket goodForMarket){
        marketService.addNewGood4Market(goodForMarket);
        return Result.success("添加成功");
    }

    @PutMapping("/good")
    public Result<String> updateGoodInformation(@RequestBody GoodDataDTO goodDataDTO){
        marketService.updateGoodInformation(goodDataDTO);
        return Result.success("更新成功");
    }

    @DeleteMapping("/good")
    public Result<String> deleteGoodInformationFromMarket(Integer marketId,Integer goodId,LocalDate date){
        marketService.deleteGoodInformationFromMarket(marketId,goodId,date);
        return Result.success("删除成功");
    }

    @GetMapping("/good/allgoods")
    public Result<List<GoodsVO>> getAllGoodChoices(String goodName){
        List<GoodsVO> allGoods = marketService.getAllGoods(goodName);
        return Result.success(allGoods);
    }
}
