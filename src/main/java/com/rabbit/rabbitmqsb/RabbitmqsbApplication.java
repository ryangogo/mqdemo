package com.rabbit.rabbitmqsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RabbitmqsbApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqsbApplication.class, args);
    }
}
