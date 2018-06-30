package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import model.Product;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

@SuppressWarnings("Duplicates")
public class ShowItemsController implements Initializable {

    @FXML
    private TreeTableView<Product> productsTV;
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
    private void rentProduct() throws SQLException {/*
        TreeItem<Product> item = productsTV.getSelectionModel().getSelectedItem();
        if (item != null && item.getChildren().size() == 0){
            DBConnection.getInstance().rentItem(item.getValue().getName());
            this.refresh();
        }*/
    }

    /**
     * Löscht das ausgewählte Produkt
     * @throws SQLException
     */
    @FXML
    private void deleteProduct() throws SQLException {/*
        TreeItem<Product> item = productsTV.getSelectionModel().getSelectedItem();
        if(item != null && item.getChildren().size() == 0) {
            DBConnection.getInstance().deleteProduct(Integer.parseInt(item.getValue().getName()));
            this.refresh();
        }else if(item != null && item.getChildren().size() != 0){
            DBConnection.getInstance().deleteProductWithItems(Integer.parseInt(item.getValue().getId()));
            this.refresh();
        }else if(item != null && item.getChildren().size() != 0){
            DBConnection.getInstance().deleteProduct(Integer.parseInt(item.getValue().getId()));
            this.refresh();
        }*/
    }

    private void refresh() throws SQLException {/*
        productsTV.setRoot(null);
        List<Product> products = DBConnection.getInstance().getProductsList();
        TreeItem<Product> root = new TreeItem<>(new Product(-1, -1, "", "", -1, -1)); //empty root element

        for (Product product : products) {
            TreeItem<Product> cache = new TreeItem<>(product);

            try {
                List<Integer> itemIDS = DBConnection.getInstance().getAvailableProducts(Integer.parseInt(item.getId()));
                for (Integer itemID : itemIDS) {
                    TreeItem<Product> subCacheItem = new TreeItem<>(new Product(Integer.toString(itemID), "", ""));
                    cache.getChildren().add(subCacheItem);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            root.getChildren().add(cache);
        }
        productsTV.setShowRoot(false);
        productsTV.setRoot(root);*/
    }
}