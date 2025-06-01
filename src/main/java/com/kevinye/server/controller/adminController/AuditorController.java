package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.Entity.Auditor;
import com.kevinye.pojo.VO.AuditorVO;
import com.kevinye.pojo.VO.MarketVO;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.AuditorService;
import com.kevinye.server.service.MarketService;
import com.kevinye.utils.excelUtils.ExcelUtils;
import com.kevinye.utils.excelUtils.excelHandlers.AuditorExcelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AuditorController {
    private final AuditorService auditorService;
    private final MarketService marketService;
    private final AuditorExcelHandler auditorExcelHandler;
    public AuditorController(AuditorService auditorService,MarketService marketService,AuditorExcelHandler auditorExcelHandler) {
        this.auditorService = auditorService;
        this.marketService = marketService;
        this.auditorExcelHandler = auditorExcelHandler;
    }

    @GetMapping("/staff")
    public Result<List<AuditorVO>> getAllAuditors(String name , String marketName) {
        List<AuditorVO> allAuditors = auditorService.getAllAuditors(name, marketName);
        return Result.success(allAuditors);
    }

    @PostMapping("/staff")
    public Result<String> addAuditor(@RequestBody Auditor auditor) {
        boolean b = auditorService.addAuditor(auditor);
        return Result.success(b ? "添加成功" : "添加失败");
    }

    @PutMapping("/staff")
    public Result<String> updateAuditor(@RequestBody Auditor auditor){
        boolean b = auditorService.updateAuditor(auditor);
        return Result.success(b ? "更新成功" : "更新失败");
    }

    @DeleteMapping("/staff")
    public Result<String> deleteAuditors(Integer auditorId){
        boolean b = auditorService.deleteAuditorById(auditorId);
        return Result.success(b ? "删除成功" : "删除失败");
    }

    @PostMapping("/staff/import")
    public Result<String> importAuditors(@RequestBody String excelUrl){
        if(!excelUrl.startsWith("https://kevinye-web.oss-cn-hangzhou.aliyuncs.com.")){
            return Result.success("不可上传非白名单url");
        }
        try {
            ExcelUtils.downloadAndAnalyze(excelUrl,auditorExcelHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success("添加成功");
    }

}
