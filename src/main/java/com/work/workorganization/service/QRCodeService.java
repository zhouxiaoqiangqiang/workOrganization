package com.work.workorganization.service;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
  * -@Desc:   二维码服务接口服务层
  * -@Author: zhouzhiqiang
  * -@Date: 2025/7/21 17:44
 **/
public interface QRCodeService {
    /**
     * 生成普通二维码字节数组
     */
    byte[] generateQRCode(String content) throws Exception;

    /**
     * 生成带Logo的二维码字节数组
     */
    byte[] generateQRCodeWithLogo(String content, MultipartFile logoFile) throws Exception;

    /**
     * 生成带Token的二维码字节数组
     */
    byte[] generateQRCodeWithToken(String content, String token) throws Exception;

    /**
     * 生成带多个参数的二维码字节数组
     */
    byte[] generateQRCodeWithParams(Map<String, String> params) throws Exception;

    /**
     * 解析二维码内容
     */
    String decodeQRCode(byte[] qrCodeBytes) throws Exception;

    /**
     * 解析二维码并提取参数
     */
    Map<String, Object> parseQRCodeParams(byte[] qrCodeBytes) throws Exception;

    /**
     * 生成普通二维码 Base64
     */
    String generateQRCodeBase64(String content) throws IOException, WriterException;

    /**
     * 生成带Logo的二维码 Base64
     */
    String generateQRCodeWithLogoBase64(String content, MultipartFile logoFile) throws IOException, WriterException;

    /**
     * 生成带Token的二维码 Base64
     */
    String generateQRCodeWithTokenBase64(String content, String token) throws IOException, WriterException;

    /**
     * 生成带多个参数的二维码 Base64
     */
    String generateQRCodeWithParamsBase64(Map<String, String> params) throws IOException, WriterException;

    /**
     * 解析二维码 Base64
     */
    String decodeQRCodeBase64(String base64) throws NotFoundException, IOException;

    /**
     * 解析二维码并提取参数 Base64
     */
    Map<String, Object> parseQRCodeParamsBase64(String base64) throws NotFoundException, IOException;
}