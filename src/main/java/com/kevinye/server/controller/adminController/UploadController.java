package com.kevinye.server.controller.adminController;

import com.kevinye.pojo.result.Result;
import com.kevinye.utils.Aliyun.AliOssUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController("adminUploadController")
@RequestMapping("/admin")
public class UploadController {
    private final AliOssUtils aliOssUtils;
    public UploadController(AliOssUtils aliOssUtils) {
        this.aliOssUtils = aliOssUtils;
    }
    @PostMapping("/statistics/sheet")
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
}
