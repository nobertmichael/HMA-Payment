package com.example.hmapayment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
    private int id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String nationality;
    private String sex;
    private String accountType;

    public Customer() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
}
