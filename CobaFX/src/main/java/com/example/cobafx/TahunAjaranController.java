package com.example.cobafx;

import com.example.cobafx.classes.KelasSuatuTahun;
import com.example.cobafx.classes.TahunAjaran;
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
import java.util.Optional;

public class TahunAjaranController {

    @FXML
    private TableView<TahunAjaran> tableTahunAjaran;
    @FXML
    private TableColumn<TahunAjaran, Integer> idTahunAjaran = new TableColumn<>();
    @FXML
    private TableColumn<TahunAjaran, Integer> tahunAjaran = new TableColumn<>();

    @FXML
    private Button add;
    @FXML
    private Button update;
    @FXML
    private Button delete;
    @FXML
    private Button search;
    @FXML
    private Button back;

    @FXML
    private TextField searchField, IdTahunAjaranField,TahunAjaranField;

    private ObservableList<TahunAjaran> listTahunAjaran = FXCollections.observableArrayList();

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        idTahunAjaran.setCellValueFactory(new PropertyValueFactory<>("id_tahunajaran"));
        tahunAjaran.setCellValueFactory(new PropertyValueFactory<>("tahun_ajaran"));
        setTableTahunAjaran();
    }

    @FXML
    public void handleAddTahunAjaran() {
        String tahunAjaranStr = TahunAjaranField.getText();
        if (tahunAjaranStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error!", "Please enter the academic year.");
            return;
        }
        if(IdTahunAjaranField.getText().isEmpty()){
            IdTahunAjaranField.setText(" ");
        }
        String tahunAjaran = tahunAjaranStr;
//        Integer id = Integer.parseInt(IdTahunAjaranField.getText());

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO tahun_ajaran VALUES (?,?)";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setInt(1, TahunAjaran.getSerial());
            pstmt.setString(2, tahunAjaran);
            pstmt.executeUpdate();

            listTahunAjaran.add(new TahunAjaran(tahunAjaran));
            clearFields();
            tableTahunAjaran.setItems(listTahunAjaran);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error occurred while adding academic year.");
            return;
        }
    }

    @FXML
    public void handleDeleteTahunAjaran() {
        TahunAjaran selectedData = tableTahunAjaran.getSelectionModel().getSelectedItem();

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select an academic year.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Academic Year");
        alert.setContentText("Are you sure you want to delete this academic year?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection db = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM tahun_ajaran WHERE id_tahunajaran = ?";
                PreparedStatement pstmt = db.prepareStatement(query);
                pstmt.setInt(1, selectedData.getId_tahunajaran());
                pstmt.executeUpdate();

                listTahunAjaran.remove(selectedData);
                tableTahunAjaran.setItems(listTahunAjaran);
                if (selectedData.getId_tahunajaran() == KelasSuatuTahun.getSerial() - 1) KelasSuatuTahun.serial--;
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error occurred while deleting academic year.");
                return;
            }
        }
    }

    @FXML
    public void handleUpdateTahunAjaran() {
        TahunAjaran selectedData = tableTahunAjaran.getSelectionModel().getSelectedItem();

        if (selectedData == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select an academic year.");
            return;
        }

        String tahunAjaranStr = TahunAjaranField.getText();
        if (tahunAjaranStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Empty New Data", "Please fill the field.");
            return;
        }

        String tahunAjaran = tahunAjaranStr;

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "UPDATE tahun_ajaran SET tahun_ajaran = ? WHERE id_tahunajaran = ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, tahunAjaran);
            pstmt.setInt(2, selectedData.getId_tahunajaran());
            pstmt.executeUpdate();

            selectedData.setTahun_ajaran(tahunAjaran);
            setTableTahunAjaran();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error occurred while updating academic year.");
            return;
        }
        TahunAjaran.setSerial(1);
        setTableTahunAjaran();
    }

    @FXML
    public void handleSearchTahunAjaran() {
        String search = searchField.getText().trim();

        if (search.isEmpty()) {
            setTableTahunAjaran();
            return;
        }

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM tahun_ajaran WHERE tahun_ajaran LIKE ?";
            PreparedStatement pstmt = db.prepareStatement(query);
            pstmt.setString(1, "%" + search + "%");

            ResultSet resultSet = pstmt.executeQuery();
            listTahunAjaran.clear();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_tahunajaran");
                String tahunAjaran = resultSet.getString("tahun_ajaran");

                TahunAjaran foundTahunAjaran = new TahunAjaran(id, tahunAjaran);
                listTahunAjaran.add(foundTahunAjaran);
            }

            tableTahunAjaran.setItems(listTahunAjaran);
            TahunAjaran.setSerial(1);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error occurred while searching for academic year.");
            return;
        }
    }

    @FXML
    protected void handleBackButtonAction() {
        loadLandingPage();
    }

    private void setTableTahunAjaran() {
        listTahunAjaran.clear();

        try (Connection db = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM tahun_ajaran";
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Integer id = rs.getInt("id_tahunajaran");
                String tahunAjaran = rs.getString("tahun_ajaran");

                TahunAjaran tahunAjaranObj = new TahunAjaran(id, tahunAjaran);
                listTahunAjaran.add(tahunAjaranObj);
            }

            tableTahunAjaran.setItems(listTahunAjaran);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error occurred while fetching academic year list.");
            return;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("landing.fxml"));
            Parent root = loader.load();

            LandingController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        IdTahunAjaranField.clear();
        TahunAjaranField.clear();
        searchField.clear();
    }
}
