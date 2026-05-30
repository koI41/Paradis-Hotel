package com.paradis.hotel.model;

/**
 * Room Model Class
 * Represents a hotel room with its properties and status
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class Room {
    private int roomId;
    private String roomNumber;
    private String roomType;    // Single, Double, Deluxe
    private double pricePerDay;
    private boolean availability;  // true = available, false = occupied

    /**
     * Constructor for Room
     * @param roomId Unique room identifier
     * @param roomNumber Room number (e.g., 101)
     * @param roomType Type of room (Single, Double, Deluxe)
     * @param pricePerDay Price per day for the room
     * @param availability Availability status
     */
    public Room(int roomId, String roomNumber, String roomType, double pricePerDay, boolean availability) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.availability = availability;
    }

    // Default constructor
    public Room() {
        this.availability = true;
    }

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    /**
     * Get availability status as string for UI display
     * @return "Available" or "Occupied"
     */
    public String getAvailabilityStatus() {
        return availability ? "Available" : "Occupied";
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", availability=" + availability +
                '}';
    }
}