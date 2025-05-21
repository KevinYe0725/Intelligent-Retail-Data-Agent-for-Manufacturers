package com.kevinye.server.controller.userController;

import com.kevinye.pojo.VO.GoodVO;
import com.kevinye.pojo.VO.MarketVO;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.MarketService;
import com.kevinye.server.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import java.util.List;

@RestController("UserMarketController")
@Slf4j
@RequestMapping("/user")
public class MarketController {
    private final MarketService marketService;
    private final TimeService timeService;

    public  MarketController(MarketService marketService , TimeService timeService) {
        this.marketService = marketService;
        this.timeService = timeService;
    }

    @GetMapping("/main/market")
    public Result<MarketVO> getMarket(Integer auditorId) {
        MarketVO marketVO = marketService.getMarketByAuditorId(auditorId);
        return Result.success(marketVO);
    }

    @GetMapping("/main/goods")
    public Result<List<GoodVO>> getGoods(Integer marketId,@RequestParam(required = false ,defaultValue = "1") Integer status) {
        LocalDateTime now = LocalDateTime.now();
        Integer period = timeService.getTimePeriod(now);
        if(period ==-1){
            return Result.error("不在提交时间段");
        }
        List<GoodVO> goods4Market = marketService.getGoods4Market(marketId, status, period);
        return Result.success(goods4Market);
    }
}
