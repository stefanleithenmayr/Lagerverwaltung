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
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DeleteSetsController implements Initializable{
    @FXML
    JFXButton btDelete;
    @FXML
    JFXCheckBox cbdDeleteSubsets;
    @FXML
    TreeTableView<Product> TTVSets;
    @FXML
    TreeTableColumn tcSelect,tcName, tcDescription;
    @FXML
    Rectangle errorRec;
    @FXML
    Text errorTxt;
    @FXML
    JFXTextField tfSearch;
    List<Product> setHeaders;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.prepare();
    }

    @FXML
    private  void serachProduct(KeyEvent event) throws SQLException {
        TTVSets.setRoot(null);
        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if(keycode == KeyCode.BACK_SPACE && search.length() > 0){
            search = search.substring(0,search.length()-1);
        }
        else search += event.getText();

        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element
        for (Product listHeader : setHeaders){
            TreeItem<Product> parent = new TreeItem<>(listHeader);
            List<Product> childs = DBConnection.getInstance().getAllChildsOfProduct(listHeader.getProductID());
            for(Product child : childs){
                child.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(DBConnection.getInstance().getProductTypeIdByProductID(child.getProductID())));
                child.setSelected(null);
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                printSetsTree(child, cache);
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0 && listHeader.getProductTypeName().toLowerCase().contains(search.toLowerCase())){
                root.getChildren().add(parent);
            }
        }
        TTVSets.setShowRoot(false);
        TTVSets.setRoot(root);
    }
    @FXML
    public void deleteSelectedSets() throws SQLException {
        boolean deleted = false;
        if (!cbdDeleteSubsets.isSelected()){
            for (int i = 0; i < setHeaders.size(); i++){
                if (setHeaders.get(i).getSelected().isSelected()){
                    DBConnection.getInstance().setSuperProductNrNullBySuperProductNR(setHeaders.get(i).getProductID());
                    DBConnection.getInstance().deleteProduct(setHeaders.get(i).getProductID());
                    if (DBConnection.getInstance().getAllProductsByProductTypeID(setHeaders.get(i).getProducttypeID()).size() == 0){
                        DBConnection.getInstance().deleteProductTypeByID(setHeaders.get(i).getProducttypeID());
                    }
                    deleted = true;
                }
            }
        }
        else{
            for (int i = 0; i < setHeaders.size(); i++){
                if (setHeaders.get(i).getSelected().isSelected()){
                    DeleteSetsWithUnderSets(setHeaders.get(i));
                    deleted = true;
                }
            }
        }
        if (deleted){
            errorRec.setFill(Color.web("#00802b"));
            errorRec.setStroke(Color.web("#00802b"));
            ErrorMessageUtils.showErrorMessage("Successfully deleted", errorRec, errorTxt);
        }
        else{
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("No sets selected", errorRec, errorTxt);
        }
        this.prepare();
    }

    private void prepare() {
        try {
            setHeaders = DBConnection.getInstance().getHighestSetHeaders();
            for (int i = 0; i < setHeaders.size(); i++){
                CheckBox cb = new CheckBox();
                cb.setSelected(false);
                setHeaders.get(i).setSelected(cb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tcName.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeName"));
        tcDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("productTypeDescription"));
        tcSelect.setCellValueFactory(new TreeItemPropertyValueFactory<>("selected"));
        try {
            refreshTTV(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void DeleteSetsWithUnderSets(Product head) throws SQLException {
        List<Product> listOfChildren = DBConnection.getInstance().getProductsChildrenByProductID(head);
        if (listOfChildren.size() != 0){
            DBConnection.getInstance().setSuperProductNrNullBySuperProductNR(head.getProductID());
            DBConnection.getInstance().deleteProduct(head.getProductID());
            if (DBConnection.getInstance().getAllProductsByProductTypeID(head.getProducttypeID()).size() == 0){
                DBConnection.getInstance().deleteProductTypeByID(head.getProducttypeID());
            }
        }
        for(int i = 0; i < listOfChildren.size(); i++) {
            Product childProduct = listOfChildren.get(i);
            DeleteSetsWithUnderSets(childProduct);
        }
    }

    private void  refreshTTV(Integer i) throws SQLException {
        TTVSets.setRoot(null);
        TreeItem<Product> root = new TreeItem<>(new Product(-1, null, null, null, null, null)); //empty root element

        if (setHeaders == null) return;
        for (Product setHeader : setHeaders){
            TreeItem<Product> parent = new TreeItem<>(setHeader);
            List<Product> childs = DBConnection.getInstance().getAllChildsOfProduct(setHeader.getProductID());

            if (childs == null) break;
            for (Product child: childs){
                child.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(DBConnection.getInstance().getProductTypeIdByProductID(child.getProductID())));
                child.setSelected(null);
                child.setIsChild(true);
                TreeItem<Product> cache = new TreeItem<>(child);
                printSetsTree(child, cache);
                parent.getChildren().add(cache);
            }
            if (parent.getChildren().size() > 0){
                root.getChildren().add(parent);
            }
        }
        TTVSets.setShowRoot(false);
        TTVSets.setRoot(root);
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
    public void refresh(){
        TTVSets.refresh();
    }
}
