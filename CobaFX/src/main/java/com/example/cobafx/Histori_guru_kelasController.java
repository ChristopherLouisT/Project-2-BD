package com.example.cobafx;
import com.example.cobafx.classes.histori_guru_kelas;
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

public class Histori_guru_kelasController {
    @FXML
    TableView<histori_guru_kelas> tableHistoriGuruKelas;
    ObservableList<histori_guru_kelas> listHistoriGuruKelas = FXCollections.observableArrayList();

    @FXML
    TableColumn<histori_guru_kelas, Integer> id_histori;
    @FXML
    TableColumn <histori_guru_kelas, Integer> id_guru;
    @FXML
    TableColumn <histori_guru_kelas, Integer> id_kelastahun;

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
    TextField searchField, addIdGuruField, addIDKelasTahunField;
    @FXML
    TextField updateIDGuruField;
    @FXML
    TextField updateIDKelasTahunField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize () {
        id_histori.setCellValueFactory(new PropertyValueFactory<histori_guru_kelas, Integer>("id_histori"));
        id_guru.setCellValueFactory(new PropertyValueFactory<histori_guru_kelas, Integer>("id_guru"));
        id_kelastahun.setCellValueFactory(new PropertyValueFactory<histori_guru_kelas, Integer>("id_kelastahun"));
        setTableHistoriGuruKelas();
    }

    @FXML
    public void handleAddHistoriGuruKelas() {
        if (addIDKelasTahunField.getText().isEmpty() || addIdGuruField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "id_guru and id_kelastahun cannot be null.");
            return;
        }
        Integer idGuru;
        Integer idKelasTahun;

        try {
            idGuru = Integer.parseInt(addIdGuruField.getText());
            idKelasTahun = Integer.parseInt(addIDKelasTahunField.getText());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please enter an integer.");
            return;
        }
        if (idKelasTahun < 0 || idGuru < 0) {
            showAlert(Alert.AlertType.ERROR, "Minus Error!", "id cannot be a negative number.");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO histori_guru_kelas VALUES (?, ?, ?)";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, histori_guru_kelas.lastID);
            pstmt.setInt(2, idGuru);
            pstmt.setInt(3, idKelasTahun);
            pstmt.executeUpdate();
            listHistoriGuruKelas.add(new histori_guru_kelas(histori_guru_kelas.lastID, idGuru, idKelasTahun));
            clearFields();
            tableHistoriGuruKelas.setItems(listHistoriGuruKelas);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Relation Error!", "ID not match with the database");
            return;
        }
    }

    @FXML
    public void handleDeleteHistoriGuruKelas() {
        histori_guru_kelas selectedData = tableHistoriGuruKelas.getSelectionModel().getSelectedItem();

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Select a row of data.");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection();){
            String query = "DELETE FROM histori_guru_kelas WHERE id_historiguru = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, selectedData.getId_histori());
            pstmt.executeUpdate();

            listHistoriGuruKelas.remove(selectedData);
            tableHistoriGuruKelas.setItems(listHistoriGuruKelas);
            if (selectedData.getId_histori() == histori_guru_kelas.lastID - 1) histori_guru_kelas.lastID--;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleUpdateHistoriGuruKelas() {
        if (updateIDGuruField.getText().isEmpty() && updateIDKelasTahunField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "id_guru and id_kelastahun cannot be null.");
            return;
        }
        histori_guru_kelas selectedData = tableHistoriGuruKelas.getSelectionModel().getSelectedItem();
        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please Select A Data");
            return;
        }
        Integer idGuru = null;
        Integer idKelasTahun = null;

        try {
            idGuru = Integer.parseInt(updateIDGuruField.getText());
            idKelasTahun = Integer.parseInt(updateIDKelasTahunField.getText());
        } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Value", "Please input the correct value.");
        }
        if ((idKelasTahun != null && idKelasTahun < 0) || (idGuru != null && idGuru < 0)) {
            showAlert(Alert.AlertType.ERROR, "Minus Error!", "id cannot be a negative number.");
            return;
        }

        try {
            Connection db = DatabaseConnection.getConnection();
            String query;
            PreparedStatement pstmt = null;
            if (idGuru != null) {
                query = "UPDATE histori_guru_kelas SET id_guru = ? WHERE id_historiguru = ?";
                pstmt = db.prepareStatement(query);
                pstmt.setInt(1,idGuru);
                pstmt.setInt(2,selectedData.getId_histori());
                pstmt.executeUpdate();
                selectedData.setId_guru(idGuru);
            }
            if (idKelasTahun != null) {
                query = "UPDATE histori_guru_kelas SET id_kelastahun = ? WHERE id_historiguru = ?";
                pstmt = db.prepareStatement(query);
                pstmt.setInt(1,idKelasTahun);
                pstmt.setInt(2,selectedData.getId_histori());
                pstmt.executeUpdate();
                selectedData.setId_kelastahun(idKelasTahun);
            }
//            pstmt.setInt(2,selectedData.getId());
//            pstmt.executeUpdate();
            histori_guru_kelas.lastID = 1;
            setTableHistoriGuruKelas();
        } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR, "Double Data", "id_historiguru must be unique");
//            return;
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSearchHistoriGuruKelas () {
        String search = searchField.getText();
        String query;
        Integer searchIDGuru; //Fokus searchnya ke id_guru doang, ke kelas_tahun tidak
        if (search.isEmpty()) {
            setTableHistoriGuruKelas();
            return;
        }
        try {
            searchIDGuru = Integer.valueOf(search);
            query = "SELECT * FROM histori_guru_kelas WHERE id_guru = ";
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Search using the correct id_guru.");
            return;
        }

        try {
            listHistoriGuruKelas.clear();
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(query + searchIDGuru);


            while (resultSet.next()) {
                Integer idHistori = resultSet.getInt("id_historiguru");
                Integer idGuru = resultSet.getInt("id_guru");
                Integer idKelasTahun = resultSet.getInt("id_kelastahun");

                histori_guru_kelas dataHistori = new histori_guru_kelas(idHistori, idGuru, idKelasTahun);
                listHistoriGuruKelas.add(dataHistori);
            }
            tableHistoriGuruKelas.setItems(listHistoriGuruKelas);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        histori_guru_kelas.lastID = 1;
    }

    @FXML
    protected void handleBackButtonAction () {
        loadLandingPage();
    }

    public void setTableHistoriGuruKelas() {
        listHistoriGuruKelas.clear();
        String query = "SELECT * FROM histori_guru_kelas";
        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet =statement.executeQuery(query);

            while (resultSet.next()) {
                Integer id_hhistori = resultSet.getInt("id_historiguru");
                Integer id_gguru = resultSet.getInt("id_guru");
                Integer id_kkelasTahun = resultSet.getInt("id_kelastahun");

                histori_guru_kelas dataHistori = new histori_guru_kelas(id_hhistori, id_gguru, id_kkelasTahun);
                listHistoriGuruKelas.add(dataHistori);
            }
            tableHistoriGuruKelas.setItems(listHistoriGuruKelas);
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
        addIdGuruField.clear();
        addIDKelasTahunField.clear();
        updateIDGuruField.clear();
        updateIDKelasTahunField.clear();
    }

}
