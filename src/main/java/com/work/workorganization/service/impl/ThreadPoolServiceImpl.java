package com.work.workorganization.service.impl;

import com.work.workorganization.service.ThreadPoolService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ThreadPoolServiceImpl implements ThreadPoolService {
    @Override
    @Async
    public void testAsync() {

        long startTime = System.currentTimeMillis();
        System.out.println("异步方法开始执行，线程名称：" + Thread.currentThread().getName());

        for (int i = 0; i < 1000000; i++) {
            // 模拟耗时操作，可以是一些复杂的计算或其他耗时操作
            double result = Math.pow(Math.random(), Math.random());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("异步方法执行完成，耗时：" + (endTime - startTime) + "ms");


    }

    @Override
    public void testSync() {
        long startTime = System.currentTimeMillis();
        System.out.println("同步方法开始执行，线程名称：" + Thread.currentThread().getName());

        for (int i = 0; i < 1000000; i++) {
            // 模拟耗时操作，可以是一些复杂的计算或其他耗时操作
            double result = Math.pow(Math.random(), Math.random());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("同步方法执行完成，耗时：" + (endTime - startTime) + "ms");
    }
}
