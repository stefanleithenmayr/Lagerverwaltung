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
    private Text tnameID, tdesc, tquantity;

    @FXML
    private void showItem(MouseEvent event) throws SQLException {
        String selectedItem = (String)listV.getSelectionModel().getSelectedItem();
        tnameID.setText(selectedItem);
        int id = DBConnection.getInstance().getItemIDByName(selectedItem);
        int quantity = 1;
        if (quantity != -1){
            tquantity.setText(Integer.toString(quantity));
        }
        String description = DBConnection.getInstance().getIemDescriptionByID(id);
        if (!description.equals("")){
            tdesc.setText(description);
        }
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