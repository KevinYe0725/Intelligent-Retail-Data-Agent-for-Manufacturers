package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.Entity.DayData;
import com.kevinye.pojo.Entity.GoodGrowth;
import com.kevinye.pojo.Entity.MarketGrowth;
import com.kevinye.pojo.Entity.MonthData;
import com.kevinye.pojo.VO.TopGoodVO;
import com.kevinye.pojo.VO.TopMarketVO;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.TotalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController("AdminTotalController")
@RequestMapping("admin/total")
@Slf4j
public class TotalController {
    private final TotalService totalService;
    public TotalController(TotalService totalService) {
        this.totalService = totalService;
    }

    @GetMapping("/top/goods")
    public Result<List<TopGoodVO>> getSortedGoodByNumber(LocalDate date) {
        List<TopGoodVO> sortedGoodByNumber = totalService.getSortedGoodByNumber(date);
        return Result.success(sortedGoodByNumber);
    }

    @GetMapping("/top/market")
    public Result<List<TopMarketVO>> getSortedMarketByNumber(LocalDate date) {
        List<TopMarketVO> sortedMarketByNumber = totalService.getSortedMarketByNumber(date);
        return Result.success(sortedMarketByNumber);
    }

    @GetMapping("/month")
    public Result<List<DayData>> getMonthData(LocalDate date, Integer goodId) {
        List<DayData> monthData = totalService.getMonthData(date, goodId);
        return Result.success(monthData);
    }

    @GetMapping("/year")
    public Result<List<MonthData>> getYearData(LocalDate date,Integer goodId){
        List<MonthData> yearData = totalService.getYearData(date, goodId);
        log.info("年数据{}",yearData);
        return Result.success(yearData);
    }

    @GetMapping("/growth/market")
    public Result<List<MarketGrowth>> getMarketData(LocalDate date){
        List<MarketGrowth> growthOfMarkets = totalService.getGrowthOfMarkets(date);
        return Result.success(growthOfMarkets);
    }

    @GetMapping("/growth/good")
    public Result<List<GoodGrowth>> getGoodData(LocalDate date){
        List<GoodGrowth> growthOfGoods = totalService.getGrowthOfGoods(date);
        return Result.success(growthOfGoods);
    }
}
