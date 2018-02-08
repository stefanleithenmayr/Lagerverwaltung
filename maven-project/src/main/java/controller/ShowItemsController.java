package controller;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import loginPackage.DBConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ShowItemsController implements Initializable {

    @FXML
    private JFXListView listV;
    @FXML
    private Text nameID, desc, amount;

    @FXML
    private void showItem(MouseEvent event){
        String selectedItem = (String)listV.getSelectionModel().getSelectedItem();
        nameID.setText(selectedItem);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> items = null;
        try {
            items = (List<String>) DBConnection.getInstance().getItems();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(items != null){
            listV.setItems(FXCollections.observableArrayList(items));
        }
    }
}