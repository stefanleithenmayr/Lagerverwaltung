package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import loginPackage.DBConnection;
import model.Product;
import model.ProductType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShowSetsController implements Initializable {
    @FXML
    private TreeTableView<Product> TTVShowSets;
    @FXML
    private TreeTableColumn<Object, Object> tcShowSetsProductName;
    @FXML
    private TreeTableColumn<Object, Object> tcShowSetsDescription;
    @FXML
    private TextField tfSearch;
    List<Product> listHeaders;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TTVShowSets.setVisible(true);
        tcShowSetsProductName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        tcShowSetsDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));

        try {
            listHeaders = GetListHeaders(DBConnection.getInstance().getAllSetProductTypes());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TTVShowSets.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        if (listHeaders == null) return;
        for (Product listHeader : listHeaders) {
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = null;
            try {
                childs = DBConnection.getInstance().getAllProductsByProductTypeID(listHeader.getProducttypeID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (Product child : childs) {
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                try {
                    printSetsTree(child, cache);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0) {
                root.getChildren().add(parent);
            }
        }
        TTVShowSets.setShowRoot(false);
        TTVShowSets.setRoot(root);
    }

    private void printSetsTree(Product head, TreeItem<Product> father) throws SQLException {
        List<Product> listOfChildren = DBConnection.getInstance().getProductsChildrenByProductID(head);
        for (Product aListOfChildren : listOfChildren) {
            aListOfChildren.setIsChild(true);
            aListOfChildren.setSelected(null);
            TreeItem<Product> child = new TreeItem<>(aListOfChildren);
            father.getChildren().add(child);
            Product childProduct = aListOfChildren;
            printSetsTree(childProduct, child);
        }
    }

    @FXML
    private void serachProduct(KeyEvent event) throws SQLException {
        TTVShowSets.setVisible(true);
        TTVShowSets.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if (keycode == KeyCode.BACK_SPACE && search.length() > 0) {
            search = search.substring(0, search.length() - 1);
        } else search += event.getText();



        TTVShowSets.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        if (listHeaders == null) return;
        for (Product listHeader : listHeaders) {
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = null;
            try {
                childs = DBConnection.getInstance().getAllProductsByProductTypeID(listHeader.getProducttypeID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (Product child : childs) {
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                try {
                    printSetsTree(child, cache);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())) {
                root.getChildren().add(parent);
            }
        }
        TTVShowSets.setShowRoot(false);
        TTVShowSets.setRoot(root);
    }
    private List<Product> GetListHeaders(List<ProductType> productTypes) throws SQLException {
        List<Product> listHeaders = new ArrayList<>();
        for (ProductType productType : productTypes) {
            Product p = DBConnection.getInstance().getProductPerProductTypeID(productType.getProductTypeID());
            if (p != null) {
                p.setIsChild(false);
                p.setSelected(null);
                if (DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(productType.getProductTypeID()).size() > 0) {
                    listHeaders.add(p);
                }
            }
        }
        return listHeaders;
    }
    @FXML
    public void refreshTTV() {
        TTVShowSets.refresh();
    }
}
