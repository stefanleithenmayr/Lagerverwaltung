package controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    @FXML
    private void addNewUser() throws InterruptedException {
        obList.add(0, new User("Replace with Username",  "Replace with Password", "Replace with Name"));
        userTV.getSelectionModel().select(0);
        this.activateEditing();
    }

    @FXML
    private void removeUser() throws SQLException {
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user != null){
            DBConnection.getInstance().removeUser(user.getUsername().getText());
            obList.remove(user);
        }
    }

    @FXML
    private void activateEditing() {
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user != null) {
            user.setB(false);
            this.handleButton(true);
            userTV.refresh();
        }
    }

    @FXML
    private void cancelEditing(){
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user != null) {
            user.setB(true);
        }

        if (user.getUserNameField().getText().equals("Replace with Username")){
            obList.remove(user);
        }

        this.handleButton(false);
        userTV.refresh();
    }

    @FXML
    private void saveUser() throws SQLException {
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user.getUserNameField().getText().equals("Replace with Username")){
            obList.remove(user);
            this.handleButton(false);
            return;
        }
        //get Text aus Feld user.getPasswordField().getText()
        String name = user.getNameField().getText();
        String userName = user.getUserNameField().getText();
        String password = user.getPasswordField().getText();

        DBConnection.getInstance().upDateUser(user, name, userName, password);
        handleButton(false);
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

        nameCol.setMinWidth(300);
        userNameCol.setMinWidth(300);
        passwordCol.setMinWidth(300);

        obList = FXCollections.observableArrayList(users);

        if(users != null){
            userTV.setItems(obList);
        }
    }

    private void handleButton(boolean b) { //aktivieren oder deaktivieren
        cancelBT.setVisible(b);
        saveBT.setVisible(b);
        editBT.setDisable(b);
        removeBT.setDisable(b);
        newUserBT.setDisable(b);
    }
}