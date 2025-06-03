package com.kevinye.utils.excelUtils;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtils {
    public static void downloadAndAnalyze(String excelUrl, ExcelHandler handler) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        Resource resource = restTemplate.getForObject(excelUrl, Resource.class);
        List<List<String>> allData = new ArrayList<>();

        if (resource != null) {
            try (InputStream is = resource.getInputStream()) {
                EasyExcel.read(is, new AnalysisEventListener<Map<Integer, String>>() {
                            @Override
                            public void invoke(Map<Integer, String> rowMap, AnalysisContext context) {
                                // 把 LinkedHashMap<Integer, String> 转成 List<String>
                                List<String> rowList = new ArrayList<>();
                                int maxIndex = rowMap.keySet().stream().max(Integer::compareTo).orElse(-1);
                                for (int i = 0; i <= maxIndex; i++) {
                                    rowList.add(rowMap.getOrDefault(i, ""));
                                }
                                allData.add(rowList);
                            }

                            @Override
                            public void doAfterAllAnalysed(AnalysisContext context) {
                                // 不做任何额外操作
                            }
                        })
                        .sheet()              // 默认第一个 sheet
                        .headRowNumber(0)     // 不跳过任何行（包括表头），按需求自行调整
                        .doRead();
            }
        }
        log.info("allData = {}", allData);
        handler.handleSheet(allData);
    }
}