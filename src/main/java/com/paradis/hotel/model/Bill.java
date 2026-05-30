package com.paradis.hotel.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Bill Model Class
 * Represents a hotel bill/invoice
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class Bill {
    private int billId;
    private int bookingId;
    private String customerName;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private long numberOfDays;
    private double pricePerDay;
    private double subtotal;
    private double taxRate;  // Default 5%
    private double tax;
    private double totalAmount;
    private LocalDate billGeneratedDate;
    private String status;  // Paid, Unpaid

    /**
     * Constructor for Bill
     * @param billId Unique bill identifier
     * @param bookingId Associated booking ID
     * @param customerName Customer name
     * @param roomNumber Room number
     * @param checkInDate Check-in date
     * @param checkOutDate Check-out date
     * @param numberOfDays Number of days stayed
     * @param pricePerDay Price per day
     */
    public Bill(int billId, int bookingId, String customerName, String roomNumber,
                LocalDate checkInDate, LocalDate checkOutDate, long numberOfDays, double pricePerDay) {
        this.billId = billId;
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfDays = numberOfDays;
        this.pricePerDay = pricePerDay;
        this.taxRate = 0.05;  // 5% tax
        this.billGeneratedDate = LocalDate.now();
        this.status = "Unpaid";
        calculateBill();
    }

    // Default constructor
    public Bill() {
        this.taxRate = 0.05;
        this.billGeneratedDate = LocalDate.now();
        this.status = "Unpaid";
    }

    // Getters and Setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
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

    public long getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(long numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getBillGeneratedDate() {
        return billGeneratedDate;
    }

    public void setBillGeneratedDate(LocalDate billGeneratedDate) {
        this.billGeneratedDate = billGeneratedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculate bill amounts (subtotal, tax, total)
     */
    private void calculateBill() {
        this.subtotal = numberOfDays * pricePerDay;
        this.tax = subtotal * taxRate;
        this.totalAmount = subtotal + tax;
    }

    /**
     * Generate bill as formatted string
     * @return Bill formatted text
     */
    public String generateBill() {
        StringBuilder bill = new StringBuilder();
        bill.append("═══════════════════════════════════════════════\n");
        bill.append("          PARADIS HOTEL - BILL INVOICE\n");
        bill.append("═══════════════════════════════════════════════\n\n");
        
        bill.append("BILL ID: ").append(billId).append("\n");
        bill.append("BOOKING ID: ").append(bookingId).append("\n\n");
        
        bill.append("CUSTOMER INFORMATION:\n");
        bill.append("─────────────────────\n");
        bill.append("Name: ").append(customerName).append("\n");
        bill.append("Room: ").append(roomNumber).append("\n\n");
        
        bill.append("STAY DETAILS:\n");
        bill.append("─────────────\n");
        bill.append("Check-in Date: ").append(checkInDate).append("\n");
        bill.append("Check-out Date: ").append(checkOutDate).append("\n");
        bill.append("Number of Days: ").append(numberOfDays).append("\n");
        bill.append("Price per Day: ₹").append(String.format("%.2f", pricePerDay)).append("\n\n");
        
        bill.append("BILLING DETAILS:\n");
        bill.append("────────────────\n");
        bill.append("Subtotal (").append(numberOfDays).append(" days × ₹")
           .append(String.format("%.2f", pricePerDay)).append("): ₹")
           .append(String.format("%.2f", subtotal)).append("\n");
        bill.append("Tax (5%): ₹").append(String.format("%.2f", tax)).append("\n");
        bill.append("─────────────────────────────────\n");
        bill.append("TOTAL AMOUNT: ₹").append(String.format("%.2f", totalAmount)).append("\n");
        bill.append("─────────────────────────────────\n\n");
        
        bill.append("Status: ").append(status).append("\n");
        bill.append("Generated: ").append(billGeneratedDate).append("\n\n");
        bill.append("Thank you for choosing Paradis Hotel!\n");
        bill.append("═══════════════════════════════════════════════\n");
        
        return bill.toString();
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", bookingId=" + bookingId +
                ", customerName='" + customerName + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", numberOfDays=" + numberOfDays +
                ", pricePerDay=" + pricePerDay +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}