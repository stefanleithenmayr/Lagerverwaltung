package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import loginPackage.DBConnection;

import java.net.URL;
import java.rmi.activation.ActivationID;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {
    private Integer quantity;
    @FXML
    JFXTextField tfItemName, tfQuantity, tfDescription;
    @FXML
    JFXSlider slQuantity;
    @FXML
    JFXButton btnInsert;


    @FXML
    public void substractQuantity(ActionEvent event) {
        if (quantity > 1) {
            quantity--;
        }
        tfQuantity.setText(Integer.toString(quantity));
        slQuantity.setValue(quantity);
    }

    @FXML
    public void addQuantity(ActionEvent event) {
        if (quantity < 100) {
            quantity++;
        }
        tfQuantity.setText(Integer.toString(quantity));
        slQuantity.setValue(quantity);
    }

    @FXML
    public void upDateQuantity(MouseEvent event) {
        if (!tfQuantity.getText().equals("")){
            quantity = Integer.parseInt(tfQuantity.getText());
            slQuantity.setValue(quantity);
        }
    }
    @FXML
    public void insertItem(ActionEvent event) throws SQLException {
        DBConnection.getInstance().addProduct(tfItemName.getText(), tfDescription.getText(), quantity);
    }

    @FXML
    public void upDateQuantitySlider(MouseEvent event) {
        Double d = slQuantity.getValue();
        quantity = Integer.valueOf(d.intValue());
        tfQuantity.setText(quantity.toString());
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quantity = 0;
    }
}