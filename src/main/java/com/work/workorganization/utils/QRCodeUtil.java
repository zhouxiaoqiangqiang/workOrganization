package com.work.workorganization.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
  * -@Desc:  二维码工具类
  * -@Author: zhouzhiqiang
  * -@Date: 2025/7/21 17:22
 **/
public class QRCodeUtil {

    // 默认二维码宽度
    private static final int DEFAULT_WIDTH = 300;
    // 默认二维码高度
    private static final int DEFAULT_HEIGHT = 300;
    // 默认二维码文件格式
    private static final String DEFAULT_FORMAT = "png";
    // 默认编码
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 生成二维码字节数组（默认尺寸）
     *
     * @param content 二维码内容
     * @return byte[]
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] generateQRCode(String content) throws WriterException, IOException {
        return generateQRCode(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 生成二维码字节数组（自定义尺寸）
     *
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @return byte[]
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] generateQRCode(String content, int width, int height) throws WriterException, IOException {
        return generateQRCode(content, width, height, DEFAULT_FORMAT);
    }

    /**
     * 生成二维码字节数组（自定义尺寸和格式）
     *
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @param format 图片格式
     * @return byte[]
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] generateQRCode(String content, int width, int height, String format) throws WriterException, IOException {
        BufferedImage image = generateQRCodeImage(content, width, height);
        return imageToBytes(image, format);
    }

    /**
     * 生成二维码Base64字符串（默认尺寸）
     *
     * @param content 二维码内容
     * @return Base64编码的字符串
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeBase64(String content) throws WriterException, IOException {
        return generateQRCodeBase64(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 生成二维码Base64字符串（自定义尺寸）
     *
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @return Base64编码的字符串
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeBase64(String content, int width, int height) throws WriterException, IOException {
        byte[] bytes = generateQRCode(content, width, height);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 生成带logo的二维码字节数组（默认尺寸）
     *
     * @param content 二维码内容
     * @param logoPath logo路径
     * @return byte[]
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] generateQRCodeWithLogo(String content, String logoPath) throws WriterException, IOException {
        return generateQRCodeWithLogo(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, logoPath);
    }

    /**
     * 生成带logo的二维码字节数组（自定义尺寸）
     *
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @param logoPath logo路径
     * @return byte[]
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] generateQRCodeWithLogo(String content, int width, int height, String logoPath) throws WriterException, IOException {
        BufferedImage image = generateQRCodeWithLogoImage(content, width, height, logoPath);
        return imageToBytes(image, DEFAULT_FORMAT);
    }

    /**
     * 生成带logo的二维码Base64字符串（默认尺寸）
     *
     * @param content 二维码内容
     * @param logoPath logo路径
     * @return Base64编码的字符串
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeWithLogoBase64(String content, String logoPath) throws WriterException, IOException {
        return generateQRCodeWithLogoBase64(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, logoPath);
    }

    /**
     * 生成带logo的二维码Base64字符串（自定义尺寸）
     *
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @param logoPath logo路径
     * @return Base64编码的字符串
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeWithLogoBase64(String content, int width, int height, String logoPath) throws WriterException, IOException {
        byte[] bytes = generateQRCodeWithLogo(content, width, height, logoPath);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 生成带token参数的二维码字节数组（默认尺寸）
     *
     * @param content 二维码基础内容
     * @param token token参数
     * @return byte[]
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] generateQRCodeWithToken(String content, String token) throws WriterException, IOException {
        return generateQRCode(content + "?token=" + token);
    }

    /**
     * 生成带token参数的二维码字节数组（自定义尺寸）
     *
     * @param content 二维码基础内容
     * @param token token参数
     * @param width 宽度
     * @param height 高度
     * @return byte[]
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] generateQRCodeWithToken(String content, String token, int width, int height) throws WriterException, IOException {
        return generateQRCode(content + "?token=" + token, width, height);
    }

    /**
     * 生成带token参数的二维码Base64字符串（默认尺寸）
     *
     * @param content 二维码基础内容
     * @param token token参数
     * @return Base64编码的字符串
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeWithTokenBase64(String content, String token) throws WriterException, IOException {
        return generateQRCodeBase64(content + "?token=" + token);
    }

    /**
     * 生成带token参数的二维码Base64字符串（自定义尺寸）
     *
     * @param content 二维码基础内容
     * @param token token参数
     * @param width 宽度
     * @param height 高度
     * @return Base64编码的字符串
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeWithTokenBase64(String content, String token, int width, int height) throws WriterException, IOException {
        return generateQRCodeBase64(content + "?token=" + token, width, height);
    }

    /**
     * 生成带自定义参数的二维码字节数组（默认尺寸）
     *
     * @param baseUrl 基础URL
     * @param params 参数Map
     * @return byte[]
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] generateQRCodeWithParams(String baseUrl, Map<String, String> params) throws WriterException, IOException {
        return generateQRCode(buildUrlWithParams(baseUrl, params));
    }

    /**
     * 生成带自定义参数的二维码字节数组（自定义尺寸）
     *
     * @param baseUrl 基础URL
     * @param params 参数Map
     * @param width 宽度
     * @param height 高度
     * @return byte[]
     * @throws WriterException
     * @throws IOException
     */
    public static byte[] generateQRCodeWithParams(String baseUrl, Map<String, String> params, int width, int height) throws WriterException, IOException {
        return generateQRCode(buildUrlWithParams(baseUrl, params), width, height);
    }

    /**
     * 生成带自定义参数的二维码Base64字符串（默认尺寸）
     *
     * @param baseUrl 基础URL
     * @param params 参数Map
     * @return Base64编码的字符串
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeWithParamsBase64(String baseUrl, Map<String, String> params) throws WriterException, IOException {
        return generateQRCodeBase64(buildUrlWithParams(baseUrl, params));
    }

    /**
     * 生成带自定义参数的二维码Base64字符串（自定义尺寸）
     *
     * @param baseUrl 基础URL
     * @param params 参数Map
     * @param width 宽度
     * @param height 高度
     * @return Base64编码的字符串
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeWithParamsBase64(String baseUrl, Map<String, String> params, int width, int height) throws WriterException, IOException {
        return generateQRCodeBase64(buildUrlWithParams(baseUrl, params), width, height);
    }

    /**
     * 解析二维码文件
     *
     * @param filePath 文件路径
     * @return 二维码内容
     * @throws IOException
     * @throws NotFoundException
     */
    public static String decodeQRCode(String filePath) throws IOException, NotFoundException {
        BufferedImage image = ImageIO.read(new File(filePath));
        return decodeQRCode(image);
    }

    /**
     * 解析二维码图片
     *
     * @param image 二维码图片
     * @return 二维码内容
     * @throws NotFoundException
     */
    public static String decodeQRCode(BufferedImage image) throws NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
        Result result = new MultiFormatReader().decode(binaryBitmap, hints);
        return result.getText();
    }

    /**
     * 解析二维码字节数组
     *
     * @param bytes 二维码字节数组
     * @return 二维码内容
     * @throws IOException
     * @throws NotFoundException
     */
    public static String decodeQRCode(byte[] bytes) throws IOException, NotFoundException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        return decodeQRCode(image);
    }

    /**
     * 解析Base64编码的二维码
     *
     * @param base64Str Base64编码的二维码图片
     * @return 二维码内容
     * @throws IOException
     * @throws NotFoundException
     */
    public static String decodeQRCodeBase64(String base64Str) throws IOException, NotFoundException {
        byte[] bytes = Base64.getDecoder().decode(base64Str);
        return decodeQRCode(bytes);
    }

    // ============== 私有方法 ==============

    /**
     * 生成二维码图片对象（内部使用）
     */
    private static BufferedImage generateQRCodeImage(String content, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = getEncodeHints();
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 解析二维码文件并提取参数
     *
     * @param filePath 文件路径
     * @return 包含基础URL和参数的Map
     * @throws IOException
     * @throws NotFoundException
     */
    public static Map<String, Object> parseQRCodeParams(String filePath) throws IOException, NotFoundException {
        String content = decodeQRCode(filePath);
        return parseContentWithParams(content);
    }

    /**
     * 解析二维码图片并提取参数
     *
     * @param image 二维码图片
     * @return 包含基础URL和参数的Map
     * @throws NotFoundException
     */
    public static Map<String, Object> parseQRCodeParams(BufferedImage image) throws NotFoundException {
        String content = decodeQRCode(image);
        return parseContentWithParams(content);
    }

    /**
     * 解析二维码字节数组并提取参数
     *
     * @param bytes 二维码字节数组
     * @return 包含基础URL和参数的Map
     * @throws IOException
     * @throws NotFoundException
     */
    public static Map<String, Object> parseQRCodeParams(byte[] bytes) throws IOException, NotFoundException {
        String content = decodeQRCode(bytes);
        return parseContentWithParams(content);
    }

    /**
     * 解析Base64编码的二维码并提取参数
     *
     * @param base64Str Base64编码的二维码图片
     * @return 包含基础URL和参数的Map
     * @throws IOException
     * @throws NotFoundException
     */
    public static Map<String, Object> parseQRCodeParamsBase64(String base64Str) throws IOException, NotFoundException {
        String content = decodeQRCodeBase64(base64Str);
        return parseContentWithParams(content);
    }

    // ============== 私有工具方法 ==============

    /**
     * 解析带参数的内容
     *
     * @param content 二维码内容
     * @return 包含基础URL和参数的Map
     */
    private static Map<String, Object> parseContentWithParams(String content) {
        Map<String, Object> result = new HashMap<>();

        if (content == null || content.isEmpty()) {
            return result;
        }

        // 分离基础URL和参数部分
        int paramIndex = content.indexOf('?');
        if (paramIndex == -1) {
            result.put("baseUrl", content);
            result.put("params", new HashMap<String, String>());
            return result;
        }

        String baseUrl = content.substring(0, paramIndex);
        String paramStr = content.substring(paramIndex + 1);

        // 解析参数
        Map<String, String> params = new HashMap<>();
        String[] pairs = paramStr.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }

        result.put("baseUrl", baseUrl);
        result.put("params", params);
        return result;
    }

    /**
     * 生成带logo的二维码图片对象（内部使用）
     */
    private static BufferedImage generateQRCodeWithLogoImage(String content, int width, int height, String logoPath) throws WriterException, IOException {
        BufferedImage qrCodeImage = generateQRCodeImage(content, width, height);
        return addLogoToQRCode(qrCodeImage, logoPath);
    }

    /**
     * 获取编码参数
     */
    private static Map<EncodeHintType, Object> getEncodeHints() {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, DEFAULT_CHARSET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1);
        return hints;
    }

    /**
     * 添加logo到二维码
     */
    private static BufferedImage addLogoToQRCode(BufferedImage qrCodeImage, String logoPath) throws IOException {
        Graphics2D graphics = qrCodeImage.createGraphics();

        BufferedImage logoImage = ImageIO.read(new File(logoPath));
        int logoWidth = qrCodeImage.getWidth() / 5;
        int logoHeight = qrCodeImage.getHeight() / 5;
        int x = (qrCodeImage.getWidth() - logoWidth) / 2;
        int y = (qrCodeImage.getHeight() - logoHeight) / 2;

        graphics.drawImage(logoImage, x, y, logoWidth, logoHeight, null);
        graphics.drawRoundRect(x, y, logoWidth, logoHeight, 10, 10);
        graphics.setStroke(new BasicStroke(2));
        graphics.setColor(Color.WHITE);
        graphics.drawRect(x, y, logoWidth, logoHeight);

        graphics.dispose();
        logoImage.flush();

        return qrCodeImage;
    }

    /**
     * 图片转字节数组
     */
    private static byte[] imageToBytes(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    /**
     * 构建带参数的URL
     */
    private static String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        Objects.requireNonNull(baseUrl, "Base URL cannot be null");

        if (params == null || params.isEmpty()) {
            return baseUrl;
        }

        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        if (!baseUrl.contains("?")) {
            urlBuilder.append("?");
        } else if (!baseUrl.endsWith("?") && !baseUrl.endsWith("&")) {
            urlBuilder.append("&");
        }

        boolean firstParam = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!firstParam) {
                urlBuilder.append("&");
            }
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            firstParam = false;
        }

        return urlBuilder.toString();
    }
}