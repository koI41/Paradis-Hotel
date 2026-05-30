package com.paradis.hotel.dao;

import com.paradis.hotel.model.Bill;
import com.paradis.hotel.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Bill Data Access Object (DAO)
 * Handles all database operations related to bills
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class BillDAO {
    
    /**
     * Add a new bill to the database
     * @param bill Bill object to add
     * @return Generated bill ID, or -1 if failed
     */
    public static int addBill(Bill bill) {
        String sql = "INSERT INTO bills(booking_id, customer_name, room_number, check_in_date, " +
                     "check_out_date, number_of_days, price_per_day, subtotal, tax_rate, " +
                     "tax, total_amount, bill_generated_date, status) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, bill.getBookingId());
            pstmt.setString(2, bill.getCustomerName());
            pstmt.setString(3, bill.getRoomNumber());
            pstmt.setString(4, bill.getCheckInDate().toString());
            pstmt.setString(5, bill.getCheckOutDate().toString());
            pstmt.setLong(6, bill.getNumberOfDays());
            pstmt.setDouble(7, bill.getPricePerDay());
            pstmt.setDouble(8, bill.getSubtotal());
            pstmt.setDouble(9, bill.getTaxRate());
            pstmt.setDouble(10, bill.getTax());
            pstmt.setDouble(11, bill.getTotalAmount());
            pstmt.setString(12, bill.getBillGeneratedDate().toString());
            pstmt.setString(13, bill.getStatus());
            
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
            System.err.println("Error adding bill: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Get all bills from database
     * @return List of Bill objects
     */
    public static List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bill bill = new Bill(
                        rs.getInt("bill_id"),
                        rs.getInt("booking_id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate(),
                        rs.getLong("number_of_days"),
                        rs.getDouble("price_per_day")
                );
                bill.setStatus(rs.getString("status"));
                bills.add(bill);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving bills: " + e.getMessage());
        }
        
        return bills;
    }
    
    /**
     * Get bill by ID
     * @param billId Bill ID to search
     * @return Bill object or null if not found
     */
    public static Bill getBillById(int billId) {
        String sql = "SELECT * FROM bills WHERE bill_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Bill bill = new Bill(
                        rs.getInt("bill_id"),
                        rs.getInt("booking_id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate(),
                        rs.getLong("number_of_days"),
                        rs.getDouble("price_per_day")
                );
                bill.setStatus(rs.getString("status"));
                return bill;
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving bill: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get bills by booking ID
     * @param bookingId Booking ID to search
     * @return List of Bill objects for the booking
     */
    public static List<Bill> getBillsByBookingId(int bookingId) {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE booking_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Bill bill = new Bill(
                        rs.getInt("bill_id"),
                        rs.getInt("booking_id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate(),
                        rs.getLong("number_of_days"),
                        rs.getDouble("price_per_day")
                );
                bill.setStatus(rs.getString("status"));
                bills.add(bill);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving bills by booking: " + e.getMessage());
        }
        
        return bills;
    }
    
    /**
     * Get unpaid bills
     * @return List of unpaid Bill objects
     */
    public static List<Bill> getUnpaidBills() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE status = 'Unpaid'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bill bill = new Bill(
                        rs.getInt("bill_id"),
                        rs.getInt("booking_id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate(),
                        rs.getLong("number_of_days"),
                        rs.getDouble("price_per_day")
                );
                bill.setStatus(rs.getString("status"));
                bills.add(bill);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving unpaid bills: " + e.getMessage());
        }
        
        return bills;
    }
    
    /**
     * Update bill status
     * @param billId Bill ID to update
     * @param status New status
     * @return true if successful, false otherwise
     */
    public static boolean updateBillStatus(int billId, String status) {
        String sql = "UPDATE bills SET status = ? WHERE bill_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, billId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating bill status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete bill from database
     * @param billId Bill ID to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteBill(int billId) {
        String sql = "DELETE FROM bills WHERE bill_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting bill: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get total revenue
     * @return Total revenue from all paid bills
     */
    public static double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) as total FROM bills WHERE status = 'Paid'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total revenue: " + e.getMessage());
        }
        
        return 0.0;
    }
}