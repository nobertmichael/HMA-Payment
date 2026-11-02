package com.example.hmapayment.entity;

import com.example.hmapayment.dto.Booking;
import com.example.hmapayment.dto.Customer;
import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "booking_id")
    private int bookingId;

    // ↓↓↓ NEW: not saved in the payment table, filled from other services
    @Transient
    private Customer customer;

    @Transient
    private Booking booking;
    // ↑↑↑

    public Payment() {
    }

    public Payment(String referenceNumber, int customerId, int bookingId) {
        this.referenceNumber = referenceNumber;
        this.customerId = customerId;
        this.bookingId = bookingId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    // ↓↓↓ NEW
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    // ↑↑↑
}