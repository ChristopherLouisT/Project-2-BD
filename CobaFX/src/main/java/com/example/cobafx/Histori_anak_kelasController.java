package com.example.cobafx;

import com.example.cobafx.classes.histori_anak_kelas;
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

public class Histori_anak_kelasController {
    @FXML
    TableView<histori_anak_kelas> tableHistoriAnakKelas;
    ObservableList<histori_anak_kelas> listHistoriAnakKelas = FXCollections.observableArrayList();

    @FXML
    TableColumn<histori_anak_kelas, Integer> idHistori;
    @FXML
    TableColumn <histori_anak_kelas, Integer> id_Anak;
    @FXML
    TableColumn <histori_anak_kelas, Integer> id_KelasTahun;

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
    TextField searchField, addIdAnakField, addIDKelasTahunField;
    @FXML
    TextField updateIDAnakField;
    @FXML
    TextField updateIDKelasTahunField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize () {
        idHistori.setCellValueFactory(new PropertyValueFactory<histori_anak_kelas, Integer>("id_histori"));
        id_Anak.setCellValueFactory(new PropertyValueFactory<histori_anak_kelas, Integer>("id_anak"));
        id_KelasTahun.setCellValueFactory(new PropertyValueFactory<histori_anak_kelas, Integer>("id_kelasTahun"));
        setTableHistoriAnakKelas();
    }

    @FXML
    public void handleAddHistoriAnakKelas() {
        if (addIDKelasTahunField.getText().isEmpty() || addIdAnakField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please enter a ID_anak AND ID_KelasTahun");
            return;
        }
        Integer idAnak;
        Integer idKelasTahun;

        try {
            idAnak = Integer.parseInt(addIdAnakField.getText());
            idKelasTahun = Integer.parseInt(addIDKelasTahunField.getText());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please enter a correct number");
            return;
        }
        if (idKelasTahun < 0 || idAnak < 0) {
            showAlert(Alert.AlertType.ERROR, "Minus Error!", "Please enter a positive number");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO histori_anak_kelas VALUES (?, ?, ?)";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, histori_anak_kelas.lastID);
            pstmt.setInt(2, idAnak);
            pstmt.setInt(3, idKelasTahun);
            pstmt.executeUpdate();
            listHistoriAnakKelas.add(new histori_anak_kelas(histori_anak_kelas.lastID, idAnak, idKelasTahun));
            clearFields();
            tableHistoriAnakKelas.setItems(listHistoriAnakKelas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteHistoriAnakKelas() {
        histori_anak_kelas selectedData = tableHistoriAnakKelas.getSelectionModel().getSelectedItem();

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please Select A Data");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection();){
            String query = "DELETE FROM histori_anak_kelas WHERE id_histori_anak = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, selectedData.getId_histori());
            pstmt.executeUpdate();

            listHistoriAnakKelas.remove(selectedData);
            tableHistoriAnakKelas.setItems(listHistoriAnakKelas);
            if (selectedData.getId_histori() == histori_anak_kelas.lastID - 1) histori_anak_kelas.lastID--;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleUpdateHistoriAnakKelas() {
        if (updateIDAnakField.getText().isEmpty() && updateIDKelasTahunField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please enter a ID_anak or ID_KelasTahun");
            return;
        }
        histori_anak_kelas selectedData = tableHistoriAnakKelas.getSelectionModel().getSelectedItem();
        Integer idAnak = null;
        Integer idKelasTahun = null;

        try {
            idAnak = Integer.parseInt(updateIDAnakField.getText());
        } catch (Exception e) {
            try {
                idKelasTahun = Integer.parseInt(updateIDKelasTahunField.getText());
            } catch (Exception er) {
                showAlert(Alert.AlertType.ERROR, "Invalid Value", "Please Input A Correct Value");
            }
        }
        if ((idKelasTahun != null && idKelasTahun < 0) || (idAnak != null && idAnak < 0)) {
            showAlert(Alert.AlertType.ERROR, "Minus Error!", "Please enter a positive number");
            return;
        }

        try {
            Connection db = DatabaseConnection.getConnection();
            String query;
            PreparedStatement pstmt = null;
            if (idAnak != null) {
                query = "UPDATE histori_anak_kelas SET id_anak = ? WHERE id_histori_anak = ?";
                pstmt = db.prepareStatement(query);
                pstmt.setInt(1,idAnak);
                pstmt.setInt(2,selectedData.getId_histori());
                pstmt.executeUpdate();
                selectedData.setId_anak(idAnak);
            }
            if (idKelasTahun != null) {
                query = "UPDATE histori_anak_kelas SET id_kelas_tahun = ? WHERE id_histori_anak = ?";
                pstmt = db.prepareStatement(query);
                pstmt.setInt(1,idKelasTahun);
                pstmt.setInt(2,selectedData.getId_histori());
                pstmt.executeUpdate();
                selectedData.setId_kelasTahun(idKelasTahun);
            }
//            pstmt.setInt(2,selectedData.getId());
//            pstmt.executeUpdate();

            setTableHistoriAnakKelas();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Double Data", "Double Devotion In The Same Day");
            return;
        }
    }

    @FXML
    public void handleSearchHistoriAnakKelas () {
        String search = searchField.getText();
        String query;
        Integer searchIDAnak; //Fokus searchnya ke anak doang, ke kelas_tahun NO
        if (search.isEmpty()) {
            setTableHistoriAnakKelas();
            return;
        }
        try {
            searchIDAnak = Integer.valueOf(search);
            query = "SELECT * FROM histori_anak_kelas WHERE id_anak = ";
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Please Search Either Devotion Theme or Devotion Date");
            return;
        }

        try {
            listHistoriAnakKelas.clear();
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery(query + searchIDAnak);


            while (resultSet.next()) {
                Integer idHistori = resultSet.getInt("id_histori_anak");
                Integer idAnak = resultSet.getInt("id_anak");
                Integer idKelasTahun = resultSet.getInt("id_kelas_tahun");

                histori_anak_kelas dataHistori = new histori_anak_kelas(idHistori, idAnak, idKelasTahun);
                listHistoriAnakKelas.add(dataHistori);
            }
            tableHistoriAnakKelas.setItems(listHistoriAnakKelas);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleBackButtonAction () {
        loadLandingPage();
    }

    public void setTableHistoriAnakKelas() {
        listHistoriAnakKelas.clear();
        String query = "SELECT * FROM histori_anak_kelas";
        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet =statement.executeQuery(query);

            while (resultSet.next()) {
                Integer id_histori = resultSet.getInt("id_histori_anak");
                Integer id_anak = resultSet.getInt("id_anak");
                Integer id_kelasTahun = resultSet.getInt("id_kelas_tahun");

                histori_anak_kelas dataHistori = new histori_anak_kelas(id_histori, id_anak, id_kelasTahun);
                listHistoriAnakKelas.add(dataHistori);
            }
            tableHistoriAnakKelas.setItems(listHistoriAnakKelas);
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
        addIdAnakField.clear();
    }
}
