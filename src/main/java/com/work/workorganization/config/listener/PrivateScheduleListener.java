package com.work.workorganization.config.listener;

import com.work.workorganization.config.PrivateScheduleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
  * -@Desc: 定时任务监听器
  * -@Author: zhouzhiqiang
  * -@Date: 2025/6/4 10:28
 **/
@Component
public class PrivateScheduleListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private List<PrivateScheduleTask> taskList; // 存储所有定时任务的列表

    @Autowired
    public PrivateScheduleListener() {
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.setPoolSize(10); // 设置线程池大小
        this.taskScheduler.setThreadNamePrefix("private-schedule-");
        this.taskScheduler.initialize();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 从Spring容器中获取所有PrivateScheduleTask的实现类并存入List
        taskList = new ArrayList<>(event.getApplicationContext()
                .getBeansOfType(PrivateScheduleTask.class)
                .values());

        // 注册所有定时任务
        taskList.forEach(task -> {
            ScheduledFuture<?> future = taskScheduler.schedule(
                    task,
                    new CronTrigger(task.getCron())
            );
            scheduledTasks.put(task.getId(), future);
            System.out.println("Registered task: " + task.getId() + " with cron: " + task.getCron());
        });
    }

    /**
     * 获取所有定时任务列表
     */
    public List<PrivateScheduleTask> getTaskList() {
        return taskList;
    }

    /**
     * 取消指定任务
     */
    public void cancelTask(String taskId) {
        ScheduledFuture<?> future = scheduledTasks.get(taskId);
        if (future != null) {
            future.cancel(true);
            scheduledTasks.remove(taskId);
        }
    }
}

/*
分析代码中使用的设计模式
在这个自定义定时任务实现中，主要使用了以下几种设计模式：
*/

/**
1. 模板方法模式 (Template Method Pattern)
体现在 PrivateScheduleTask 类中：

基类 PrivateScheduleTask 定义了 run() 方法的整体结构（任务开始、执行、结束的日志记录和异常处理）

将具体任务逻辑留给子类通过 execute() 方法实现

这是典型的模板方法模式应用

java
public abstract class PrivateScheduleTask implements Runnable {
    @Override
    public void run() {
        try {
            logger.info("Task [{}] started", id);
            execute();  // 抽象方法，由子类实现
            logger.info("Task [{}] completed", id);
        } catch (Exception e) {
            logger.error("Task [{}] failed: {}", id, e.getMessage(), e);
        }
    }

    protected abstract void execute();
}
**/

/**
 2. 观察者模式 (Observer Pattern)
体现在 PrivateScheduleListener 类中：

实现了 ApplicationListener<ContextRefreshedEvent> 接口

监听 Spring 容器的 ContextRefreshedEvent 事件

当事件发生时自动执行 onApplicationEvent 方法

这是观察者模式的典型应用

java
@Component
public class PrivateScheduleListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 处理事件
    }
}
**/

/**
3. 工厂方法模式 (Factory Method Pattern)
体现在 Spring 的依赖注入机制中：

通过 @Component 注解标记 TestTask 为 Spring 管理的 bean

Spring 容器充当工厂，负责创建和管理这些任务实例

getBeansOfType(PrivateScheduleTask.class) 方法获取所有任务实例

java
event.getApplicationContext().getBeansOfType(PrivateScheduleTask.class)
 **/

/**
4. 策略模式 (Strategy Pattern)
体现在任务执行方式上：

每个 PrivateScheduleTask 实现类可以有不同的 execute() 实现

运行时通过多态性决定具体执行哪个策略

任务调度器不需要知道具体任务细节，只需调用统一的 run() 方法
 **/

/**
5. 命令模式 (Command Pattern)
体现在任务封装方式上：

每个 PrivateScheduleTask 实例封装了一个具体的操作（命令）

ThreadPoolTaskScheduler 作为调用者，接收并执行这些命令

符合命令模式将"请求封装为对象"的特点

java
taskScheduler.schedule(task, new CronTrigger(task.getCron()));
 **/

/**
6. 单例模式 (Singleton Pattern)
体现在 Spring 的组件管理上：

@Component 注解默认创建的是单例 bean

PrivateScheduleListener 和各个 PrivateScheduleTask 实现类在 Spring 容器中都是单例的
**/

/*
设计模式协同工作的效果
这些设计模式协同工作，形成了一个灵活、可扩展的定时任务框架：

模板方法模式 提供了任务执行的标准流程

观察者模式 实现了任务的自动注册机制

工厂方法模式 简化了任务对象的创建和管理

策略模式 使得可以灵活替换不同的任务实现

命令模式 将任务封装为可传递和调用的对象

单例模式 确保了资源的合理利用

这种多模式组合的设计使得系统具有以下优点：

新增任务类型只需扩展 PrivateScheduleTask，符合开闭原则

任务注册自动化，与业务逻辑解耦

执行流程标准化，异常处理统一

调度机制与具体任务实现分离

易于扩展和管理

这样的设计非常适合需要灵活调度和管理多种定时任务的应用程序。
 */