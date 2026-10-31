package com.example.hotel_management_jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.hotel_management_jpa")
@EnableDiscoveryClient
public class HotelManagementJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelManagementJpaApplication.class, args);
    }
}