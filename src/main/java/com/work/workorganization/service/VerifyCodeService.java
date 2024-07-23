package com.work.workorganization.service;

import javax.servlet.http.HttpServletResponse;

// 定义一个验证码服务接口
public interface VerifyCodeService {
    // 创建拖拽验证码
    void createShearCaptcha(HttpServletResponse response);
    // 创建线条验证码
    void createLineCaptcha(HttpServletResponse response);
    // 创建圆形验证码
    void createCircleCaptcha(HttpServletResponse response);
}
