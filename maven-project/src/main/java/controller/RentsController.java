package controller;


import com.jfoenix.controls.JFXButton;
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
    private TableColumn<?,?> productNameCol, exemplarIDCol;

    @FXML
    private JFXButton returnBT, exportBT;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productNameCol.setCellValueFactory(
                new PropertyValueFactory<>("itemName"));

        exemplarIDCol.setCellValueFactory(
                new PropertyValueFactory<>("iD"));

        productNameCol.setMinWidth(200);
        exemplarIDCol.setMinWidth(200);

        ObservableList<Rent> rentList = null;
        try {
            rentList = FXCollections.observableArrayList(DBConnection.getInstance().getUserRents());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewRents.setItems(rentList);
    }
}