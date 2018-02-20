package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import loginPackage.DBConnection;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserManagerController implements Initializable{

    @FXML
    private TableView<User> userTV;
    @FXML
    private TableColumn<?,?> nameCol, userNameCol, passwordCol;

    private ObservableList<User> obList;

    @FXML
    private void removeUser(ActionEvent event) throws SQLException {
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user != null){
            DBConnection.getInstance().removeUser(user.getUsername());
            obList.remove(user);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<User> users = null;
        try {
            users = DBConnection.getInstance().getUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        userNameCol.setCellValueFactory(
                new PropertyValueFactory<>("username"));

        passwordCol.setCellValueFactory(
                new PropertyValueFactory<>("password"));

        nameCol.setMinWidth(200);
        userNameCol.setMinWidth(300);
        passwordCol.setMinWidth(300);

        obList = FXCollections.observableArrayList(users);

        if(users != null){
            userTV.setItems(obList);
        }
    }
}