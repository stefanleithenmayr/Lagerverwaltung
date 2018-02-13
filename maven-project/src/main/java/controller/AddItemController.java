package controller;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import loginPackage.DBConnection;

import java.net.URL;
import java.rmi.activation.ActivationID;
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
        int counter  = 0;
        DBConnection.getInstance().addItem(tfItemName.getText(), tfDescription.getText(), quantity);
        int itemId = DBConnection.getInstance().getLastItemID();
        while(counter != quantity){
            DBConnection.getInstance().addItemExemplar(itemId);
            counter++;
        }
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