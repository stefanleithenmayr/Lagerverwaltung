package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import loginPackage.DBConnection;
import model.Rent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RentsController implements Initializable{

    @FXML
    private TableView<Rent> tableViewRents;
    @FXML
    private TableColumn<?,?> productNameCol, exemplarIDCol, userCol, fullNameCol;
    @FXML
    private JFXComboBox<String> chooseRents;
    @FXML
    private JFXButton returnBT;

    private ObservableList<Rent> rentList;

    @FXML
    private void returnExemplar() throws SQLException {
        Rent selectedRent = tableViewRents.getSelectionModel().getSelectedItem();
        if (selectedRent != null){
            DBConnection.getInstance().removeRent(selectedRent.getID());
            rentList.remove(selectedRent);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (DBConnection.getInstance().getActualUser().equals("stuetz")) {
            chooseRents.setVisible(true);
            userCol.setVisible(true);
            fullNameCol.setVisible(true);

            chooseRents.valueProperty().addListener((ov, oldVal, newVal) -> {
                if (newVal != null){
                    if (newVal.equals("My Rents")){
                        loadMyRents();
                        returnBT.setDisable(false);
                    }else if(newVal.equals("All Rents")){
                        loadAllRents();
                        returnBT.setDisable(true);
                    }
                }
            });
        }else{
            loadMyRents();
        }

        productNameCol.setCellValueFactory(
                new PropertyValueFactory<>("itemName"));

        exemplarIDCol.setCellValueFactory(
                new PropertyValueFactory<>("iD"));

        userCol.setCellValueFactory(
                new PropertyValueFactory<>("userName"));

        fullNameCol.setCellValueFactory(
                new PropertyValueFactory<>("fullName"));

        productNameCol.setMinWidth(200);
        exemplarIDCol.setMinWidth(200);
        userCol.setMinWidth(200);
        fullNameCol.setMinWidth(200);

        chooseRents.getItems().add("My Rents");
        chooseRents.getItems().add("All Rents");
    }

    //Submethods
    private void loadAllRents() {
        rentList = null;
        try {
            rentList = FXCollections.observableArrayList(DBConnection.getInstance().getAllRents());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewRents.setItems(rentList);
    }

    private void loadMyRents(){
        rentList = null;
        try {
            rentList = FXCollections.observableArrayList(DBConnection.getInstance().getUserRents());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewRents.setItems(rentList);
    }
}