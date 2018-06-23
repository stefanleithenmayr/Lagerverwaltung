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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import loginPackage.DBConnection;
import model.BarcodesToPdfGenerator;
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
        System.out.println(amount);
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
        System.out.println(tfProductTypeName.getText());
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
    }

    private int getNextXPosition(int x) {
        if (x == 20) return 270;
        else if (x == 270) return 540;
        return 20;
    }

    public static BufferedImage getBufferedImageForCode128Bean(String barcodeString) {
        Code128Bean code128Bean = new Code128Bean();
        final int dpi = 150;
        code128Bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar
        code128Bean.doQuietZone(false);
        BitmapCanvasProvider canvas1 = new BitmapCanvasProvider(
                dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0
        );
        //Generate the barcode
        code128Bean.generateBarcode(canvas1, barcodeString);
        return canvas1.getBufferedImage();
    }

    private File fileChooser() {
        Stage primaryStage = new Stage();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(primaryStage);
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
        tvProductTypes.getSelectionModel().clearSelection();
    }
    @FXML
    public void productTypeSelected(){
        selectedProductType = tvProductTypes.getSelectionModel().getSelectedItem();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources){
        tfQuantity.setText("1");
        tfQuantity1.setText("1");
        Code128Bean code128 = new Code128Bean();
        code128.setHeight(15f);
        code128.setModuleWidth(0.3);
        code128.setQuietZone(10);
        code128.doQuietZone(true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(baos, "image/x-png", 400, BufferedImage.TYPE_BYTE_BINARY, false, 0);
        code128.generateBarcode(canvas, "6549874");
        try {
            canvas.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("barcode.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        //Image image = new Image("maven-project\\barcode.png");
        //IVBarcode.setImage(image);

        System.out.println("alskdjf");



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