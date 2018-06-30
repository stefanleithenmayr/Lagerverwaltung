package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import loginPackage.DBConnection;
import model.ErrorMessageUtils;
import model.Output;
import model.Product;

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
    Rectangle errorRec;
    @FXML
    Text errorTxt;

    @FXML
    private void addToBoard() throws SQLException {
        if (eanCodeTF.getText().equals("")){
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("No eancode scanned", errorRec, errorTxt);
        }
        String eanCode = eanCodeTF.getText();
        if (eanCode != null && !eanCode.isEmpty()){
            Product p = DBConnection.getInstance().getProductPerEan(eanCode);
            if (DBConnection.getInstance().IsProductRented(p)){
                List<Output> outputs = FXCollections.observableArrayList(DBConnection.getInstance().unrentAllProducts(p));
                logTV.setItems((ObservableList) outputs);
            }
            else{
                errorRec.setFill(Color.web("#f06060"));
                errorRec.setStroke(Color.web("#f06060"));
                ErrorMessageUtils.showErrorMessage("Scanned product isn´t rented", errorRec, errorTxt);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userNameCol.setMinWidth(200);
        productNrCol.setCellValueFactory(new PropertyValueFactory<>("productNr"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        sucessfullCol.setCellValueFactory(new PropertyValueFactory<>("successfully"));
    }
}
