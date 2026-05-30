package com.paradis.hotel.main;

import com.paradis.hotel.controller.MainController;
import com.paradis.hotel.util.DatabaseUtil;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        DatabaseUtil.initializeDatabase();

        MainController mainController = new MainController();
        BorderPane root = mainController.createMainLayout();

        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setTitle("Paradis Hotel Management System");
        loadAppIcon(primaryStage);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadAppIcon(Stage primaryStage) {
        Image icon = loadLogoImage();
        if (icon != null) {
            primaryStage.getIcons().add(icon);
        }
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

    public static void main(String[] args) {
        launch(args);
    }
}
