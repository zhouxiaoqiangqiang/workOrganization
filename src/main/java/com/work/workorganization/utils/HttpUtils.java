package com.work.workorganization.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * -@Desc:   http工具类
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/17 10:16
 **/
@Slf4j
public class HttpUtils {

    public static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

    /**
     * 通用发起GET请求
     *
     * @param url 请求接口路径url
     */
    public static String doGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            //发起get请求
            response = HTTP_CLIENT.execute(httpGet);
        } catch (IOException e) {
            log.error("=====发起GET请求失败!失败原因:{}=====", e.getMessage());
            e.printStackTrace();
        }
        if (null != response) {
            try {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
            } catch (IOException e) {
                log.error("=====发起GET请求失败!失败原因:{}=====", e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    response.close();
                    HTTP_CLIENT.close();
                } catch (IOException e) {
                    log.error("=====发起GET请求失败!失败原因:{}=====", e.getMessage());
                    e.printStackTrace();
                }

            }
        }
        return result;
    }

    /**
     * 发起带有参数的GET请求
     *
     * @param url   请求接口路径url
     * @param param 请求参数
     */
    public static String doGet(String url, Map<String, String> param) {
        log.info("=====发起GET请求!请求路径:{},请求参数:{}=====", url, param);
        //将GET请求参数拼接到请求路径中
        StringBuilder stringBuilder = new StringBuilder(url);
        if (CollectionUtil.isNotEmpty(param)) {
            String doGetParam = param.entrySet().stream().map(entry ->
                    entry.getKey() + "=" + entry.getValue()
            ).collect(Collectors.joining(","));
            if (StringUtils.isNotBlank(doGetParam)) {
                stringBuilder.append("?").append(doGetParam);
            }
        }
        return doGet(stringBuilder.toString());
    }

    /**
     * 发起带有参数的POST请求
     *
     * @param url   请求接口路径url
     * @param param 请求参数
     */
    public static String doPost(String url, Map<String, Object> param) {
        log.info("=====发起POST请求!请求路径:{},请求参数:{}=====", url, param);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            //处理参数
            if (CollectionUtil.isNotEmpty(param)) {
                String requestBody = JSON.toJSONString(param);
                httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
            }
            //发起post请求
            response = HTTP_CLIENT.execute(httpPost);
            if (null != response) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    log.info("=====发起POST请求成功!=====");
                    result = EntityUtils.toString(response.getEntity());
                }
            }
        } catch (Exception e) {
            log.error("=====发起POST请求失败!失败原因:{}=====", e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != response)
                try {
                    response.close();
                    HTTP_CLIENT.close();
                } catch (IOException e) {
                    log.error("=====发起POST请求失败!失败原因:{}=====", e.getMessage());
                    e.printStackTrace();
                }
        }
        return result;
    }

    /**
     * 发起带有参数和文件的POST请求
     *
     * @param url   请求接口路径url
     * @param param 请求参数
     * @param file  请求文件
     */
    public static String doPostWithFile(String url, Map<String, Object> param, File file) {
        log.info("=====发起POST请求!请求路径:{},请求参数:{}=====", url, param);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            //构建请求体
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            //设置请求体
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntityBuilder.setCharset(StandardCharsets.UTF_8);
            multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
            //处理文件参数
            if (null != file) {
                FileInputStream fileInputStream = new FileInputStream(file);
                String fileName = file.getName();
                fileName = fileName.substring((fileName.lastIndexOf("/") + 1));
                MockMultipartFile mockMultipartFile = new MockMultipartFile(fileName, fileName, ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                multipartEntityBuilder.addBinaryBody("file", mockMultipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);
            }
            //处理其他参数
            if (CollectionUtil.isNotEmpty(param)) {
                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    multipartEntityBuilder.addTextBody(entry.getKey(), ObjectUtil.isNull(entry.getValue()) ? null : String.valueOf(entry.getValue()));
                }
            }
            //构建请求体
            HttpEntity httpEntity = multipartEntityBuilder.build();
            //设置请求体
            httpPost.setEntity(httpEntity);

            //发起post请求
            response = HTTP_CLIENT.execute(httpPost);
            if (null != response) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    log.info("=====发起POST请求成功!=====");
                    result = EntityUtils.toString(response.getEntity());
                }
            }
        } catch (Exception e) {
            log.error("=====发起POST请求失败!失败原因:{}=====", e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != response)
                try {
                    response.close();
                    HTTP_CLIENT.close();
                } catch (IOException e) {
                    log.error("=====发起POST请求失败!失败原因:{}=====", e.getMessage());
                    e.printStackTrace();
                }
        }
        return result;
    }

    /**
     * 发起带有参数的POST请求下载文件
     *
     * @param url   请求接口路径url
     * @param param 请求参数
     */
    public static byte[] doPostDownloadFile(String url, Map<String, Object> param) {
        log.info("=====发起POST请求!请求路径:{},请求参数:{}=====", url, param);
        CloseableHttpResponse response = null;
        byte[] result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            //设置请求体
            if (CollectionUtil.isNotEmpty(param)) {
                if (ObjectUtil.isNotNull(param.get("cookie"))) {
                    httpPost.setHeader("Cookie", param.get("cookie").toString());
                }
            }
            //设置请求参数
            StringEntity stringEntity = new StringEntity(JSON.toJSONString(param), "utf-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            //设置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            httpPost.setConfig(requestConfig);

            //发起post请求
            response = HTTP_CLIENT.execute(httpPost);
            if (null != response)
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    log.info("=====发起POST请求成功!=====");
                    result = EntityUtils.toByteArray(response.getEntity());
                }
        } catch (Exception e) {
            log.error("=====发起POST请求失败!失败原因:{}=====", e.getMessage());
            e.printStackTrace();
        } finally {
            if (null != response)
                try {
                    response.close();
                    HTTP_CLIENT.close();
                } catch (IOException e) {
                    log.error("=====发起POST请求失败!失败原因:{}=====", e.getMessage());
                    e.printStackTrace();
                }
        }
        return result;
    }
}
