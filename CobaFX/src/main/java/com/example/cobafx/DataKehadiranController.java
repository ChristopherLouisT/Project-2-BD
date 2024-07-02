package com.example.cobafx;

import com.example.cobafx.classes.DataKehadiran;
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

public class DataKehadiranController {
    @FXML
    TableView <DataKehadiran>tableKehadiran;
    @FXML
    TableColumn <DataKehadiran, Integer> idKehadiran;
    @FXML
    TableColumn <DataKehadiran, Integer> idAnak;
    @FXML
    TableColumn <DataKehadiran, String>absence;
    @FXML
    TableColumn <DataKehadiran, Date> devotionDate;

    @FXML
    Button search;
    @FXML
    Button back;
    @FXML
    Button add;

    @FXML
    TextField searchField;
    @FXML
    TextField addidAnakField;
    @FXML
    TextField addAbsenceField;
    @FXML
    TextField addDevotionDateField;
    @FXML
    TextField updateNewValueField;

    @FXML
    MenuBar menubar;
    @FXML
    Menu Add;
    @FXML
    Menu Update;

    private ObservableList <DataKehadiran> listKehadiran = FXCollections.observableArrayList();
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
        idKehadiran.setCellValueFactory(new PropertyValueFactory<DataKehadiran, Integer>("idKehadiran"));
        idAnak.setCellValueFactory(new PropertyValueFactory<DataKehadiran, Integer>("idAnak")); // Sesuaikan dengan nama var dikelas biasanya
        absence.setCellValueFactory(new PropertyValueFactory<DataKehadiran, String>("absence"));
        devotionDate.setCellValueFactory(new PropertyValueFactory<DataKehadiran, Date>("date"));
        setTableDataKehadiran();
    }

    @FXML
    public void handleAddDataKehadiran() {
        if (addidAnakField.getText().isEmpty() || addDevotionDateField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please enter ID Anak dan Tanggal Kebaktian");
            return;
        }
        Integer idAnak = Integer.parseInt(addidAnakField.getText());
        String absence = addAbsenceField.getText();
        Date date = Date.valueOf(addDevotionDateField.getText());


        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO data_kehadiran (id_kehadiran, id_anak, keterangan_kehadiran, tanggal_kebaktian) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, DataKehadiran.lastID);
            pstmt.setInt(2, idAnak);
            pstmt.setString(3, absence);
            pstmt.setDate(4, date);
            pstmt.executeUpdate();
            listKehadiran.add(new DataKehadiran(DataKehadiran.lastID, idAnak, absence, date));
            clearFields();
            tableKehadiran.setItems(listKehadiran);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "There is no ID_ANAK or Tanggal_kebaktian matches");
            return;
        }
    }

    @FXML
    public void handleUpdateDataKehadiran() {
        DataKehadiran selectedData = tableKehadiran.getSelectionModel().getSelectedItem();
        Integer newID = null; Date newDate = null;

        try {
            newID = Integer.parseInt(updateNewValueField.getText());
        } catch (Exception e) {
            try {
                newDate = Date.valueOf(updateNewValueField.getText());
            } catch (Exception er) {
                showAlert(Alert.AlertType.ERROR, "Invalid Value", "Please Input A Correct Value");
                return;
            }
        }

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please Select A Data");
            return;
        }
        if (newID != null && newID < 1) {
            showAlert(Alert.AlertType.ERROR, "Out Range Value", "Please Use The Correct ID");
            return;
        }
        if (newDate != null && Integer.valueOf(String.valueOf(newDate).substring(0, 3+1)) < 2019) {
            showAlert(Alert.AlertType.ERROR, "Out Range Value", "Year Below 2019");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()){
            String query;
            PreparedStatement pstmt = null;
            if (newID != null) {
                query = "UPDATE DATA_KEHADIRAN SET id_anak = ? WHERE id_kehadiran = ?";
                pstmt = db.prepareStatement(query);
                pstmt.setInt(1,newID);
                selectedData.setIdAnak(newID);
            }
            if (newDate != null) {
                query = "UPDATE DATA_KEHADIRAN SET tanggal_kebaktian = ? WHERE id_kehadiran = ?";
                pstmt = db.prepareStatement(query);
                pstmt.setDate(1,newDate);
                selectedData.setDate(newDate);
            }
            pstmt.setInt(2,selectedData.getIdKehadiran());
            pstmt.executeUpdate();

            DataKehadiran.lastID = 1;
            setTableDataKehadiran();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteDataKehadiran() {
        DataKehadiran selectedData = tableKehadiran.getSelectionModel().getSelectedItem();

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please Select A Data");
            return;
        }

        try (Connection db = DatabaseConnection.getConnection();){
            String query = "DELETE FROM DATA_KEHADIRAN WHERE id_kehadiran = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, selectedData.getIdKehadiran());
            pstmt.executeUpdate();

            listKehadiran.remove(selectedData);
            tableKehadiran.setItems(listKehadiran);
            if (selectedData.getIdKehadiran() == DataKehadiran.lastID - 1) DataKehadiran.lastID--;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleSearchDataKehadiran () {
        String search = searchField.getText();
        String query = null;
        Integer searchIDAnak = null;
        Date searchDate = null;
        if (search.isEmpty()) {
            setTableDataKehadiran();
            return;
        }
        try {
            searchIDAnak = Integer.valueOf(search);
            if (searchIDAnak < 1) {
                showAlert(Alert.AlertType.ERROR, "Out Range Value", "Please Use The Correct ID");
                return;
            }
            query = "SELECT * FROM data_kehadiran WHERE id_anak = ";
        } catch (Exception e) {
            try {
                searchDate = Date.valueOf(search);
                if (Integer.valueOf(String.valueOf(searchDate).substring(0, 3 + 1)) < 2019) {
                    showAlert(Alert.AlertType.ERROR, "Out Range Value", "Year Below 2019 Or Above 2024");
                    return;
                }
                query = "SELECT * FROM data_kehadiran WHERE tanggal_kebaktian = ";
            } catch (Exception er) {
                showAlert(Alert.AlertType.ERROR, "Invalid Parameter", "Please Search Either ID Anak or Devotion Date");
                return;
            }
        }

        try {
            listKehadiran.clear();
            Connection db = DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet;
            if (searchIDAnak != null) resultSet = statement.executeQuery(query + searchIDAnak);
            else resultSet = statement.executeQuery(query + "'" + searchDate + "'");


            while (resultSet.next()) {
                Integer idKehadiran = resultSet.getInt("id_kehadiran");
                Integer idAnak = resultSet.getInt("id_anak");
                String absence = resultSet.getString("keterangan_kehadiran");
                Date date = resultSet.getDate("tanggal_kebaktian");

                DataKehadiran dataKehadiran = new DataKehadiran(idKehadiran, idAnak, absence, date);
                listKehadiran.add(dataKehadiran);
            }
            tableKehadiran.setItems(listKehadiran);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DataKehadiran.lastID = 1;
    }

    public void setTableDataKehadiran () {
        listKehadiran.clear();
        String query = "SELECT * FROM data_kehadiran";
        try {
            Connection db =DatabaseConnection.getConnection();
            Statement statement = db.createStatement();
            ResultSet resultSet =statement.executeQuery(query);

            while (resultSet.next()) {
                Integer idKehadiran = resultSet.getInt("id_kehadiran");
                Integer idAnak = resultSet.getInt("id_anak");
                String absence = resultSet.getString("keterangan_kehadiran");
                Date date = resultSet.getDate("tanggal_kebaktian");

                DataKehadiran dataKehadiran = new DataKehadiran(idKehadiran, idAnak, absence, date);
                listKehadiran.add(dataKehadiran);
            }
            tableKehadiran.setItems(listKehadiran);
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

        private void clearFields() {
        searchField.clear();
        addidAnakField.clear();
        addDevotionDateField.clear();
        updateNewValueField.clear();
    }
}
