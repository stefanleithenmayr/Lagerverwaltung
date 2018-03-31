package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import loginPackage.DBConnection;
import model.Item;
import model.Product;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class SetsManagerController implements Initializable{
    @FXML
    JFXTextField tfSearch, tfSetName;
    @FXML
    JFXTextArea taDescription;
    @FXML
    TableView tvProducts;
    @FXML
    JFXButton btCreateSet;
    @FXML
    TableColumn<Product,String> tcProduct;
    ObservableList<Product> products;
    @FXML
    TableColumn tcSelect;
    @FXML
    ListView <String> lvSets;
    @FXML
    private void createNewSet() throws SQLException {
        if (tfSetName.getText() != null && !tfSetName.getText().equals("") && enoughProductsSelected()){
            List<Product> selectedProducts = getSelectedProducts();
            DBConnection.getInstance().saveNewSet(tfSetName.getText(),tfSearch.getText(),  selectedProducts);
        }
    }

    private List<Product> getSelectedProducts() {
        List<Product> cache = new ArrayList<>();
        for (int i = 0; i < products.size(); i++){
            if (products.get(i).getSelected().isSelected()){
                cache.add(products.get(i));
            }
        }
        return  cache;
    }

    private boolean enoughProductsSelected() {
        int counter = 0;
        for (int i = 0; i < products.size(); i++){
            if (products.get(i).getSelected().isSelected()){
                counter++;
            }
        }
        return counter >= 2;
    }

    @FXML
    private  void serachProduct(KeyEvent event){
        List<Product> cache = new ArrayList(products);
        tvProducts.getItems().clear();
        try {
            products = FXCollections.observableArrayList(DBConnection.getInstance().getProductsList());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        KeyCode keycode = event.getCode();
        String search = tfSearch.getText();
        if(keycode == KeyCode.BACK_SPACE && search.length() > 0){
            search = search.substring(0,search.length()-1);
        }
        else search += event.getText();
        for (int i = 0; i < products.size(); i++){
            products.get(i).setSelected(cache.get(i).getSelected());
            if (products.get(i).getName().toLowerCase().contains(search.toLowerCase())){
                tvProducts.getItems().add(products.get(i));
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcSelect.setCellValueFactory(new PropertyValueFactory<>("selected"));

        try {
            products = FXCollections.observableArrayList(DBConnection.getInstance().getProductsList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvProducts.setItems(products);




        int anzSets = 0;
        try {
            anzSets = DBConnection.getInstance().countSets();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int counter = 0;
        while (anzSets != counter){
            lvSets.getItems().add("Set "+ (counter+1));
            try {
                List<Item> items = DBConnection.getInstance().getSet(counter);
                for (int i = 0; i < items.size(); i++){
                    lvSets.getItems().add(DBConnection.getInstance().getProductNameByItemID(items.get(i).getItemnr()));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            counter++;
        }
    }
}
