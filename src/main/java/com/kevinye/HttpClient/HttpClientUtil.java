package com.kevinye.HttpClient;

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil{
    //5000ms为超时时间，也就是5秒
    static final int TIMEOUT =  5*1000;
    public static String doGet(String url , Map<String,String> paramMap){
        //创建HttpClient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String result = "";
        CloseableHttpResponse response = null;
        try{
            //URI是唯一资源标识符
            //URL事唯一资源地址符
            //URL是URI的一种分类，在此处通过传进来的参数构建URL
            URIBuilder uriBuilder = new URIBuilder(url);
            if(paramMap != null){
                for(String key : paramMap.keySet()){
                    uriBuilder.addParameter(key, paramMap.get(key));
                }
            }
            URI uri = uriBuilder.build();
            HttpGet httpGet  = new HttpGet(uri);

            response = httpclient.execute(httpGet);

            //检查返回状态码是否为200 ，是则返回成功传送
            if(response.getStatusLine().getStatusCode() == 200){
                result = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
                httpclient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String doPost(String url , Map<String,String>paramMap){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = "";
        CloseableHttpResponse response = null;
        try{
            HttpPost httpPost = new HttpPost(url);
            if(paramMap != null){
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    paramList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(urlEncodedFormEntity);
            }
            httpPost.setConfig(builderRequestConfig());
            response = httpclient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(),"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                response.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String doPost4Json(String url, Map<String,String>paramMap) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        try{
            HttpPost httpPost = new HttpPost(url);
            if(paramMap != null){
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    jsonObject.put(param.getKey(),param.getValue());
                }
                StringEntity entity = new StringEntity(jsonObject.toString(),"UTF-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            httpPost.setConfig(builderRequestConfig());
            response = httpclient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(),"UTF-8");
        }catch (Exception e){
            throw e;
        }finally {
            try {
                response.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }
    private static RequestConfig builderRequestConfig(){
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build();
    }
}
