package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import loginPackage.DBConnection;
import model.Item;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ShowItemsController implements Initializable {

    @FXML
    private TreeTableView<Item> itemsTV;
    @FXML
    private TreeTableColumn<Item, String> prodNameCol;
    @FXML
    private TreeTableColumn<Item, String> descCol;
    @FXML
    private TreeTableColumn<Item, String> totalProdCol;
    @FXML
    private TreeTableColumn<Item, Integer> availableProdCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        prodNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        totalProdCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("totalExemplars"));
        availableProdCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("availableExemplars"));

        prodNameCol.setMinWidth(200);
        descCol.setMinWidth(300);
        totalProdCol.setMinWidth(300);

        try {
            this.refresh();
        } catch (SQLException e) {
             e.printStackTrace();
        }
    }

    @FXML
    private void rentProduct() throws SQLException {
        TreeItem<Item> item = itemsTV.getSelectionModel().getSelectedItem();
        if (item != null && item.getChildren().size() == 0){
            DBConnection.getInstance().rentItem(item.getValue().getName());
            this.refresh();
        }
    }

    private void refresh() throws SQLException {
        itemsTV.setRoot(null);
        List<Item> items = DBConnection.getInstance().getItemsList();

        TreeItem<Item> root = new TreeItem<>(new Item("", "", ""));
        for (Item item : items) {
            TreeItem<Item> cache = new TreeItem<>(item);
            try {
                List<Integer> ids = DBConnection.getInstance().getAvailableExemplars(Integer.parseInt(item.getId()));
                for (Integer id : ids) {
                    TreeItem<Item> secondCache = new TreeItem<>(new Item(Integer.toString(id), "", ""));
                    cache.getChildren().add(secondCache);
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