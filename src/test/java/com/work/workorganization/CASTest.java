package com.work.workorganization;

public class CASTest {
    //计数变量
    static volatile int count = 0;
    public static void main(String[] args) throws InterruptedException {
        //线程 1 给 count 加 10000
        Thread t1 = new Thread(() -> {
            for (int j = 0; j <10000; j++) {
                count++;
            }
            System.out.println("thread t1 count 加 10000 结束,count目前="+count);
        });
        //线程 2 给 count 加 10000
        Thread t2 = new Thread(() -> {
            for (int j = 0; j <10000; j++) {
                count++;
            }
            System.out.println("thread t2 count 加 10000 结束,count目前="+count);
        });
        //启动线程 1
        t1.start();
        //启动线程 2
        t2.start();
        //等待线程 1 执行完成
        t1.join();
        //等待线程 2 执行完成
        t2.join();
        //打印 count 变量
        System.out.println(count);
    }
}
