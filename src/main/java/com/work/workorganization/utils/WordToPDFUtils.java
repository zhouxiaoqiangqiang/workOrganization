package com.work.workorganization.utils;

import lombok.extern.slf4j.Slf4j;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import java.io.*;
 
/**
  * -@Desc: word转pdf工具类
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/30 16:26
 **/
@Slf4j
public class WordToPDFUtils {
 
    /**
     * @param wordFilePath word文件路径
     * @param PDFFilePath  pdf文件路径
     */
    public static void docxToPDF(String wordFilePath, String PDFFilePath){
        try {
            fileExists(PDFFilePath);
            OutputStream outPdf = new FileOutputStream(PDFFilePath);
            InputStream fileInputStream = new FileInputStream(wordFilePath);
            IConverter converter = LocalConverter.builder().build();
            log.debug("开始转换");
            converter.convert(fileInputStream).as(DocumentType.DOCX).to(outPdf).as(DocumentType.PDF).execute();
            converter.shutDown();
            log.debug("转换结束");
            outPdf.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断文件路径是否存在（如若不存在，则创建该路径）
     *
     * @param filePath 文件路径
     */
    private static File fileExists(String filePath) {
        File desc = new File(filePath);
        if (!desc.exists() && !desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        return desc;
    }
}