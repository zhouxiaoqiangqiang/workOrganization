package com.work.workorganization.controller;

import com.work.workorganization.pojo.ResponseBo;
import com.work.workorganization.pojo.validPojo.Order;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * -@Desc: Redisson分布式锁
 * -@Author: zhouzhiqiang
 * -@Date: 2024/7/24 17:03
 **/
@RestController
@RequestMapping("/redissonLock")
public class RedissonLockController {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 测试分布式锁
     */
    @PostMapping("/testFairLock")
    public ResponseBo test(@Validated @RequestBody Order order) {
        //订单号
        Integer id = order.getId();
        //锁的key  (此处的 "order:"  是 自定义的前缀 ,可以根据业务需求自定义)
        String key = "order:" + id;
        RLock fairLock = redissonClient.getFairLock(key);
        try {
            //尝试获取锁  此处的Time是获取锁的约定的等待时间, TimeUnit是时间单位, 可以根据业务需求自定义
            boolean tryLockResult = fairLock.tryLock(5, TimeUnit.SECONDS);
            if (tryLockResult) {
                //业务逻辑
                System.out.println("执行业务逻辑xxxx");
                //模拟处理业务逻辑 休眠10秒
                Thread.sleep(10000);
            } else {
                return ResponseBo.error("获取分布式锁失败!业务执行失败!已有其他线程在处理该订单!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseBo.error("获取分布式锁失败!业务执行成功!");
        } finally {
            if (fairLock.isHeldByCurrentThread()) {
                fairLock.unlock();
            }
        }
        return ResponseBo.ok("获取分布式锁成功!业务执行成功!");
    }
}
