package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import loginPackage.DBConnection;
import loginPackage.Main;
import model.Product;
import model.ProductType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;


public class SetsManagerController implements Initializable {
    @FXML
    JFXTextField tfSearch, tfSetName, taDescription, tfEanCode;
    @FXML
    JFXButton btCreateSet, btAddToBoardTTV, btAddToBoardEancode;
    @FXML
    private TreeTableView<Product> TTVProductToChoose;
    @FXML
    private TreeTableView<Product> TTVfinalProductsForSet;
    private List<Product> products;
    private List<Product> finalSelectedProducts;
    @FXML
    private TreeTableColumn<Product, String> prodNameCol, descCol, tcFinalProductsForSetProductName, tcFinalProductsForSetProductDescription;
    @FXML
    private TreeTableColumn<Product, Integer> tcFinalProductsForSetProductID;
    @FXML
    private TreeTableColumn selectCol;
    @FXML
    Label LyourSet;


    @FXML
    private void createNewSet() throws SQLException {
        if (tfSetName.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Enter a Setname!");
            alert.showAndWait();
            return;
        }
        int productTypeID = DBConnection.getInstance().createNewSetHeaderProductType(tfSetName.getText(), taDescription.getText()); //remove comment
        int productHeaderId = DBConnection.getInstance().createNewSetHeaderProduct(productTypeID);
        for (int i = 0; i < finalSelectedProducts.size(); i++){
            DBConnection.getInstance().addProductToSetHeader(finalSelectedProducts.get(i).getProductID(),productHeaderId);
        }
    }
    @FXML
    private  void addSelectedProductsFromTTV() throws SQLException {
        TreeItem<Product> rootTTVFinalProductsForSet = new TreeItem<>(new Product(DBConnection.getInstance().getLastProductID()
                ,null,null,null, null, null));
        TTVfinalProductsForSet.setVisible(true);
        LyourSet.setVisible(true);
        for (Product product : products){
            if (IsProductInSelectedList(product.getProductID()) || product.getSelected() != null && product.getSelected().isSelected()){
                finalSelectedProducts.add(product);
                TreeItem<Product> cache = new TreeItem<>(product);
                printSetsTree(product, cache);
                rootTTVFinalProductsForSet.getChildren().add(cache);
                product.setIsChild(false);
            }
        }
        if (rootTTVFinalProductsForSet.getChildren().size() > 1){
            btCreateSet.setVisible(true);
        }
        TTVfinalProductsForSet.setShowRoot(false);
        TTVfinalProductsForSet.setRoot(rootTTVFinalProductsForSet);
        refreshTTV(1);
    }
    @FXML
    private  void serachProduct(KeyEvent event) throws SQLException {
        TTVProductToChoose.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if(keycode == KeyCode.BACK_SPACE && search.length() > 0){
            search = search.substring(0,search.length()-1);
        }
        else search += event.getText();

        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element
        List<Product> listHeaders = GetListHeaders(DBConnection.getInstance().getAllProductTypes());
        for (Product listHeader : listHeaders){
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(listHeader.getProducttypeID());
            for(Product child : childs){
                if (IsProductSelected(child.getProductID())){
                    CheckBox cb = new CheckBox();
                    cb.setSelected(true);
                    child.setSelected(cb);
                }
                removeProduct(child.getProductID());
                products.add(child);
                if (!IsProductInSelectedList(child.getProductID())){
                    child.setIsChild(true);
                    TreeItem<Product> cache = new TreeItem<>(child);
                    printSetsTree(child, cache);
                    parent.getChildren().add(cache);
                }
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())){
                root.getChildren().add(parent);
            }
        }
        TTVProductToChoose.setShowRoot(false);
        TTVProductToChoose.setRoot(root);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TTVfinalProductsForSet.setRoot(null);
        finalSelectedProducts = new ArrayList<>();
        products = new ArrayList<>();

        prodNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        selectCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));

//        List<Product> productsNotInASet = DBConnection.getInstance().getAllProductsWhichAreNotInASet();
/*
        try {
            DBConnection.getInstance().deleteAllDatas();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            DBConnection.getInstance().InsertTestDatas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
*/

        try {
            refreshTTV(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tcFinalProductsForSetProductName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        tcFinalProductsForSetProductDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        tcFinalProductsForSetProductID.setCellValueFactory(new TreeItemPropertyValueFactory<>("productID"));
    }
    private void  refreshTTV(int i) throws SQLException {
        TTVProductToChoose.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        List<Product> listHeaders = GetListHeaders(DBConnection.getInstance().getAllProductTypes());
        if (listHeaders == null) return;

        for (Product listHeader : listHeaders){
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(listHeader.getProducttypeID());
            for (Product child: childs){
                if (i == 0)products.add(child);
                if (i == 1){
                    removeProduct(child.getProductID());
                    products.add(child);
                }
                if (!IsProductInSelectedList(child.getProductID())){
                    child.setIsChild(true);
                    TreeItem<Product> cache = new TreeItem<>(child);
                    printSetsTree(child, cache);
                    parent.getChildren().add(cache);
                }
            }
            if (parent.getChildren().size() > 0){
                root.getChildren().add(parent);
            }
        }
        TTVProductToChoose.setShowRoot(false);
        TTVProductToChoose.setRoot(root);
    }

    private boolean IsProductSelected(Integer productID) {
        for (Product product : products){
            if (product.getProductID() == productID && product.getSelected().isSelected()){
                return true;
            }
        }
        return false;
    }

    private List<Product> GetListHeaders(List<ProductType> productTypes) throws SQLException {
        List<Product> listHeaders = new ArrayList<>();
        for(ProductType productType : productTypes){
            Product p = DBConnection.getInstance().getProductPerProductTypeID(productType.getProductTypeID());
            if (p == null) break;
            p.setIsChild(false);
            p.setSelected(null);
            if (DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(productType.getProductTypeID()).size()>0){
                listHeaders.add(p);
            }
        }
        return  listHeaders;
    }

    private void removeProduct(Integer productID) {
        for (int i = 0; i < products.size(); i++){
            if (products.get(i).getProductID() == productID){
                products.remove(i);
            }
        }
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
    private boolean IsProductInSelectedList(Integer productID) {
        for (Product product : finalSelectedProducts){
            if (product.getProductID() == productID){
                return  true;
            }
        }
        return  false;
    }
    @FXML
    public void refreshTTV(){
        TTVProductToChoose.refresh();
        TTVfinalProductsForSet.refresh();
    }
    @FXML
    public void addProductToListByEanCode() throws SQLException {
        String code = tfEanCode.getText();
        Product scannedProduct = DBConnection.getInstance().getProductByProductEanNotInASet(code);
        if (scannedProduct == null) return;
        for (int i = 0; i < products.size(); i++){
            if (scannedProduct.getProductID() == products.get(i).getProductID()){

                CheckBox cb = new CheckBox();
                cb.setSelected(true);
                products.get(i).setSelected(cb);
            }
        }
        this.addSelectedProductsFromTTV();
        /*
        TreeItem<Product> rootTTVFinalProductsForSet = new TreeItem<>(new Product(DBConnection.getInstance().getLastProductID()
                ,null,null,null, null, null));
        TTVfinalProductsForSet.setVisible(true);
        LyourSet.setVisible(true);
        for (Product product : products){
            if (IsProductInSelectedList(product.getProductID()) || product.getSelected() != null && product.getSelected().isSelected()||product.getProductEan().equals(scannedProduct.getProductEan())){
                finalSelectedProducts.add(product);
                TreeItem<Product> cache = new TreeItem<>(product);
                printSetsTree(product, cache);
                rootTTVFinalProductsForSet.getChildren().add(cache);
                product.setIsChild(false);
                break;
            }
        }
        if (rootTTVFinalProductsForSet.getChildren().size() > 1){
            btCreateSet.setVisible(true);
        }
        TTVfinalProductsForSet.setShowRoot(false);
        TTVfinalProductsForSet.setRoot(rootTTVFinalProductsForSet);
        refreshTTV(1);*/
        tfEanCode.clear();
    }

    @FXML
    public void eanCodeEntered(){
        System.out.println("Hallo Welt");
    }
}
