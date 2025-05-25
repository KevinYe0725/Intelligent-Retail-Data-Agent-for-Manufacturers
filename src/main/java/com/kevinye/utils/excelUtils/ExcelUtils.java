package com.kevinye.utils.excelUtils;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    public static void downloadAndAnalyze(String excelUrl,ExcelHandler handler )throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        Resource resource = restTemplate.getForObject(excelUrl, Resource.class);
        List<List<String>> allData = new ArrayList<>();
        //使用Listener来将所有的行数据填入allData的数据表中
        if (resource != null) {
            try(InputStream is = resource.getInputStream()){
                EasyExcel.read(is, new AnalysisEventListener<List<String>>() {
                    @Override
                    public void invoke(List<String> row, AnalysisContext analysisContext) {
                        allData.add(row);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                    }
                }).sheet().headRowNumber(0).doRead();
            }
        }
        handler.handleSheet(allData);
    }
}
