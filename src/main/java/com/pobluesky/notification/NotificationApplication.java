package com.pobluesky.notification;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.pobluesky.global",
    "com.pobluesky.notification",
//    "com.pobluesky.kafka"
})
@EnableFeignClients(basePackages = "com.pobluesky.feign")
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Bean
    public JPAQueryFactory init(EntityManager em) {
        return new JPAQueryFactory(em);
    }

}
