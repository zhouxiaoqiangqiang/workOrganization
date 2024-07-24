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
     *
     * 可以用于控制一个或多个线程等待多个任务完成后再执行。
     * 计数器只能够被减少，不能够被增加
     *
     *
     *  CountDownLatch 内部维护了一个计数器，该计数器初始值为 N，代表需要等待的线程数目，
     * 当一个线程完成了需要等待的任务后，就会调用 countDown() 方法将计数器减 1，
     * 当计数器的值为 0 时，等待的线程就会开始执行
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

        //调用 countDown() 方法将计数器减 1
        //每次调用 countDown() 方法会将计数器减 1，计数器为 0 时，等待线程开始执行。
        count.countDown();



        /*
         *    1. CountDownLatch 对象的计数器只能减不能增，即一旦计数器为 0，
         *      就无法再重新设置为其他值，因此在使用时需要根据实际需要设置初始值。
         *
         *    2. CountDownLatch 的计数器是线程安全的，多个线程可以同时调用 countDown() 方法，而不会产生冲突。
         *
         *    3. 如果 CountDownLatch 的计数器已经为 0，再次调用 countDown() 方法也不会产生任何效果。
         *
         *    4. 如果在等待过程中，有线程发生异常或被中断，计数器的值可能不会减少到 0，因此在使用时需要根据实际情况进行异常处理。
         *
         *    5. CountDownLatch 可以与其他同步工具（如 Semaphore、CyclicBarrier）结合使用，实现更复杂的多线程同步。
         */

    }


}
