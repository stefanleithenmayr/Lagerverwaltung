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

public class DeleteItemController implements Initializable{
    @FXML
    TreeTableView<Product> TTVShowProducts;
    @FXML
    TreeTableColumn tcProductName, tcDescription, tcSelect;
    @FXML
    JFXTextField tfSearch;
    @FXML
    Rectangle errorRec;
    @FXML
    Text errorTxt;
    private List<Product> products;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        TTVShowProducts.setRoot(null);
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
        boolean deleted = false;
        for (int i = 0; i < products.size(); i++){
            if (products.get(i).getSelected().isSelected()){
                DBConnection.getInstance().deleteProduct(products.get(i).getProductID());
                if (DBConnection.getInstance().getAllProductsByProductTypeID(products.get(i).getProducttypeID()).size() == 0){
                    DBConnection.getInstance().deleteProductTypeByID(products.get(i).getProducttypeID());
                }
                deleted = true;
            }
        }
        if (deleted){
            errorRec.setFill(Color.web("#00802b"));
            errorRec.setStroke(Color.web("#00802b"));
            ErrorMessageUtils.showErrorMessage("Successfully deleted", errorRec, errorTxt);
            refreshTTV(1);
        }
        else{
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("No products selected", errorRec, errorTxt);
        }
    }
    @FXML
    public void refresh(){TTVShowProducts.refresh();}
}
