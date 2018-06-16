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

public class ShowSetsController implements Initializable {
    @FXML
    private TreeTableView TTVShowSets;
    @FXML
    private TreeTableColumn tcShowSetsProductName, tcShowSetsDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcShowSetsProductName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        tcShowSetsDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));

        List<Product> setHeaders = null;
        try {
            setHeaders = DBConnection.getInstance().getHighestSetHeaders();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TTVShowSets.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1,null,null,null, null, null));
        for (int i = 0; i < setHeaders.size(); i++){
            TreeItem<Product> cache = new TreeItem<>(setHeaders.get(i));
            try {
                this.printSetsTree(setHeaders.get(i), cache);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            root.getChildren().add(cache);
        }
        TTVShowSets.setShowRoot(false);
        TTVShowSets.setRoot(root);
    }

    public void printSetsTree(Product head, TreeItem<Product> father) throws SQLException {
        List<Product> listOfChildren = DBConnection.getInstance().getProductsChildrenByProductID(head);
        for(int i = 0; i < listOfChildren.size(); i++) {
            listOfChildren.get(i).setIsChild(true);
            listOfChildren.get(i).setSelected(null);
            TreeItem<Product> child = new TreeItem<>(listOfChildren.get(i));
            father.getChildren().add(child);
            Product childProduct = listOfChildren.get(i);
            printSetsTree(childProduct,child);
        }
    }
}
