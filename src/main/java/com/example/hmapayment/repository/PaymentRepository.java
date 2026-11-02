package com.example.hmapayment.repository;

import com.example.hmapayment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByCustomerId(int customerId);
    List<Payment> findByBookingId(int bookingId);
}