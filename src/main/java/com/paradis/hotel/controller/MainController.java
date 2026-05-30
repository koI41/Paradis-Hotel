package com.paradis.hotel.controller;

import com.paradis.hotel.dao.*;
import com.paradis.hotel.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.application.Platform;

    import java.io.FileInputStream;
    import java.io.InputStream;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;

    /**
     * Main Controller Class
     * Manages the main dashboard and navigation between modules
     * 
     * @author Paradis Hotel Management System
     * @version 1.0
     */
    public class MainController {
        
        private RoomController roomController;
        private BookingController bookingController;
        private BillingController billingController;
        
        private BorderPane mainLayout;
        private Label welcomeLabel;
        private Label roomStatusLabel;
        private Label totalBookingsLabel;
        private Label totalRoomsValueLabel;
        private Label availableRoomsValueLabel;
        private Label activeBookingsValueLabel;
        private final ExecutorService dashboardExecutor;
        
        /**
         * Initialize the main controller
         */
        public MainController() {
            this.roomController = new RoomController();
            this.bookingController = new BookingController();
            this.billingController = new BillingController();
            this.dashboardExecutor = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "dashboard-stats-worker");
                t.setDaemon(true);
                return t;
            });
        }
        
        /**
         * Create the main layout
         * @return BorderPane with complete main layout
         */
        public BorderPane createMainLayout() {
            mainLayout = new BorderPane();
            mainLayout.setStyle("-fx-font-family: 'Arial'; -fx-background-color: #f5f5f5;");
            
            // Top - Header
            mainLayout.setTop(createHeader());
            
            // Left - Navigation
            mainLayout.setLeft(createNavigation());
            
            // Center - Dashboard
            mainLayout.setCenter(createDashboard());
            
            return mainLayout;
        }
        
        /**
         * Create header section
         */
        private VBox createHeader() {
            VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #0f2439; -fx-text-fill: white;");
        header.setAlignment(Pos.CENTER_LEFT);

        ImageView logoView = createLogoView();
        
        Label titleLabel = new Label("PARADIS HOTEL MANAGEMENT SYSTEM");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        Label subtitleLabel = new Label("Premium Hotel Management Solution");
        subtitleLabel.setFont(Font.font("Arial", 12));
        subtitleLabel.setStyle("-fx-text-fill: #ecf0f1;");
        
        if (logoView != null) {
            header.getChildren().add(logoView);
        }
        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }

    private ImageView createLogoView() {
        Image image = loadLogoImage();
        if (image == null) {
            return null;
        }

        ImageView logoView = new ImageView(image);
        logoView.setFitWidth(64);
        logoView.setFitHeight(64);
        logoView.setPreserveRatio(true);
        return logoView;
    }

    private Image loadLogoImage() {
        InputStream resourceStream = getClass().getResourceAsStream("/logo.png");
        if (resourceStream != null) {
            return new Image(resourceStream);
        }

        try {
            return new Image(new FileInputStream("src/logo.png"));
        } catch (Exception ignored) {
            return null;
        }
    }
    
    /**
     * Create left navigation panel
     */
    private VBox createNavigation() {
        VBox navigation = new VBox(10);
        navigation.setPadding(new Insets(20));
        navigation.setStyle("-fx-background-color: #34495e;");
        navigation.setMinWidth(200);
        navigation.setPrefWidth(200);
        
        Label navTitle = new Label("MENU");
        navTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        navTitle.setStyle("-fx-text-fill: white;");
        
        // Navigation Buttons
        Button dashboardBtn = createNavButton("📊 Dashboard", event -> {
            mainLayout.setCenter(createDashboard());
        });
        
        Button roomBtn = createNavButton("🛏️ Room Management", event -> {
            mainLayout.setCenter(roomController.createRoomManagementUI());
        });
        
        Button bookingBtn = createNavButton("📝 Bookings", event -> {
            mainLayout.setCenter(bookingController.createBookingUI());
        });
        
        Button billingBtn = createNavButton("💳 Billing", event -> {
            mainLayout.setCenter(billingController.createBillingUI());
        });
        
        Button exitBtn = createNavButton("❌ Exit", event -> {
            System.exit(0);
        });
        
        navigation.getChildren().addAll(navTitle, 
                new Separator(), 
                dashboardBtn, 
                roomBtn, 
                bookingBtn, 
                billingBtn,
                new Separator(),
                exitBtn);
        
        return navigation;
    }
    
    /**
     * Create navigation button with consistent styling
     */
    private Button createNavButton(String text, javafx.event.EventHandler event) {
        Button btn = new Button(text);
        btn.setStyle("-fx-font-size: 12; -fx-padding: 12; " +
                     "-fx-background-color: #2c3e50; -fx-text-fill: white; " +
                     "-fx-cursor: hand;");
        btn.setMinWidth(180);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-font-size: 12; -fx-padding: 12; " +
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-font-size: 12; -fx-padding: 12; " +
                "-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-cursor: hand;"));
        btn.setOnAction(event);
        return btn;
    }
    
    /**
     * Create dashboard content
     */
    private VBox createDashboard() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(30));
        dashboard.setStyle("-fx-background-color: #f5f5f5;");
        
        // Welcome Section
        VBox welcomeSection = createWelcomeSection();
        
        // Statistics Grid
        GridPane statsGrid = createStatisticsGrid();
        
        // Quick Actions
        VBox quickActions = createQuickActionsSection();
        
        ScrollPane scrollPane = new ScrollPane(new VBox(20, welcomeSection, statsGrid, quickActions));
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f5f5;");
        
        dashboard.getChildren().add(scrollPane);
        refreshDashboardStatsAsync();
        return dashboard;
    }
    
    /**
     * Create welcome section
     */
    private VBox createWelcomeSection() {
        VBox welcome = new VBox(10);
        welcome.setPadding(new Insets(20));
        welcome.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        welcomeLabel = new Label("Welcome to Paradis Hotel Management System");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        welcomeLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label infoLabel = new Label(
            "Manage your hotel operations efficiently with our comprehensive system. " +
            "Navigate using the menu on the left to access room management, bookings, and billing."
        );
        infoLabel.setWrapText(true);
        infoLabel.setFont(Font.font("Arial", 12));
        infoLabel.setStyle("-fx-text-fill: #555;");
        
        welcome.getChildren().addAll(welcomeLabel, infoLabel);
        return welcome;
    }
    
    /**
     * Create statistics grid
     */
    private GridPane createStatisticsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(0));

        totalRoomsValueLabel = createStatValueLabel();
        availableRoomsValueLabel = createStatValueLabel();
        totalBookingsLabel = createStatValueLabel();
        activeBookingsValueLabel = createStatValueLabel();
        
        // Total Rooms
        VBox totalRoomsBox = createStatBox("Total Rooms", totalRoomsValueLabel, "#3498db");
        GridPane.setConstraints(totalRoomsBox, 0, 0);
        
        // Available Rooms
        VBox availableRoomsBox = createStatBox("Available Rooms", availableRoomsValueLabel, "#2ecc71");
        GridPane.setConstraints(availableRoomsBox, 1, 0);
        
        // Total Bookings
        VBox totalBookingsBox = createStatBox("Total Bookings", totalBookingsLabel, "#e74c3c");
        GridPane.setConstraints(totalBookingsBox, 2, 0);
        
        // Active Bookings
        VBox activeBookingsBox = createStatBox("Active Bookings", activeBookingsValueLabel, "#f39c12");
        GridPane.setConstraints(activeBookingsBox, 3, 0);
        
        grid.getChildren().addAll(totalRoomsBox, availableRoomsBox, 
                                   totalBookingsBox, activeBookingsBox);
        
        return grid;
    }

    private Label createStatValueLabel() {
        Label valueLabel = new Label("...");
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        return valueLabel;
    }
    
    /**
     * Create a statistics box
     */
    private VBox createStatBox(String title, Label valueLabel, String color) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white; -fx-border-radius: 5; " +
                     "-fx-background-radius: 5; -fx-border-color: " + color + "; -fx-border-width: 2;");
        box.setMinHeight(120);
        box.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setStyle("-fx-text-fill: #555;");
        
        valueLabel.setStyle("-fx-text-fill: " + color + ";");
        
        box.getChildren().addAll(titleLabel, valueLabel);
        return box;
    }

    /**
     * Load dashboard statistics on a worker thread and update UI on JavaFX thread.
     */
    private void refreshDashboardStatsAsync() {
        if (totalRoomsValueLabel == null || availableRoomsValueLabel == null ||
            totalBookingsLabel == null || activeBookingsValueLabel == null) {
            return;
        }

        dashboardExecutor.submit(() -> {
            try {
                int totalRooms = RoomDAO.getRoomCount();
                int availableRooms = RoomDAO.getAvailableRoomCount();
                int totalBookings = BookingDAO.getBookingCount();
                int activeBookings = BookingDAO.getActiveBookings().size();

                Platform.runLater(() -> {
                    totalRoomsValueLabel.setText(String.valueOf(totalRooms));
                    availableRoomsValueLabel.setText(String.valueOf(availableRooms));
                    totalBookingsLabel.setText(String.valueOf(totalBookings));
                    activeBookingsValueLabel.setText(String.valueOf(activeBookings));
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    totalRoomsValueLabel.setText("N/A");
                    availableRoomsValueLabel.setText("N/A");
                    totalBookingsLabel.setText("N/A");
                    activeBookingsValueLabel.setText("N/A");
                });
            }
        });
    }
    
    /**
     * Create quick actions section
     */
    private VBox createQuickActionsSection() {
        VBox actions = new VBox(15);
        actions.setPadding(new Insets(20));
        actions.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");
        
        Label actionTitle = new Label("Quick Actions");
        actionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        actionTitle.setStyle("-fx-text-fill: #2c3e50;");
        
        HBox actionButtons = new HBox(15);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        
        Button addRoomBtn = new Button("➕ Add New Room");
        addRoomBtn.setPrefWidth(150);
        addRoomBtn.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        addRoomBtn.setOnAction(e -> mainLayout.setCenter(roomController.createRoomManagementUI()));
        
        Button newBookingBtn = new Button("📝 New Booking");
        newBookingBtn.setPrefWidth(150);
        newBookingBtn.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        newBookingBtn.setOnAction(e -> mainLayout.setCenter(bookingController.createBookingUI()));
        
        Button viewBillsBtn = new Button("💳 View Bills");
        viewBillsBtn.setPrefWidth(150);
        viewBillsBtn.setStyle("-fx-padding: 10; -fx-font-size: 12;");
        viewBillsBtn.setOnAction(e -> mainLayout.setCenter(billingController.createBillingUI()));
        
        actionButtons.getChildren().addAll(addRoomBtn, newBookingBtn, viewBillsBtn);
        
        actions.getChildren().addAll(actionTitle, actionButtons);
        return actions;
    }
    
    /**
     * Refresh dashboard statistics
     */
    public void refreshDashboard() {
        if (mainLayout != null) {
            mainLayout.setCenter(createDashboard());
        }
    }
}