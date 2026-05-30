package com.paradis.hotel.controller;

import com.paradis.hotel.dao.BillDAO;
import com.paradis.hotel.dao.BookingDAO;
import com.paradis.hotel.model.Bill;
import com.paradis.hotel.model.Booking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Billing Controller Class
 * Manages billing-related UI and operations
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class BillingController {
    
    private TableView<Bill> billTable;
    private Label unpaidBillsLabel;
    private Label totalRevenueLabel;
    
    /**
     * Create billing management UI
     */
    public VBox createBillingUI() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        // Title
        Label titleLabel = new Label("💳 Billing & Checkout");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // Checkout Section
        VBox checkoutSection = createCheckoutSection();
        
        // Statistics
        HBox statsBox = createStatisticsBox();
        
        // Bill Table
        billTable = createBillTable();
        refreshBillTable();
        
        ScrollPane tableScroll = new ScrollPane(billTable);
        tableScroll.setFitToWidth(true);
        tableScroll.setPrefHeight(400);
        
        mainLayout.getChildren().addAll(
                titleLabel,
                new Separator(),
                checkoutSection,
                new Separator(),
                statsBox,
                new Label("Bill History:"),
                tableScroll
        );
        
        return mainLayout;
    }
    
    /**
     * Create checkout section
     */
    private VBox createCheckoutSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        Label formTitle = new Label("Checkout & Generate Bill");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        // Select Booking to Checkout
        Label bookingLabel = new Label("Select Booking:");
        ComboBox<String> bookingCombo = new ComboBox<>();
        bookingCombo.setPrefWidth(300);
        updateActiveBookings(bookingCombo);
        form.add(bookingLabel, 0, 0);
        form.add(bookingCombo, 1, 0);
        
        // Booking Details
        Label detailsLabel = new Label("Booking Details:");
        TextArea detailsArea = new TextArea();
        detailsArea.setPrefRowCount(5);
        detailsArea.setWrapText(true);
        detailsArea.setEditable(false);
        form.add(detailsLabel, 0, 1);
        form.add(detailsArea, 1, 1);
        
        // Update details when booking is selected
        bookingCombo.setOnAction(e -> {
            if (bookingCombo.getValue() != null) {
                try {
                    int bookingId = Integer.parseInt(bookingCombo.getValue().split(" - ")[0]);
                    Booking booking = BookingDAO.getBookingById(bookingId);
                    if (booking != null) {
                        long days = booking.getNumberOfDays();
                        double total = days * booking.getPricePerDay();
                        String details = "Booking ID: " + booking.getBookingId() + "\n" +
                                       "Customer: " + booking.getCustomerName() + "\n" +
                                       "Room: " + booking.getRoomNumber() + "\n" +
                                       "Check-in: " + booking.getCheckInDate() + "\n" +
                                       "Check-out: " + booking.getCheckOutDate() + "\n" +
                                       "Days: " + days + "\n" +
                                       "Price/Day: ₹" + booking.getPricePerDay() + "\n" +
                                       "Subtotal: ₹" + String.format("%.2f", booking.getTotalCost()) + "\n" +
                                       "Tax (5%): ₹" + String.format("%.2f", booking.getTotalCost() * 0.05) + "\n" +
                                       "Total with Tax: ₹" + String.format("%.2f", 
                                           booking.getTotalCost() * 1.05);
                        detailsArea.setText(details);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // Buttons
        Button generateBillBtn = new Button("📄 Generate Bill");
        generateBillBtn.setStyle("-fx-padding: 10; -fx-font-size: 12; -fx-background-color: #27ae60; " +
                                "-fx-text-fill: white; -fx-cursor: hand;");
        generateBillBtn.setOnAction(e -> {
            if (bookingCombo.getValue() != null) {
                generateBill(bookingCombo);
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a booking!");
            }
        });
        
        Button checkoutBtn = new Button("✅ Checkout & Confirm");
        checkoutBtn.setStyle("-fx-padding: 10; -fx-font-size: 12; -fx-background-color: #16a085; " +
                            "-fx-text-fill: white; -fx-cursor: hand;");
        checkoutBtn.setOnAction(e -> {
            if (bookingCombo.getValue() != null) {
                performCheckout(bookingCombo);
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a booking!");
            }
        });
        
        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        refreshBtn.setOnAction(e -> {
            updateActiveBookings(bookingCombo);
            detailsArea.clear();
        });
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(generateBillBtn, checkoutBtn, refreshBtn);
        
        form.add(buttonBox, 1, 2);
        
        section.getChildren().addAll(formTitle, form);
        return section;
    }
    
    /**
     * Create statistics box
     */
    private HBox createStatisticsBox() {
        HBox statsBox = new HBox(20);
        statsBox.setPadding(new Insets(15));
        statsBox.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        statsBox.setAlignment(Pos.CENTER_LEFT);
        
        // Unpaid Bills
        VBox unpaidBox = new VBox(5);
        unpaidBox.setAlignment(Pos.CENTER);
        Label unpaidLabel = new Label("Unpaid Bills");
        unpaidLabel.setFont(Font.font("Arial", 12));
        unpaidBillsLabel = new Label(String.valueOf(BillDAO.getUnpaidBills().size()));
        unpaidBillsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        unpaidBillsLabel.setStyle("-fx-text-fill: #e74c3c;");
        unpaidBox.getChildren().addAll(unpaidLabel, unpaidBillsLabel);
        
        // Total Revenue
        VBox revenueBox = new VBox(5);
        revenueBox.setAlignment(Pos.CENTER);
        Label revenueLabel = new Label("Total Revenue");
        revenueLabel.setFont(Font.font("Arial", 12));
        totalRevenueLabel = new Label("₹" + String.format("%.2f", BillDAO.getTotalRevenue()));
        totalRevenueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        totalRevenueLabel.setStyle("-fx-text-fill: #2ecc71;");
        revenueBox.getChildren().addAll(revenueLabel, totalRevenueLabel);
        
        statsBox.getChildren().addAll(
                new Separator(javafx.geometry.Orientation.VERTICAL),
                unpaidBox,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                revenueBox,
                new Separator(javafx.geometry.Orientation.VERTICAL)
        );
        
        return statsBox;
    }
    
    /**
     * Create bill table
     */
    private TableView<Bill> createBillTable() {
        TableView<Bill> table = new TableView<>();
        
        TableColumn<Bill, Integer> billIdCol = new TableColumn<>("Bill ID");
        billIdCol.setCellValueFactory(new PropertyValueFactory<>("billId"));
        billIdCol.setPrefWidth(80);
        
        TableColumn<Bill, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCol.setPrefWidth(140);
        
        TableColumn<Bill, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomCol.setPrefWidth(80);
        
        TableColumn<Bill, Long> daysCol = new TableColumn<>("Days");
        daysCol.setCellValueFactory(new PropertyValueFactory<>("numberOfDays"));
        daysCol.setPrefWidth(60);
        
        TableColumn<Bill, Double> totalCol = new TableColumn<>("Total Amount (₹)");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalCol.setPrefWidth(150);
        
        TableColumn<Bill, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);
        
        table.getColumns().addAll(billIdCol, customerCol, roomCol, daysCol, totalCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        return table;
    }
    
    /**
     * Update active bookings in combobox
     */
    private void updateActiveBookings(ComboBox<String> bookingCombo) {
        ObservableList<String> activeBookings = FXCollections.observableArrayList();
        for (Booking booking : BookingDAO.getActiveBookings()) {
            String item = booking.getBookingId() + " - " + booking.getCustomerName() + 
                         " (Room: " + booking.getRoomNumber() + ")";
            activeBookings.add(item);
        }
        bookingCombo.setItems(activeBookings);
    }
    
    /**
     * Generate bill for a booking
     */
    private void generateBill(ComboBox<String> bookingCombo) {
        try {
            int bookingId = Integer.parseInt(bookingCombo.getValue().split(" - ")[0]);
            Booking booking = BookingDAO.getBookingById(bookingId);
            
            if (booking != null) {
                Bill bill = new Bill(
                        0,
                        bookingId,
                        booking.getCustomerName(),
                        booking.getRoomNumber(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getNumberOfDays(),
                        booking.getPricePerDay()
                );
                
                // Display bill
                showBillDialog(bill);
                
                // Save bill to database
                int billId = BillDAO.addBill(bill);
                
                if (billId > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                             "Bill generated successfully!\nBill ID: " + billId);
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate bill: " + e.getMessage());
        }
    }
    
    /**
     * Show bill dialog
     */
    private void showBillDialog(Bill bill) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bill Details");
        alert.setHeaderText(null);
        
        TextArea billArea = new TextArea(bill.generateBill());
        billArea.setEditable(false);
        billArea.setWrapText(true);
        billArea.setFont(Font.font("Courier New", 11));
        billArea.setPrefRowCount(25);
        billArea.setPrefColumnCount(60);
        
        ScrollPane scrollPane = new ScrollPane(billArea);
        scrollPane.setFitToWidth(true);
        
        alert.getDialogPane().setContent(scrollPane);
        alert.showAndWait();
    }
    
    /**
     * Perform checkout
     */
    private void performCheckout(ComboBox<String> bookingCombo) {
        try {
            int bookingId = Integer.parseInt(bookingCombo.getValue().split(" - ")[0]);
            Booking booking = BookingDAO.getBookingById(bookingId);
            
            if (booking != null) {
                // Create and save bill
                Bill bill = new Bill(
                        0,
                        bookingId,
                        booking.getCustomerName(),
                        booking.getRoomNumber(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getNumberOfDays(),
                        booking.getPricePerDay()
                );
                
                int billId = BillDAO.addBill(bill);
                
                if (billId > 0) {
                    // Update booking status
                    BookingDAO.updateBookingStatus(bookingId, "Checked Out");
                    
                    // Update room availability
                    com.paradis.hotel.dao.RoomDAO.updateRoomAvailability(
                        booking.getRoomNumber(), true
                    );
                    
                    // Mark bill as paid
                    BillDAO.updateBillStatus(billId, "Paid");
                    
                    showAlert(Alert.AlertType.INFORMATION, "Checkout Successful",
                             "Booking checked out successfully!\n" +
                             "Room " + booking.getRoomNumber() + " is now available.\n" +
                             "Bill ID: " + billId + "\n" +
                             "Total Amount: ₹" + String.format("%.2f", bill.getTotalAmount()));
                    
                    updateActiveBookings(bookingCombo);
                    refreshBillTable();
                    updateStatistics();
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to checkout: " + e.getMessage());
        }
    }
    
    /**
     * Refresh bill table
     */
    private void refreshBillTable() {
        ObservableList<Bill> bills = FXCollections.observableArrayList(BillDAO.getAllBills());
        billTable.setItems(bills);
    }
    
    /**
     * Update statistics
     */
    private void updateStatistics() {
        unpaidBillsLabel.setText(String.valueOf(BillDAO.getUnpaidBills().size()));
        totalRevenueLabel.setText("₹" + String.format("%.2f", BillDAO.getTotalRevenue()));
    }
    
    /**
     * Show alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}