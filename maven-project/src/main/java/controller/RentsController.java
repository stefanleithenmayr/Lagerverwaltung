package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import loginPackage.DBConnection;
import model.Product;
import model.ProductType;
import model.Rent;
import model.SimpleOutput;

import javax.xml.bind.annotation.XmlAnyAttribute;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RentsController implements Initializable{

    @FXML
    private TableView<Rent> tableViewRents;
    @FXML
    private TableColumn<?,?> userNameCol, fromCol, untilCol, productNrCol, productNameCol;
    @FXML
    private TableView<Product> productList;
    private List<Product> products;
    private List<Rent> rentList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loadAllRents();

        tableViewRents.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                if (tableViewRents.getSelectionModel().getSelectedItem() != null){
                    try {
                        showProducts(tableViewRents.getSelectionModel().getSelectedItem());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        userNameCol.setCellValueFactory(
                new PropertyValueFactory<>("UserName"));

        fromCol.setCellValueFactory(
                new PropertyValueFactory<>("From"));

        untilCol.setCellValueFactory(
                new PropertyValueFactory<>("Until"));

        productNrCol.setCellValueFactory(
                new PropertyValueFactory<>("productNr"));

        productNameCol.setCellValueFactory(
                new PropertyValueFactory<>("productName"));

        fromCol.setMinWidth(200);
        untilCol.setMinWidth(200);
        userNameCol.setMinWidth(200);
        rentList = new LinkedList<>();
    }

    private void showProducts(Rent selectedItem) throws SQLException {
        int rentid = selectedItem.getRentID();
        List<SimpleOutput> outputs = FXCollections.observableArrayList(DBConnection.getInstance().getItemsFromRent(rentid));
        productList.setItems((ObservableList) outputs);
    }

    //Submethod
    private void loadAllRents() {
        rentList = null;
        try {
            rentList = FXCollections.observableArrayList(DBConnection.getInstance().getAllRents());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewRents.setItems((ObservableList<Rent>) rentList);
    }
}