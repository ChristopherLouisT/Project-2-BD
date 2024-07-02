package com.example.cobafx;

import com.example.cobafx.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) throws SQLException {
        loadLandingPage();
    }

    private boolean validateLogin(String username, String password) throws SQLException {

        Connection db = DatabaseConnection.getConnection();
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = db.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet result = pstmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void loadLandingPage() {
        // Load the landing page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("landing.fxml"));
            Parent root = loader.load();

            LandingController controller = loader.getController();
            controller.setStage(stage);

            stage.setScene(new Scene(root, 600,400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleSignUpButtonAction(ActionEvent event) {
        loadSignUpPage();
    }

    private void loadSignUpPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();

            SignUpController controller = loader.getController();
            controller.setStage(stage);
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}