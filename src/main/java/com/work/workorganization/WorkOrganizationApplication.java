package com.work.workorganization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync//放在线程池配置类上，也可以放在启动类上都可以
@SpringBootApplication
public class WorkOrganizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkOrganizationApplication.class, args);
    }

}
