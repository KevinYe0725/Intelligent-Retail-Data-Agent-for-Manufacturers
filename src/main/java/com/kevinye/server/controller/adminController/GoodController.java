package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.Entity.Good;
import com.kevinye.pojo.Entity.GoodInfo;
import com.kevinye.pojo.Entity.ImportRequest;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.GoodService;
import com.kevinye.utils.excelUtils.ExcelUtils;
import com.kevinye.utils.excelUtils.excelHandlers.GoodsExcelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController("AdminGoodController")
@RequestMapping("/admin/good")
@Slf4j
public class GoodController {

    private final GoodService goodService;
    private final GoodsExcelHandler goodsExcelHandler;
    public GoodController(GoodService goodService, GoodsExcelHandler goodsExcelHandler) {
        this.goodService = goodService;
        this.goodsExcelHandler = goodsExcelHandler;
    }

    @GetMapping
    public Result<List<GoodInfo>> getGood(LocalDate date) {
        if (date == null) {
            Result.error("日期不可为空");
        }
        return Result.success(goodService.getGoodInformation(date));
    }

    @PostMapping
    public Result<String> addGood(@RequestBody  Good good) {
        if (good == null) {
            return  Result.error("新增不可为空");
        }
        goodService.addNewGood(good);
        return Result.success("上传成功");

    }

    @DeleteMapping
    public Result<String> deleteGood(Integer goodId) {
        if (goodId == null) {
            return  Result.error("id不得为空");
        }
        goodService.deleteGoodById(goodId);
        return Result.success("删除成功");
    }
    @PutMapping
    public Result<String> updateGood(@RequestBody Good good) {
        if (good == null) {
            return  Result.error("货品信息不得为空");
        }
        goodService.updateGood(good);
        return Result.success("更新成功");
    }
    @PostMapping("/import")
    public Result<String> importGood(@RequestBody ImportRequest req) {
        String excelUrl = req.getExcelUrl();
        try {
            ExcelUtils.downloadAndAnalyze(excelUrl,goodsExcelHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success("添加成功");
    }


}
