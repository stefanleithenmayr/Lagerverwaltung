package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import model.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteItemController implements Initializable{
    @FXML
    TreeTableView<Product> TTVShowProducts;
    @FXML
    TreeTableColumn tcProductName, tcDescription, tcSelect;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcProductName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        tcDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        tcSelect.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));
    }

}
