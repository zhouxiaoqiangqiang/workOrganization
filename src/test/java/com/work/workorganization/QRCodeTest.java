package com.work.workorganization;

import com.work.workorganization.utils.QRCodeUtil;

import java.util.HashMap;
import java.util.Map;
/**
  * -@Desc:  二维码工具类测试类
  * -@Author: zhouzhiqiang
  * -@Date: 2025/7/22 9:59
 **/
public class QRCodeTest {
    public static void main(String[] args) {
        try {
            // 1. 生成普通二维码字节数组
            byte[] qrCodeBytes = QRCodeUtil.generateQRCode("https://www.example.com");

            // 2. 生成普通二维码Base64字符串
            String qrCodeBase64 = QRCodeUtil.generateQRCodeBase64("https://www.example.com");

            // 3. 生成带logo的二维码字节数组
            byte[] logoQrCodeBytes = QRCodeUtil.generateQRCodeWithLogo(
                    "https://www.example.com", "E:\\code\\workOrganization\\src\\main\\resources\\testFile\\logo.png");

            // 4. 生成带token的二维码Base64字符串
            String tokenQrCodeBase64 = QRCodeUtil.generateQRCodeWithTokenBase64(
                    "https://www.example.com/api", "abc123xyz456");

            // 5. 解析Base64编码的二维码
            String content = QRCodeUtil.decodeQRCodeBase64(qrCodeBase64);
            System.out.println("解析结果: " + content);

            // 6. 生成带参数的二维码
            Map<String, String> params = new HashMap<>();
            params.put("token", "abc123xyz456");
            params.put("userId", "1001");
            byte[] qrCodeBytesWithParams = QRCodeUtil.generateQRCodeWithParams(
                    "https://www.example.com/api", params, 400, 400);

            // 7. 解析带参数的二维码
            Map<String, Object> parsedResult = QRCodeUtil.parseQRCodeParams(qrCodeBytesWithParams);

            String baseUrl = (String) parsedResult.get("baseUrl");
            @SuppressWarnings("unchecked")
            Map<String, String> extractedParams = (Map<String, String>) parsedResult.get("params");

            System.out.println("基础URL: " + baseUrl);
            System.out.println("解析出的参数:");
            extractedParams.forEach((key, value) ->
                    System.out.println(key + " = " + value));

            System.out.println("============分隔线==================");

            // 8. 也可以从Base64字符串解析
            Map<String, String> paramsBase64 = new HashMap<>();
            paramsBase64.put("token", "abc123xyz789");
            paramsBase64.put("userId", "1002");
            String base64 = QRCodeUtil.generateQRCodeWithParamsBase64(
                    "https://www.example.com/api", paramsBase64);
            Map<String, Object> parsedFromBase64 = QRCodeUtil.parseQRCodeParamsBase64(base64);
            String baseUrlBase64 = (String) parsedFromBase64.get("baseUrl");
            @SuppressWarnings("unchecked")
            Map<String, String> extractedParamsBase64 = (Map<String, String>) parsedFromBase64.get("params");
            System.out.println("Base64字符串解析 基础URL: " + baseUrlBase64);
            System.out.println("Base64字符串解析 解析出的参数:");
            extractedParamsBase64.forEach((key, value) ->
                    System.out.println(key + " = " + value));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}