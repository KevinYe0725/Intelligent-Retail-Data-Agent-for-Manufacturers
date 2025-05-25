package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.DTO.GoodDataDTO;
import com.kevinye.pojo.Entity.*;
import com.kevinye.pojo.VO.GoodsVO;
import com.kevinye.pojo.VO.WarningVO;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.DataService;
import com.kevinye.server.service.MarketService;
import com.kevinye.server.service.TimeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController("AdminMarketController")
@RequestMapping("/admin/market")
@Slf4j
public class MarketController {
    private final MarketService marketService;
    private final TimeService timeService;
    private final DataService dataService;
    public MarketController(MarketService marketService, TimeService timeService, DataService dataService) {
        this.marketService = marketService;
        this.timeService = timeService;
        this.dataService = dataService;
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

    @GetMapping("/problems")
    public Result<List<Problem>> getProblem(Integer marketId){
        List<Problem> problems = marketService.getProblems(marketId);
        return Result.success(problems);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response, Integer marketId,LocalDate date) throws IOException {
        Market marketById = marketService.getMarketById(marketId);
        try (Workbook workbook = new XSSFWorkbook()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
            Sheet sheet = workbook.createSheet(marketById.getMarketName()+date.format(formatter) + "库存数据表");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("品类");
            row.createCell(1).setCellValue("采购订单");
            row.createCell(2).setCellValue("中午剩余库存");
            row.createCell(3).setCellValue("下午剩余库存");
            row.createCell(4).setCellValue("夜晚剩余库存");
            List<GoodData> goodDataList = dataService.getData4Market(marketId, date);
            int i = 1;
            for (GoodData goodData : goodDataList) {
                Row dataRow = sheet.createRow(i++);
                dataRow.createCell(0).setCellValue(goodData.getGoodName());
                dataRow.createCell(1).setCellValue(goodData.getInitialGoods());
                dataRow.createCell(2).setCellValue(goodData.getNoonGoods()==null?"":String.valueOf(goodData.getNoonGoods()));
                dataRow.createCell(3).setCellValue(goodData.getAfternoonGoods()==null?"":String.valueOf(goodData.getAfternoonGoods()));
                dataRow.createCell(4).setCellValue(goodData.getNightGoods()==null?"":String.valueOf(goodData.getNightGoods()));
            }

            String fileName = URLEncoder.encode(date.format(formatter) + "库存数据表" + ".xlsx", StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/warning")
    public Result<List<WarningVO>> getWarning(Integer marketId,LocalDate date){
        List<WarningVO> warningList = marketService.getWarningList(marketId, date);
        return Result.success(warningList);
    }
}
