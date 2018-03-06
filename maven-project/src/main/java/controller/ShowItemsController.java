package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import loginPackage.DBConnection;
import model.Product;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ShowItemsController implements Initializable {

    @FXML
    private TreeTableView<Product> itemsTV;
    @FXML
    private TreeTableColumn<Product, String> prodNameCol, descCol, totalProdCol;
    @FXML
    private TreeTableColumn<Product, Integer> availableProdCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        prodNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        totalProdCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("totalExemplars"));
        availableProdCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("availableExemplars"));

        prodNameCol.setMinWidth(200);
        descCol.setMinWidth(300);
        totalProdCol.setMinWidth(300);
        availableProdCol.setMinWidth(300);

        try {
            this.refresh();
        } catch (SQLException e) {
             e.printStackTrace();
        }
    }

    @FXML
    private void rentProduct() throws SQLException {
        TreeItem<Product> item = itemsTV.getSelectionModel().getSelectedItem();
        if (item != null && item.getChildren().size() == 0){
            DBConnection.getInstance().rentItem(item.getValue().getName());
            this.refresh();
        }
    }

    /**
     * Löscht das ausgewählte Produkt
     * @throws SQLException
     */
    @FXML
    private void deleteProduct() throws SQLException {
        TreeItem<Product> item = itemsTV.getSelectionModel().getSelectedItem();
        if(item != null && item.getChildren().size() == 0) {
            DBConnection.getInstance().deleteItem(Integer.parseInt(item.getValue().getName()));
            this.refresh();
        }else if(item != null && item.getChildren().size() != 0){
            DBConnection.getInstance().deleteProductWithItems(Integer.parseInt(item.getValue().getId()));
            this.refresh();
        }else if(item != null && item.getChildren().size() != 0){
            DBConnection.getInstance().deleteItem(Integer.parseInt(item.getValue().getId()));
            this.refresh();
        }
    }

    private void refresh() throws SQLException {
        itemsTV.setRoot(null);
        List<Product> items = DBConnection.getInstance().getProductsList();
        TreeItem<Product> root = new TreeItem<>(new Product("", "", "")); //empty root element

        for (Product item : items) {
            TreeItem<Product> cache = new TreeItem<>(item);

            try {
                List<Integer> itemIDS = DBConnection.getInstance().getAvailableItems(Integer.parseInt(item.getId()));
                for (Integer itemID : itemIDS) {
                    TreeItem<Product> subCacheItem = new TreeItem<>(new Product(Integer.toString(itemID), "", ""));
                    cache.getChildren().add(subCacheItem);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            root.getChildren().add(cache);
        }
        itemsTV.setShowRoot(false);
        itemsTV.setRoot(root);
    }
}