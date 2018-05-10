package controller;

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import loginPackage.DBConnection;
import model.Product;
import model.ProductType;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {
    @FXML
    private JFXTextField tfQuantity, tfDescription, tfProductTypeName, tfQuantity1;
    @FXML
    private JFXSlider slQuantity, slQuantity1;
    private int amount, amount1;
    @FXML
    private TableView<ProductType> tvProductTypes;
    ObservableList<ProductType> productTypes;
    ProductType selectedProductType;
    @FXML
    private TableColumn<ProductType, String> tcProducttypeName, tcProducttypeDescription;

    @FXML
    public void upDateQuantity() {
        Platform.runLater(() -> {
            double quantity = slQuantity.getValue();
            if (quantity-(int)quantity >= 0.5){
                amount = (int) (quantity+ (1-(quantity-(int)quantity)));
            }
            else{
                amount = (int)(quantity-(quantity-(int)quantity));
            }
            slQuantity.setValue(amount);
            tfQuantity.setText(Integer.toString(amount));
        });
    }


    @FXML void upDateQuantity1(){
        Platform.runLater(() -> {
            double quantity = slQuantity1.getValue();
            if (quantity-(int)quantity >= 0.5){
                amount1 = (int) (quantity+ (1-(quantity-(int)quantity)));
            }
            else{
                amount1 = (int)(quantity-(quantity-(int)quantity));
            }
            slQuantity1.setValue(amount1);
            tfQuantity1.setText(Integer.toString(amount1));
        });
    }
    @FXML
    public void insertProductTypeWithProducts() throws SQLException {
        tfProductTypeName.clear();
        tfQuantity.setText("1");
        tfDescription.clear();
        if (amount < 1) return;
        if (tfProductTypeName.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Enter a Producttypename!");
            alert.showAndWait();
            return;
        }
        int productTypeID = DBConnection.getInstance().addNewProductType(tfProductTypeName.getText(), tfDescription.getText());
        int counter = 0;
        while (counter != amount){
            DBConnection.getInstance().addNewProduct(productTypeID);
            counter++;
        }
    }
    @FXML
    public void insertProductsIntoProductType() throws SQLException {
        if (selectedProductType == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Select a ProductType!");
            alert.showAndWait();
            return;
        }
        int counter = 0;
        while (amount1 != counter){
            counter++;
            DBConnection.getInstance().addNewProduct(selectedProductType.getProductTypeID());
        }
        tfQuantity1.setText("");
        slQuantity1.setValue(0);
    }
    @FXML
    public void productTypeSelected(){
        selectedProductType = tvProductTypes.getSelectionModel().getSelectedItem();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedProductType = null;
        tfQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            for (int i = 0; i < newValue.length(); i++){
                if (newValue.charAt(i) > '9' || newValue.charAt(i) < '0'){
                    tfQuantity.setText(oldValue);
                    return;
                }
            }
            if (newValue.equals("")){
                tfQuantity.setText("");
                slQuantity.setValue(0);
                return;
            }
            amount = Integer.parseInt(newValue);
            slQuantity.setValue(amount);
        });
        tfQuantity1.textProperty().addListener((observable, oldValue, newValue) -> {
            for (int i = 0; i < newValue.length(); i++){
                if (newValue.charAt(i) > '9' || newValue.charAt(i) < '0'){

                    tfQuantity1.setText(oldValue);
                    return;
                }
            }
            if (newValue.equals("")){
                tfQuantity1.setText("");
                slQuantity1.setValue(0);
                return;
            }
            amount1 = Integer.parseInt(newValue);
            slQuantity1.setValue(amount1);
        });
        try {
            productTypes = FXCollections.observableArrayList(DBConnection.getInstance().getAllProductTypes());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvProductTypes.setItems(productTypes);
        tcProducttypeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        tcProducttypeDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }
}