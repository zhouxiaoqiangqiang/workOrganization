package com.work.workorganization.controller;

import com.work.workorganization.service.QRCodeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * -@Desc:   二维码服务接口控制层
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/21 17:44
 **/
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {

    @Resource
    private QRCodeService qrCodeService;


    /**
     * 生成普通二维码
     */
    @GetMapping("/generate")
    public byte[] generateQRCode(@RequestParam String content) throws Exception {
        return qrCodeService.generateQRCode(content);
    }

    /**
     * 生成普通二维码 Base64
     */
    @GetMapping("/generateBase64")
    public String generateQRCodeBase64(@RequestParam String content) throws Exception {
        return qrCodeService.generateQRCodeBase64(content);
    }

    /**
     * 生成带Logo的二维码
     */
    @PostMapping("/generate-with-logo")
    public byte[] generateQRCodeWithLogo(@RequestParam String content, @RequestParam("logoFile") MultipartFile logoFile) throws Exception {
        return qrCodeService.generateQRCodeWithLogo(content, logoFile);
    }

    /**
     * 生成带Logo的二维码 Base64
     */
    @PostMapping("/generate-with-logo-base64")
    public String generateQRCodeWithLogoBase64(@RequestParam String content, @RequestParam("logoFile") MultipartFile logoFile) throws Exception {
        return qrCodeService.generateQRCodeWithLogoBase64(content, logoFile);
    }

    /**
     * 生成带Token的二维码
     */
    @GetMapping("/generate-with-token")
    public byte[] generateQRCodeWithToken(@RequestParam String content, @RequestParam String token) throws Exception {
        return qrCodeService.generateQRCodeWithToken(content, token);
    }

    /**
     * 生成带Token的二维码 Base64
     */
    @GetMapping("/generate-with-token-base64")
    public String generateQRCodeWithTokenBase64(@RequestParam String content, @RequestParam String token) throws Exception {
        return qrCodeService.generateQRCodeWithTokenBase64(content, token);
    }

    /**
     * 生成带多个参数的二维码
     */
    @PostMapping("/generate-with-params")
    public byte[] generateQRCodeWithParams(@RequestBody Map<String, String> params) throws Exception {
        return qrCodeService.generateQRCodeWithParams(params);
    }

    /**
     * 生成带多个参数的二维码 Base64
     */
    @PostMapping("/generate-with-params-base64")
    public String generateQRCodeWithParamsBase64(@RequestBody Map<String, String> params) throws Exception {
        return qrCodeService.generateQRCodeWithParamsBase64(params);
    }

    /**
     * 解析二维码
     */
    @PostMapping("/decode")
    public String decodeQRCode(@RequestParam("file") MultipartFile file) throws Exception {
        return qrCodeService.decodeQRCode(file.getBytes());
    }

    /**
     * 解析二维码 Base64
     */
    @PostMapping("/decodeBase64")
    public String decodeQRCodeBase64(@RequestParam String base64) throws Exception {
        return qrCodeService.decodeQRCodeBase64(base64);
    }

    /**
     * 解析二维码并提取参数
     */
    @PostMapping("/decode-with-params")
    public Map<String, Object> parseQRCodeParams(@RequestParam("file") MultipartFile file) throws Exception {
        return qrCodeService.parseQRCodeParams(file.getBytes());
    }

    /**
     * 解析二维码并提取参数 Base64
     */
    @PostMapping("/decode-with-params-base64")
    public Map<String, Object> parseQRCodeParamsBase64(@RequestParam String base64) throws Exception {
        return qrCodeService.parseQRCodeParamsBase64(base64);
    }
}