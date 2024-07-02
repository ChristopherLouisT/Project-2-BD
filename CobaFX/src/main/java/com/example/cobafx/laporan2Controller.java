package com.example.cobafx;

import com.example.cobafx.classes.laporan2;
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

public class laporan2Controller {

    @FXML
    TableView <laporan2> tableLaporan2;
    ObservableList <laporan2> list = FXCollections.observableArrayList();
    @FXML
    TableColumn <laporan2, Integer> count;
    @FXML
    TableColumn <laporan2, String> jenisKelas;

    @FXML
    Button Back;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize () {
        count.setCellValueFactory(new PropertyValueFactory<laporan2, Integer>("count"));
        jenisKelas.setCellValueFactory(new PropertyValueFactory<laporan2, String>("jenisKelas"));
        setLaporan2();
    }

    @FXML
    protected void handleBackButtonAction () {
        loadLandingPage();
    }

    public void setLaporan2() {
        String query =  "SELECT k.JENISkelas, COUNT(hak.id_ANAK) AS jumlah_anak\n" +
                "FROM kelas k\n" +
                "JOIN KELAS_SUATU_TAHUN KST ON K.ID_KELAS = KST.ID_KELAS\n" +
                "JOIN histori_anak_kelas hak ON HAK.ID_KELAS_TAHUN = KST.id_kelasTAHUN\n" +
                "GROUP BY k.JENISkelas\n" +
                "ORDER BY jumlah_anak DESC";

        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet =statement.executeQuery(query);

            while (resultSet.next()) {
                String jenisKelas = resultSet.getString("jeniskelas");
                Integer count = resultSet.getInt("jumlah_anak");

                laporan2 data = new laporan2(jenisKelas,count);
                list.add(data);
            }
            tableLaporan2.setItems(list);
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
