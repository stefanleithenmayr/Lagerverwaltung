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
    private TableColumn<?,?> userNameCol, fromCol, untilCol;

    private ObservableList<Rent> rentList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loadAllRents();
        userNameCol.setCellValueFactory(
                new PropertyValueFactory<>("UserName"));

        fromCol.setCellValueFactory(
                new PropertyValueFactory<>("From"));

        untilCol.setCellValueFactory(
                new PropertyValueFactory<>("Until"));

        fromCol.setMinWidth(200);
        untilCol.setMinWidth(200);
        userNameCol.setMinWidth(200);
    }
    //TestComment asdfk
    //Submetho
    private void loadAllRents() {
        rentList = null;
        try {
            rentList = FXCollections.observableArrayList(DBConnection.getInstance().getAllRents());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewRents.setItems(rentList);
    }
}