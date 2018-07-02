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

public class DeleteSetsController implements Initializable {
    @FXML
    JFXButton btDelete;
    @FXML
    JFXCheckBox cbdDeleteSubsets;
    @FXML
    TreeTableView<Product> TTVSets;
    @FXML
    TreeTableColumn<Object, Object> tcSelect;
    @FXML
    TreeTableColumn<Object, Object> tcName;
    @FXML
    TreeTableColumn<Object, Object> tcDescription;
    @FXML
    Rectangle errorRec;
    @FXML
    Text errorTxt;
    @FXML
    JFXTextField tfSearch;
    private List<Product> listHeaders;
    private List<Product> childs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.prepare();
    }

    @FXML
    private void searchProduct(KeyEvent event) throws SQLException {
        TTVSets.setVisible(true);
        TTVSets.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if (keycode == KeyCode.BACK_SPACE && search.length() > 0) {
            search = search.substring(0, search.length() - 1);
        } else search += event.getText();

        TTVSets.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        if (listHeaders == null) return;
        for (Product listHeader : listHeaders) {
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = this.getChildsByListHeaderProductTypeID(listHeader.getProducttypeID());
            for (Product child : childs) {
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                try {
                    printSetsTree(child, cache);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())) {
                root.getChildren().add(parent);
            }
        }
        TTVSets.setShowRoot(false);
        TTVSets.setRoot(root);
    }

    private List<Product> getChildsByListHeaderProductTypeID(Integer producttypeID) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < childs.size(); i++){
            if (childs.get(i).getProducttypeID().equals(producttypeID)){
                products.add(childs.get(i));
            }
        }
        return  products;
    }

    @FXML
    public void deleteSelectedSets() throws SQLException {
        tfSearch.clear();
        boolean deleted = false;
        if (!cbdDeleteSubsets.isSelected()) {
            for (Product child : childs) {
                if (child.getSelected() != null && child.getSelected().isSelected()) {
                    DBConnection.getInstance().setSuperProductNrNullBySuperProductNR(child.getProductID());
                    DBConnection.getInstance().deleteProduct(child.getProductID());
                    if (DBConnection.getInstance().getAllProductsByProductTypeID(child.getProducttypeID()).size() == 0) {
                        DBConnection.getInstance().deleteProductTypeByID(child.getProducttypeID());
                    }
                    deleted = true;
                }
            }
        } else {
            for (Product child : childs) {
                if (child.getSelected().isSelected()) {
                    deleteSetsWithUnderSets(child);
                    deleted = true;
                }
            }
        }

        if (deleted) {
            errorRec.setFill(Color.web("#00802b"));
            errorRec.setStroke(Color.web("#00802b"));
            ErrorMessageUtils.showErrorMessage("Successfully deleted", errorRec, errorTxt);
        } else {
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("No sets selected", errorRec, errorTxt);
        }
        this.prepare();
    }

    private void prepare() {
        tfSearch.clear();

        tcName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        tcDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        tcSelect.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));
        childs = new ArrayList<>();
        try {
            refreshTTV(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSetsWithUnderSets(Product head) throws SQLException {
        List<Product> listOfChildren = DBConnection.getInstance().getProductsChildrenByProductID(head);
        if (listOfChildren.size() != 0) {
            DBConnection.getInstance().setSuperProductNrNullBySuperProductNR(head.getProductID());
            DBConnection.getInstance().deleteProduct(head.getProductID());
            if (DBConnection.getInstance().getAllProductsByProductTypeID(head.getProducttypeID()).size() == 0) {
                DBConnection.getInstance().deleteProductTypeByID(head.getProducttypeID());
            }
        }
        for (Product childProduct : listOfChildren) {
            deleteSetsWithUnderSets(childProduct);
        }
    }

    private void refreshTTV(Integer i) throws SQLException {
        TTVSets.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        try {
            listHeaders = GetListHeaders(DBConnection.getInstance().getAllSetProductTypes());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (listHeaders == null) return;
        for (Product listHeader : listHeaders) {
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childsPerHeader = null;
            try {
                childsPerHeader = DBConnection.getInstance().getProductsByProductTypeIdWhichAraNotInaSet(listHeader.getProducttypeID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (Product child : childsPerHeader) {
                childs.add(child);
                child.setSelected(new CheckBox());
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                try {
                    printSetsTree(child, cache);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0) {
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
            printSetsTree(aListOfChildren, child);
        }
    }
    public void refresh(){
        TTVSets.refresh();
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
}
