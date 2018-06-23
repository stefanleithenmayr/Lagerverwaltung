package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import loginPackage.DBConnection;
import model.Product;
import model.ProductType;
import sun.security.pkcs11.Secmod;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ItemSelectionController implements Initializable {

    @FXML
    private TreeTableView<Product> TTVProductToChoose;
    private List<Product> products;
    private List<Product> finalSelectedProducts;
    @FXML
    private TreeTableColumn<Product, String> prodNameCol, descCol;
    @FXML
    private TreeTableColumn selectCol;
    @FXML
    private JFXTextField eanCodeTF;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*try {
            DBConnection.getInstance().InsertTestDatas();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        finalSelectedProducts = new ArrayList<>();
        products = new ArrayList<>();

        prodNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        selectCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));

        try {
            refreshTTV(0, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refresh(){
        TTVProductToChoose.refresh();
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

    public void printSetsTree(List<Product> selectProducts, Product head, TreeItem<Product> father) throws SQLException {
        List<Product> listOfChildren = DBConnection.getInstance().getProductsChildrenByProductID(head);
        if (listOfChildren.isEmpty()){
            products.remove(head);
            return;
        }
        if (selectProducts != null){
            for (Product child : listOfChildren){
                for (Product selectedProduct : selectProducts){
                    if (child.getProductEan().equals(selectedProduct.getProductEan())){
                        child.getSelected().setSelected(true);
                    }
                }
            }
        }

        for(int i = 0; i < listOfChildren.size(); i++) {
            boolean isProductRented = DBConnection.getInstance().isProductRented(listOfChildren.get(i));
            if (!isProductRented){
                listOfChildren.get(i).setIsChild(true);
                TreeItem<Product> child = new TreeItem<>(listOfChildren.get(i));
                father.getChildren().add(child);
                Product childProduct = listOfChildren.get(i);

                if (selectProducts != null){
                        for (Product selectedProduct : selectProducts){
                            if (childProduct.getProductEan().equals(selectedProduct.getProductEan())){
                                childProduct.getSelected().setSelected(true);
                            }
                        }
                }

                products.add(childProduct);
                printSetsTree(selectProducts,childProduct,child);
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

    private void refreshTTV(int i, List<Product> selectProducts) throws SQLException {
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
                boolean isProductRented = DBConnection.getInstance().isProductRented(child);
                if (!isProductRented) {
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
                        printSetsTree(selectProducts, child, cache);
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
        return selectedProducts;
    }

    @FXML
    private void markProductAsSelected() throws SQLException {
        Product searchedProduct = DBConnection.getInstance().getProductPerEan(eanCodeTF.getText());
        List<Product> selectedProducts = new ArrayList<>();
        for (Product p : products){
            if (p.getSelected().isSelected()){
                selectedProducts.add(p);
            }
        }

        selectedProducts.add(searchedProduct);
        for (Product p : selectedProducts){
            System.out.println(p.getProductID());
        }
        refreshTTV(0, selectedProducts);
    }
}