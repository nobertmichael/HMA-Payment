package com.example.hotel_management_jpa.controller;

import com.example.hotel_management_jpa.model.Booking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service", url = "http://localhost:8089")
public interface BookingFeignClient {

    @GetMapping("/bookings/{id}")
    Booking getBookingById(@PathVariable("id") int id);
}