package controller;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import loginPackage.DBConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {
    private Integer quantity;
    @FXML
    private JFXTextField tfQuantity, tfDescription, tfItemName;
    @FXML
    private JFXSlider slQuantity;
    @FXML
    private JFXButton btnInsert;
    @FXML
    private Text qText;
    @FXML
    public void substractQuantity() {
        if (quantity > 1) {
            quantity--;
        }
        tfQuantity.setText(Integer.toString(quantity));
        slQuantity.setValue(quantity);
    }

    @FXML
    public void addQuantity() {
        if (quantity < 100) {
            quantity++;
        }
        tfQuantity.setText(Integer.toString(quantity));
        slQuantity.setValue(quantity);
    }

    @FXML
    public void upDateQuantity() {
        if (!tfQuantity.getText().equals("")){
            quantity = Integer.parseInt(tfQuantity.getText());
            slQuantity.setValue(quantity);
        }
    }
    @FXML
    public void insertItem() throws SQLException {
        int counter  = 0;
        DBConnection.getInstance().addProduct(tfItemName.getText(), tfDescription.getText());
        int itemId = DBConnection.getInstance().getLastProductID();
        while(counter != quantity){
            DBConnection.getInstance().addItem(itemId);
            counter++;
        }
    }

    @FXML
    public void upDateQuantitySlider() {
        Double value = slQuantity.getValue();
        quantity = value.intValue();
        tfQuantity.setText(quantity.toString());
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quantity = 0;
    }
}