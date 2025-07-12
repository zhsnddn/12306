package com.zhsnddn.index12306.ticketservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 票务服务启动类
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.zhsnddn.index12306.ticketservice.dao.mapper"})
public class TicketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketServiceApplication.class, args);
    }

}
