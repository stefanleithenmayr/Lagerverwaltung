package controller;

import com.jfoenix.controls.JFXButton;
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
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserManagerController implements Initializable {

    @FXML
    private TableView<User> userTV;
    @FXML
    private TableColumn<?, ?> nameCol, userNameCol, passwordCol, classCol, roleCol, emailCol;
    @FXML
    private JFXButton cancelBT, saveBT, editBT, removeBT, newUserBT;

    private ObservableList<User> obList;
    @FXML
    private Rectangle errorRec;
    @FXML
    private Text errorTxt;

    @FXML
    private void addNewUser() {
        obList.add(0, new User("Replace with Username", "Replace with Password", "Replace with Name", "", "", 2));
        userTV.getSelectionModel().select(0);
        this.activateEditing();
    }

    @FXML
    private void removeUser() throws SQLException {
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user == null){
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("No user selected", errorRec, errorTxt);
            return;
        }

        if (!DBConnection.getInstance().getActualUser().equals(user.getUsername().getText())) {
            DBConnection.getInstance().removeUser(user.getUsername().getText());
            obList.remove(user);
        }

        errorRec.setFill(Color.web("#00802b"));
        errorRec.setStroke(Color.web("#00802b"));
        ErrorMessageUtils.showErrorMessage("Successfully removed", errorRec, errorTxt);
        userTV.getSelectionModel().clearSelection();
    }

    @FXML
    private void activateEditing() {
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user != null) {
            user.setIsActivated(false);
            this.handleButton(true);
            userTV.refresh();
        }
    }

    @FXML
    private void cancelEditing() {
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user != null) {
            user.setIsActivated(true);
        }

        assert user != null;
        if (user.getUserNameField().getText().equals("Replace with Username")) {
            obList.remove(user);
        }

        this.handleButton(false);
        userTV.refresh();
    }

    @FXML
    private void saveUser() throws SQLException {
        User user = userTV.getSelectionModel().getSelectedItem();
        if (user.getUserNameField().getText().equals("Replace with Username")) {
            errorRec.setFill(Color.web("#f06060"));
            errorRec.setStroke(Color.web("#f06060"));
            ErrorMessageUtils.showErrorMessage("Username not valid", errorRec, errorTxt);
            obList.remove(user);
            this.handleButton(false);
            return;
        }
        String name = user.getNameField().getText();
        String userName = user.getUserNameField().getText();
        String password = user.getPasswordField().getText();
        String email = user.getEmailField().getText();
        String klasse = user.getKlasseField().getText();
        int userrolenr = Integer.parseInt(user.getUserroleField().getText());
        if (!(user.getUsername().getText().equals("Replace with Username") && user.getName().getText().equals("Replace with Name")
                && user.getPassword().getText().equals("Replace with Password"))) {
            DBConnection.getInstance().upDateUser(user, name, userName, password, email, klasse, userrolenr);
        } else if (user.getUsername().getText().equals("Replace with Username") && user.getName().getText().equals("Replace with Name")
                && user.getPassword().getText().equals("Replace with Password")) {
            DBConnection.getInstance().saveNewUser(name, userName, password, email, klasse, userrolenr);
        }
        obList.add(new User(userName, password, name, email, klasse, userrolenr));
        obList.remove(user);
        handleButton(false);
        user.setIsActivated(true);
        userTV.refresh();

        errorRec.setFill(Color.web("#00802b"));
        errorRec.setStroke(Color.web("#00802b"));
        ErrorMessageUtils.showErrorMessage("Successfully saved", errorRec, errorTxt);
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

        roleCol.setCellValueFactory(
                new PropertyValueFactory<>("UserRolle"));

        classCol.setCellValueFactory(
                new PropertyValueFactory<>("klasse"));

        emailCol.setCellValueFactory(
                new PropertyValueFactory<>("email"));
        nameCol.setMinWidth(300);
        userNameCol.setMinWidth(300);
        passwordCol.setMinWidth(300);

        obList = FXCollections.observableArrayList(users);

        if (users != null) {
            userTV.setItems(obList);
        }
    }

    private void handleButton(boolean isActivated) {
        cancelBT.setVisible(isActivated);
        saveBT.setVisible(isActivated);
        editBT.setDisable(isActivated);
        removeBT.setDisable(isActivated);
        newUserBT.setDisable(isActivated);
    }
}