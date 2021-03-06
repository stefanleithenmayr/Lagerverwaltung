package controller;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXCheckBox;
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
import model.BarcodesToPdfGenerator;
import model.ErrorMessageUtils;
import model.Product;
import model.ProductType;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class ExportDatasController implements Initializable {
    @FXML
    TreeTableView<Product> TTVProductToChoose;
    @FXML
    JFXTextField tfSearch;
    @FXML
    private TreeTableColumn<Product, String> prodNameCol, descCol;
    @FXML
    private TreeTableColumn<Object, Object> selectCol;
    @FXML
    private Rectangle errorRec;
    @FXML
    private Text errorTxt;
    @FXML
    private JFXCheckBox cbSelectAll;

    List<Product> products;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            products = DBConnection.getInstance().getAllProductsList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        prodNameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        descCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        selectCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));

        try {
            refreshTTV();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exportDatas() throws SQLException, FileNotFoundException, DocumentException {
        List<Product> selectedProducts = new ArrayList<>();
        for (Product product : products) {
            CheckBox c = product.getSelected();
            if (product.getSelected() != null && product.getSelected().isSelected()) {
                selectedProducts.add(product);

            }
        }

        if (selectedProducts.size() == 0) {
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("No products selected", errorRec, errorTxt);
            return;
        }

        BarcodesToPdfGenerator.generateBarcoeds(selectedProducts);
        errorRec.setFill(Color.web("#00802b"));
        errorRec.setStroke(Color.web("#00802b"));
        ErrorMessageUtils.showErrorMessage("PDF Succesfully created", errorRec, errorTxt);
    }

    @FXML
    private void searchProduct(KeyEvent event) throws SQLException {
        TTVProductToChoose.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if (keycode == KeyCode.BACK_SPACE && search.length() > 0) {
            search = search.substring(0, search.length() - 1);
        } else search += event.getText();

        TTVProductToChoose.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element
        List<Product> listHeaders = getListHeaders(DBConnection.getInstance().getAllProductTypes());
        if (listHeaders == null) return;

        for (Product listHeader : listHeaders){
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getAllProductsByProductTypeID(listHeader.getProducttypeID());
            for (Product child : childs) {
                if (getSelectedByID(child.getProductID())) {
                    CheckBox cb = new CheckBox();
                    cb.setSelected(true);
                    child.setSelected(cb);
                }
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                printSetsTree(child, cache);
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())) {
                root.getChildren().add(parent);
            }
        }
        TTVProductToChoose.setShowRoot(false);
        TTVProductToChoose.setRoot(root);
    }

    private List<Product> getListHeaders(List<ProductType> productTypes) throws SQLException {
        List<Product> listHeaders = new ArrayList<>();
        for (ProductType productType : productTypes) {
            Product p = DBConnection.getInstance().getProductPerProductTypeID(productType.getProductTypeID());
            if (p == null) break;
            p.setIsChild(false);
            p.setSelected(null);

            listHeaders.add(p);
        }
        return listHeaders;
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

    private void refreshTTV() throws SQLException {
        TTVProductToChoose.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element
        List<Product> listHeaders = getListHeaders(DBConnection.getInstance().getAllProductTypes());
        if (listHeaders == null) return;

        for (Product listHeader : listHeaders) {
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getAllProductsByProductTypeID(listHeader.getProducttypeID());
            for (Product child : childs) {
                if (getSelectedByID(child.getProductID())) {
                    CheckBox cb = new CheckBox();
                    cb.setSelected(true);
                    child.setSelected(cb);
                }
                removeProductByProductID(child.getProductID());
                products.add(child);
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                printSetsTree(child, cache);
                parent.getChildren().add(cache);
            }

            root.getChildren().add(parent);
            if (parent.getChildren().size() > 0) {
            }
        }
        TTVProductToChoose.setShowRoot(false);
        TTVProductToChoose.setRoot(root);
    }

    private void removeProductByProductID(Integer productID) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductID().equals(productID)) {
                products.remove(i);
            }
        }
    }

    private boolean getSelectedByID(Integer productID) {
        for (Product product : products) {
            if (product.getProductID().equals(productID) && product.getSelected() != null && product.getSelected().isSelected()) {
                return true;
            }
        }
        return false;
    }

    @FXML
    public void selectAll() throws SQLException {
        if (!tfSearch.getText().equals("")){
            tfSearch.clear();
            refreshTTV();

        }
        for (Product product : products) {
            CheckBox cb = new CheckBox();
            cb.setSelected(cbSelectAll.isSelected());
            product.setSelected(cb);
        }
        TTVProductToChoose.refresh();
    }

    @FXML
    public void refresh() {
        TTVProductToChoose.refresh();
    }
}