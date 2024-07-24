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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseBo handlerBindException(BindException e) {
        // 获取绑定结果
        BindingResult exceptions = e.getBindingResult();

        // 如果有错误
        if (exceptions.hasErrors()) {

            // 获取字段错误
            List<FieldError> fieldErrors = exceptions.
                    getFieldErrors();
            // 获取当前触发异常的对象的类
            Class<?> targetClass = Objects.requireNonNull(e.getTarget()).getClass();
            // 按照字段在类中的定义顺序排序
            fieldErrors = fieldErrors.stream()
                    .sorted((fe1, fe2) -> {
                        int index1 = getFieldIndex(targetClass, fe1.getField());
                        int index2 = getFieldIndex(targetClass, fe2.getField());
                        return Integer.compare(index1, index2);
                    })
                    .collect(Collectors.toList());

            // 如果有字段错误
            if (!fieldErrors.isEmpty()) {
                // 返回第一个错误信息
                return ResponseBo.error(fieldErrors.get(0).getDefaultMessage());
            }
        }
        // 返回参数异常
        return ResponseBo.error("参数异常!");
    }

    // 获取字段在类中的索引位置
    private int getFieldIndex(Class<?> clazz, String fieldName) {
        // 获取clazz类的所有字段
        Field[] fields = clazz.getDeclaredFields();
        // 遍历所有字段
        for (int i = 0; i < fields.length; i++) {
            // 如果字段名等于fieldName
            if (fields[i].getName().equals(fieldName)) {
                // 返回字段索引
                return i;
            }
        }
        // 如果没有找到匹配的字段，返回-1
        return -1;
    }

    /**
     * 规范中的验证异常，嵌套检验问题
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseBo handleConstraintViolationException(ConstraintViolationException e) {
        // 获取违反约束的集合
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        // 将违反约束的信息拼接成字符串
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
        // 返回错误信息
        return ResponseBo.error(message);
    }
}
