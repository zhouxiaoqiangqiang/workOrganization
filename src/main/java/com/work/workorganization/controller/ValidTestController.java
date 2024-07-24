package com.work.workorganization.controller;

import com.work.workorganization.pojo.ResponseBo;
import com.work.workorganization.pojo.validPojo.Order;
import com.work.workorganization.pojo.validPojo.TestRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 分组校验 测试新增
     */
    @PostMapping("/testValidAddOrder")
    public ResponseBo testValidAddOrder(@Validated(value = Order.Add.class) @RequestBody Order order){
        System.out.println(order);
        return ResponseBo.ok("OK");
    }

    /**
     * 分组校验 测试修改
     */
    @PostMapping("/testValidUpdateOrder")
    public ResponseBo testValidUpdateOrder(@Validated(value = {Order.Update.class}) @RequestBody Order order){
        System.out.println(order);
        return ResponseBo.ok("OK");
    }

    /**
     * 分组校验 测试删除
     */
    @PostMapping("/testValidDeleteOrder")
    public ResponseBo testValidDeleteOrder(@Validated(value = Order.Delete.class) @RequestBody Order order){
        System.out.println(order);
        return ResponseBo.ok("OK");
    }
}
