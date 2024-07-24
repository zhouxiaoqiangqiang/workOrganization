package com.work.workorganization.config;

import com.work.workorganization.pojo.ResponseBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
  * -@Desc: 全局异常处理器
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/23 15:27
 **/
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    public ResponseBo handlerNullException(RuntimeException e) {
        return ResponseBo.error(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseBo handlerException(Exception e) {
        return ResponseBo.error(e.getMessage());
    }

    /**
     * 参数校验异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //设置状态码为 400
    @ExceptionHandler({BindException.class,MethodArgumentNotValidException.class})
    public ResponseBo handlerBindException(BindException e) {
        BindingResult exceptions = e.getBindingResult();

        if (exceptions.hasErrors()) {

            List<FieldError> fieldErrors = exceptions.
                    getFieldErrors();
            // 获取当前触发异常的对象的类
            Class<?> targetClass = Objects.requireNonNull(e.getTarget()).getClass();
            // 按照字段在类中的定义顺序排序
            fieldErrors = fieldErrors.stream()
                    .sorted((fe1, fe2) -> {
                        int index1 = getFieldIndex(targetClass,fe1.getField());
                        int index2 = getFieldIndex(targetClass,fe2.getField());
                        return Integer.compare(index1, index2);
                    })
                    .collect(Collectors.toList());

            if (!fieldErrors.isEmpty()) {
                return ResponseBo.error(fieldErrors.get(0).getDefaultMessage());
            }
        }
        return ResponseBo.error("参数异常!");
    }

    // 获取字段在类中的索引位置
    private int getFieldIndex(Class<?> clazz, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(fieldName)) {
                return i;
            }
        }
        return -1;
    }
}
