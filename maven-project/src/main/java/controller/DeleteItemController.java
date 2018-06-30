package controller;

import com.jfoenix.controls.JFXTextField;
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
import model.ProductType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DeleteItemController implements Initializable{
    @FXML
    TreeTableView<Product> TTVShowProducts;
    @FXML
    TreeTableColumn<Object, Object> tcProductName;
    @FXML
    TreeTableColumn tcDescription;
    @FXML
    TreeTableColumn tcSelect;
    @FXML
    JFXTextField tfSearch;
    private List<Product> products;
    @Override
    public void initialize(URL location, ResourceBundle resources) {/*
        try {
            DBConnection.getInstance().InsertTestDatas();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        TTVShowProducts.setRoot(null);
        products = new ArrayList<>();

        tcProductName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        tcDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        tcSelect.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));


        try {
            refreshTTV(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void  refreshTTV(Integer i) throws SQLException {
        TTVShowProducts.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        List<Product> listHeaders = GetListHeaders(DBConnection.getInstance().getAllProductTypes());
        if (listHeaders == null) return;
        for (Product listHeader : listHeaders){
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getallProductWhichAreNotRentet(listHeader.getProducttypeID());
            for (Product child: childs){
                if (i.equals(0))products.add(child);
                if (i.equals(1)){
                    removeProduct(child.getProductID());
                    products.add(child);
                }
                    child.setIsChild(true);
                    TreeItem<Product> cache = new TreeItem<>(child);
                    printSetsTree(child, cache);
                    parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0){
                root.getChildren().add(parent);
            }
        }
        TTVShowProducts.setShowRoot(false);
        TTVShowProducts.setRoot(root);
    }
    private List<Product> GetListHeaders(List<ProductType> productTypes) throws SQLException {
        List<Product> listHeaders = new ArrayList<>();
        for(ProductType productType : productTypes){
            Product p = DBConnection.getInstance().getProductPerProductTypeID(productType.getProductTypeID());
            if (p == null) break;
            p.setIsChild(false);
            p.setSelected(null);
            if (DBConnection.getInstance().getAllChildsOfProduct(p.getProductID()).size() == 0){
                listHeaders.add(p);
            }
        }
        return  listHeaders;
    }

    private void removeProduct(Integer productID) {
        for (int i = 0; i < products.size(); i++){
            if (products.get(i).getProductID().equals(productID) ){
                products.remove(i);
            }
        }
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

    @FXML
    private  void serachProduct(KeyEvent event) throws SQLException {
        TTVShowProducts.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if(keycode == KeyCode.BACK_SPACE && search.length() > 0){
            search = search.substring(0,search.length()-1);
        }
        else search += event.getText();
        System.out.println("A "+ search+" A");

        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        List<Product> listHeaders = GetListHeaders(DBConnection.getInstance().getAllProductTypes());
        for (Product listHeader : listHeaders){
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getAllProductsByProductTypeID(listHeader.getProducttypeID());
            for(Product child : childs){
                CheckBox cb = new CheckBox();
                child.setSelected(cb);
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                printSetsTree(child, cache);
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())){
                root.getChildren().add(parent);
            }
        }
        TTVShowProducts.setShowRoot(false);
        TTVShowProducts.setRoot(root);
    }
    @FXML
    public void deleteSelectedProducts() throws SQLException {
        for (Product product : products) {
            if (product.getSelected().isSelected()) {
                DBConnection.getInstance().deleteProduct(product.getProductID());
                if (DBConnection.getInstance().getAllProductsByProductTypeID(product.getProducttypeID()).size() == 0) {
                    DBConnection.getInstance().deleteProductTypeByID(product.getProducttypeID());
                }
            }
        }
        refreshTTV(1);
    }
    @FXML
    public void refresh(){TTVShowProducts.refresh();}
}
