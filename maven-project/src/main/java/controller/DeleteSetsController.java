package controller;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import loginPackage.DBConnection;
import model.Product;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DeleteSetsController implements Initializable{
    @FXML
    JFXButton btDelete;
    @FXML
    JFXCheckBox cbdDeleteSubsets;
    @FXML
    TreeTableView<Product> TTVSets;
    @FXML
    TreeTableColumn<Object, Object> tcSelect;
    @FXML
    TreeTableColumn tcName;
    @FXML
    TreeTableColumn tcDescription;
    @FXML
    JFXTextField tfSearch;
    List<Product> setHeaders;
    @Override
    public void initialize(URL location, ResourceBundle resources) {/*
        try {
            DBConnection.getInstance().InsertTestDatas();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        try {
            setHeaders = DBConnection.getInstance().getAllSetHaders();
            for (Product setHeader : setHeaders) {
                CheckBox cb = new CheckBox();
                cb.setSelected(false);
                setHeader.setSelected(cb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tcName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        tcDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        tcSelect.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));
        try {
            refreshTTV(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private  void serachProduct(KeyEvent event) throws SQLException {
        TTVSets.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if(keycode == KeyCode.BACK_SPACE && search.length() > 0){
            search = search.substring(0,search.length()-1);
        }
        else search += event.getText();

        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element
        List<Product> listHeaders = DBConnection.getInstance().getAllSetHaders();
        for (Product listHeader : listHeaders){
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getAllChildsOfProduct(listHeader.getProductID());
            for(Product child : childs){
                child.setSelected(null);
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                printSetsTree(child, cache);
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())){
                root.getChildren().add(parent);
            }
        }
        TTVSets.setShowRoot(false);
        TTVSets.setRoot(root);
    }
    @FXML
    public void deleteSelectedSets() throws SQLException {
        if (!cbdDeleteSubsets.isSelected()){
            for (Product setHeader : setHeaders) {
                if (setHeader.getSelected().isSelected()) {
                    DBConnection.getInstance().setSuperProductNrNullBySuperProductNR(setHeader.getProductID());
                    DBConnection.getInstance().deleteProduct(setHeader.getProductID());
                    if (DBConnection.getInstance().getAllProductsByProductTypeID(setHeader.getProductID()).size() == 0) {
                        DBConnection.getInstance().deleteProductTypeByID(setHeader.getProducttypeID());
                    }
                }
            }
        }
        else{
            for (Product setHeader : setHeaders)
                if (setHeader.getSelected().isSelected()) {
                    DeleteSetsWithUnderSets(setHeader);
                }
        }
    }

    private void DeleteSetsWithUnderSets(Product head) throws SQLException {
        List<Product> listOfChildren = DBConnection.getInstance().getProductsChildrenByProductID(head);
        if (listOfChildren.size() != 0){
            DBConnection.getInstance().setSuperProductNrNullBySuperProductNR(head.getProductID());
            DBConnection.getInstance().deleteProduct(head.getProductID());
            if (DBConnection.getInstance().getAllProductsByProductTypeID(head.getProductID()).size() == 0){
                DBConnection.getInstance().deleteProductTypeByID(head.getProducttypeID());
            }
        }
        for (Product childProduct : listOfChildren) {
            DeleteSetsWithUnderSets(childProduct);
        }
    }

    private void  refreshTTV(Integer i) throws SQLException {
        TTVSets.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        if (setHeaders == null) return;
        for (Product setHeader : setHeaders){
            TreeItem<Product> parent = new TreeItem<>(setHeader);
            List<Product> childs = DBConnection.getInstance().getAllChildsOfProduct(setHeader.getProductID());
            if (childs == null) break;
            for (Product child: childs){
                child.setSelected(null);
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                printSetsTree(child, cache);
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0){
                root.getChildren().add(parent);
            }
        }
        TTVSets.setShowRoot(false);
        TTVSets.setRoot(root);
    }
    public void printSetsTree(Product head, TreeItem<Product> father) throws SQLException {
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
}
