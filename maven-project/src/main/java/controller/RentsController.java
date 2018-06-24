package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import loginPackage.DBConnection;
import model.Product;
import model.ProductType;
import model.Rent;

import javax.xml.bind.annotation.XmlAnyAttribute;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RentsController implements Initializable{

    @FXML
    private TableView<Rent> tableViewRents;
    @FXML
    private TableColumn<?,?> userNameCol, fromCol, untilCol;
    @FXML
    private TreeTableView<Product> TTVProductToChoose;

    private ObservableList<Rent> rentList;
    private List<Product> products, finalSelectedProducts;

    @FXML
    private TreeTableColumn<Product, String> prodNameCol, descCol;
    @FXML
    private TreeTableColumn selectCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loadAllRents();

        tableViewRents.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                if (tableViewRents.getSelectionModel().getSelectedItem() != null){
                    try {
                        refreshTTV(0, null, tableViewRents.getSelectionModel().getSelectedItem());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        userNameCol.setCellValueFactory(
                new PropertyValueFactory<>("UserName"));

        fromCol.setCellValueFactory(
                new PropertyValueFactory<>("From"));

        untilCol.setCellValueFactory(
                new PropertyValueFactory<>("Until"));

        fromCol.setMinWidth(200);
        untilCol.setMinWidth(200);
        userNameCol.setMinWidth(200);

        products = new ArrayList<>();
        finalSelectedProducts = new ArrayList<>();

        prodNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        selectCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));


    }

    private List<Product> GetListHeaders(List<ProductType> productTypes) throws SQLException {
        if (productTypes == null || productTypes.isEmpty()){
            return null;
        }
        List<Product> listHeaders = new ArrayList<>();
        for(ProductType productType : productTypes){
            Product p = DBConnection.getInstance().getProductPerProductTypeID(productType.getProductTypeID());
            if(p != null){
                p.setIsChild(false);
                p.setSelected(null);
                if (DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(productType.getProductTypeID()).size()>0){
                    listHeaders.add(p);
                }
            }

        }
        return listHeaders;
    }

    private void removeProduct(Integer productID) {
        for (int i = 0; i < products.size(); i++){
            if (Objects.equals(products.get(i).getProductID(), productID)){
                products.remove(i);
            }
        }
    }

    public void printSetsTree(List<Product> selectProducts, Product head, TreeItem<Product> father, Rent rent) throws SQLException {
        List<Product> listOfChildren = DBConnection.getInstance().getProductsChildrenByProductID(head);
        if (listOfChildren.isEmpty()) {
            products.remove(head);
            return;
        }
        if (selectProducts != null) {
            for (Product child : listOfChildren) {
                for (Product selectedProduct : selectProducts) {
                    if (child.getProductEan().equals(selectedProduct.getProductEan())) {
                        child.getSelected().setSelected(true);
                    }
                }
            }
        }

        for (Product p : listOfChildren) {
            products.add(p);
        }

        for (int i = 0; i < listOfChildren.size(); i++) {
            boolean isProductRented = DBConnection.getInstance().isProductByThisRent(listOfChildren.get(i), rent);
            if (listOfChildren.get(i).getStatusID() == 1 && isProductRented) {
                listOfChildren.get(i).setIsChild(true);
                TreeItem<Product> child = new TreeItem<>(listOfChildren.get(i));
                father.getChildren().add(child);
                Product childProduct = listOfChildren.get(i);

                if (selectProducts != null) {
                    for (Product selectedProduct : selectProducts) {
                        if (childProduct.getProductEan().equals(selectedProduct.getProductEan())) {
                            childProduct.getSelected().setSelected(true);
                        }
                    }
                }

                isProductRented = DBConnection.getInstance().isProductByThisRent(childProduct, rent);
                if (childProduct.getStatusID() == 1 && isProductRented) {
                    products.add(childProduct);
                }
                printSetsTree(selectProducts, childProduct, child, rent);
            }
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

    private void refreshTTV(int i, List<Product> selectProducts, Rent rent) throws SQLException {
        TTVProductToChoose.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        List<Product> listHeaders = GetListHeaders(DBConnection.getInstance().getAllProductTypes());
        if (listHeaders == null || listHeaders.isEmpty()){
            return;
        }
        for (Product listHeader : listHeaders){
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(listHeader.getProducttypeID());

            if (selectProducts != null){
                for (Product child : childs){
                    for (Product selectedProduct : selectProducts){
                        if (child.getProductEan().equals(selectedProduct.getProductEan())){
                            child.getSelected().setSelected(true);
                        }
                    }
                }
            }

            for (Product child: childs){
                boolean isProductRented = DBConnection.getInstance().isProductByThisRent(child, rent);
                if (child.getStatusID() == 1 && isProductRented) {
                    if (i == 0) products.add(child);
                    if (i == 1) {
                        removeProduct(child.getProductID());
                        if (selectProducts != null){
                            for (Product selectedProduct : selectProducts){
                                if (child.getProductEan().equals(selectedProduct.getProductEan())){
                                    child.getSelected().setSelected(true);
                                }

                            }
                        }
                        products.add(child);
                    }
                    if (!IsProductInSelectedList(child.getProductID())) {
                        if (selectProducts != null){
                            for (Product selectedProduct : selectProducts){
                                if (child.getProductEan().equals(selectedProduct.getProductEan())){
                                    child.getSelected().setSelected(true);
                                }

                            }
                        }
                        products.add(child);
                        child.setIsChild(true);
                        TreeItem<Product> cache = new TreeItem<>(child);
                        printSetsTree(selectProducts, child, cache, rent);
                        parent.getChildren().add(cache);
                    }
                }
            }
            if (parent.getChildren().size() > 0){
                root.getChildren().add(parent);
            }
        }
        TTVProductToChoose.setShowRoot(false);
        TTVProductToChoose.setRoot(root);
    }

    public List<Product> getSelectedItems(){
        List<Product> selectedProducts = new ArrayList<>();
        for (Product product:
                products) {
            if (product.getSelected().isSelected()){
                selectedProducts.add(product);
            }
        }
        selectedProducts = selectedProducts.stream().distinct().collect(Collectors.toList());
        return selectedProducts;
    }
    //TestComment asdfk
    //Submetho
    private void loadAllRents() {
        rentList = null;
        try {
            rentList = FXCollections.observableArrayList(DBConnection.getInstance().getAllRents());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewRents.setItems(rentList);
    }

    @FXML
    private void returnRentedItems() throws SQLException {
        //Product searchedProduct = DBConnection.getInstance().getProductPerEan(eanCodeTF.getText());
        List<Product> selectedProducts = new ArrayList<>();
        for (Product p : products){
            if (p.getSelected().isSelected()){
                selectedProducts.add(p);
            }
        }
        selectedProducts = selectedProducts.stream().distinct().collect(Collectors.toList());
        for (Product p : selectedProducts){
            DBConnection.getInstance().unrentAllProducts(p);
        }
/*
        selectedProducts.add(searchedProduct);
        selectedProducts = selectedProducts.stream().distinct().collect(Collectors.toList());
        for (Product p : products){
            System.out.println(p.getProductID());
        }
        refreshTTV(0, selectedProducts);*/
    }
}