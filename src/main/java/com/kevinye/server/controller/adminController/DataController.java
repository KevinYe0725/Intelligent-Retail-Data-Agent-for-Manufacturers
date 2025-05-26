package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.Entity.Market;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.DataService;
import com.kevinye.utils.excelUtils.ExcelUtils;
import com.kevinye.utils.excelUtils.excelHandlers.GoodsExcelHandler;
import com.kevinye.utils.excelUtils.excelHandlers.MarketExcelHandler;
import com.kevinye.utils.excelUtils.excelHandlers.SingleMarketDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController("AdminDataController")
@RequestMapping("/admin/statistics")
@Slf4j
public class DataController {
    private final DataService dataService;
    private final SingleMarketDataHandler singleMarketDataHandler;
    private final MarketExcelHandler marketExcelHandler;
    private final GoodsExcelHandler goodsExcelHandler;
    public DataController(DataService dataService, SingleMarketDataHandler singleMarketDataHandler, MarketExcelHandler marketExcelHandler, GoodsExcelHandler goodsExcelHandler) {
        this.dataService = dataService;
        this.singleMarketDataHandler = singleMarketDataHandler;
        this.marketExcelHandler = marketExcelHandler;
        this.goodsExcelHandler = goodsExcelHandler;
    }

    @GetMapping("/market/all")
    public Result<List<Market>> getAllMarket(String marketName) {
        List<Market> allMarkets = dataService.getAllMarkets(marketName);
        return Result.success(allMarkets);
    }

    @GetMapping("/market/{id}")
    public Result<Market> getMarketById(@PathVariable Integer id) {
        Market marketById = dataService.getMarketById(id);
        return Result.success(marketById);
    }

    @PostMapping("/market/add")
    public Result<String> addNewMarket(@RequestBody Market market) {
        dataService.addNewMarket(market);
        return Result.success("添加成功");
    }

    @DeleteMapping("/market/{ids}")
    public Result<String> deleteMarkets(@PathVariable List<Integer>ids){
        if(ids==null || ids.isEmpty()){
            return Result.error("id不得为空");
        }
        dataService.deleteMarkets(ids);
        return Result.success("删除成功");
    }

    @PutMapping("/market")
    public Result<String> updateMarket(@RequestBody Market market){
        if(market.getMarketName()==null || market.getMarketName().isEmpty() ||market.getAddress().isEmpty()){
            return Result.error("必需数据不得为空");
        }
        dataService.updateMarket(market);
        return Result.success("修改成功");
    }

    @PostMapping("/upload")
    public Result<String> importDailyData4Market(String excelUrl){
        if(!excelUrl.startsWith("https://kevinye-web.oss-cn-hangzhou.aliyuncs.com.")){
            return Result.success("不可上传非白名单url");
        }
        try {
            ExcelUtils.downloadAndAnalyze(excelUrl,singleMarketDataHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success("添加成功");
    }

    @PostMapping("/uploadAll")
    public Result<String> importMarketsOrMarketsData(String excelUrl ,Integer type){
        if(!excelUrl.startsWith("https://kevinye-web.oss-cn-hangzhou.aliyuncs.com.")){
            return Result.success("不可上传非白名单url");
        }
        try {
            if (type == 1) {
                ExcelUtils.downloadAndAnalyze(excelUrl, marketExcelHandler);
            }else if (type == 2) {
                ExcelUtils.downloadAndAnalyze(excelUrl, goodsExcelHandler);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success("添加成功");
    }
}
