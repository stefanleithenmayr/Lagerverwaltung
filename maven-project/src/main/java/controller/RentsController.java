package controller;


import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Rent;

import java.net.URL;
import java.util.ResourceBundle;

public class RentsController implements Initializable{

    @FXML
    private TableView<Rent> tableViewRents;
    @FXML
    private TableColumn<?,?> productName;

    @FXML
    private JFXButton returnBT, exportBT;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productName.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        productName.setMinWidth(200);

        ObservableList<Rent> rentList = FXCollections.observableArrayList();
        rentList.add(new Rent("Kamera"));
        rentList.add(new Rent("PC"));
        rentList.add(new Rent("Fussball"));
        rentList.add(new Rent("Test"));
        rentList.add(new Rent("Roboter"));
        rentList.add(new Rent("Verlängerungskabel"));
        rentList.add(new Rent("Monitor"));
        tableViewRents.setItems(rentList);
    }
}