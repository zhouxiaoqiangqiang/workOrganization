package com.work.workorganization.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import static cn.hutool.core.img.ImgUtil.toBufferedImage;
/**
  * -@Desc: 生成图形验证码
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/12 15:02
 **/
@Slf4j
@RestController
@RequestMapping("/verify/code")
public class VerifyCodeController {

    //获取扭曲干扰的验证码
    @GetMapping("/createShearCaptcha")
    public void getShearCaptcha(HttpServletResponse response)
    {

        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(150, 50,4,3);
        //设置背景颜色
        shearCaptcha.setBackground(new Color(249, 251, 220));
        //生成四位验证码
        String code = RandomUtil.randomNumbers(4);
        //生成验证码图片
        Image image = shearCaptcha.createImage(code);
        //返回验证码信息
        responseCode(response, code, image);
    }

    //获取线条干扰的验证码
    @GetMapping("/createLineCaptcha")
    public void getLineCaptcha(HttpServletResponse response)
    {

        //定义图形验证码的长、宽、验证码位数、干扰线数量
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(150, 50,4,80);
        //设置背景颜色
        lineCaptcha.setBackground(new Color(249, 251, 220));
        //生成四位验证码
        String code = RandomUtil.randomNumbers(4);
        Image image = lineCaptcha.createImage(code);
        //返回验证码信息
        responseCode(response, code, image);
    }

    //获取圆圈干扰的验证码
    @GetMapping("/createCircleCaptcha")
    public void getCircleCaptcha(HttpServletResponse response)
    {

        //定义图形验证码的长、宽、验证码位数、干扰圈圈数量
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 50,4,30);
        //设置背景颜色
        circleCaptcha.setBackground(new Color(249, 251, 220));
        //生成四位验证码
        String code = RandomUtil.randomNumbers(4);
        Image image = circleCaptcha.createImage(code);
        //返回验证码信息
        responseCode(response, code, image);
    }




    private static void responseCode(HttpServletResponse response, String code, Image image) {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        String uuidStr = UUID.randomUUID().toString().replace("-", "");
        log.info("生成验证码 uuidStr：{} ，code:{}",uuidStr, code);
        response.setHeader("verifyCodeUuid",uuidStr);
        try {
            BufferedImage bufferedImage = toBufferedImage(image);
            // 创建一个ByteArrayOutputStream，用于存储图片数据
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 写入图片数据到ByteArrayOutputStream
            ImageIO.write(bufferedImage, "jpeg", baos);
            // 将ByteArrayOutputStream转换为ByteArrayInputStream
            byte[] imageInBytes = baos.toByteArray();
            //输出
            IoUtil.write(response.getOutputStream(), true, imageInBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
