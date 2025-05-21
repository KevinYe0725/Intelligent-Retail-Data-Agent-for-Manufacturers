package com.kevinye.utils.Aliyun;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Slf4j
public class AliOssUtils {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 将文件以及文件名上传至AliyunOSS的服务器
     * @param bytes 将文件转换为字节数组
     * @param objectName 对象名（大概率以UUID形式生成的随机对象名）
     * @return the URL of the file in the aliyun oss
     */
  public String upload(byte[] bytes , String objectName){
      //根据endpoint ，ID ，key获得一个可以操作的OSS（对象存储服务）的类
      OSS ossClient  = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

      try{
          ossClient.putObject(bucketName,objectName,new ByteArrayInputStream(bytes));
      } catch (OSSException oe) {
          System.out.println("Caught an OSSException, which means your request made it to OSS, "
                  + "but was rejected with an error response for some reason.");
          System.out.println("Error Message:" + oe.getErrorMessage());
          System.out.println("Error Code:" + oe.getErrorCode());
          System.out.println("Request ID:" + oe.getRequestId());
          System.out.println("Host ID:" + oe.getHostId());
      } catch (ClientException ce) {
          System.out.println("Caught an ClientException, which means the client encountered "
                  + "a serious internal problem while trying to communicate with OSS, "
                  + "such as not being able to access the network.");
          System.out.println("Error Message:" + ce.getMessage());
      }finally{
          //假如说Oss没有被创建就被报错，那么就无需将其关闭
          if(ossClient!=null){
              ossClient.shutdown();
          }
      }

      StringBuilder stringBuilder = new StringBuilder("https://");
      endpoint = endpoint.replace("http://", "");
      stringBuilder.append(bucketName).append(".").append(endpoint).append(".").append(objectName);
      log.info("文件上传到：{}",stringBuilder.toString());

      //返回Url
      return stringBuilder.toString();
  }
}
