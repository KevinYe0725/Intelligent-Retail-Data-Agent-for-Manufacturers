package com.kevinye.server.controller.userController;

import com.kevinye.pojo.Entity.GoodData;
import com.kevinye.pojo.VO.RecommendVO;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.mapper.DataMapper;
import com.kevinye.server.service.DataService;
import com.kevinye.utils.algorithm.MovingAverageUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController("UserDataController")
@RequestMapping("/user/data")
@Slf4j
public class DataController {
    private final DataService dataService;
    private final MovingAverageUtil movingAverageUtil;
    public DataController(DataService dataService, DataMapper dataMapper, MovingAverageUtil movingAverageUtil) {
        this.dataService = dataService;
        this.movingAverageUtil = movingAverageUtil;
    }
    @GetMapping("/{marketId}")
    public Result<List<GoodData>> getData(@PathVariable("marketId") Integer marketId,  LocalDate date) {
        List<GoodData> goodDataList = dataService.getData4Market(marketId, date);
        log.info("今日的货品数据 ------------------------------: {}", goodDataList);
        return Result.success(goodDataList);
    }

    @GetMapping("/downloads")
    public void downloads(HttpServletResponse response, LocalDate date,Integer marketId)  {
        try (Workbook workbook = new XSSFWorkbook()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
            Sheet sheet = workbook.createSheet(date.format(formatter) + "库存数据表");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("品类名");
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

    @GetMapping("/problem")
    public Result<List<GoodData>> getProblems(LocalDate date,Integer marketId){
        List<GoodData> problemData4Market = dataService.getProblemData4Market(date, marketId);
        return Result.success(problemData4Market);
    }

    @GetMapping("/recommend/{ids}")
    public Result<List<RecommendVO>> getRecommends(Integer marketId, LocalDate date, @PathVariable List<Integer> ids){
        log.info("date = {}, marketId = {}", date, marketId);
        if(ids.getFirst()==0){
            return Result.success(new ArrayList<RecommendVO>());
        }
        List<RecommendVO> recommendsByIds = dataService.getRecommendsByIds(ids, date, marketId);
        log.info("recommendsByIds: {}", recommendsByIds);
        return  Result.success(recommendsByIds);
    }

    @GetMapping("/recommend")
    public Result<List<RecommendVO>> getAllRecommends(Integer marketId, LocalDate date){
        return Result.success(movingAverageUtil.recommendOrders(marketId, date));

    }

}
