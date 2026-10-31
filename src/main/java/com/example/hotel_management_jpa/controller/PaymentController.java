package com.example.hotel_management_jpa.controller;

import com.example.hotel_management_jpa.dto.PaymentRepository;
import com.example.hotel_management_jpa.model.Booking;
import com.example.hotel_management_jpa.model.Customer;
import com.example.hotel_management_jpa.model.Payment;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerFeignClient customerFeignClient;   // → customer-service (8082)

    @Autowired
    private BookingFeignClient bookingFeignClient;     // → booking-service (8089)

    // ======================================================================
    // 1. CREATE PAYMENT  →  Circuit Breaker
    // ======================================================================
    @CircuitBreaker(name = "paymentService", fallbackMethod = "createPaymentFallback")
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Payment payment) {

        System.out.println("******* createPayment is Called *******");

        try {
            customerFeignClient.getCustomerById(payment.getCustomerId());   // → 8082
            bookingFeignClient.getBookingById(payment.getBookingId());      // → 8089
        } catch (FeignException.NotFound e) {
            return ResponseEntity.badRequest().body("Customer or booking not found");
        }

        Payment saved = paymentRepository.save(payment);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Do NOT save a payment we could not verify
    public ResponseEntity<?> createPaymentFallback(Payment payment, Throwable ex) {
        System.out.println("******* createPayment Fallback: " + ex.getMessage() + " *******");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Customer or booking service is unavailable. Try again later.");
    }

    // ======================================================================
    // 2. ONE PAYMENT WITH DETAILS  →  Circuit Breaker + Rate Limiter
    // ======================================================================
    @CircuitBreaker(name = "paymentService", fallbackMethod = "getPaymentByIdFallback")
    @RateLimiter(name = "paymentRateLimiter", fallbackMethod = "getPaymentByIdFallback")
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable int id) {

        System.out.println("******* getPaymentById is Called *******");

        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Payment payment = optionalPayment.get();

        try {
            Customer customer = customerFeignClient.getCustomerById(payment.getCustomerId());   // → 8082
            Booking booking = bookingFeignClient.getBookingById(payment.getBookingId());        // → 8089
            payment.setCustomer(customer);
            payment.setBooking(booking);
        } catch (FeignException.NotFound e) {
            System.out.println("******* Customer or booking not found *******");
        }

        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    // One fallback for BOTH protections
    public ResponseEntity<Payment> getPaymentByIdFallback(int id, Throwable ex) {

        // a) Rate limiter: too many requests → 429
        if (ex instanceof RequestNotPermitted) {
            System.out.println("******* Rate limit exceeded *******");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

        // b) Circuit breaker: other service down/OPEN → return payment without details, 503
        System.out.println("******* getPaymentById Fallback: " + ex.getMessage() + " *******");
        return paymentRepository.findById(id)
                .map(p -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(p))
                .orElse(ResponseEntity.notFound().build());
    }

    // ======================================================================
    // 3. CUSTOMER OF A PAYMENT  →  Circuit Breaker
    // ======================================================================
    @CircuitBreaker(name = "paymentService", fallbackMethod = "getCustomerOfPaymentFallback")
    @GetMapping("/{id}/customer")
    public ResponseEntity<Customer> getCustomerOfPayment(@PathVariable int id) {

        System.out.println("******* getCustomerOfPayment is Called *******");

        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Customer customer = customerFeignClient.getCustomerById(optionalPayment.get().getCustomerId()); // → 8082
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Customer> getCustomerOfPaymentFallback(int id, Throwable ex) {
        System.out.println("******* getCustomerOfPayment Fallback: " + ex.getMessage() + " *******");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    // ======================================================================
    // LOCAL ONLY (own database) → no protection needed
    // ======================================================================
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @GetMapping("/customer/{customerId}")
    public List<Payment> getPaymentsByCustomer(@PathVariable int customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }

    @GetMapping("/booking/{bookingId}")
    public List<Payment> getPaymentsByBooking(@PathVariable int bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }
}
