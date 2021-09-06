package com.wfg.redisweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.wfg.redisweb","wfg.config","wfg.service","wfg.task","core","redis"})
@MapperScan(basePackages = {"wfg.mapper"})
public class RedisWebApplication
{

    public static void main(String[] args) {
        SpringApplication.run(RedisWebApplication.class, args);
    }

}
