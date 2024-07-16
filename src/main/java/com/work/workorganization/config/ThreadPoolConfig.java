package com.work.workorganization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
@Configuration
public class ThreadPoolConfig {

        @Bean("taskExecutor")
        public ThreadPoolTaskExecutor taskExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(10);//核心线程数目
            executor.setMaxPoolSize(20);//指定最大线程数
            executor.setQueueCapacity(200);//队列中最大的数目
            executor.setKeepAliveSeconds(60);//线程空闲后的最大存活时间
            executor.setThreadNamePrefix("自定义多线程--线程Task--");//线程名称前缀
            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            executor.initialize();
            return executor;
        }

}
