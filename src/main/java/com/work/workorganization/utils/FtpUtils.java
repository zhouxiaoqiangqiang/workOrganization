package com.work.workorganization.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FtpUtils {

    //不同系统的路径符号转化
    private static final String separator = File.separator;

    private static String server;
    private static String port;
    private static String username;
    private static String password;
    private static String path;

    public FtpUtils(@Value("${ftp.server}") String server,
                    @Value("${ftp.port}") String port,
                    @Value("${ftp.username}") String username,
                    @Value("${ftp.password}") String password,
                    @Value("${ftp.path}") String path) {
        FtpUtils.server = server;
        FtpUtils.port = port;
        FtpUtils.username = username;
        FtpUtils.password = password;
        FtpUtils.path = path;
    }

    /**
     * 获取FTP连接
     */
    public static FTPClient getFtpConnection() {
        log.info("======开始连接到FTP服务器======");
        FTPClient ftpClient = new FTPClient();
        try {
            //连接
            ftpClient.connect(server, Integer.parseInt(port));
            //设置编码
            ftpClient.setControlEncoding("UTF-8");
            //设置传输文件类型
            ftpClient.setFileStructure(FTPClient.BINARY_FILE_TYPE);
            //登录
            ftpClient.login(username, password);
            //查看登录状态
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                log.error("======FTP服务器连接失败!用户名或密码错误!======");
                ftpClient.disconnect();
            } else {
                log.info("======FTP服务器连接成功!======");
            }
        } catch (SocketException e) {
            log.error("======FTP服务器连接失败!ip地址错误!======");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("======FTP服务器连接失败!端口错误!======");
            e.printStackTrace();
        }
        return ftpClient;

    }

    /**
     * 上传文件至ftp
     *
     * @param file       文件
     * @param saveFileRemotePath 文件保存路径
     * @return 上传是否成功
     */
    public static boolean uploadFileToFtp(File file, String saveFileRemotePath) {
        if (null == file) {
            log.error("======上传文件至FTP服务器失败!未发现需要上传的文件!======");
            return false;
        }
        if (StringUtils.isBlank(saveFileRemotePath)) {
            log.error("======上传文件至FTP服务器失败!文件保存路径不能为空!======");
            return false;
        }

        log.info("======即将开始上传文件至FTP服务器======");
        log.info("======文件名称:{}======", file.getName());
        log.info("======文件上传FTP保存路径:{}======", saveFileRemotePath);

        //是否上传成功
        boolean isUploadSuccess = false;
        //获取连接
        FTPClient ftpClient = getFtpConnection();
        try {
            //工作模式被动
            ftpClient.enterLocalPassiveMode();
            //设置操作文件类型
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //获取FTP默认编码
            String ftpServerDefaultEncoding = getFtpServerDefaultEncoding(ftpClient);
            // 设置编码
            ftpClient.setControlEncoding(ftpServerDefaultEncoding);
            //切换目录
            boolean isChangeSaveDirSuccess = ftpClient.changeWorkingDirectory(saveFileRemotePath);
            if (isChangeSaveDirSuccess) {
                log.info("======切换至文件保存目录成功======");
                //转为文件流
                FileInputStream  fileInputStream = new FileInputStream(file);
                //上传文件
                isUploadSuccess = ftpClient.storeFile(new String(file.getName().getBytes(ftpServerDefaultEncoding), StandardCharsets.ISO_8859_1), fileInputStream);
                if (isUploadSuccess) {
                    log.info("======成功上传文件至FTP服务器!======");
                } else {
                    log.error("======上传文件至FTP服务器失败!======");
                }
                fileInputStream.close();
            } else {
                log.error("======上传文件至FTP服务器失败!切换至文件保存目录失败!======");

            }

            ftpClient.logout();
        } catch (Exception e) {
            log.error("======上传文件至FTP服务器连接失败!======");
            e.printStackTrace();
        }
        return isUploadSuccess;
    }

    /**
     * 查询 FTP服务器中 某个 文件夹下的所有文件名称
     * @param ftpFilePath   ftp文件路径
     */
    public static List<String> selectFileNamesFromFtp(String ftpFilePath) {
        List<String> fileNames = new ArrayList<>();

        if (StringUtils.isBlank(ftpFilePath)) {
            log.error("======查询FTP服务器中某个文件夹下的所有文件名称失败!文件路径不能为空!======");
            return null;
        }

        log.info("======即将查询FTP服务器中某个文件夹下的所有文件名称======");
        log.info("======查询的文件路径:{}======", ftpFilePath);

        //获取连接
        FTPClient ftpClient = getFtpConnection();
        try {
            //工作模式被动
            ftpClient.enterLocalPassiveMode();
            //设置操作文件类型
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            //获取FTP默认编码
            String ftpServerDefaultEncoding = getFtpServerDefaultEncoding(ftpClient);
            // 设置编码
            ftpClient.setControlEncoding(ftpServerDefaultEncoding);
            //切换至文件所在路径
            boolean isChangeFilePathSuccess = ftpClient.changeWorkingDirectory(ftpFilePath);
            if (isChangeFilePathSuccess) {
                log.info("======成功切换至文件所在路径!======");
                //查询该目录下的所有文件
                FTPFile[] ftpFiles = ftpClient.listFiles();
                int count = 0;
                for (FTPFile ftpFile : ftpFiles) {
                    if (ftpFile.isFile()) {
                        fileNames.add(ftpFile.getName());
                        count++;
                    }
                }
                log.info("======共查询到{}个文件!======", count);

            } else {
                log.error("======FTP服务器下载文件失败!切换至文件所在路径失败!======");
                return null;
            }
        } catch (IOException e) {
            log.error("======查看FTP服务器文件夹中所有文件失败!======");
            e.printStackTrace();
        }
        return fileNames;
    }

    /**
     * 从ftp下载文件
     * @param fileName      文件名称
     * @param ftpFilePath   文件所在路径
     * @return 文件流
     */
    public static InputStream downloadFileInputStreamFromFtp(String fileName, String ftpFilePath) {

        InputStream inputStream = null;

        if (StringUtils.isBlank(fileName)) {
            log.error("======FTP服务器下载文件失败!文件名不能为空!======");
            return null;
        }
        if (StringUtils.isBlank(ftpFilePath)) {
            log.error("======FTP服务器下载文件失败!文件所在路径不能为空!======");
            return null;
        }

        log.info("======即将从FTP服务器下载文件======");
        log.info("======下载的文件名称:{}======", fileName);
        log.info("======下载的文件保存路径:{}======", ftpFilePath);

        try {
            //获取连接
            FTPClient ftpClient = getFtpConnection();
            //工作模式被动
            ftpClient.enterLocalPassiveMode();
            //设置操作文件类型
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //切换至文件所在路径
            boolean isChangeFilePathSuccess = ftpClient.changeWorkingDirectory(ftpFilePath);
            if (isChangeFilePathSuccess) {
                log.info("======成功切换至文件所在路径!======");
                //获取FTP服务器默认字符集
                String ftpServerDefaultEncoding = getFtpServerDefaultEncoding(ftpClient);
                //下载文件
                /*【注意】: FTP服务器字符集不同系统不同
                           windows搭建FTP服务器默认字符集GBK
                           Linux搭建FTP服务器默认子父级UFT_8
                 */
                if (StringUtils.isNotBlank(ftpServerDefaultEncoding)) {
                    //使用FTP服务器默认字符集下载
                    inputStream = ftpClient.retrieveFileStream(new String(fileName.getBytes(ftpServerDefaultEncoding), FTP.DEFAULT_CONTROL_ENCODING));

                } else {
                    //如果未成功获取FTP默认字符集 则先尝试windows字符集 GBK下载
                    inputStream = ftpClient.retrieveFileStream(new String(fileName.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
                    if (null == inputStream) {
                        //如果windows字符集下载失败 则尝试UFT_8下载
                        inputStream = ftpClient.retrieveFileStream(new String(fileName.getBytes(StandardCharsets.UTF_8), FTP.DEFAULT_CONTROL_ENCODING));
                    }
                }

                if (null == inputStream) {
                    log.error("======FTP服务器下载文件失败!文件不存在!======");
                } else {
                    log.info("======成功从FTP服务器下载文件!======");
                }

            } else {
                log.error("======FTP服务器下载文件失败!切换至文件所在路径失败!======");
                return null;
            }

        } catch (IOException e) {
            log.error("======FTP服务器下载文件失败!======");
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 下载FTP服务器文件到本地(返回File对象)
     * @param ftpFileName    ftp文件名称
     * @param remoteFilePath ftp文件路径
     * @param saveLocalDir  本地保存目录
     * @return 下载的File对象
     */
    public static File downLoadFileFromFtp(String ftpFileName,String remoteFilePath, String saveLocalDir){
        File localFile = null;
        try {
            createLocalDir(saveLocalDir);

            String localFilePath = saveLocalDir + separator+ ftpFileName;
            localFile = new File(localFilePath);

            InputStream inputStream = downloadFileInputStreamFromFtp(ftpFileName, remoteFilePath);
            if (null != inputStream) {
                Files.copy(inputStream, localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                log.error("======从FTP服务器下载文件失败!======");
            }
        } catch (IOException e) {
            log.error("======从FTP服务器下载文件失败!======");
            e.printStackTrace();
        }

        return localFile;
    }

    /**
     * 获取FTP服务器字符集
     *
     * @param ftpClient FTP服务连接客户端
     *
     *
     * 【注意】: FTP服务器字符集不同系统不同
     *               windows搭建FTP服务器默认字符集GBK
     *               Linux搭建FTP服务器默认子父级UFT_8
     */
    public static String getFtpServerDefaultEncoding(FTPClient ftpClient) {
        //FTP服务器的字符集
        String encoding = null;
        try {
            // 获取FTP服务器系统信息
            String system = ftpClient.getSystemType().toLowerCase();
            //获取FTP服务器字符集
            if (system.contains("windows")) {
                encoding = "GBK";
            } else {
                encoding = "UTF-8";
            }
        } catch (IOException e) {
            log.error("======获取FTP服务器默认字符集失败!======");
            e.printStackTrace();
        }
        return encoding;
    }

    /**
     * 在FTP服务器上创建目录(包括父目录)
     * @param ftpClient FTP连接
     * @param dirPath 目录路径
     * @throws IOException 创建目录失败
     */
    private static void createDirectories(FTPClient ftpClient, String dirPath) throws IOException {
        String[] pathElements = dirPath.split("/");
        for (String singleDir : pathElements) {
            if (singleDir.isEmpty()) continue;

            if (!ftpClient.changeWorkingDirectory(singleDir)) {
                if (!ftpClient.makeDirectory(singleDir)) {
                    log.error("======创建FTP服务器文件目录失败!======");
                    throw new IOException("Unable to create directory: " + singleDir);
                }
                ftpClient.changeWorkingDirectory(singleDir);
            }
        }
    }


    /**
     * 创建本地目录
     * @param dirPath 目录路径
     * @throws IOException 创建目录失败
     */
    public static void createLocalDir(String dirPath) throws IOException {
        if (!Files.exists(Paths.get(dirPath))) {
            Files.createDirectories(Paths.get(dirPath));
        }
    }
}
