package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

public class ShowSetsController implements Initializable{
    @FXML
    private TreeTableView TTVShowSets;
    @FXML
    private TreeTableColumn tcShowSetsProductName, tcShowSetsDescription;
    @FXML
    private TextField tfSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TTVShowSets.setVisible(true);
        tcShowSetsProductName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        tcShowSetsDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));

        List<Product> setHeaders = null;
        try {
            setHeaders = DBConnection.getInstance().getHighestSetHeaders();
        } catch (SQLException e) {
            e.printStackTrace();
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
    @FXML
    private  void serachProduct(KeyEvent event) throws SQLException {
        TTVShowSets.setVisible(true);
        TTVShowSets.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if(keycode == KeyCode.BACK_SPACE && search.length() > 0){
            search = search.substring(0,search.length()-1);
        }
        else search += event.getText();

        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element
        List<Product> listHeaders = DBConnection.getInstance().getHighestSetHeaders();
        for (Product listHeader : listHeaders){
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(listHeader.getProducttypeID());
            for(Product child : childs){
                if (listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())){
                    child.setIsChild(true);
                    TreeItem<Product> cache = new TreeItem<>(child);
                    printSetsTree(child, cache);
                    parent.getChildren().add(cache);
                }
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())){
                root.getChildren().add(parent);
            }
            else TTVShowSets.setVisible(false);
        }
        TTVShowSets.setShowRoot(false);
        TTVShowSets.setRoot(root);
    }
    @FXML
    public void refreshTTV(){
        TTVShowSets.refresh();
    }
}
