package com.kevinye.server.service.serviceImpl;

import com.kevinye.pojo.Entity.DayData;
import com.kevinye.pojo.Entity.GoodGrowth;
import com.kevinye.pojo.Entity.MarketGrowth;
import com.kevinye.pojo.Entity.MonthData;
import com.kevinye.pojo.VO.TopGoodVO;
import com.kevinye.pojo.VO.TopMarketVO;
import com.kevinye.server.mapper.TotalMapper;
import com.kevinye.server.service.TotalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class TotalServiceImpl implements TotalService {
    private final TotalMapper totalMapper;
    public TotalServiceImpl(TotalMapper totalMapper) {
        this.totalMapper = totalMapper;
    }

    @Override
    public List<TopGoodVO> getSortedGoodByNumber(LocalDate date) {
        LocalDate beginDate = date.minusDays(30);
        return totalMapper.getTopGoodList(beginDate, date);
    }

    @Override
    public List<TopMarketVO> getSortedMarketByNumber(LocalDate date) {
        LocalDate beginDate = date.minusDays(30);
        return totalMapper.getTopMarketList(beginDate,date);
    }

    @Override
    public List<DayData> getMonthData(LocalDate date, Integer goodId) {
        LocalDate twoMonthAgoFirstDay = date.minusMonths(1).withDayOfMonth(1);
        return totalMapper.getMonthData(twoMonthAgoFirstDay, date, goodId);
    }

    @Override
    public List<MonthData> getYearData(LocalDate date, Integer goodId) {
        LocalDate twoYearAgoDate = date.minusYears(2);

        return totalMapper.getYearData(twoYearAgoDate,date,goodId);
    }

    @Override
    public List<MarketGrowth> getGrowthOfMarkets(LocalDate date) {
        LocalDate endDateOfLastMonth = date.withDayOfMonth(1).minusDays(1);
        LocalDate beginDateOfLastMonth = date.minusMonths(1).withDayOfMonth(1);
        LocalDate endDateOfLast2Month = beginDateOfLastMonth.minusDays(1);
        LocalDate beginDateOfLast2Month = endDateOfLast2Month.withDayOfMonth(1);
        List<MarketGrowth> lastMonthSales = totalMapper.getMonthSales4Markets(beginDateOfLastMonth, endDateOfLastMonth);
        List<MarketGrowth> last2monthSales = totalMapper.getMonthSales4Markets(beginDateOfLast2Month, endDateOfLast2Month);
        if(lastMonthSales.isEmpty()||last2monthSales.isEmpty()){
            throw new RuntimeException("数据不足");
        }
        for (MarketGrowth lastMonthSale : lastMonthSales) {
            Integer marketId = lastMonthSale.getMarketId();
            for (MarketGrowth last2monthSale : last2monthSales) {
                if(last2monthSale.getMarketId().equals(marketId)){
                    log.info("lastMonthSale :{},last2monthSale :{}", lastMonthSale, last2monthSale);
                    lastMonthSale.setMonthGrowth((lastMonthSale.getMonthSales()-last2monthSale.getMonthSales())/last2monthSale.getMonthSales());
                }
            }
        }

        lastMonthSales.removeIf(item->item.getMonthGrowth()==null);
        lastMonthSales.sort((o1, o2) ->Double.compare(o2.getMonthGrowth(),o1.getMonthGrowth()));
        return lastMonthSales;
    }

    @Override
    public List<GoodGrowth> getGrowthOfGoods(LocalDate date) {
        LocalDate endDateOfLastMonth = date.withDayOfMonth(1).minusDays(1);
        LocalDate beginDateOfLastMonth = date.minusMonths(1).withDayOfMonth(1);
        LocalDate endDateOfLast2Month = beginDateOfLastMonth.minusDays(1);
        LocalDate beginDateOfLast2Month = endDateOfLast2Month.withDayOfMonth(1);
        List<GoodGrowth> monthSales4Goods = totalMapper.getMonthSales4Goods(beginDateOfLastMonth, endDateOfLastMonth);
        List<GoodGrowth> monthSales4Goods1 = totalMapper.getMonthSales4Goods(beginDateOfLast2Month, endDateOfLast2Month);
        if(monthSales4Goods.isEmpty()||monthSales4Goods1.isEmpty()){
            throw new RuntimeException("数据不足");
        }
        for (GoodGrowth monthSales4Good : monthSales4Goods) {
            Integer goodId = monthSales4Good.getGoodId();
            for (GoodGrowth goodGrowth : monthSales4Goods1) {
                if(goodGrowth.getGoodId().equals(goodId)) {
                    log.info("monthSales4Good:{},goodGrowth:{}",monthSales4Good,goodGrowth);
                    monthSales4Good.setGrowth((monthSales4Good.getGoodSales()-goodGrowth.getGoodSales())/goodGrowth.getGoodSales());
                }
            }
        }
        monthSales4Goods.removeIf(item->item.getGrowth()==null);
        log.info("monthSales4Goods:{}",monthSales4Goods);
        monthSales4Goods.sort((o1,o2) -> Double.compare(o2.getGrowth(), o1.getGrowth()));
        return monthSales4Goods;
    }
}
