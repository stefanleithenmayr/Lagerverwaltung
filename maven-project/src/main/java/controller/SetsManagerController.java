package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import loginPackage.DBConnection;
import model.ErrorMessageUtils;
import model.Product;
import model.ProductType;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SetsManagerController implements Initializable {

    @FXML
    private JFXTextField tfSearch, tfSetName, taDescription, tfEanCode;
    @FXML
    private JFXButton btCreateSet, btAddToBoardTTV, btAddToBoardEancode;
    @FXML
    private TreeTableView<Product> TTVProductToChoose;
    @FXML
    private TreeTableView<Product> TTVfinalProductsForSet;
    @FXML
    private TreeTableColumn<Product, String> prodNameCol, descCol, tcFinalProductsForSetProductName, tcFinalProductsForSetProductDescription;
    @FXML
    private TreeTableColumn<Product, Integer> tcFinalProductsForSetProductID;
    @FXML
    private TableView<ProductType> tvProductTypes;
    private ProductType selectedProductType;
    @FXML
    private TableColumn<ProductType, String> tcProducttypeName, tcProducttypeDescription;
    @FXML
    private TreeTableColumn selectCol;
    @FXML
    private JFXToggleButton toggleButton;
    @FXML
    private Rectangle errorRec;
    @FXML
    private Text errorTxt;
    @FXML
    private Label LyourSet;

    private List<Product> products;
    private List<Product> finalSelectedProducts;


    @FXML
    private void createNewSet() throws SQLException {
        int productTypeID;
        if (tfSetName.getText().equals("") && !toggleButton.isSelected()){
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("Please enter a setname", errorRec, errorTxt);
            return;
        }
        else if (toggleButton.isSelected()){
            if (selectedProductType == null){
                errorRec.setFill(Color.web("#f06060"));
                errorRec.setStroke(Color.web("#f06060"));
                ErrorMessageUtils.showErrorMessage("Please select a producttype", errorRec, errorTxt);
                return;
            }
            productTypeID = selectedProductType.getProductTypeID();
        }
        else {
            productTypeID = DBConnection.getInstance().createNewSetHeaderProductType(tfSetName.getText(), taDescription.getText()); //remove comment
        }

        int productHeaderId = DBConnection.getInstance().createNewSetHeaderProduct(productTypeID);
        for (Product finalSelectedProduct : finalSelectedProducts) {
            DBConnection.getInstance().addProductToSetHeader(finalSelectedProduct.getProductID(), productHeaderId);
        }
        this.Prepare(1);
        errorRec.setFill(Color.web("#00802b"));
        errorRec.setStroke(Color.web("#00802b"));
        ErrorMessageUtils.showErrorMessage("Successfully created", errorRec, errorTxt);
        tfSetName.clear();
        taDescription.clear();
        btCreateSet.setVisible(false);
    }

    @FXML
    private void addSelectedProductsFromTTV() throws SQLException {
        TreeItem<Product> rootTTVFinalProductsForSet = new TreeItem<>(new Product(DBConnection.getInstance().getLastProductID()
                , null, null, null, null, null));
        TTVfinalProductsForSet.setVisible(true);
        LyourSet.setVisible(true);
        for (Product product : products) {
            if (IsProductInSelectedList(product.getProductID()) || product.getSelected() != null && product.getSelected().isSelected()) {
                finalSelectedProducts.add(product);
                TreeItem<Product> cache = new TreeItem<>(product);
                printSetsTree(product, cache);
                rootTTVFinalProductsForSet.getChildren().add(cache);
                product.setIsChild(false);
            }
        }
        if (rootTTVFinalProductsForSet.getChildren().size() > 1) {
            btCreateSet.setVisible(true);
        }
        TTVfinalProductsForSet.setShowRoot(false);
        TTVfinalProductsForSet.setRoot(rootTTVFinalProductsForSet);
        refreshTTV(1);
    }

    @FXML
    private void serachProduct(KeyEvent event) throws SQLException {
        TTVProductToChoose.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if (keycode == KeyCode.BACK_SPACE && search.length() > 0) {
            search = search.substring(0, search.length() - 1);
        } else search += event.getText();

        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element
        List<Product> listHeaders = GetListHeaders(DBConnection.getInstance().getAllProductTypes());
        for (Product listHeader : listHeaders) {
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(listHeader.getProducttypeID());
            for (Product child : childs) {
                if (IsProductSelected(child.getProductID())) {
                    CheckBox cb = new CheckBox();
                    cb.setSelected(true);
                    child.setSelected(cb);
                }
                removeProduct(child.getProductID());
                products.add(child);
                if (!IsProductInSelectedList(child.getProductID())) {
                    child.setIsChild(true);
                    TreeItem<Product> cache = new TreeItem<>(child);
                    printSetsTree(child, cache);
                    parent.getChildren().add(cache);
                }
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())) {
                root.getChildren().add(parent);
            }
        }
        TTVProductToChoose.setShowRoot(false);
        TTVProductToChoose.setRoot(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Prepare(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Prepare(int i) throws SQLException {
        selectedProductType = null;
        LyourSet.setVisible(false);
        TTVfinalProductsForSet.setVisible(false);
        TTVfinalProductsForSet.setRoot(null);
        if (i == 0) {
            finalSelectedProducts = new ArrayList<>();
            products = new ArrayList<>();

            prodNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
            descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
            selectCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));
            tcFinalProductsForSetProductName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
            tcFinalProductsForSetProductDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
            tcFinalProductsForSetProductID.setCellValueFactory(new TreeItemPropertyValueFactory<>("productID"));

            tcProducttypeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
            tcProducttypeDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        }
        try {
            refreshTTV(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvProductTypes.setItems(FXCollections.observableArrayList(DBConnection.getInstance().getAllSetProductTypes()));
    }

    private void refreshTTV(Integer i) throws SQLException {
        TTVProductToChoose.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        List<Product> listHeaders = GetListHeaders(DBConnection.getInstance().getAllProductTypes());
        if (listHeaders == null) return;
        for (Product listHeader : listHeaders) {
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(listHeader.getProducttypeID());
            for (Product child : childs) {
                if (i.equals(0)) products.add(child);
                if (i.equals(1)) {
                    removeProduct(child.getProductID());
                    products.add(child);
                }
                if (!IsProductInSelectedList(child.getProductID())) {
                    child.setIsChild(true);
                    TreeItem<Product> cache = new TreeItem<>(child);
                    printSetsTree(child, cache);
                    parent.getChildren().add(cache);
                }
            }
            if (parent.getChildren().size() > 0) {
                root.getChildren().add(parent);
            }
        }
        TTVProductToChoose.setShowRoot(false);
        TTVProductToChoose.setRoot(root);
    }

    private boolean IsProductSelected(Integer productID) {
        for (Product product : products) {
            if (product.getProductID().equals(productID) && product.getSelected().isSelected()) {
                return true;
            }
        }
        return false;
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

    private void removeProduct(Integer productID) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductID().equals(productID)) {
                products.remove(i);
            }
        }
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

    private boolean IsProductInSelectedList(Integer productID) {
        for (Product product : finalSelectedProducts) {
            if (product.getProductID().equals(productID)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    public void refreshTTV() {
        TTVProductToChoose.refresh();
        TTVfinalProductsForSet.refresh();
    }

    @FXML
    public void eanEntered() throws SQLException {
        if (tfEanCode.getText().length() != 11) return;
        String code = tfEanCode.getText();
        Product scannedProduct = DBConnection.getInstance().getProductByProductEanNotInASet(code);
        if (scannedProduct == null) {
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("Scanned product is already in a Set or doesn´t exist", errorRec, errorTxt);
            tfEanCode.clear();
            return;
        }

        for (Product finalSelectedProduct : finalSelectedProducts) {
            if (finalSelectedProduct.getProductID().equals(scannedProduct.getProductID())) {
                errorRec.setFill(Color.web("#f06060"));
                errorRec.setStroke(Color.web("#f06060"));
                ErrorMessageUtils.showErrorMessage("Product is already scanned", errorRec, errorTxt);
                tfEanCode.clear();
                return;
            }
        }

        for (Product product : products) {
            if (scannedProduct.getProductID().equals(product.getProductID())) {
                CheckBox cb = new CheckBox();
                cb.setSelected(true);
                product.setSelected(cb);
            }
        }
        this.addSelectedProductsFromTTV();
        tfEanCode.clear();
    }
    @FXML
    public void tbOnAction(){
        if (toggleButton.isSelected()){
            toggleButton.setText("Insert into existing producttype");
            tfSetName.setVisible(false);
            taDescription.setVisible(false);
            tvProductTypes.setVisible(true);

        }
        else{
            toggleButton.setText("Insert new producttype");
            tfSetName.setVisible(true);
            taDescription.setVisible(true);
            tvProductTypes.setVisible(false);
        }
    }
    @FXML
    public void productTypeSelected(){
        selectedProductType = tvProductTypes.getSelectionModel().getSelectedItem();
    }
}