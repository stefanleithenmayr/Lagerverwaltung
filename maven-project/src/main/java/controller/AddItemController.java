package controller;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import loginPackage.DBConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {
    @FXML
    private JFXTextField tfQuantity, tfDescription, tfProductTypeName;
    @FXML
    private JFXSlider slQuantity;
    @FXML
    private JFXButton btnInsert;
    @FXML
    private Text qText;

    @FXML
    public void upDateQuantity() {
        double quantity = slQuantity.getValue();
        if (quantity-(int)quantity >= 0.5){
            double value = quantity+ (1-(quantity-(int)quantity));
            tfQuantity.setText(Integer.toString((int)value));
        }
        else{
            double value = quantity-(quantity-(int)quantity);
            tfQuantity.setText(Integer.toString((int)value));
        }
    }
    @FXML
    public void insertProductTypeWithProducts() throws SQLException {
        int amount = Integer.parseInt(tfQuantity.getText());
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}