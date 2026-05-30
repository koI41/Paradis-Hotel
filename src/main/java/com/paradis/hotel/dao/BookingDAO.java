package com.paradis.hotel.dao;

import com.paradis.hotel.model.Booking;
import com.paradis.hotel.util.DatabaseUtil;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Booking Data Access Object (DAO)
 * Handles all database operations related to bookings
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class BookingDAO {

    /**
     * Read LocalDate from SQLite column supporting SQL date, ISO text, and epoch millis.
     */
    private static LocalDate readLocalDate(ResultSet rs, String columnName) throws SQLException {
        Object rawValue = rs.getObject(columnName);
        if (rawValue == null) {
            return null;
        }

        if (rawValue instanceof Date) {
            return ((Date) rawValue).toLocalDate();
        }

        if (rawValue instanceof Number) {
            long epochMillis = ((Number) rawValue).longValue();
            return Instant.ofEpochMilli(epochMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        String value = rawValue.toString().trim();
        if (value.isEmpty()) {
            return null;
        }

        if (value.matches("\\d+")) {
            long epochMillis = Long.parseLong(value);
            return Instant.ofEpochMilli(epochMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        return LocalDate.parse(value);
    }
    
    /**
     * Add a new booking to the database
     * @param booking Booking object to add
     * @return Generated booking ID, or -1 if failed
     */
    public static int addBooking(Booking booking) {
        String sql = "INSERT INTO bookings(customer_id, customer_name, room_number, room_type, " +
                     "check_in_date, check_out_date, price_per_day, total_cost, status) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, booking.getCustomerId());
            pstmt.setString(2, booking.getCustomerName());
            pstmt.setString(3, booking.getRoomNumber());
            pstmt.setString(4, booking.getRoomType());
            pstmt.setString(5, booking.getCheckInDate().toString());
            pstmt.setString(6, booking.getCheckOutDate().toString());
            pstmt.setDouble(7, booking.getPricePerDay());
            pstmt.setDouble(8, booking.getTotalCost());
            pstmt.setString(9, booking.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (Statement keyStatement = conn.createStatement();
                     ResultSet generatedKeys = keyStatement.executeQuery("SELECT last_insert_rowid()")) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Get all bookings from database
     * @return List of Booking objects
     */
    public static List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                    readLocalDate(rs, "check_in_date"),
                    readLocalDate(rs, "check_out_date"),
                        rs.getDouble("price_per_day")
                );
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving bookings: " + e.getMessage());
        }
        
        return bookings;
    }
    
    /**
     * Get booking by ID
     * @param bookingId Booking ID to search
     * @return Booking object or null if not found
     */
    public static Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                    readLocalDate(rs, "check_in_date"),
                    readLocalDate(rs, "check_out_date"),
                        rs.getDouble("price_per_day")
                );
                booking.setStatus(rs.getString("status"));
                return booking;
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving booking: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get active bookings
     * @return List of active Booking objects
     */
    public static List<Booking> getActiveBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE lower(trim(status)) = 'active'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                    readLocalDate(rs, "check_in_date"),
                    readLocalDate(rs, "check_out_date"),
                        rs.getDouble("price_per_day")
                );
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving active bookings: " + e.getMessage());
        }
        
        return bookings;
    }
    
    /**
     * Get bookings by customer ID
     * @param customerId Customer ID to search
     * @return List of Booking objects for the customer
     */
    public static List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE customer_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                    readLocalDate(rs, "check_in_date"),
                    readLocalDate(rs, "check_out_date"),
                        rs.getDouble("price_per_day")
                );
                booking.setStatus(rs.getString("status"));
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving bookings by customer: " + e.getMessage());
        }
        
        return bookings;
    }
    
    /**
     * Get booking by room number
     * @param roomNumber Room number to search
     * @return Booking object or null if not found
     */
    public static Booking getBookingByRoomNumber(String roomNumber) {
        String sql = "SELECT * FROM bookings WHERE room_number = ? AND status = 'Active'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                    readLocalDate(rs, "check_in_date"),
                    readLocalDate(rs, "check_out_date"),
                        rs.getDouble("price_per_day")
                );
                booking.setStatus(rs.getString("status"));
                return booking;
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving booking by room: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Update booking status
     * @param bookingId Booking ID to update
     * @param status New status
     * @return true if successful, false otherwise
     */
    public static boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating booking status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete booking from database
     * @param bookingId Booking ID to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get booking count
     * @return Total number of bookings
     */
    public static int getBookingCount() {
        String sql = "SELECT COUNT(*) as count FROM bookings";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting booking count: " + e.getMessage());
        }
        
        return 0;
    }
}