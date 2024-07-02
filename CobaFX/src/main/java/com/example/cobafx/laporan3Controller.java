package com.example.cobafx;

import com.example.cobafx.classes.laporan3;
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

public class laporan3Controller {
    @FXML
    TableView<laporan3> tableLaporan3;
    ObservableList<laporan3> list = FXCollections.observableArrayList();
    @FXML
    TableColumn<laporan3, String> nama;
    @FXML
    TableColumn <laporan3, Integer> count;
    @FXML
    TableColumn <laporan3, String> jenisKelas;

    @FXML
    Button Back;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize () {
        nama.setCellValueFactory(new PropertyValueFactory<laporan3, String>("nama"));
        count.setCellValueFactory(new PropertyValueFactory<laporan3, Integer>("count"));
        jenisKelas.setCellValueFactory(new PropertyValueFactory<laporan3, String>("jenisKelas"));
        setLaporan3();
    }

    @FXML
    protected void handleBackButtonAction () {
        loadLandingPage();
    }

    public void setLaporan3() {
        String query =  "select g.nama, RTRIM(ks.kelas_suatu_tahun, '/1234567890') as \"Jenis Kelas\", count(*) as \"Jumlah Tahun\"\n" +
                "from guru g\n" +
                "join histori_guru_kelas hgk on g.id_guru = hgk.id_guru\n" +
                "join kelas_suatu_tahun ks on hgk.id_kelastahun = ks.id_kelastahun\n" +
                "group by g.nama, RTRIM(ks.kelas_suatu_tahun, '/1234567890')\n" +
                "order by g.nama";
        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet =statement.executeQuery(query);

            while (resultSet.next()) {
                String nama = resultSet.getString("nama");
                String jenisKelas = resultSet.getString("Jenis Kelas");
                Integer count = resultSet.getInt("Jumlah Tahun");

                laporan3 data = new laporan3(count,nama, jenisKelas);
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
