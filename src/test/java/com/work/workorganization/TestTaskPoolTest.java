package com.work.workorganization;

import com.work.workorganization.service.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class TestTaskPoolTest {

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @Resource
    private ThreadPoolService threadPoolService;

   @Test
    public void testTestTaskPoolTest(){
       long startTime = System.currentTimeMillis();
       System.out.println("主线程开始执行");

       // 启动5个线程进行循环耗时操作
       for (int i = 0; i < 5; i++) {
           threadPoolService.testAsync();
       }

       // 单线程进行5次循环耗时操作
       for (int i = 0; i < 5; i++) {
           threadPoolService.testSync();
       }

       System.out.println("主线程执行完成");
       long endTime = System.currentTimeMillis();
       System.out.println("总耗时：" + (endTime - startTime) + "ms");
   }
}
