package com.example.cobafx;

import com.example.cobafx.classes.laporan1;
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

public class laporan1Controller {

    @FXML
    TableView <laporan1> tableLaporan1;
    ObservableList <laporan1> list = FXCollections.observableArrayList();
    @FXML
    TableColumn <laporan1, String> nama;
    @FXML
    TableColumn <laporan1, Integer> count;
    @FXML
    TableColumn <laporan1, String> jenisKelas;

    @FXML
    Button Back;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize () {
        nama.setCellValueFactory(new PropertyValueFactory<laporan1, String>("nama"));
        count.setCellValueFactory(new PropertyValueFactory<laporan1, Integer>("count"));
        jenisKelas.setCellValueFactory(new PropertyValueFactory<laporan1, String>("jenisKelas"));
        setLaporan1();
    }

    @FXML
    protected void handleBackButtonAction () {
        loadLandingPage();
    }

    public void setLaporan1() {
        String query =  "SELECT A.NAMA, COUNT(*), K.JENISKELAS\n" +
                        "FROM ANAK A\n" +
                        "JOIN DATA_KEHADIRAN DK ON DK.ID_ANAK = A.ID_ANAK\n" +
                        "JOIN HISTORI_ANAK_KELAS HAK ON HAK.ID_ANAK = A.ID_ANAK\n" +
                        "JOIN KELAS_SUATU_TAHUN KST ON HAK.ID_KELAS_TAHUN = KST.ID_KELASTAHUN\n" +
                        "JOIN KELAS K ON KST.ID_KELAS = K.ID_KELAS\n" +
                        "GROUP BY A.NAMA, K.JENISKELAS";
        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet =statement.executeQuery(query);

            while (resultSet.next()) {
                String nama = resultSet.getString("nama");
                Integer count = resultSet.getInt("count");
                String jenisKelas = resultSet.getString("jeniskelas");

                laporan1 data = new laporan1(count,nama, jenisKelas);
                list.add(data);
            }
            tableLaporan1.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
}
