package com.work.workorganization.utils;


import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
  * -@Desc: 导出word工具类
  * -@Author: zhouzhiqiang
  * -@Date: 2025/5/12 14:18
 **/
@Component
@Data
@Slf4j
public class ExportWordUtil {

    private Configuration configuration;
    private String encoding;
    private String outputDir;//导出路径
    private String outputFileName;//导出文件名
    private String templateDir;//模板路径
    private String templateFileName;//模板名称


    public ExportWordUtil() {
        configuration = new Configuration();
        configuration.setDefaultEncoding(encoding);
    }

    public File createWord(Map<String,Object>dateMap){
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(this.getClass(), "");
        Template template;
        try {
            //得到模板
            configuration.setDirectoryForTemplateLoading(new File(templateDir));
            template = configuration.getTemplate(templateFileName);
        } catch (IOException e) {
            log.error("获取模板失败!");
            e.printStackTrace();
            return null;
        }
        try {
            //文件流输出
            File file = new File(outputDir + outputFileName);
            Writer out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            template.process(dateMap,out);
            out.close();
            return file;
        } catch (Exception e) {
            log.error("生成word失败!");
            e.printStackTrace();
            return null;
        }
    }
}
