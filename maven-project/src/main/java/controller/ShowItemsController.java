package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import loginPackage.DBConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import model.Item;

public class ShowItemsController implements Initializable {

    @FXML
    private TableView<Item> itemsTV;

    @FXML
    private TableColumn<?,?> prodNameCol, descCol, totalProdCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Item> items = null;
        try {
            items =  DBConnection.getInstance().getItemsList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        prodNameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        descCol.setCellValueFactory(
                new PropertyValueFactory<>("description"));

        totalProdCol.setCellValueFactory(
                new PropertyValueFactory<>("totalExemplars"));


        prodNameCol.setMinWidth(200);
        descCol.setMinWidth(300);
        totalProdCol.setMinWidth(300);

        if(items != null){
            itemsTV.setItems(FXCollections.observableArrayList(items));
        }
    }
}