package com.work.workorganization.config.exception;

/**
  * -@Desc:   自定义状态校验异常
  * -@Author: zhouzhiqiang
  * -@Date: 2025/7/15 14:35
 **/
public class StatusCheckException extends RuntimeException {
    public StatusCheckException(String message) {
        super(message);
    }
}
