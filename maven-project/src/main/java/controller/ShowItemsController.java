package controller;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import loginPackage.DBConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import model.Item;


public class ShowItemsController implements Initializable {

    @FXML
    private JFXListView<Item> listV;
    @FXML
    private TextArea taName, taDesc;

    @FXML
    private void showItem(MouseEvent event) throws SQLException {
        Item selectedItem = listV.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            taName.setText(selectedItem.toString());
            int id = selectedItem.getId();
            String description = selectedItem.getDescription();
            if (!description.equals("")){
                taDesc.setText(description);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Item> items = null;
        try {
            items =  DBConnection.getInstance().getItemsList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(items != null){
            listV.setItems(FXCollections.observableArrayList(items));
        }
    }
}