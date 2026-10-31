package com.example.hotel_management_jpa.dto;

import com.example.hotel_management_jpa.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByCustomerId(int customerId);
    List<Payment> findByBookingId(int bookingId);
}