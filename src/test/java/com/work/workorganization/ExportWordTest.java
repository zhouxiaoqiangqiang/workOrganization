package com.work.workorganization;

import com.work.workorganization.utils.ExportWordUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class ExportWordTest {

    @Resource
    FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 导出word
     */
    @Test
    public void exportNewValvePressure1() throws Exception {
        // TODO 获取数据源，查询需要动态加入的数据

        // 文件名称
        String fileName =  "非TA份额化单一计划申赎交易确认单模板.doc";
        Map<String, Object> dataMap = new HashMap<>();
        // 这里的key要与FTL模板中设置的${xxx}的值对应。
        dataMap.put("productName", "国泰君安xxxx产品");
        dataMap.put("applicationDate", "2025-05-12");
        dataMap.put("confirmDate", "2025-05-11");
        dataMap.put("businessType", "认购");
        dataMap.put("confirmAmount", "10000");
        dataMap.put("confirmShare", "20000");
        dataMap.put("confirmNetAmount", "30000");
        dataMap.put("averageNav", "18888");
        dataMap.put("performancePay", "29999");
        dataMap.put("createdate", "2025-05-12");
        ExportWordUtil exportWordUtil = new ExportWordUtil();
        exportWordUtil.setEncoding("UTF-8");
        exportWordUtil.setTemplateDir("E:\\");
        exportWordUtil.setTemplateFileName("非TA份额化单一计划申赎交易确认单模板.ftl");
        exportWordUtil.setOutputDir("E:\\");
        exportWordUtil.setOutputFileName("非TA份额化单一计划申赎交易确认单模板.doc");
        File file = exportWordUtil.createWord(dataMap);
    }


}
