package controller;

import com.jfoenix.controls.JFXButton;
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
    @FXML
    private JFXButton cancelBT, saveBT, editBT, removeBT, newUserBT;

    private ObservableList<User> obList;
    private User acutalEdit;

    @FXML
    private void removeUser(ActionEvent event) throws SQLException {
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user != null){
            DBConnection.getInstance().removeUser(user.getUsername().getText());
            obList.remove(user);
        }
    }

    @FXML
    private void activateEditing(ActionEvent event){
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user != null){
            acutalEdit = user;
            user.getUsername().setDisable(false);
            user.getName().setDisable(false);
            user.getPassword().setDisable(false);

            cancelBT.setVisible(true);
            saveBT.setVisible(true);
            editBT.setDisable(true);
            removeBT.setDisable(true);
            newUserBT.setDisable(true);
        }
    }

    @FXML
    private void cancelEditing(){
        cancelBT.setVisible(false);
        saveBT.setVisible(false);
        editBT.setDisable(false);
        removeBT.setDisable(false);
        newUserBT.setDisable(false);
        userTV.refresh();
    }

    @FXML
    private void saveUser() {
        
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