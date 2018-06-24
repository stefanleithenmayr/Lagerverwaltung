package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import loginPackage.DBConnection;
import model.Output;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ReturnProductController implements Initializable {

    @FXML
    private TableView logTV;
    @FXML
    private TableColumn<?,?> productNrCol, productNameCol, sucessfullCol, userNameCol;
    @FXML
    private JFXTextField eanCodeTF;

    @FXML
    private void addToBoard() throws SQLException {
        String eanCode = eanCodeTF.getText();
        if (eanCode != null && !eanCode.isEmpty()){
            List<Output> outputs = FXCollections.observableArrayList(DBConnection.getInstance().unrentAllProducts(DBConnection.getInstance().getProductPerEan(eanCode)));
            logTV.setItems((ObservableList) outputs);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productNrCol.setCellValueFactory(new PropertyValueFactory<>("productNr"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        sucessfullCol.setCellValueFactory(new PropertyValueFactory<>("successfully"));
    }
}
