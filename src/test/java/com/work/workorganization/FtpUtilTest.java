package com.work.workorganization;

import cn.hutool.core.collection.CollectionUtil;
import com.work.workorganization.utils.FtpUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

@SpringBootTest
@Slf4j
public class FtpUtilTest {

    /**
     * 测试从FTP下载文件
     */
    @Test
    public void testDownloadFileFromFtp() {
        String ftpFileName = "测试文件.txt";
        String ftpFilePath = "/home/testFtp";
        String saveLocalPath = "E:\\";
        File file = FtpUtils.downLoadFileFromFtp(ftpFileName, ftpFilePath, saveLocalPath);
        if (null == file || !file.exists()) {
            System.out.println("FTP下载文件失败!");
        } else {
            System.out.println("FTP下载文件成功!");
        }

    }

    /**
     * 测试从FTP下载文件
     */
    @Test
    public void testUploadFileToFtp() {
        String ftpFileName = "测试上传文件.txt";
        String fileLocalPath = "E:\\";
        String ftpSaveFilePath = "/home/testFtp";
        File file = new File(fileLocalPath + ftpFileName);
        boolean isSuccess = FtpUtils.uploadFileToFtp(file, ftpSaveFilePath);
        if (isSuccess) {
            System.out.println("FTP上传文件成功!");
        } else {
            System.out.println("FTP上传文件失败!");
        }
    }

    /**
     * 查看FTP目录下的文件名称
     */
    @Test
    public void testSelectFileNamesFromFtp() {
        String ftpSaveFilePath = "/home/testFtp";
        List<String> fileNameList = FtpUtils.selectFileNamesFromFtp(ftpSaveFilePath);
        if (null !=fileNameList && CollectionUtil.isNotEmpty(fileNameList)) {
            int fileCount = fileNameList.size();
            System.out.println("共查询到" + fileCount+"个文件!");
        }
    }

}
