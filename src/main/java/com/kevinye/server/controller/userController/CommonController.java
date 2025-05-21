package com.kevinye.server.controller.userController;

import com.kevinye.utils.Aliyun.AliOssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserCommonController")
@RequestMapping
@Slf4j
public class CommonController {
    private final AliOssUtils aliOssUtils;
    public CommonController(AliOssUtils aliOssUtils) {
        this.aliOssUtils = aliOssUtils;
    }
}
