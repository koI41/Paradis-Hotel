package com.paradis.hotel.dao;

import com.paradis.hotel.model.Customer;
import com.paradis.hotel.util.DatabaseUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer Data Access Object (DAO)
 * Handles all database operations related to customers
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class CustomerDAO {
    
    /**
     * Add a new customer to the database
     * @param customer Customer object to add
     * @return Generated customer ID, or -1 if failed
     */
    public static int addCustomer(Customer customer) {
        String sql = "INSERT INTO customers(customer_name, contact_number, room_number, " +
                     "check_in_date, check_out_date, is_checked_in) " +
                     "VALUES(?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, customer.getCustomerName());
            pstmt.setString(2, customer.getContactNumber());
            pstmt.setString(3, customer.getRoomNumber());
            pstmt.setString(4, customer.getCheckInDate().toString());
            pstmt.setString(5, customer.getCheckOutDate().toString());
            pstmt.setInt(6, customer.isCheckedIn() ? 1 : 0);
            
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
            System.err.println("Error adding customer: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Get all customers from database
     * @return List of Customer objects
     */
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("contact_number"),
                        rs.getString("room_number"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate()
                );
                customer.setCheckedIn(rs.getInt("is_checked_in") == 1);
                customers.add(customer);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving customers: " + e.getMessage());
        }
        
        return customers;
    }
    
    /**
     * Get customer by ID
     * @param customerId Customer ID to search
     * @return Customer object or null if not found
     */
    public static Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("contact_number"),
                        rs.getString("room_number"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate()
                );
                customer.setCheckedIn(rs.getInt("is_checked_in") == 1);
                return customer;
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving customer: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get customers by room number
     * @param roomNumber Room number to search
     * @return List of Customer objects in that room
     */
    public static List<Customer> getCustomersByRoomNumber(String roomNumber) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE room_number = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("contact_number"),
                        rs.getString("room_number"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate()
                );
                customer.setCheckedIn(rs.getInt("is_checked_in") == 1);
                customers.add(customer);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving customers by room: " + e.getMessage());
        }
        
        return customers;
    }
    
    /**
     * Update customer information
     * @param customer Customer object with updated information
     * @return true if successful, false otherwise
     */
    public static boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET customer_name = ?, contact_number = ?, " +
                     "room_number = ?, check_in_date = ?, check_out_date = ?, " +
                     "is_checked_in = ? WHERE customer_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getCustomerName());
            pstmt.setString(2, customer.getContactNumber());
            pstmt.setString(3, customer.getRoomNumber());
            pstmt.setString(4, customer.getCheckInDate().toString());
            pstmt.setString(5, customer.getCheckOutDate().toString());
            pstmt.setInt(6, customer.isCheckedIn() ? 1 : 0);
            pstmt.setInt(7, customer.getCustomerId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete customer from database
     * @param customerId Customer ID to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get customer count
     * @return Total number of customers
     */
    public static int getCustomerCount() {
        String sql = "SELECT COUNT(*) as count FROM customers";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer count: " + e.getMessage());
        }
        
        return 0;
    }
}