package com.work.workorganization.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
  * -@Desc: 自定义注解 --> 接口限流和黑白名单
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/17 16:00
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
	//限制次数
    int limit() default 5;
    //限制时间 秒
    int timeout() default 60;
}
