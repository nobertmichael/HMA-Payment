package com.example.hmapayment.client;

import com.example.hmapayment.dto.AppResponse;
import com.example.hmapayment.dto.Booking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service", url = "http://localhost:9000")
public interface BookingFeignClient {

    @GetMapping("/booking/{id}")
    Booking getBookingById(@PathVariable("id") int id);

    @GetMapping("/customer-bookings-reference/{referenceNumber}")
    AppResponse<Booking> getAllBookingsByReferenceNumber(@PathVariable String referenceNumber);

}
