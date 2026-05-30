package com.paradis.hotel.model;

import java.time.LocalDate;

/**
 * Booking Model Class
 * Represents a hotel booking/reservation
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class Booking {
    private int bookingId;
    private int customerId;
    private String customerName;
    private String roomNumber;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double pricePerDay;
    private double totalCost;
    private String status;  // Active, Checked Out, Cancelled

    /**
     * Constructor for Booking
     * @param bookingId Unique booking identifier
     * @param customerId Customer ID
     * @param customerName Customer name
     * @param roomNumber Room number
     * @param roomType Type of room
     * @param checkInDate Check-in date
     * @param checkOutDate Check-out date
     * @param pricePerDay Price per day
     */
    public Booking(int bookingId, int customerId, String customerName, String roomNumber,
                   String roomType, LocalDate checkInDate, LocalDate checkOutDate, double pricePerDay) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.pricePerDay = pricePerDay;
        this.status = "Active";
        calculateTotalCost();
    }

    // Default constructor
    public Booking() {
        this.status = "Active";
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        calculateTotalCost();
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        calculateTotalCost();
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
        calculateTotalCost();
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculate total cost based on number of days and price per day
     */
    private void calculateTotalCost() {
        if (checkInDate != null && checkOutDate != null) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            this.totalCost = days * pricePerDay;
        }
    }

    /**
     * Get number of days for the booking
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
        return "Booking{" +
                "bookingId=" + bookingId +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", pricePerDay=" + pricePerDay +
                ", totalCost=" + totalCost +
                ", status='" + status + '\'' +
                '}';
    }
}