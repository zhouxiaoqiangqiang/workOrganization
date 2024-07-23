package com.work.workorganization.controller;

import com.work.workorganization.config.aop.RateLimit;
import com.work.workorganization.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
/**
  * -@Desc: 生成图形验证码
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/12 15:02
 **/
@Slf4j
@RestController
@RequestMapping("/verify/code")
public class VerifyCodeController {

    @Resource
    private VerifyCodeService verifyCodeService;

    // 获取扭曲干扰的验证码
    @GetMapping("/createShearCaptcha")
    @RateLimit()
    public void getShearCaptcha(HttpServletResponse response) {
        verifyCodeService.createShearCaptcha(response);
    }

    // 获取线条干扰的验证码
    @GetMapping("/createLineCaptcha")
    @RateLimit(limit = 2, timeout = 5)
    public void getLineCaptcha(HttpServletResponse response) {
        verifyCodeService.createLineCaptcha(response);
    }

    // 获取圆圈干扰的验证码
    @GetMapping("/createCircleCaptcha")
    @RateLimit(limit = 10, timeout = 10)
    public void getCircleCaptcha(HttpServletResponse response) {
        verifyCodeService.createCircleCaptcha(response);
    }
}
