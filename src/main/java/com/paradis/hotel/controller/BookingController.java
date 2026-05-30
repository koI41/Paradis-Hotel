package com.paradis.hotel.controller;

import com.paradis.hotel.dao.BookingDAO;
import com.paradis.hotel.dao.CustomerDAO;
import com.paradis.hotel.dao.RoomDAO;
import com.paradis.hotel.model.Booking;
import com.paradis.hotel.model.Customer;
import com.paradis.hotel.model.Room;
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
import java.time.LocalDate;

/**
 * Booking Controller Class
 * Manages booking-related UI and operations
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class BookingController {
    
    private TableView<Booking> bookingTable;
    private Label activeBookingsLabel;
    
    /**
     * Create booking management UI
     */
    public VBox createBookingUI() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        // Title
        Label titleLabel = new Label("📝 Booking Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // Booking Form
        VBox bookingFormSection = createBookingFormSection();
        
        // Statistics
        HBox statsBox = createStatisticsBox();
        
        // Booking Table
        bookingTable = createBookingTable();
        refreshBookingTable();
        
        ScrollPane tableScroll = new ScrollPane(bookingTable);
        tableScroll.setFitToWidth(true);
        tableScroll.setPrefHeight(400);

        HBox bookingActions = createBookingActions();
        
        mainLayout.getChildren().addAll(
                titleLabel,
                new Separator(),
                bookingFormSection,
                new Separator(),
                statsBox,
                new Label("Active Bookings:"),
                tableScroll,
                bookingActions
        );
        
        return mainLayout;
    }

    /**
     * Create booking action buttons
     */
    private HBox createBookingActions() {
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button cancelBtn = new Button("❌ Cancel Selected Booking");
        cancelBtn.setStyle("-fx-padding: 10; -fx-font-size: 12; -fx-background-color: #e74c3c; " +
                "-fx-text-fill: white; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> cancelSelectedBooking());

        Button refreshBtn = new Button("🔄 Refresh Bookings");
        refreshBtn.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        refreshBtn.setOnAction(e -> refreshBookingTable());

        actions.getChildren().addAll(cancelBtn, refreshBtn);
        return actions;
    }
    
    /**
     * Create booking form section
     */
    private VBox createBookingFormSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        Label formTitle = new Label("New Booking");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        // Customer Name
        Label nameLabel = new Label("Customer Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Full name");
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);
        
        // Contact Number
        Label phoneLabel = new Label("Contact Number:");
        TextField phoneField = new TextField();
        phoneField.setPromptText("10-digit mobile number");
        form.add(phoneLabel, 2, 0);
        form.add(phoneField, 3, 0);
        
        // Room Selection
        Label roomLabel = new Label("Select Room:");
        ComboBox<String> roomCombo = new ComboBox<>();
        updateAvailableRooms(roomCombo);
        roomCombo.setPrefWidth(200);
        form.add(roomLabel, 0, 1);
        form.add(roomCombo, 1, 1);
        
        // Check-in Date
        Label checkInLabel = new Label("Check-in Date:");
        DatePicker checkInPicker = new DatePicker();
        checkInPicker.setValue(LocalDate.now());
        form.add(checkInLabel, 2, 1);
        form.add(checkInPicker, 3, 1);
        
        // Check-out Date
        Label checkOutLabel = new Label("Check-out Date:");
        DatePicker checkOutPicker = new DatePicker();
        checkOutPicker.setValue(LocalDate.now().plusDays(1));
        form.add(checkOutLabel, 0, 2);
        form.add(checkOutPicker, 1, 2);
        
        // Room Details Display
        Label roomDetailsLabel = new Label("Room Details:");
        TextArea roomDetailsArea = new TextArea();
        roomDetailsArea.setPrefRowCount(3);
        roomDetailsArea.setWrapText(true);
        roomDetailsArea.setEditable(false);
        form.add(roomDetailsLabel, 2, 2);
        form.add(roomDetailsArea, 3, 2);
        
        // Update room details when room is selected
        roomCombo.setOnAction(e -> {
            if (roomCombo.getValue() != null) {
                Room room = RoomDAO.getRoomByNumber(roomCombo.getValue());
                if (room != null) {
                    String details = "Room: " + room.getRoomNumber() + "\n" +
                                   "Type: " + room.getRoomType() + "\n" +
                                   "Price: ₹" + room.getPricePerDay() + "/day";
                    roomDetailsArea.setText(details);
                }
            }
        });
        
        // Buttons
        Button bookBtn = new Button("✅ Book Room");
        bookBtn.setStyle("-fx-padding: 10; -fx-font-size: 12; -fx-background-color: #3498db; " +
                        "-fx-text-fill: white; -fx-cursor: hand;");
        bookBtn.setOnAction(e -> {
            if (validateBookingForm(nameField, phoneField, roomCombo, checkInPicker, checkOutPicker)) {
                bookRoom(nameField, phoneField, roomCombo, checkInPicker, checkOutPicker);
            }
        });
        
        Button refreshBtn = new Button("🔄 Refresh Rooms");
        refreshBtn.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        refreshBtn.setOnAction(e -> updateAvailableRooms(roomCombo));
        
        Button clearBtn = new Button("🔄 Clear");
        clearBtn.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        clearBtn.setOnAction(e -> {
            nameField.clear();
            phoneField.clear();
            roomCombo.setValue(null);
            roomDetailsArea.clear();
            checkInPicker.setValue(LocalDate.now());
            checkOutPicker.setValue(LocalDate.now().plusDays(1));
        });
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(bookBtn, refreshBtn, clearBtn);
        
        form.add(buttonBox, 1, 3);
        
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
        
        // Active Bookings
        VBox activeBox = new VBox(5);
        activeBox.setAlignment(Pos.CENTER);
        Label activeLabel = new Label("Active Bookings");
        activeLabel.setFont(Font.font("Arial", 12));
        activeBookingsLabel = new Label(String.valueOf(BookingDAO.getActiveBookings().size()));
        activeBookingsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        activeBookingsLabel.setStyle("-fx-text-fill: #e74c3c;");
        activeBox.getChildren().addAll(activeLabel, activeBookingsLabel);
        
        // Total Bookings
        VBox totalBox = new VBox(5);
        totalBox.setAlignment(Pos.CENTER);
        Label totalLabel = new Label("Total Bookings");
        totalLabel.setFont(Font.font("Arial", 12));
        Label totalBookingsLabel = new Label(String.valueOf(BookingDAO.getBookingCount()));
        totalBookingsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        totalBookingsLabel.setStyle("-fx-text-fill: #3498db;");
        totalBox.getChildren().addAll(totalLabel, totalBookingsLabel);
        
        statsBox.getChildren().addAll(
                new Separator(javafx.geometry.Orientation.VERTICAL),
                activeBox,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                totalBox,
                new Separator(javafx.geometry.Orientation.VERTICAL)
        );
        
        return statsBox;
    }
    
    /**
     * Create booking table
     */
    private TableView<Booking> createBookingTable() {
        TableView<Booking> table = new TableView<>();
        
        TableColumn<Booking, Integer> bookingIdCol = new TableColumn<>("Booking ID");
        bookingIdCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        bookingIdCol.setPrefWidth(80);
        
        TableColumn<Booking, String> customerCol = new TableColumn<>("Customer Name");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerCol.setPrefWidth(140);
        
        TableColumn<Booking, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomCol.setPrefWidth(80);
        
        TableColumn<Booking, String> roomTypeCol = new TableColumn<>("Room Type");
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        roomTypeCol.setPrefWidth(100);
        
        TableColumn<Booking, LocalDate> checkInCol = new TableColumn<>("Check-in");
        checkInCol.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkInCol.setPrefWidth(100);
        
        TableColumn<Booking, LocalDate> checkOutCol = new TableColumn<>("Check-out");
        checkOutCol.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        checkOutCol.setPrefWidth(100);
        
        TableColumn<Booking, Double> costCol = new TableColumn<>("Total Cost (₹)");
        costCol.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        costCol.setPrefWidth(120);
        
        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);
        
        table.getColumns().addAll(bookingIdCol, customerCol, roomCol, roomTypeCol, 
                                   checkInCol, checkOutCol, costCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        return table;
    }
    
    /**
     * Update available rooms in combobox
     */
    private void updateAvailableRooms(ComboBox<String> roomCombo) {
        ObservableList<String> availableRooms = FXCollections.observableArrayList();
        for (Room room : RoomDAO.getAvailableRooms()) {
            availableRooms.add(room.getRoomNumber());
        }
        roomCombo.setItems(availableRooms);
    }
    
    /**
     * Refresh booking table
     */
    private void refreshBookingTable() {
        ObservableList<Booking> bookings = FXCollections.observableArrayList(
            BookingDAO.getActiveBookings()
        );
        bookingTable.setItems(bookings);
        activeBookingsLabel.setText(String.valueOf(bookings.size()));
    }

    /**
     * Cancel selected booking and free the room
     */
    private void cancelSelectedBooking() {
        Booking selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a booking from the table to cancel.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancellation");
        confirm.setHeaderText("Cancel Booking ID: " + selectedBooking.getBookingId());
        confirm.setContentText("Are you sure you want to cancel this booking?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        boolean bookingCancelled = BookingDAO.updateBookingStatus(selectedBooking.getBookingId(), "Cancelled");
        if (!bookingCancelled) {
            showAlert(Alert.AlertType.ERROR, "Cancellation Failed",
                    "Could not cancel booking. Please try again.");
            return;
        }

        if (!RoomDAO.updateRoomAvailability(selectedBooking.getRoomNumber(), true)) {
            showAlert(Alert.AlertType.WARNING, "Partial Success",
                    "Booking was cancelled, but room status update failed.");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Booking Cancelled",
                    "Booking cancelled successfully. Room " + selectedBooking.getRoomNumber() + " is now available.");
        }

        refreshBookingTable();
    }
    
    /**
     * Book a room
     */
    private void bookRoom(TextField nameField, TextField phoneField, ComboBox<String> roomCombo,
                         DatePicker checkInPicker, DatePicker checkOutPicker) {
        try {
            String customerName = nameField.getText().trim();
            String phoneNumber = phoneField.getText().trim();
            String roomNumber = roomCombo.getValue();
            LocalDate checkInDate = checkInPicker.getValue();
            LocalDate checkOutDate = checkOutPicker.getValue();

            Room room = RoomDAO.getRoomByNumber(roomNumber);
            if (room == null || !room.isAvailability()) {
                showAlert(Alert.AlertType.ERROR, "Room Unavailable",
                        "The selected room is no longer available. Please refresh and try again.");
                updateAvailableRooms(roomCombo);
                return;
            }
            
            // Create customer
            Customer customer = new Customer();
            customer.setCustomerName(customerName);
            customer.setContactNumber(phoneNumber);
            customer.setRoomNumber(roomNumber);
            customer.setCheckInDate(checkInDate);
            customer.setCheckOutDate(checkOutDate);
            
            int customerId = CustomerDAO.addCustomer(customer);
            if (customerId <= 0) {
                showAlert(Alert.AlertType.ERROR, "Booking Failed",
                        "Could not save customer details. Please check input and try again.");
                return;
            }
            
            // Create booking
            Booking booking = new Booking(
                    0,
                    customerId,
                    customerName,
                    roomNumber,
                    room.getRoomType(),
                    checkInDate,
                    checkOutDate,
                    room.getPricePerDay()
            );

            int bookingId = BookingDAO.addBooking(booking);
            if (bookingId <= 0) {
                CustomerDAO.deleteCustomer(customerId);
                showAlert(Alert.AlertType.ERROR, "Booking Failed",
                        "Could not create booking record. Please try again.");
                return;
            }

            if (!RoomDAO.updateRoomAvailability(roomNumber, false)) {
                showAlert(Alert.AlertType.WARNING, "Partial Success",
                        "Booking was created but room status update failed. Please refresh data.");
            }

            showAlert(Alert.AlertType.INFORMATION, "Success",
                     "Booking created successfully!\nBooking ID: " + bookingId);

            nameField.clear();
            phoneField.clear();
            roomCombo.setValue(null);
            checkInPicker.setValue(LocalDate.now());
            checkOutPicker.setValue(LocalDate.now().plusDays(1));

            refreshBookingTable();
            updateAvailableRooms(roomCombo);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to book room: " + e.getMessage());
        }
    }
    
    /**
     * Validate booking form
     */
    private boolean validateBookingForm(TextField nameField, TextField phoneField,
                                       ComboBox<String> roomCombo, DatePicker checkInPicker,
                                       DatePicker checkOutPicker) {
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter customer name!");
            return false;
        }
        
        if (phoneField.getText().isEmpty() || !phoneField.getText().matches("\\d{10}")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                     "Please enter valid 10-digit phone number!");
            return false;
        }
        
        if (roomCombo.getValue() == null || roomCombo.getValue().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a room!");
            return false;
        }
        
        LocalDate checkInDate = checkInPicker.getValue();
        LocalDate checkOutDate = checkOutPicker.getValue();
        
        if (!checkOutDate.isAfter(checkInDate)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                     "Check-out date must be after check-in date!");
            return false;
        }
        
        return true;
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