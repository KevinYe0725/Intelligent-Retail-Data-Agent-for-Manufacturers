package com.kevinye.utils.Aliyun;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliOssConfiguration {
    //bean注释的意义是将方法返回的对象作为bean注册仅容器中
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtils aliOssUtils(AliOssProperties aliOssProperties) {
        return new AliOssUtils(aliOssProperties.getEndpoint(),aliOssProperties.getAccessKeyId(),aliOssProperties.getAccessKeySecret(),aliOssProperties.getBucketName());
    }

}
