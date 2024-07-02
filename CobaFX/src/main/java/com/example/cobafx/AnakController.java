package com.example.cobafx;

import com.example.cobafx.classes.Anak;
import javafx.fxml.FXML;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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


//implements RUDController

public class AnakController{
    ObservableList <Anak> listAnak = FXCollections.observableArrayList();
    private Stage stage;
    @FXML
    private TextField addidAnakField,addNamaField,addGenderField,addTanggalLahirField,
            addAlamatField,addNamaWaliField,addNoTelpWaliField,SearchField,SearchColumnNameField;
    //Update Menu
    @FXML
    private TextField updateColumnNameField,updateReplacedNamaField,updateNamaField;

    @FXML
    private Button add,SearchButton,UpdateButton,BackToMenuButton;
    @FXML
    TableView<Anak> tableAnak = new TableView<>();
    @FXML
    TableColumn <Anak,Integer> idAnak = new TableColumn<>();
    @FXML
    TableColumn <Anak,String> nama = new TableColumn<>();
    @FXML
    TableColumn <Anak,Character> gender = new TableColumn<>();
    @FXML
    TableColumn <Anak,String> tanggalLahir = new TableColumn<>();
    @FXML
    TableColumn <Anak,String> alamat = new TableColumn<>();
    @FXML
    TableColumn <Anak,String> namaWali = new TableColumn<>();
    @FXML
    TableColumn <Anak,String> noTelpWali = new TableColumn<>();
    @FXML
    public void initialize () {
        //Binding
        idAnak.setCellValueFactory(new PropertyValueFactory<Anak, Integer>("id_anak"));
        nama.setCellValueFactory(new PropertyValueFactory<Anak, String>("nama"));
        gender.setCellValueFactory(new PropertyValueFactory<Anak, Character>("gender"));
        tanggalLahir.setCellValueFactory(new PropertyValueFactory<Anak, String>("tanggal_lahir"));
        alamat.setCellValueFactory(new PropertyValueFactory<Anak, String>("alamat"));
        namaWali.setCellValueFactory(new PropertyValueFactory<Anak, String>("nama_wali"));
        noTelpWali.setCellValueFactory(new PropertyValueFactory<Anak, String>("noTelp_wali"));
        setTableAnak();
    }
    public void handleAddAnakButton() {
        String query = "INSERT INTO anak VALUES (?,?,?,?,?,?,?)";
        //Mandatory variables
        if(addNamaField.getText().isEmpty() || addGenderField.getText().isEmpty() ||
                addTanggalLahirField.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please input all mandatory fields!");
            return;
        }
        //Optional variables
        if(addAlamatField.getText() == null){
            addAlamatField.setText(null);
        }
        if(addNamaWaliField.getText() == null){
            addNamaWaliField.setText(null);
        }
        if(addNoTelpWaliField.getText() == null){
            addNoTelpWaliField.setText(null);
        }
        try {
            Connection db = DatabaseConnection.getConnection();
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1,Anak.serial);
            statement.setString(2,addNamaField.getText());
            statement.setString(3,addGenderField.getText());
            statement.setDate(4,Date.valueOf(addTanggalLahirField.getText()));
            statement.setString(5,addAlamatField.getText());
            statement.setString(6,addNamaWaliField.getText());
            statement.setString(7,addNoTelpWaliField.getText());
            statement.executeUpdate();
            listAnak.add(new Anak(Anak.serial, addNamaField.getText(), addGenderField.getText().charAt(0), addTanggalLahirField.getText(), addAlamatField.getText(), addNamaWaliField.getText(), addNoTelpWaliField.getText()));
            tableAnak.setItems(listAnak);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleUpdateAnakButton(){
        String columnName = updateColumnNameField.getText();
        String query = "UPDATE anak SET " + columnName + " = ? ";
        String wherecondition = "WHERE " + columnName + " = ?";
        query += wherecondition;

        if(columnName.isEmpty() || updateNamaField.getText().isEmpty() || updateReplacedNamaField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please input all fields!");
        }
        if(!(columnName.matches("id_anak") || columnName.matches("nama") || columnName.matches("gender") || columnName.matches("tanggal_lahir")
                || columnName.matches("alamat") || columnName.matches("nama_wali") || columnName.matches("noTelp_wali"))){
            showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Please enter the appropriate column name");
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
        setTableAnak();
    }
    public void handleDeleteAnakButton(){
        String query = "DELETE FROM anak WHERE id_anak = ?";
        Anak selectedAnak = tableAnak.getSelectionModel().getSelectedItem();
        try {
            Connection db = DatabaseConnection.getConnection();
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1,selectedAnak.getId_anak());
            statement.executeUpdate();

            listAnak.remove(selectedAnak);
            tableAnak.setItems(listAnak);
            if (selectedAnak.getId_anak() == Anak.serial - 1) Anak.serial--;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void handleReadAnakButton(){
        String columnName = SearchColumnNameField.getText();
        columnName = columnName.toLowerCase();
        String search = SearchField.getText();
        String query = "SELECT * FROM anak WHERE " + columnName + " like " + "'%" + search + "%'";
        if (search.isEmpty()) {
            setTableAnak();
            return;
        }
        if(!(columnName.matches("id_anak") || columnName.matches("nama") || columnName.matches("gender") || columnName.matches("tanggal_lahir")
                || columnName.matches("alamat") || columnName.matches("nama_wali") || columnName.matches("noTelp_wali"))){
            showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Please enter the appropriate column name");
        }
        try {
            listAnak.clear();
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Integer idAnak = resultSet.getInt("id_anak");
                String nama = resultSet.getString("nama");
                Character gender = resultSet.getString("gender").charAt(0);
                String tanggalLahir = resultSet.getString("tanggal_lahir");
                String alamat = resultSet.getString("alamat");
                String namaWali = resultSet.getString("nama_wali");
                String noTelpWali = resultSet.getString("noTelp_wali");

                Anak anak = new Anak(idAnak,nama,gender,tanggalLahir,alamat,namaWali,noTelpWali);
                listAnak.add(anak);
            }
            tableAnak.setItems(listAnak);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Anak.serial = 1;
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


    public void setTableAnak () {
        listAnak.clear();
        String query = "SELECT * FROM anak";
        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Integer idAnak = resultSet.getInt("id_anak");
                String nama = resultSet.getString("nama");
                Character gender = resultSet.getString("gender").charAt(0);
                String tanggalLahir = resultSet.getString("tanggal_lahir");
                String alamat = resultSet.getString("alamat");
                String namaWali = resultSet.getString("nama_wali");
                String noTelpWali = resultSet.getString("noTelp_wali");

                Anak anak = new Anak(idAnak,nama,gender,tanggalLahir,alamat,namaWali,noTelpWali);
                listAnak.add(anak);
            }
            tableAnak.setItems(listAnak);
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
