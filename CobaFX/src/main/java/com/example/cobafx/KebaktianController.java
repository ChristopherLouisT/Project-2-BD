package com.example.cobafx;


import com.example.cobafx.classes.Kebaktian;
import com.example.cobafx.classes.KelasSuatuTahun;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class KebaktianController {

    @FXML
    TableView <Kebaktian> tableKebaktian;
    ObservableList <Kebaktian> listKebaktian = FXCollections.observableArrayList();

    @FXML
    TableColumn <Kebaktian, Integer> idKebaktian;
    @FXML
    TableColumn <Kebaktian, String> temaKebaktian;
    @FXML
    TableColumn <Kebaktian, Date> DevotionDate;

    @FXML
    Button Add;
    @FXML
    Button Update;
    @FXML
    Button Delete;
    @FXML
    Button Search;
    @FXML
    Button Back;

    @FXML
    TextField searchField, addDevotionDateField;
    @FXML
    TextField updateDevotionDateField;

    @FXML
    ChoiceBox addThemes;
    @FXML
    ChoiceBox updateThemes;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize () {
        idKebaktian.setCellValueFactory(new PropertyValueFactory<Kebaktian, Integer>("id"));
        temaKebaktian.setCellValueFactory(new PropertyValueFactory<Kebaktian, String>("temaKebaktian"));
        DevotionDate.setCellValueFactory(new PropertyValueFactory<Kebaktian, Date>("date"));
        setTableKebaktian();
        ObservableList <String> tema = FXCollections.observableArrayList();
        tema.add("Tahun Baru");
        tema.add("Umum");
        tema.add("Jumat Agung");
        tema.add("Paskah");
        tema.add("Natal");
        addThemes.setItems(tema); updateThemes.setItems(tema);
    }

    @FXML
    public void handleAddKebaktian() {
        String selectedTheme = addThemes.getValue().toString();
        if (selectedTheme.isEmpty() || addDevotionDateField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please enter a theme and Devotion Date");
            return;
        }
        Date date = Date.valueOf(addDevotionDateField.getText());


        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO kebaktian VALUES (?, ?, ?)";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, Kebaktian.lastID);
            pstmt.setString(2, selectedTheme);
            pstmt.setDate(3, date);
            pstmt.executeUpdate();
            listKebaktian.add(new Kebaktian(Kebaktian.lastID, selectedTheme, date));
            clearFields();
            tableKebaktian.setItems(listKebaktian);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteKebaktian() {
        Kebaktian selectedData = tableKebaktian.getSelectionModel().getSelectedItem();

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please Select A Data");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection();){
            String query = "DELETE FROM KEBAKTIAN WHERE id_kebaktian = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, selectedData.getId());
            pstmt.executeUpdate();

            listKebaktian.remove(selectedData);
            tableKebaktian.setItems(listKebaktian);
            if (selectedData.getId() == Kebaktian.lastID - 1) Kebaktian.lastID--;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleUpdateKebaktian() {
        Kebaktian selectedData = tableKebaktian.getSelectionModel().getSelectedItem();
        String selectedTheme = null;
        Date newDate = null;

        if (updateThemes.getValue() != null) selectedTheme = updateThemes.getValue().toString();
        if (!updateDevotionDateField.getText().isEmpty()) newDate = Date.valueOf(updateDevotionDateField.getText());

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please Select A Data");
            return;
        }
        if (selectedTheme == null && newDate == null) {
            showAlert(Alert.AlertType.ERROR, "Empty New Data", "Please Fill The Field");
            return;
        }
        if (newDate != null)
            if (Integer.valueOf(String.valueOf(newDate).substring(0, 3+1)) < 2019 || Integer.valueOf(String.valueOf(newDate).substring(0, 3+1)) > 2024) {
            showAlert(Alert.AlertType.ERROR, "Out Range Value", "Year Below 2019 or Above 2024");
            return;
        }

        try {
            Connection db = DatabaseConnection.getConnection();
            String query;
            PreparedStatement pstmt = null;
            if (selectedTheme != null) {
                query = "UPDATE KEBAKTIAN SET tema_kebaktian = ? WHERE id_kebaktian = ?";
                pstmt = db.prepareStatement(query);
                pstmt.setString(1,selectedTheme);
                pstmt.setInt(2,selectedData.getId());
                pstmt.executeUpdate();
                selectedData.setTemaKebaktian(selectedTheme);
            }
            if (newDate != null) {
                query = "UPDATE KEBAKTIAN SET tanggal_kebaktian = ? WHERE id_kebaktian = ?";
                pstmt = db.prepareStatement(query);
                pstmt.setDate(1,newDate);
                pstmt.setInt(2,selectedData.getId());
                pstmt.executeUpdate();
                selectedData.setDate(newDate);
            }
//            pstmt.setInt(2,selectedData.getId());
//            pstmt.executeUpdate();

            KelasSuatuTahun.setSerial(1);
            setTableKebaktian();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Double Data", "Double Devotion In The Same Day");
            return;
        }
    }

    @FXML
    public void handleSearchKebaktian () {
        String search = searchField.getText();
        String query = null;
        String searchTema = null;
        Date searchDate = null;
        if (search.isEmpty()) {
            setTableKebaktian();
            return;
        }
        try {
            searchTema = search.toLowerCase();
            searchTema = Character.toUpperCase(searchTema.charAt(0)) + searchTema.substring(1, searchTema.length());
            query = "SELECT * FROM KEBAKTIAN WHERE tema_kebaktian = ";
        } catch (Exception e) {
            try {
                searchDate = Date.valueOf(search);
                if (Integer.valueOf(String.valueOf(searchDate).substring(0, 3 + 1)) < 2019 && Integer.valueOf(String.valueOf(searchDate).substring(0, 3 + 1)) > 2024) {
                    showAlert(Alert.AlertType.ERROR, "Out Range Value", "Year Below 2019 Or Above 2024");
                    return;
                }
                query = "SELECT * FROM KEBAKTIAN WHERE tanggal_kebaktian = ";
            } catch (Exception er) {
                showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Please Search Either Devotion Theme or Devotion Date");
                return;
            }
        }

        try {
            listKebaktian.clear();
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet;
            if (searchTema != null) resultSet = statement.executeQuery(query + "'" +searchTema + "'");
            else resultSet = statement.executeQuery(query + "'" + searchDate + "'");


            while (resultSet.next()) {
                Integer idKebaktian = resultSet.getInt("id_kebaktian");
                String temaKebaktian = resultSet.getString("tema_kebaktian");
                Date date = resultSet.getDate("tanggal_kebaktian");

                Kebaktian kebaktian = new Kebaktian(idKebaktian, temaKebaktian, date);
                listKebaktian.add(kebaktian);
            }
            tableKebaktian.setItems(listKebaktian);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Kebaktian.lastID = 1;
    }

    @FXML
    protected void handleBackButtonAction () {
        loadLandingPage();
    }

    public void setTableKebaktian () {
        listKebaktian.clear();
        String query = "SELECT * FROM kebaktian";
        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet =statement.executeQuery(query);

            while (resultSet.next()) {
                Integer idKebaktian = resultSet.getInt("id_kebaktian");
                String tema = resultSet.getString("tema_kebaktian");
                Date date = resultSet.getDate("tanggal_kebaktian");

                Kebaktian dataKebaktian = new Kebaktian(idKebaktian,tema, date);
                listKebaktian.add(dataKebaktian);
            }
            tableKebaktian.setItems(listKebaktian);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    private void clearFields () {
        searchField.clear();
        addDevotionDateField.clear();
        updateDevotionDateField.clear();
    }
}
