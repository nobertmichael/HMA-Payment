package com.example.hotel_management_jpa.controller;

import com.example.hotel_management_jpa.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "http://localhost:8082")
public interface CustomerFeignClient {

    @GetMapping("/customers/{id}")
    Customer getCustomerById(@PathVariable("id") int id);
}