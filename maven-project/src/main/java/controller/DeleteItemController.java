package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class DeleteItemController implements Initializable {

    //FXML variables
    @FXML
    TreeTableView<Product> TTVShowProducts;
    @FXML
    TreeTableColumn<Object, Object> tcProductName;
    @FXML
    TreeTableColumn<Object, Object> tcDescription;
    @FXML
    TreeTableColumn<Object, Object> tcSelect;
    @FXML
    JFXTextField tfSearch;
    @FXML
    Rectangle errorRec;
    @FXML
    Text errorTxt;

    //Class intern variables
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

    private void refreshTTV(Integer i) throws SQLException {
        TTVShowProducts.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        List<Product> listHeaders = GetListHeaders(DBConnection.getInstance().getAllProductTypes());
        if (listHeaders == null) return;
        for (Product listHeader : listHeaders) {
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getallProductWhichAreNotRentet(listHeader.getProducttypeID());
            for (Product child : childs) {
                if (i.equals(0)) products.add(child);
                if (i.equals(1)) {
                    removeProduct(child.getProductID());
                    products.add(child);
                }
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                printSetsTree(child, cache);
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0) {
                root.getChildren().add(parent);
            }
        }
        TTVShowProducts.setShowRoot(false);
        TTVShowProducts.setRoot(root);
    }

    private List<Product> GetListHeaders(List<ProductType> productTypes) throws SQLException {
        List<Product> listHeaders = new ArrayList<>();
        for (ProductType productType : productTypes) {
            Product p = DBConnection.getInstance().getProductPerProductTypeID(productType.getProductTypeID());
            if (p == null) break;
            p.setIsChild(false);
            p.setSelected(null);
            if (DBConnection.getInstance().getAllChildsOfProduct(p.getProductID()).size() == 0) {
                listHeaders.add(p);
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

    @FXML
    private void serachProduct(KeyEvent event) throws SQLException {
        TTVShowProducts.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if (keycode == KeyCode.BACK_SPACE && search.length() > 0) {
            search = search.substring(0, search.length() - 1);
        } else search += event.getText();

        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        List<Product> listHeaders = GetListHeaders(DBConnection.getInstance().getAllProductTypes());
        for (Product listHeader : listHeaders) {
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getAllProductsByProductTypeID(listHeader.getProducttypeID());
            for (Product child : childs) {
                CheckBox cb = new CheckBox();
                child.setSelected(cb);
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                printSetsTree(child, cache);
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())) {
                root.getChildren().add(parent);
            }
        }
        TTVShowProducts.setShowRoot(false);
        TTVShowProducts.setRoot(root);
    }

    @FXML
    public void deleteSelectedProducts() throws SQLException {
        List<Product> deletedProducts = new ArrayList<>();
        Boolean deleted = false;
        Boolean cancled = false;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getSelected().isSelected()) {
                if (products.get(i).getSuperProductID() != null && products.get(i).getSuperProductID() != 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Attention");
                    alert.setHeaderText("Product " + products.get(i).getProductTypeName() + " is part of the set: '" + DBConnection.getInstance().getProductTypeNameByID(DBConnection.getInstance().getProductByProductID(products.get(i).getSuperProductID()).getProducttypeID()) + "'");
                    //DBConnection.getInstance().getProductTypeNameByID(DBConnection.getInstance().getProductTypeNameByID(DBConnection.getInstance().getProductByProductID(products.get(i).getSuperProductID()).getProducttypeID())))
                    alert.setContentText("Do you want do Cancel or to Delete");

                    ButtonType buttonTypeOne = new ButtonType("Delete");
                    ButtonType buttonTypeTwo = new ButtonType("Cancel");

                    alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne) {
                        DBConnection.getInstance().deleteProduct(products.get(i).getProductID());
                        if (DBConnection.getInstance().getAllProductsByProductTypeID(products.get(i).getProducttypeID()).size() == 0) {
                            DBConnection.getInstance().deleteProductTypeByID(products.get(i).getProducttypeID());
                        }
                        deleted = true;
                        deletedProducts.add(products.get(i));
                    } else {
                        cancled = true;
                    }
                } else {
                    DBConnection.getInstance().deleteProduct(products.get(i).getProductID());
                    if (DBConnection.getInstance().getAllProductsByProductTypeID(products.get(i).getProducttypeID()).size() == 0) {
                        DBConnection.getInstance().deleteProductTypeByID(products.get(i).getProducttypeID());
                    }
                    deleted = true;
                    deletedProducts.add(products.get(i));
                }
            }
        }
        for (Product p : deletedProducts) {
            products.remove(p);
        }
        if (cancled && !deleted) {
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("No products deleted - canceled", errorRec, errorTxt);
            return;
        } else if (deleted && cancled) {
            errorRec.setFill(Color.web("#00802b"));
            errorRec.setStroke(Color.web("#00802b"));
            ErrorMessageUtils.showErrorMessage("Some Products deleted", errorRec, errorTxt);
            refreshTTV(1);
        } else if (deleted && !cancled) {
            errorRec.setFill(Color.web("#00802b"));
            errorRec.setStroke(Color.web("#00802b"));
            ErrorMessageUtils.showErrorMessage("Successfully deleted", errorRec, errorTxt);
            refreshTTV(1);
        } else {
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("No products selected", errorRec, errorTxt);
        }
        refreshTTV(1);
    }

    @FXML
    public void refresh() {
        TTVShowProducts.refresh();
    }
}