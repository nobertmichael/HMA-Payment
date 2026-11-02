package com.example.hmapayment.client;

import com.example.hmapayment.dto.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "http://localhost:8088")
public interface CustomerFeignClient {

    @GetMapping("/customer/{id}")
    Customer getCustomerById(@PathVariable("id") int id);
}
