package com.paradis.hotel.model;

import java.time.LocalDate;

/**
 * Customer Model Class
 * Represents a hotel customer/guest
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class Customer {
    private int customerId;
    private String customerName;
    private String contactNumber;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private boolean isCheckedIn;

    /**
     * Constructor for Customer
     * @param customerId Unique customer identifier
     * @param customerName Full name of the customer
     * @param contactNumber Contact number
     * @param roomNumber Room number assigned to customer
     * @param checkInDate Check-in date
     * @param checkOutDate Check-out date
     */
    public Customer(int customerId, String customerName, String contactNumber, 
                    String roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isCheckedIn = true;
    }

    // Default constructor
    public Customer() {
        this.isCheckedIn = true;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        isCheckedIn = checkedIn;
    }

    /**
     * Calculate the number of days of stay
     * @return Number of days
     */
    public long getNumberOfDays() {
        if (checkInDate != null && checkOutDate != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", isCheckedIn=" + isCheckedIn +
                '}';
    }
}