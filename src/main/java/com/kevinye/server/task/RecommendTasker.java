package com.kevinye.server.task;



import com.kevinye.pojo.Entity.Market;
import com.kevinye.server.service.MarketService;
import com.kevinye.utils.algorithm.MovingAverageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;


@Component
@Slf4j
public class RecommendTasker implements Runnable {
    private final MovingAverageUtil movingAverageUtil;
    private final MarketService marketService;
    public RecommendTasker(MovingAverageUtil movingAverageUtil, MarketService marketService) {
        this.movingAverageUtil = movingAverageUtil;
        this.marketService = marketService;
    }

    /**
     * 我不打算将其存入数据库中，首先它并不会减少太多IO，并且有利于之后的算法更迭
     * 每次在结束上传时间的时候更新recommendation
     */
    @Override
    public void run() {
        LocalDate date = LocalDate.now();
        log.error("经过了RecommendTasker");
        List<Market> allMarket = marketService.getAllMarket();
        for (Market market : allMarket) {
            Integer marketId = market.getId();
            movingAverageUtil.updateWarningLine(marketId,date);
            movingAverageUtil.recommendOrders(marketId, date);
        }
    }
}
