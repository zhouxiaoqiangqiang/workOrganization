package com.work.workorganization.config;

import com.work.workorganization.pojo.ResponseBo;
import com.work.workorganization.pojo.TestRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
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
    public ResponseBo handlerNullException(RuntimeException runtimeException) {
        return ResponseBo.error(runtimeException.getMessage());
    }

    /**
     * 参数校验异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) //设置状态码为 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseBo handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult exceptions = e.getBindingResult();

        if (exceptions.hasErrors()) {

            List<FieldError> fieldErrors = exceptions.getFieldErrors();

            // 按照字段在类中的定义顺序排序
            fieldErrors = fieldErrors.stream()
                    .sorted((fe1, fe2) -> {
                        int index1 = getFieldIndex(fe1.getField());
                        int index2 = getFieldIndex(fe2.getField());
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
    private int getFieldIndex(String fieldName) {
        java.lang.reflect.Field[] fields = TestRequest.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals(fieldName)) {
                return i;
            }
        }
        return -1;
    }
}
