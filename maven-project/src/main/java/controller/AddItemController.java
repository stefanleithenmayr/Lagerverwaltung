package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import loginPackage.DBConnection;
import model.BarcodesToPdfGenerator;
import model.ErrorMessageUtils;
import model.Product;
import model.ProductType;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private CheckBox CBGenerateBarcodes;
    @FXML
    Rectangle errorRec;
    @FXML
    Text errorTxt;

    @FXML
    public void upDateQuantity() {
        double quantity = slQuantity.getValue();
        if (quantity-(int)quantity >= 0.5){
            amount = (int) (quantity+ (1-(quantity-(int)quantity)));
        }
        else{
            amount = (int)(quantity-(quantity-(int)quantity));
        }
        slQuantity.setValue(amount);
        tfQuantity.setText(Integer.toString(amount));
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
    public void insertProductTypeWithProducts() throws SQLException, FileNotFoundException, DocumentException {
        if (amount < 1) return;
        if (tfProductTypeName.getText().equals("")){

            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("Please enter a Producttypename", errorRec, errorTxt);
            return;
        }
        int productTypeID = DBConnection.getInstance().addNewProductType(tfProductTypeName.getText(), tfDescription.getText());
        int counter = 0;

        List<Product> insertedProducts = new ArrayList<>();
        while (counter != amount){
            int productID = DBConnection.getInstance().addNewProduct(productTypeID);
            if (CBGenerateBarcodes.isSelected()){
                insertedProducts.add(DBConnection.getInstance().getProductByProductID(productID));
            }
            counter++;
        }
        if(CBGenerateBarcodes.isSelected()){
            BarcodesToPdfGenerator.generateBarcoeds(insertedProducts);
        }
        tfProductTypeName.clear();
        tfDescription.clear();
        slQuantity.setValue(0);
        tfQuantity.clear();
        productTypes = FXCollections.observableArrayList(DBConnection.getInstance().getAllProductTypes());
        tvProductTypes.getItems().clear();
        tvProductTypes.setItems(productTypes);
        errorRec.setFill(Color.web("#00802b"));
        errorRec.setStroke(Color.web("#00802b"));
        ErrorMessageUtils.showErrorMessage("Successfully inserted", errorRec, errorTxt);
    }
    @FXML
    public void insertProductsIntoProductType() throws SQLException {
        if (selectedProductType == null){
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("Please select a producttype", errorRec, errorTxt);
            return;
        }
        int counter = 0;
        while (amount1 != counter){
            counter++;
            DBConnection.getInstance().addNewProduct(selectedProductType.getProductTypeID());
        }
        tfQuantity1.setText("1");
        slQuantity1.setValue(1);
        tvProductTypes.getSelectionModel().clearSelection();

        errorRec.setFill(Color.web("#00802b"));
        errorRec.setStroke(Color.web("#00802b"));
        ErrorMessageUtils.showErrorMessage("Successfully inserted", errorRec, errorTxt);
    }
    @FXML
    public void productTypeSelected(){
        selectedProductType = tvProductTypes.getSelectionModel().getSelectedItem();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources){
        tfQuantity.setText("1");
        tfQuantity1.setText("1");
        amount1 = 1;
        amount = 1;
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
            productTypes = FXCollections.observableArrayList(DBConnection.getInstance().getAllNotSetProductTypes());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvProductTypes.setItems(productTypes);
        tcProducttypeName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        tcProducttypeDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    }
}