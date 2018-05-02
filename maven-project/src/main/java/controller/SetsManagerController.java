package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.LinearGradient;
import loginPackage.DBConnection;
import model.Product;
import model.ProductType;
import sun.security.pkcs11.Secmod;

import javax.sound.sampled.Line;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SetsManagerController implements Initializable{
    @FXML
    JFXTextField tfSearch, tfSetName, taDescription, tfEanCode;
    @FXML
    JFXButton btCreateSet, btAddToBoardTTV, btAddToBoardEancode;
    @FXML
    private TreeTableView<ProductType> TTVProductTypes;
    private List<ProductType> productTypeList;
    @FXML
    private TreeTableColumn<Product, String> prodNameCol, descCol ;
    @FXML
    private TreeTableColumn<Product, Integer> totalProdCol;
    @FXML
    private TreeTableColumn selectCol;
    @FXML
    private TableView<Product> TVfinalProductsForSet;
    @FXML
    private TableColumn<Product, String> tcProductName;
    @FXML
    private TableColumn<Product, String> tcProductID;
    ObservableList<Product> selectedProducts;
    List<Product> selectedProductsCache;
    @FXML
    Label LyourSet;
    @FXML
    TableView<Product> tvShowSets;
    @FXML
    TableColumn<Product, String> tcShowProductsName;
    @FXML
    TableColumn<Product, Integer> tcShowProductsID;

    @FXML
    private void createNewSet() throws SQLException {
        if (tfSetName.getText().equals("")){
            return;
        }
        int productTypeID = DBConnection.getInstance().createNewSetHeaderProductType(tfSetName.getText(), taDescription.getText()); //remove comment
        int productHeaderId = DBConnection.getInstance().createNewSetHeaderProduct(productTypeID);
        for (int i = 0; i < selectedProducts.size(); i++){
            DBConnection.getInstance().addProductToSetHeader(selectedProducts.get(i).getProductID(),productHeaderId);
        }
    }

    @FXML
    private  void addSelectedProductsFromTTV(){
        TVfinalProductsForSet.setVisible(true);
        LyourSet.setVisible(true);
        for (int i = 0; i < productTypeList.size(); i++){
            if (productTypeList.get(i).getSelected() != null && productTypeList.get(i).getSelected().isSelected()){
                try{
                    int productID = Integer.parseInt(productTypeList.get(i).getDescription());
                    int productTypeID = DBConnection.getInstance().getProductTypeIdByProductID(productID);
                    String productEan = DBConnection.getInstance().getProductEanByProductID(productID);
                    int superProductID = DBConnection.getInstance().getSuperProductIDByProductID(productID);
                    int statusID = DBConnection.getInstance().getProductStatusByProductID(productID);
                    Product p = new Product(productID, productTypeID, "", productEan, superProductID, statusID);
                    if (productTypeID != -1 && statusID != -1 && superProductID != -1 && !ContainsProductInList(p)){
                        selectedProductsCache.add(p);
                    }
                }catch(Exception e){

                }
            }
        }
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productTypeName"));
        tcProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        selectedProducts = FXCollections.observableArrayList(selectedProductsCache);
        TVfinalProductsForSet.setItems(selectedProducts);
        if (selectedProducts.size() > 1){
            btCreateSet.setVisible(true);
        }
        refreshTTV(1);
    }

    private boolean ContainsProductInList(Product p) {
        for (int i = 0; i < selectedProductsCache.size(); i++){
            if (selectedProductsCache.get(i).getProductID() == p.getProductID()){
                return true;
            }
        }
        return false;
    }

    @FXML
    private  void serachProduct(KeyEvent event) throws SQLException {
        List<ProductType> cache = new ArrayList<>(productTypeList);
        TTVProductTypes.setRoot(null);

        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if(keycode == KeyCode.BACK_SPACE && search.length() > 0){
            search = search.substring(0,search.length()-1);
        }
        else search += event.getText();

        List<ProductType> productTypesCache = new ArrayList<>(productTypeList);
        TreeItem<ProductType> root = new TreeItem<>(new ProductType(-1, "", "", null)); //empty root element

        for (int i = 0; i < productTypesCache.size(); i++){
            if (productTypesCache.get(i).getSelected() == null){
                TreeItem<ProductType> cache1 = new TreeItem<>(productTypesCache.get(i));
                List<Product> products = DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(cache1.getValue().getProductTypeID());
                for (int k = 0; k < products.size(); k++){
                    if (!IsProductSelected(products.get(k).getProductID())) {
                        ProductType p = new ProductType(products.get(k).getProductID(), cache1.getValue().getTypeName(), Integer.toString(products.get(k).getProductID()), getChoiceBoxByProductID(products.get(k).getProductID(), cache));
                        productTypeList.add(p);
                        TreeItem<ProductType> subCacheProduct = new TreeItem<>(p);
                        cache1.getChildren().add(subCacheProduct);
                    }
                }
                if (productTypesCache.get(i).getTypeName().toLowerCase().contains(search.toLowerCase()) && cache1.getChildren().size() != 0){
                    root.getChildren().add(cache1);
                }
            }
        }

        TTVProductTypes.setShowRoot(false);
        TTVProductTypes.setRoot(root);
    }

    private CheckBox getChoiceBoxByProductID(Integer productID, List<ProductType> cache) {
        for (int i = 0; i < cache.size(); i++){
            int id = -1;
            try{
                id =  Integer.parseInt(cache.get(i).getDescription());
            }catch(Exception e){

            }
            if (id == productID){
                return cache.get(i).getSelected();
            }
        }
        return  null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*try {
            DBConnection.getInstance().InsertTestDatas();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        selectedProductsCache = new ArrayList<>();
        prodNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("typeName"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        totalProdCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("totalProducts"));
        selectCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));
        try {
            productTypeList = DBConnection.getInstance().getAllProductTypes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshTTV(0);

        try {
            ShowAllRents(); //Methode wird später gelöscht
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ShowAllRents() throws SQLException {
        List<Product> setHeaders = DBConnection.getInstance().getHighestSetHeaders();
        for (int i = 0; i < setHeaders.size(); i++){
            Product product = setHeaders.get(i);
            List<Product> juniors = DBConnection.getInstance().getProductJuniorsByProductId(product);
            /*while(juniors.size() == 0){
                juniors = DBConnection.getInstance().getProductJuniorsByProductId(juniors.get(0));
            }*/

            System.out.println("Name: "+DBConnection.getInstance().getProductTypeNameByID(setHeaders.get(i).getProducttypeID())+"  ID: "+setHeaders.get(i).getProductID());
        }
    }

    private void  refreshTTV(int i){
        TTVProductTypes.setRoot(null);
        List<ProductType> productTypesCache = new ArrayList<>(productTypeList);
        TreeItem<ProductType> root = new TreeItem<>(new ProductType(-1, "", "", null)); //empty root element

        for (ProductType productType : productTypesCache) {
            if (productType.getSelected() == null){
                TreeItem<ProductType> cache = new TreeItem<>(productType);
                try {
                    List<Product> products = DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(cache.getValue().getProductTypeID());
                    for (Product product : products){
                        if (!IsProductSelected(product.getProductID())){
                            ProductType p = new ProductType(product.getProductID(), cache.getValue().getTypeName(), Integer.toString(product.getProductID()));
                            if (i == 0){
                                productTypeList.add(p);
                            }
                            else{
                                productTypeList.remove(product.getProductID());
                                productTypeList.add(p);
                            }
                            TreeItem<ProductType> subCacheProduct = new TreeItem<>(p);
                            cache.getChildren().add(subCacheProduct);
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (cache.getChildren().size() != 0){
                    root.getChildren().add(cache);
                }
            }
        }
        TTVProductTypes.setShowRoot(false);
        TTVProductTypes.setRoot(root);
    }


    private boolean IsProductSelected(Integer productID) {
        for (int i = 0; i < productTypeList.size(); i++){
            if (productTypeList.get(i).getProductTypeID() == productID && productTypeList.get(i).getSelected() != null && productTypeList.get(i).getSelected().isSelected()){
                return true;
            }
        }
        return  false;
    }
}
