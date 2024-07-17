package com.work.workorganization.config;

import com.work.workorganization.pojo.ResponseBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler(RuntimeException.class)
    public ResponseBo handlerNullException(RuntimeException runtimeException) {
        return ResponseBo.error(runtimeException.getMessage());
    }

}
