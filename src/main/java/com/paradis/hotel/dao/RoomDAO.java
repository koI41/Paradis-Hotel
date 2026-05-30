package com.paradis.hotel.dao;

import com.paradis.hotel.model.Room;
import com.paradis.hotel.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Room Data Access Object (DAO)
 * Handles all database operations related to rooms
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class RoomDAO {
    
    /**
     * Add a new room to the database
     * @param room Room object to add
     * @return true if successful, false otherwise
     */
    public static boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms(room_number, room_type, price_per_day, availability) " +
                     "VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setDouble(3, room.getPricePerDay());
            pstmt.setInt(4, room.isAvailability() ? 1 : 0);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get all rooms from database
     * @return List of Room objects
     */
    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_day"),
                        rs.getInt("availability") == 1
                );
                rooms.add(room);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving rooms: " + e.getMessage());
        }
        
        return rooms;
    }
    
    /**
     * Get all available rooms
     * @return List of available Room objects
     */
    public static List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE availability = 1";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_day"),
                        true
                );
                rooms.add(room);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving available rooms: " + e.getMessage());
        }
        
        return rooms;
    }
    
    /**
     * Get room by room number
     * @param roomNumber Room number to search
     * @return Room object or null if not found
     */
    public static Room getRoomByNumber(String roomNumber) {
        String sql = "SELECT * FROM rooms WHERE room_number = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_day"),
                        rs.getInt("availability") == 1
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving room: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Update room availability
     * @param roomNumber Room number to update
     * @param availability New availability status
     * @return true if successful, false otherwise
     */
    public static boolean updateRoomAvailability(String roomNumber, boolean availability) {
        String sql = "UPDATE rooms SET availability = ? WHERE room_number = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, availability ? 1 : 0);
            pstmt.setString(2, roomNumber);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating room availability: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update room details
     * @param room Room object with updated information
     * @return true if successful, false otherwise
     */
    public static boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET room_type = ?, price_per_day = ?, availability = ? " +
                     "WHERE room_number = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, room.getRoomType());
            pstmt.setDouble(2, room.getPricePerDay());
            pstmt.setInt(3, room.isAvailability() ? 1 : 0);
            pstmt.setString(4, room.getRoomNumber());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete room from database
     * @param roomNumber Room number to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteRoom(String roomNumber) {
        String sql = "DELETE FROM rooms WHERE room_number = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get room count
     * @return Total number of rooms
     */
    public static int getRoomCount() {
        String sql = "SELECT COUNT(*) as count FROM rooms";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting room count: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Get available room count
     * @return Number of available rooms
     */
    public static int getAvailableRoomCount() {
        String sql = "SELECT COUNT(*) as count FROM rooms WHERE availability = 1";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting available room count: " + e.getMessage());
        }
        
        return 0;
    }
}