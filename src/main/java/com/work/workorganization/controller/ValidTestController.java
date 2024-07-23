package com.work.workorganization.controller;

import com.work.workorganization.pojo.ResponseBo;
import com.work.workorganization.pojo.TestRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
  * -@Desc:   参数校验测试
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/23 15:24
 **/
@RestController
@RequestMapping("/valid")
public class ValidTestController {

    @PostMapping("/test")
    public ResponseBo test(@Validated @RequestBody TestRequest request){
        System.out.println(request);
        return ResponseBo.ok("OK");
    }
}
