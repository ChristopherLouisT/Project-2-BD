package com.example.cobafx;

import com.example.cobafx.classes.Kelas;
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


public class KelasController {
    @FXML
    TableView <Kelas> tableKelas;
    @FXML
    TableColumn <Kelas, Integer> id_kelas;

    @FXML
    TableColumn <Kelas, String> jenis_kelas;

    @FXML
    Button search;
    @FXML
    Button back;
    @FXML
    Button add;

    @FXML
    TextField searchField;

    @FXML
    TextField add_jenisKelas;
    @FXML
    TextField updateColumnNameField;
    @FXML
    TextField updateNewValueField;

    @FXML
    MenuBar menubar;
    @FXML
    Menu Add;
    @FXML
    Menu Update;

    private ObservableList <Kelas> listKelas = FXCollections.observableArrayList();
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void handleBackButtonAction () {
        loadLandingPage();
    }

    @FXML
    public void initialize () {
        //Binding
        id_kelas.setCellValueFactory(new PropertyValueFactory<Kelas, Integer>("id_kelas"));
        jenis_kelas.setCellValueFactory(new PropertyValueFactory<Kelas, String>("jeniskelas"));
        setTableKelas();
    }

    @FXML
    public void handleAddKelas() {
        if (add_jenisKelas.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "jenis_kelas cannot be null.");
            return;
        }
//        Integer idAnak = Integer.parseInt(add_idKelas.getText());
//        String absence = addAbsenceField.getText();
        String jeniskls = add_jenisKelas.getText();


        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO kelas (id_kelas, jeniskelas) VALUES (?, ?)";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, Kelas.lastID);
            pstmt.setString(2, jeniskls);
            pstmt.executeUpdate();
            listKelas.add(new Kelas(Kelas.lastID, jeniskls));
            clearFields();
            tableKelas.setItems(listKelas);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "No data matches the given argument.");
            //return;
        }
    }

    @FXML
    public void handleUpdateKelas() {
        Kelas selectedData = tableKelas.getSelectionModel().getSelectedItem();
        //Integer newID = null;
        String newJenis = null;

//        try {
//            newID = Integer.parseInt(updateNewValueField.getText());
//        } catch (Exception e) {
        try {
            newJenis = updateNewValueField.getText();
        } catch (Exception er) {
            showAlert(Alert.AlertType.ERROR, "Invalid Value", "Please Input A Correct Value");
            return;
        }
//        }

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please Select A Data");
            return;
        }
//        if (newID != null && newID < 1) {
//            showAlert(Alert.AlertType.ERROR, "Out Range Value", "Please Use The Correct ID");
//            return;
//        }


        try (Connection db = DatabaseConnection.getConnection()){
            String query;
            PreparedStatement pstmt = null;
//            if (newID != null) {
//                query = "UPDATE DATA_KEHADIRAN SET id_anak = ? WHERE id_kehadiran = ?";
//                pstmt = db.prepareStatement(query);
//                pstmt.setInt(1,newID);
//                selectedData.setIdAnak(newID);
//            }
            if (newJenis != null) {
                query = "UPDATE KELAS SET jeniskelas = ? WHERE id_kelas = ?";
                pstmt = db.prepareStatement(query);
                pstmt.setString(1,newJenis);
                selectedData.setJeniskelas(newJenis);
            }
            pstmt.setInt(2,selectedData.getId_kelas());
            pstmt.executeUpdate();
            Kelas.lastID = 1;
            setTableKelas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteKelas() {
        Kelas selectedData = tableKelas.getSelectionModel().getSelectedItem();

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please Select A Data");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection();){
            String query = "DELETE FROM kelas WHERE id_kelas = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, selectedData.getId_kelas());
            pstmt.executeUpdate();

            listKelas.remove(selectedData);
            tableKelas.setItems(listKelas);
            if (selectedData.getId_kelas() == Kelas.lastID - 1) Kelas.lastID--;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleSearchKelas () {
        String search = searchField.getText();
        String query = null;
        //Integer searchIDAnak = null;
        String searchJenis = search;
        if (search.isEmpty()) {
            setTableKelas();
            return;
        }
//        try {
//            searchIDAnak = Integer.valueOf(search);
//            if (searchIDAnak < 1) {
//                showAlert(Alert.AlertType.ERROR, "Out Range Value", "Please Use The Correct ID");
//                return;
//            }
//            query = "SELECT * FROM data_kehadiran WHERE id_anak = ";
//        } catch (Exception e) {
        try {
            query = "SELECT * FROM kelas WHERE jeniskelas = ";
        } catch (Exception er) {
            showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Please enter the correct value.");
            return;
        }
        //}

        try {
            listKelas.clear();
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(query + "'" + searchJenis + "'");


            while (resultSet.next()) {
                Integer idKelas = resultSet.getInt("id_kelas");
                String jenisKelas = resultSet.getString("jeniskelas");
                Kelas kls = new Kelas(idKelas, jenisKelas);
                listKelas.add(kls);
            }
            tableKelas.setItems(listKelas);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Kelas.lastID = 1;
    }

    public void setTableKelas() {
        listKelas.clear();
        String query = "SELECT * FROM kelas";
        try {
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Integer idKelas = resultSet.getInt("id_kelas");
                String jenisKelas = resultSet.getString("jeniskelas");

                Kelas kls = new Kelas(idKelas, jenisKelas);
                listKelas.add(kls);
            }
            tableKelas.setItems(listKelas);
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        //add_IdKelas.clear();
        add_jenisKelas.clear();
    }

}