package com.xlx.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 网关启动类
 * Spring Boot 应用入口，聚合所有业务模块
 * 通过 @MapperScan 扫描所有模块的 Mapper 接口
 */
@SpringBootApplication
@MapperScan("com.xlx.api.**.mapper")
public class XlxApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(XlxApiApplication.class, args);
    }
}
