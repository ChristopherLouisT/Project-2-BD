package com.example.cobafx;

import com.example.cobafx.classes.laporan4;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class laporan4Controller {
    @FXML
    TableView<laporan4> tableLaporan3;
    ObservableList<laporan4> list = FXCollections.observableArrayList();
    @FXML
    TableColumn<laporan4, String> gender;
    @FXML
    TableColumn <laporan4, Integer> count;
    @FXML
    TableColumn <laporan4, String> jenisKelas;

    @FXML
    Button Back;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize () {
        gender.setCellValueFactory(new PropertyValueFactory<laporan4, String>("gender"));
        count.setCellValueFactory(new PropertyValueFactory<laporan4, Integer>("count"));
        jenisKelas.setCellValueFactory(new PropertyValueFactory<laporan4, String>("jenisKelas"));
        setLaporan4();
    }

    @FXML
    protected void handleBackButtonAction () {
        loadLandingPage();
    }

    public void setLaporan4() {
        String query =  "select A.GENDER, COUNT(A.GENDER), K.JENISKELAS\n" +
                "FROM DATA_KEHADIRAN DK\n" +
                "JOIN ANAK A ON DK.ID_ANAK = A.ID_ANAK\n" +
                "JOIN HISTORI_ANAK_KELAS HAK ON HAK.ID_ANAK = A.ID_ANAK\n" +
                "JOIN KELAS_SUATU_TAHUN KST ON HAK.ID_KELAS_TAHUN = KST.ID_KELASTAHUN\n" +
                "JOIN KELAS K ON KST.ID_KELAS = K.ID_KELAS\n" +
                "GROUP BY A.GENDER, K.JENISKELAS\n" +
                "ORDER BY K.JENISKELAS";
        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet =statement.executeQuery(query);

            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                Integer count = resultSet.getInt("count");
                String jenisKelas = resultSet.getString("jeniskelas");

                laporan4 data = new laporan4(count,gender, jenisKelas);
                list.add(data);
            }
            tableLaporan3.setItems(list);

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
