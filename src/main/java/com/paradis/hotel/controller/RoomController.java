package com.paradis.hotel.controller;

import com.paradis.hotel.dao.RoomDAO;
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

/**
 * Room Controller Class
 * Manages room-related UI and operations
 * 
 * @author Paradis Hotel Management System
 * @version 1.0
 */
public class RoomController {
    
    private TableView<Room> roomTable;
    private ComboBox<String> filterComboBox;
    private Label totalRoomsLabel;
    private Label availableRoomsLabel;
    
    /**
     * Create room management UI
     */
    public VBox createRoomManagementUI() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        // Title
        Label titleLabel = new Label("🛏️ Room Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // Add Room Form
        VBox addRoomSection = createAddRoomSection();
        
        // Statistics
        HBox statsBox = createStatisticsBox();
        
        // Filters and Actions
        HBox filterBox = createFilterBox();
        
        // Room Table
        roomTable = createRoomTable();
        refreshRoomTable();
        
        ScrollPane tableScroll = new ScrollPane(roomTable);
        tableScroll.setFitToWidth(true);
        tableScroll.setPrefHeight(400);
        
        mainLayout.getChildren().addAll(
                titleLabel,
                new Separator(),
                addRoomSection,
                new Separator(),
                statsBox,
                filterBox,
                new Label("Room List:"),
                tableScroll
        );
        
        return mainLayout;
    }
    
    /**
     * Create add room form section
     */
    private VBox createAddRoomSection() {
        VBox section = new VBox(10);
        section.setPadding(new Insets(15));
        section.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        Label formTitle = new Label("Add New Room");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        // Room Number
        Label roomNumberLabel = new Label("Room Number:");
        TextField roomNumberField = new TextField();
        roomNumberField.setPromptText("e.g., 101, 102");
        form.add(roomNumberLabel, 0, 0);
        form.add(roomNumberField, 1, 0);
        
        // Room Type
        Label roomTypeLabel = new Label("Room Type:");
        ComboBox<String> roomTypeCombo = new ComboBox<>();
        roomTypeCombo.setItems(FXCollections.observableArrayList("Single", "Double", "Deluxe"));
        roomTypeCombo.setPrefWidth(200);
        form.add(roomTypeLabel, 0, 1);
        form.add(roomTypeCombo, 1, 1);
        
        // Price Per Day
        Label priceLabel = new Label("Price Per Day (₹):");
        TextField priceField = new TextField();
        priceField.setPromptText("e.g., 3000");
        form.add(priceLabel, 0, 2);
        form.add(priceField, 1, 2);
        
        // Add Room Button
        Button addBtn = new Button("✅ Add Room");
        addBtn.setStyle("-fx-padding: 10; -fx-font-size: 12; -fx-background-color: #2ecc71; " +
                       "-fx-text-fill: white; -fx-cursor: hand;");
        addBtn.setOnAction(e -> {
            if (validateRoomForm(roomNumberField, roomTypeCombo, priceField)) {
                addRoom(roomNumberField, roomTypeCombo, priceField);
            }
        });
        
        Button clearBtn = new Button("🔄 Clear");
        clearBtn.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        clearBtn.setOnAction(e -> {
            roomNumberField.clear();
            roomTypeCombo.setValue(null);
            priceField.clear();
        });
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addBtn, clearBtn);
        
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
        
        // Total Rooms
        VBox totalBox = new VBox(5);
        totalBox.setAlignment(Pos.CENTER);
        Label totalLabel = new Label("Total Rooms");
        totalLabel.setFont(Font.font("Arial", 12));
        totalRoomsLabel = new Label(String.valueOf(RoomDAO.getRoomCount()));
        totalRoomsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        totalRoomsLabel.setStyle("-fx-text-fill: #3498db;");
        totalBox.getChildren().addAll(totalLabel, totalRoomsLabel);
        
        // Available Rooms
        VBox availableBox = new VBox(5);
        availableBox.setAlignment(Pos.CENTER);
        Label availableLabel = new Label("Available Rooms");
        availableLabel.setFont(Font.font("Arial", 12));
        availableRoomsLabel = new Label(String.valueOf(RoomDAO.getAvailableRoomCount()));
        availableRoomsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        availableRoomsLabel.setStyle("-fx-text-fill: #2ecc71;");
        availableBox.getChildren().addAll(availableLabel, availableRoomsLabel);
        
        statsBox.getChildren().addAll(
                new Separator(javafx.geometry.Orientation.VERTICAL),
                totalBox,
                new Separator(javafx.geometry.Orientation.VERTICAL),
                availableBox,
                new Separator(javafx.geometry.Orientation.VERTICAL)
        );
        
        return statsBox;
    }
    
    /**
     * Create filter box
     */
    private HBox createFilterBox() {
        HBox filterBox = new HBox(10);
        filterBox.setPadding(new Insets(15));
        filterBox.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        filterBox.setAlignment(Pos.CENTER_LEFT);
        
        Label filterLabel = new Label("Filter By:");
        filterLabel.setFont(Font.font("Arial", 12));
        
        filterComboBox = new ComboBox<>();
        filterComboBox.setItems(FXCollections.observableArrayList("All Rooms", "Available", "Occupied"));
        filterComboBox.setValue("All Rooms");
        filterComboBox.setPrefWidth(150);
        filterComboBox.setOnAction(e -> applyFilter());
        
        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle("-fx-padding: 8; -fx-font-size: 11;");
        refreshBtn.setOnAction(e -> refreshRoomTable());
        
        filterBox.getChildren().addAll(filterLabel, filterComboBox, 
                                        new Separator(javafx.geometry.Orientation.VERTICAL),
                                        refreshBtn);
        
        return filterBox;
    }
    
    /**
     * Create room table
     */
    private TableView<Room> createRoomTable() {
        TableView<Room> table = new TableView<>();
        
        // Room Number Column
        TableColumn<Room, String> roomNumberCol = new TableColumn<>("Room Number");
        roomNumberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomNumberCol.setPrefWidth(120);
        
        // Room Type Column
        TableColumn<Room, String> roomTypeCol = new TableColumn<>("Room Type");
        roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        roomTypeCol.setPrefWidth(100);
        
        // Price Column
        TableColumn<Room, Double> priceCol = new TableColumn<>("Price/Day (₹)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        priceCol.setPrefWidth(120);
        
        // Availability Column
        TableColumn<Room, String> availabilityCol = new TableColumn<>("Status");
        availabilityCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getAvailabilityStatus()
            )
        );
        availabilityCol.setPrefWidth(100);
        
        table.getColumns().addAll(roomNumberCol, roomTypeCol, priceCol, availabilityCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        return table;
    }
    
    /**
     * Refresh room table with data
     */
    private void refreshRoomTable() {
        ObservableList<Room> rooms = FXCollections.observableArrayList(RoomDAO.getAllRooms());
        roomTable.setItems(rooms);
        updateStatistics();
    }
    
    /**
     * Apply filter to room table
     */
    private void applyFilter() {
        String filter = filterComboBox.getValue();
        ObservableList<Room> filteredRooms = FXCollections.observableArrayList();
        
        for (Room room : RoomDAO.getAllRooms()) {
            if (filter.equals("Available") && room.isAvailability()) {
                filteredRooms.add(room);
            } else if (filter.equals("Occupied") && !room.isAvailability()) {
                filteredRooms.add(room);
            } else if (filter.equals("All Rooms")) {
                filteredRooms.add(room);
            }
        }
        
        roomTable.setItems(filteredRooms);
    }
    
    /**
     * Add new room
     */
    private void addRoom(TextField roomNumberField, ComboBox<String> roomTypeCombo, 
                        TextField priceField) {
        try {
            Room room = new Room();
            room.setRoomNumber(roomNumberField.getText());
            room.setRoomType(roomTypeCombo.getValue());
            room.setPricePerDay(Double.parseDouble(priceField.getText()));
            room.setAvailability(true);
            
            if (RoomDAO.addRoom(room)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                         "Room added successfully!");
                roomNumberField.clear();
                roomTypeCombo.setValue(null);
                priceField.clear();
                refreshRoomTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", 
                         "Failed to add room. Room number might already exist.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid price format!");
        }
    }
    
    /**
     * Validate room form inputs
     */
    private boolean validateRoomForm(TextField roomNumberField, ComboBox<String> roomTypeCombo, 
                                     TextField priceField) {
        if (roomNumberField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Please enter room number!");
            return false;
        }
        
        if (roomTypeCombo.getValue() == null || roomTypeCombo.getValue().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Please select room type!");
            return false;
        }
        
        if (priceField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", 
                     "Please enter price per day!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Update statistics labels
     */
    private void updateStatistics() {
        totalRoomsLabel.setText(String.valueOf(RoomDAO.getRoomCount()));
        availableRoomsLabel.setText(String.valueOf(RoomDAO.getAvailableRoomCount()));
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