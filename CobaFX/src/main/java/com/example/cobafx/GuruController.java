package com.example.cobafx;

import com.example.cobafx.classes.Guru;
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
public class GuruController{

    //Add Textfields
    ObservableList<Guru> listGuru = FXCollections.observableArrayList();
    @FXML
    private TextField addNamaField,addGenderField,addTanggalLahirField,addNoTelpGuruField,addAlamatField;
    //Update Textfields
    @FXML
    private TextField SearchField,SearchColumnNameField;
    //Update Menu
    @FXML
    private TextField updateColumnNameField,updateReplacedNamaField,updateNamaField;

    @FXML
    private Button addGuruButton,SearchButton,UpdateButton,BackToMenuButton;
    @FXML
    TableView<Guru> tableGuru = new TableView<>();
    @FXML
    TableColumn <Guru,Integer> idGuru = new TableColumn<>();
    @FXML
    TableColumn <Guru,String> nama = new TableColumn<>();
    @FXML
    TableColumn <Guru,String> gender = new TableColumn<>();
    @FXML
    TableColumn <Guru,String> tanggalLahir = new TableColumn<>();
    @FXML
    TableColumn <Guru,String> noTelpGuru = new TableColumn<>();
    @FXML
    TableColumn <Guru,String> alamat = new TableColumn<>();
    private Stage stage;

    public void initialize () {
        //Binding
        idGuru.setCellValueFactory(new PropertyValueFactory<Guru, Integer>("id_guru"));
        nama.setCellValueFactory(new PropertyValueFactory<Guru, String>("nama"));
        gender.setCellValueFactory(new PropertyValueFactory<Guru,String>("gender"));
        tanggalLahir.setCellValueFactory(new PropertyValueFactory<Guru, String>("tanggal_lahir"));
        noTelpGuru.setCellValueFactory(new PropertyValueFactory<Guru, String>("noTelp_guru"));
        alamat.setCellValueFactory(new PropertyValueFactory<Guru, String>("alamat"));
        setTableGuru();
    }
    public void handleAddGuruButton() {
        String query = "INSERT INTO guru VALUES (?,?,?,?,?,?)";
        String nama = addNamaField.getText();
        String gender = addGenderField.getText();
        String tanggalLahir = addTanggalLahirField.getText();
        String noTelpGuru = addNoTelpGuruField.getText();
        String alamat = addAlamatField.getText();
        if(addNamaField.getText() == null || addGenderField.getText() == null ||
                addTanggalLahirField.getText().isEmpty() || addNoTelpGuruField.getText() == null){
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please input all fields!");
            return;
        }
        if(addAlamatField.getText().isBlank()){
            addAlamatField.setText(null);
        }
        try {
            Connection db = DatabaseConnection.getConnection();
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1,Guru.getSerial());
            statement.setString(2,addNamaField.getText());
            statement.setString(3,addGenderField.getText());
            statement.setDate(4, Date.valueOf(addTanggalLahirField.getText()));
            statement.setString(5,addNoTelpGuruField.getText());
            statement.setString(6,addAlamatField.getText());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        listGuru.add(new Guru(nama,gender,tanggalLahir,noTelpGuru,alamat));
        clearFields();
        tableGuru.setItems(listGuru);


    }
    public void handleUpdateGuruButton(){
        String columnName = updateColumnNameField.getText();
        String query = "UPDATE guru SET " + columnName + " = ? ";
        String wherecondition = "WHERE " + columnName + " = ?";
        query += wherecondition;

        if(columnName.isEmpty() || updateNamaField.getText().isEmpty() || updateReplacedNamaField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please input all fields!");
            return;
        }
        if(!(columnName.matches("id_guru") || columnName.matches("nama") || columnName.matches("gender") ||
                columnName.matches("tanggal_lahir") || columnName.matches("noTelp_guru") || columnName.matches("alamat"))){
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
        Guru.setSerial(1);
        setTableGuru();

    }
    public void handleDeleteGuruButton(){
        String query = "DELETE FROM guru WHERE id_guru = ?";
        Guru selectedGuru = tableGuru.getSelectionModel().getSelectedItem();
        try {
            Connection db = DatabaseConnection.getConnection();
            PreparedStatement statement = db.prepareStatement(query);
            statement.setInt(1,selectedGuru.getId_guru());
            statement.executeUpdate();
            listGuru.remove(selectedGuru);
            tableGuru.setItems(listGuru);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(selectedGuru.getId_guru() - 1 == Guru.getSerial() - 1) Guru.setSerial(Guru.getSerial()-1);


    }
    public void handleReadGuruButton(){ObservableList<Guru> listGuru = FXCollections.observableArrayList();
        String columnName = SearchColumnNameField.getText();
        columnName = columnName.toLowerCase();
        String search = SearchField.getText();
        String query = "SELECT * FROM guru WHERE " + columnName + " like " + "'%" + search + "%'";
        if (search.isEmpty()) {
            setTableGuru();
            return;
        }
        if(!(columnName.matches("nama") || columnName.matches("gender") ||
                columnName.matches("tanggal_lahir") || columnName.matches("noTelp_guru") || columnName.matches("alamat"))){
            showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Please enter the appropriate column name");
            return;
        }
        try {
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Integer idGuru = resultSet.getInt("id_guru");
                String nama = resultSet.getString("nama");
                String gender = resultSet.getString("gender");
                String tanggalLahir = resultSet.getString("tanggal_lahir");
                String noTelpGuru = resultSet.getString("noTelp_guru");
                String alamat = resultSet.getString("alamat");

                Guru kelasTahun = new Guru(idGuru,nama,gender,tanggalLahir,noTelpGuru,alamat);
                listGuru.add(kelasTahun);
            }
            tableGuru.setItems(listGuru);
            Guru.setSerial(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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


    public void setTableGuru () {
        String query = "SELECT * FROM guru";
        try {
            listGuru.clear();
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Integer idGuru = resultSet.getInt("id_guru");
                String nama = resultSet.getString("nama");
                String gender = resultSet.getString("gender");
                String tanggalLahir = resultSet.getString("tanggal_lahir");
                String noTelpGuru = resultSet.getString("noTelp_guru");
                String alamat = resultSet.getString("alamat");

                Guru kelasTahun = new Guru(idGuru,nama,gender,tanggalLahir,noTelpGuru,alamat);
                listGuru.add(kelasTahun);
            }
            tableGuru.setItems(listGuru);
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
    private void clearFields(){
        addNamaField.clear();
        addGenderField.clear();
        addTanggalLahirField.clear();
        addNoTelpGuruField.clear();
        addAlamatField.clear();
    }
}
