package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import loginPackage.DBConnection;
import model.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class UserSelectionController implements Initializable, Observer {
    @FXML
    private TableView<User> tvUser;
    private ObservableList<User> users;
    @FXML
    private TableColumn<User,String> tcUser;
    @FXML
    JFXTextField tfSearchName;

    @FXML
    private void searchUser(KeyEvent event){
        List<User> cache = new ArrayList<>(users);
        tvUser.getItems().clear();
        try {
            users = FXCollections.observableArrayList(DBConnection.getInstance().getUsers());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        KeyCode keycode = event.getCode();
        String search = tfSearchName.getText();
        if(keycode == KeyCode.BACK_SPACE && search.length() > 0){
            search = search.substring(0,search.length()-1);
        }
        else search += event.getText();
        for (int i = 0; i < users.size(); i++){
            users.get(i).setSelected(cache.get(i).getSelected());
            if (users.get(i).getRealName().toLowerCase().contains(search.toLowerCase())){
                tvUser.getItems().add(users.get(i));
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcUser.setCellValueFactory(new PropertyValueFactory<>("realName"));
        try {
            users = FXCollections.observableArrayList(DBConnection.getInstance().getUsers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvUser.setItems(users);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
