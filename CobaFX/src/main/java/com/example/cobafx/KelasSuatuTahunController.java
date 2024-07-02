package com.example.cobafx;

import com.example.cobafx.classes.KelasSuatuTahun;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class KelasSuatuTahunController{
    ObservableList<KelasSuatuTahun> listKelasTahun = FXCollections.observableArrayList();

    //Add Textfields
    @FXML
    private TextField addIdKelasTahunField,addIdTahunAjaranField,addIdKelasField,addKelasSuatuTahunField;
    @FXML
    private TextField addAlamatField,addNamaWaliField,addNoTelpWaliField,SearchField,SearchColumnNameField;
    //Update Menu
    @FXML
    private TextField updateColumnNameField,updateReplacedNamaField,updateNamaField;

    @FXML
    private Button addKelasTahunButton,SearchButton,UpdateButton,BackToMenuButton;
    @FXML
    TableView<KelasSuatuTahun> tableKelasTahun = new TableView<>();
    @FXML
    TableColumn <KelasSuatuTahun,Integer> idKelasTahun = new TableColumn<>();
    @FXML
    TableColumn <KelasSuatuTahun,Integer> idTahunAjaran = new TableColumn<>();
    @FXML
    TableColumn <KelasSuatuTahun,Integer> idKelas = new TableColumn<>();
    @FXML
    TableColumn <KelasSuatuTahun,String> kelasSuatuTahun = new TableColumn<>();
    private Stage stage;

    public void initialize () {
        //Binding
        idKelasTahun.setCellValueFactory(new PropertyValueFactory<KelasSuatuTahun, Integer>("id_kelastahun"));
        idTahunAjaran.setCellValueFactory(new PropertyValueFactory<KelasSuatuTahun, Integer>("id_tahunajaran"));
        idKelas.setCellValueFactory(new PropertyValueFactory<KelasSuatuTahun, Integer>("id_kelas"));
        kelasSuatuTahun.setCellValueFactory(new PropertyValueFactory<KelasSuatuTahun, String>("kelas_suatu_tahun"));
        setTableKelasTahun();
    }
    public void handleAddKelasTahunbutton() {
        String query = "INSERT INTO kelas_suatu_tahun VALUES (?,?,?,?)";
        if(addIdTahunAjaranField.getText().isEmpty() || addIdKelasField.getText().isEmpty() ||
                addKelasSuatuTahunField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please input all fields!");
            return;
        }
        try {
            Connection db = DatabaseConnection.getConnection();
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1,KelasSuatuTahun.serial);
            statement.setString(2,addKelasSuatuTahunField.getText());
            statement.setInt(3,Integer.parseInt(addIdKelasField.getText()));
            statement.setInt(4,Integer.parseInt(addIdTahunAjaranField.getText()));
            statement.executeUpdate();
            listKelasTahun.add(new KelasSuatuTahun(KelasSuatuTahun.serial, Integer.parseInt(addIdTahunAjaranField.getText()), Integer.parseInt(addIdKelasField.getText()), addKelasSuatuTahunField.getText()));
            tableKelasTahun.setItems(listKelasTahun);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleUpdateKelasTahunButton(){
        String columnName = updateColumnNameField.getText();
        String query = "UPDATE kelas_suatu_tahun SET " + columnName + " = ? ";
        String wherecondition = "WHERE " + columnName + " = ?";
        query += wherecondition;

        if(columnName.isEmpty() || updateNamaField.getText().isEmpty() || updateReplacedNamaField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please input all fields!");
            return;
        }
        if(!(columnName.matches("id_kelastahun") || columnName.matches("id_tahunajaran") || columnName.matches("id_kelas") ||
                columnName.matches("kelas_suatu_tahun"))){
            showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Please enter the appropriate column name");
            return;
        }
        try {
            Connection db = DatabaseConnection.getConnection();
            PreparedStatement statement = db.prepareStatement(query);
            statement.setString(1,updateNamaField.getText());
            statement.setString(2,updateReplacedNamaField.getText());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setTableKelasTahun();
    }
    public void handleDeleteKelasTahunButton(){
        String query = "DELETE FROM kelas_suatu_tahun WHERE id_kelastahun = ?";
        KelasSuatuTahun selectedKelasTahun = tableKelasTahun.getSelectionModel().getSelectedItem();
        try {
            Connection db = DatabaseConnection.getConnection();
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1,selectedKelasTahun.getId_kelastahun());
            statement.executeUpdate();

            listKelasTahun.remove(selectedKelasTahun);
            tableKelasTahun.setItems(listKelasTahun);
            if (selectedKelasTahun.getId_kelastahun() == KelasSuatuTahun.getSerial() - 1) KelasSuatuTahun.serial--;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleReadKelasTahunButton(){
        String columnName = SearchColumnNameField.getText();
        columnName = columnName.toLowerCase();
        String search = SearchField.getText();
        String query = "SELECT * FROM kelas_suatu_tahun WHERE " + columnName + " like " + "'%" + search + "%'";
        if (search.isEmpty()) {
            setTableKelasTahun();
            return;
        }
        if(!(columnName.matches("id_kelastahun") || columnName.matches("id_tahunajaran") || columnName.matches("id_kelas") || columnName.matches("kelas_suatu_tahun"))){
            showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Please enter the appropriate column name");
            return;
        }
        try {
            listKelasTahun.clear();
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Integer idKelasTahun = resultSet.getInt("id_kelastahun");
                Integer idTahunAjaran = resultSet.getInt("id_tahunajaran");
                Integer idKelas = resultSet.getInt("id_kelas");
                String kelasSuatuTahun = resultSet.getString("kelas_suatu_tahun");

                KelasSuatuTahun kelasTahun = new KelasSuatuTahun(idKelasTahun,idTahunAjaran,idKelas,kelasSuatuTahun);
                listKelasTahun.add(kelasTahun);
            }
            tableKelasTahun.setItems(listKelasTahun);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        KelasSuatuTahun.serial = 1;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void setTableKelasTahun () {
        listKelasTahun.clear();
        String query = "SELECT * FROM kelas_suatu_tahun";
        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Integer idKelasTahun = resultSet.getInt("id_kelastahun");
                Integer idTahunAjaran = resultSet.getInt("id_tahunajaran");
                Integer idKelas = resultSet.getInt("id_kelas");
                String kelasSuatuTahun = resultSet.getString("kelas_suatu_tahun");

                KelasSuatuTahun kelasTahun = new KelasSuatuTahun(idKelasTahun,idTahunAjaran,idKelas,kelasSuatuTahun);
                listKelasTahun.add(kelasTahun);
            }
            tableKelasTahun.setItems(listKelasTahun);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleBackToMenuButton(){
        loadLandingPage();
    }
    private void loadLandingPage() {
        // Load the landing page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("landing.fxml"));
            Parent root = loader.load();

            LandingController controller = loader.getController();
            controller.setStage(stage);

//            stage.getScene().setRoot(root);
            stage.setScene(new Scene(root,600,375));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}