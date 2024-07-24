package com.work.workorganization;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * -@Desc: Redisson分布式锁测试类
 * -@Author: zhouzhiqiang
 * -@Date: 2024/7/24 17:31
 **/
@SpringBootTest
@Slf4j
public class RedissonLockTest {

    @Resource
    private RedissonClient redissonClient;

    /*
     * CountDownLatch 是 Java 中的一个并发工具类
     * 用于协调多个线程之间的同步
     * 其作用是让某一个线程等待多个线程的操作完成之后再执行。
     * 它可以使一个或多个线程等待一组事件的发生，而其他的线程则可以触发这组事件
     */

    // 定义一个CountDownLatch对象，初始值为2
    // 表示有两个线程需要等待
    private final CountDownLatch count = new CountDownLatch(2);

    @Test
    public void testRedissonLock() {
        String lockName = "我是锁的名称";

        new Thread(() -> {

            String threadName = Thread.currentThread().getName();

            log.info("线程：{} 正在尝试获取锁。。。", threadName);
            startTask(lockName, threadName);

        }).start();

        new Thread(() -> {

            String threadName = Thread.currentThread().getName();

            log.info("线程：{} 正在尝试获取锁。。。", threadName);
            startTask(lockName, threadName);
        }).start();

        try {

            count.await();

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        log.info("子线程都已执行完毕，main函数可以结束了！");

    }

    private void startTask(String lockName, String threadName) {
        RLock fairLock = redissonClient.getFairLock(lockName);
        boolean lock = false;
        try {
            lock = fairLock.tryLock(2L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("线程：{} 获取锁失败!", threadName);
            e.printStackTrace();
        }

        doSomething(lock, threadName, fairLock);
    }

    private void doSomething(boolean lock, String threadName, RLock fairLock) {

        if (lock) {

            log.info("线程：{}，获取到了锁", threadName);

            try {

                try {

                    TimeUnit.SECONDS.sleep(5L);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }

            } finally {

                fairLock.unlock();

                log.info("线程：{}，释放了锁", threadName);

            }

        } else {

            log.info("线程：{}，没有获取到锁，过了等待时长，结束等待", threadName);

        }

        count.countDown();


    }


}
