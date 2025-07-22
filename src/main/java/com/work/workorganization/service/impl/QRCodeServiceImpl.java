package com.work.workorganization.service.impl;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.work.workorganization.service.QRCodeService;
import com.work.workorganization.utils.AssertUtil;
import com.work.workorganization.utils.QRCodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
/**
  * -@Desc: 二维码服务接口服务层实现类
  * -@Author: zhouzhiqiang
  * -@Date: 2025/7/21 17:45
 **/
@Service
public class QRCodeServiceImpl implements QRCodeService {

    /**
     * 生成普通二维码
     */
    @Override
    public byte[] generateQRCode(String content) throws Exception {
        return QRCodeUtil.generateQRCode(content);
    }

    /**
     * 生成带Logo的二维码
     */
    @Override
    public byte[] generateQRCodeWithLogo(String content, MultipartFile logoFile) throws Exception {
        // 将上传的Logo保存到临时文件
        String logoPath = saveUploadedFile(logoFile);
        return QRCodeUtil.generateQRCodeWithLogo(content, logoPath);
    }

    /**
     * 生成带Token的二维码
     */
    @Override
    public byte[] generateQRCodeWithToken(String content, String token) throws Exception {
        return QRCodeUtil.generateQRCodeWithToken(content, token);
    }

    /**
     * 生成带多个参数的二维码
     */
    @Override
    public byte[] generateQRCodeWithParams(Map<String, String> params) throws Exception {
        String baseUrl = params.getOrDefault("baseUrl", null);
        AssertUtil.isBlank(baseUrl, "baseUrl不能为空!");
        return QRCodeUtil.generateQRCodeWithParams(baseUrl, params);
    }

    /**
     * 解析二维码
     */
    @Override
    public String decodeQRCode(byte[] qrCodeBytes) throws Exception {
        return QRCodeUtil.decodeQRCode(qrCodeBytes);
    }

    /**
     * 解析二维码并提取参数
     */
    @Override
    public Map<String, Object> parseQRCodeParams(byte[] qrCodeBytes) throws Exception {
        return QRCodeUtil.parseQRCodeParams(qrCodeBytes);
    }

    /**
     * 生成普通二维码 Base64
     */
    @Override
    public String generateQRCodeBase64(String content) throws IOException, WriterException {
        return QRCodeUtil.generateQRCodeBase64(content);
    }

    /**
     * 生成带Logo的二维码 Base64
     */
    @Override
    public String generateQRCodeWithLogoBase64(String content, MultipartFile logoFile) throws IOException, WriterException {
        // 将上传的Logo保存到临时文件
        String logoPath = saveUploadedFile(logoFile);
        return QRCodeUtil.generateQRCodeWithLogoBase64(content, logoPath);
    }

    /**
     * 生成带Token的二维码 Base64
     */
    @Override
    public String generateQRCodeWithTokenBase64(String content, String token) throws IOException, WriterException {
        return QRCodeUtil.generateQRCodeWithTokenBase64(content, token);
    }

    /**
     * 生成带多个参数的二维码 Base64
     */
    @Override
    public String generateQRCodeWithParamsBase64(Map<String, String> params) throws IOException, WriterException {
        String baseUrl = params.getOrDefault("baseUrl", null);
        AssertUtil.isBlank(baseUrl, "baseUrl不能为空!");
        return QRCodeUtil.generateQRCodeWithParamsBase64(baseUrl, params);
    }

    /**
     * 解析二维码 Base64
     */
    @Override
    public String decodeQRCodeBase64(String base64) throws NotFoundException, IOException {
        return QRCodeUtil.decodeQRCodeBase64(base64);
    }

    /**
     * 解析二维码并提取参数 Base64
     */
    @Override
    public Map<String, Object> parseQRCodeParamsBase64(String base64) throws NotFoundException, IOException {
        return QRCodeUtil.parseQRCodeParamsBase64(base64);
    }

    /**
     * 保存上传的文件到临时目录
     */
    private String saveUploadedFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件为空");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path tempFilePath = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
        file.transferTo(tempFilePath);
        return tempFilePath.toString();
    }
}