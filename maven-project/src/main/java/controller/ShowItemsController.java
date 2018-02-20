package controller;

import com.sun.xml.internal.ws.util.QNameMap;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import loginPackage.DBConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import model.Item;
import model.User;

public class ShowItemsController implements Initializable {

    @FXML
    private TreeTableView<Item> itemsTV;
    @FXML
    private TreeTableColumn<Item, String> prodNameCol;
    @FXML
    private TreeTableColumn<Item, String> descCol;
    @FXML
    private TreeTableColumn<Item, String> totalProdCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Item> items = null;
        try {
            items =  DBConnection.getInstance().getItemsList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        prodNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        totalProdCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("totalExemplars"));

        prodNameCol.setMinWidth(200);
        descCol.setMinWidth(300);
        totalProdCol.setMinWidth(300);

        if(items != null){
            TreeItem<Item> root = new TreeItem<>(new Item("", "", ""));
            for (Item item : items) {
                TreeItem<Item> cache = new TreeItem<>(item);
                try {
                    List<Integer> ids = DBConnection.getInstance().getExemplars(Integer.parseInt(item.getId()));
                    //error wenn keine exemplare!
                    for (int index = 0; index < ids.size(); index++){
                        TreeItem<Item> secondCache = new TreeItem<>(new Item(Integer.toString(ids.get(index)),"", ""));
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
}