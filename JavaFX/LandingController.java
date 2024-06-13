package com.example.cobafx;

import com.example.cobafx.classes.Anak;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LandingController {

   @FXML
   Button TahunAjaran;

   @FXML
   Button KelasTahun;

   @FXML
   Button Kelas;

   @FXML
   Button Guru;

   @FXML
   Button Anak;

   @FXML
   Button Kebaktian;

   @FXML
   Button HistoryGuru;

   @FXML
   Button HistoryHadir;

   @FXML
    Button DataKehadiran;

   @FXML
   Button LogOut;



    private Stage stage;

    private ObservableList<Anak> listAnak = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;

    }

//    @FXML
//    public void initialize() {
//        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
//        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
//        userTable.setItems(userList);
//        loadUserData();
//    }

//    private void loadUserData() {
//        userList.clear();
//        try (Connection db = DatabaseConnection.getConnection()) {
//            String query = "SELECT * FROM users";
//            ResultSet rs = db.createStatement().executeQuery(query);
//
//            while (rs.next()) {
//                System.out.println(rs.getString("username"));
//                userList.add(new User(rs.getString("username"), rs.getString("password")));
//            }
//
//            for (User user: userList) {
//                System.out.println(user);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    @FXML
//    private void handleAddUser() {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert(AlertType.ERROR, "Form Error!", "Please enter username and password");
//            return;
//        }
//
//        try (Connection db = DatabaseConnection.getConnection()) {
//            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
//            PreparedStatement pstmt = db.prepareStatement(query);
//            pstmt.setString(1, username);
//            pstmt.setString(2, password);
//            pstmt.executeUpdate();
//            userList.add(new User(username, password));
//            clearFields();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    @FXML
//    private void handleUpdateUser() {
//        User selectedUser = userTable.getSelectionModel().getSelectedItem();
//        if (selectedUser == null) {
//            showAlert(AlertType.ERROR, "Selection Error", "Please select a user to update");
//            return;
//        }
//
//        String newUsername = usernameField.getText();
//        String newPassword = passwordField.getText();
//
//        if (newUsername.isEmpty() || newPassword.isEmpty()) {
//            showAlert(AlertType.ERROR, "Form Error", "Please enter username and password");
//            return;
//        }
//
//        try (Connection db = DatabaseConnection.getConnection()) {
//            String query = "UPDATE users SET username = ?, password = ? WHERE username = ?";
//            PreparedStatement pstmt = db.prepareStatement(query);
//            pstmt.setString(1, newUsername);
//            pstmt.setString(2, newPassword);
//            pstmt.setString(3, selectedUser.getUsername());
//            pstmt.executeUpdate();
//
//            selectedUser.setUsername(newUsername);
//            selectedUser.setPassword(newPassword);
////            userTable.refresh();
//            loadUserData();
//            clearFields();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    @FXML
//    private void handleDeleteUser() {
//        User selectedUser = userTable.getSelectionModel().getSelectedItem();
//        if (selectedUser == null) {
//            showAlert(AlertType.ERROR, "Selection Error", "Please select a user to delete");
//            return;
//        }
//
//        try (Connection db = DatabaseConnection.getConnection()) {
//            String query = "DELETE FROM users WHERE username = ?";
//            PreparedStatement pstmt = db.prepareStatement(query);
//            pstmt.setString(1, selectedUser.getUsername());
//            pstmt.executeUpdate();
//
////            userList.remove(selectedUser);
//            loadUserData();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    private void clearFields() {
//        usernameField.clear();
//        passwordField.clear();
//    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}