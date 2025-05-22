package com.kevinye.server.controller.userController;

import com.kevinye.pojo.DTO.UploadDTO;
import com.kevinye.pojo.result.Result;
import com.kevinye.server.service.TimeService;
import com.kevinye.server.service.UploadService;
import com.kevinye.utils.Aliyun.AliOssUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController("UserUploadController")
@RequestMapping("/user/upload")
public class UploadController {

    private final AliOssUtils aliOssUtils;
    private final TimeService timeService;
    private final UploadService uploadService;

    public UploadController(AliOssUtils aliOssUtils, TimeService timeService, UploadService uploadService) {
        this.aliOssUtils = aliOssUtils;
        this.timeService = timeService;
        this.uploadService = uploadService;
    }


    @PostMapping("/photo")
    public Result<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        String imageUrl = null;
        try {
            String suffix ="" ;
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString().replace("-","")+ suffix;
             imageUrl = aliOssUtils.upload(file.getBytes(), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(imageUrl);
    }

    /**
     * 按照时间段上传当前库存数据
     * @param uploadDTO
     * @return
     */
    @PostMapping
    public Result<String> uploadStatistics( @RequestBody UploadDTO uploadDTO){
        //将当前时段的数据加入进去
        uploadDTO.setDate(LocalDate.now());
        uploadDTO.setPeriod(timeService.getTimePeriod(LocalDateTime.now()));
        boolean b = uploadService.uploadRemaining(uploadDTO);
        if(b){
            return Result.success("上传成功");
        }
        return Result.error("不在上传时间段内");
    }

    @PutMapping
    public Result<String> updateStatistics(@RequestBody UploadDTO uploadDTO){
        uploadDTO.setDate(LocalDate.now());
        uploadDTO.setPeriod(timeService.getTimePeriod(LocalDateTime.now()));
        if(uploadDTO.getPeriod().equals(-1)){
            return Result.error("不在操作时间段内");
        }
        boolean b = uploadService.updateRemaining(uploadDTO);
        if(!b){
            return Result.error("今日不得更换人员进行操作");
        }
        return Result.success("上传成功");
    }
}
