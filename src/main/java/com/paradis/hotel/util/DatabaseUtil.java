package com.paradis.hotel.util;

import java.sql.*;

/**
 * Database Utility Class
 * Handles all database connections and operations using SQLite
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class DatabaseUtil {
    
    // SQLite database file path
    private static final String DATABASE_URL = "jdbc:sqlite:paradis_hotel.db";
    
    /**
     * Initialize database and create tables if they don't exist
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                // Create tables
                createRoomsTable(conn);
                createCustomersTable(conn);
                createBookingsTable(conn);
                createBillsTable(conn);
                
                System.out.println("✓ Database initialized successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    /**
     * Get database connection
     * @return Connection object
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(DATABASE_URL);
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite driver not found: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Create Rooms table
     */
    private static void createRoomsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS rooms (" +
                "room_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_number TEXT NOT NULL UNIQUE," +
                "room_type TEXT NOT NULL," +
                "price_per_day REAL NOT NULL," +
                "availability INTEGER NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    /**
     * Create Customers table
     */
    private static void createCustomersTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS customers (" +
                "customer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_name TEXT NOT NULL," +
                "contact_number TEXT NOT NULL," +
                "room_number TEXT NOT NULL," +
                "check_in_date DATE NOT NULL," +
                "check_out_date DATE NOT NULL," +
                "is_checked_in INTEGER NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    /**
     * Create Bookings table
     */
    private static void createBookingsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS bookings (" +
                "booking_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_id INTEGER NOT NULL," +
                "customer_name TEXT NOT NULL," +
                "room_number TEXT NOT NULL," +
                "room_type TEXT NOT NULL," +
                "check_in_date DATE NOT NULL," +
                "check_out_date DATE NOT NULL," +
                "price_per_day REAL NOT NULL," +
                "total_cost REAL NOT NULL," +
                "status TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(customer_id) REFERENCES customers(customer_id)" +
                ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    /**
     * Create Bills table
     */
    private static void createBillsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS bills (" +
                "bill_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "booking_id INTEGER NOT NULL," +
                "customer_name TEXT NOT NULL," +
                "room_number TEXT NOT NULL," +
                "check_in_date DATE NOT NULL," +
                "check_out_date DATE NOT NULL," +
                "number_of_days INTEGER NOT NULL," +
                "price_per_day REAL NOT NULL," +
                "subtotal REAL NOT NULL," +
                "tax_rate REAL NOT NULL," +
                "tax REAL NOT NULL," +
                "total_amount REAL NOT NULL," +
                "bill_generated_date DATE NOT NULL," +
                "status TEXT NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(booking_id) REFERENCES bookings(booking_id)" +
                ")";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close resources (ResultSet, Statement, Connection)
     */
    public static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}