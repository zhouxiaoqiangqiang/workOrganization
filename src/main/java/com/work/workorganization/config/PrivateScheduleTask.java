package com.work.workorganization.config;

import lombok.extern.slf4j.Slf4j;

/**
  * -@Desc: 自定义定时任务基类  【用于实现 多线程定时任务管理执行】
  * -@Author: zhouzhiqiang
  * -@Date: 2025/6/4 10:17
 **/
@Slf4j
public abstract class PrivateScheduleTask implements Runnable {

    // 任务ID，用于区分不同任务
    private String id;
    
    // cron表达式，定义任务执行时间
    private String cron;
    
    public PrivateScheduleTask(String id, String cron) {
        this.id = id;
        this.cron = cron;
    }
    
    public String getId() {
        return id;
    }
    
    public String getCron() {
        return cron;
    }
    
    @Override
    public void run() {
        try {
            log.info("Task [{}] started", id);
            execute();
            log.info("Task [{}] completed", id);
        } catch (Exception e) {
            log.error("Task [{}] failed: {}", id, e.getMessage(), e);
        }
    }
    
    /**
     * 具体任务逻辑由子类实现
     */
    protected abstract void execute();
}