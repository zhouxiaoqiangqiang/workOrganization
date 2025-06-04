package com.work.workorganization.config.task;

import com.work.workorganization.config.PrivateScheduleTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
  * -@Desc:   测试定时任务3
  * -@Author: zhouzhiqiang
  * -@Date: 2025/6/4 10:25
 **/
@Component
@Slf4j
public class TestTask3 extends PrivateScheduleTask {

    public TestTask3(@Value("test-测试定时任务3")String taskId, @Value("${cron.testTask3}")String cron) {
        super(taskId,cron); // 每1分钟秒执行一次
    }
    
    @Override
    protected void execute() {
        long startTime = System.currentTimeMillis();
        log.info("test-测试定时任务3开始执行,当前线程:{},当前时间:{}",Thread.currentThread().getName(), LocalDateTime.now());
        // 这里写具体的业务逻辑
        log.info("test-测试定时任务3执行结束,总耗时:{}",System.currentTimeMillis() - startTime);
    }
}