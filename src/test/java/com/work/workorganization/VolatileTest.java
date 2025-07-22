package com.work.workorganization;

/**
 * -@Desc:  volatile关键字测试
 * -@Author: zhouzhiqiang
 * -@Date: 2025/7/22 14:55
 *
 *
    总结:

        volatile 关键字用于修饰变量，提供可见性和禁止指令重排的特性。
        可见性保证线程对 volatile 变量的读写操作都是直接在主内存中进行的。
        禁止指令重排防止编译器和 CPU 对 volatile 变量相关的指令进行重排序。
        volatile 不保证 原子性。
        volatile 通常用于状态标志、双重检查锁定等场景。
 **/

/*
    问题背景：    在多线程环境下，每个线程都有自己的工作内存（例如 CPU 缓存），
                线程对共享变量的读写操作可能不是直接在主内存中进行的，而是在工作内存中进行的。
                这可能导致一个线程修改了共享变量的值，而其他线程却看不到这个修改，从而读取到过期的值。
    volatile 的作用：

        当一个变量被 volatile 修饰时，它会保证：

         写操作： 当一个线程修改了 volatile 变量的值，这个新值会立即被 刷新 到主内存中。
         读操作： 当一个线程读取 volatile 变量时，它会从主内存中读取最新的值，而不是从自己的工作内存中读取。

    实现原理： volatile 通过 内存屏障 (Memory Barrier) 来实现可见性。内存屏障是一种 CPU 指令，
             它可以阻止编译器和 CPU 对指令进行重排序，并强制将工作内存中的数据刷新到主内存，或从主内存读取数据。
*/
public class VolatileTest {
    private static volatile boolean flag = false;
    // private static boolean flag = false; 如果不使用volatile,可能出现线程1无法停止的情况

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            if (!flag) {
                System.out.println("Thread 1 fund flag is false");
            }
            System.out.println("Thread 1 stopped.");
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1000); // 让 thread1 先执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true; // 修改 flag 的值
            System.out.println("Thread 2 set flag to true.");
            System.out.println("Thread 2 stopped.");
        });

        Thread thread3 = new Thread(() -> {
            try {
                Thread.sleep(3000); // 让 thread 1、2 先执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (flag) {
                System.out.println("Thread 3 fund flag is true");
            }
            System.out.println("Thread 3 stopped.");
        });

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
