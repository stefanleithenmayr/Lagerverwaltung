package loginPackage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private AnchorPane loginPane;
    @FXML
    private Button closeButton;
    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField userNameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private Text alreadyRegisteredField;
    @FXML
    private JFXToggleButton toggleButton;
    @FXML
    private ImageView avaterIMG, imageVCancelBT;
    @FXML
    private JFXButton registerBT;


    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void registerAction(ActionEvent event) throws ClassNotFoundException, IOException, SQLException {

        if(DBConnection.getInstance().register(nameField.getText(),userNameField.getText(),passwordField.getText())) {
            alreadyRegisteredField.setVisible(true);
        }
        else {
            DBConnection.getInstance().register(nameField.getText(),userNameField.getText(),passwordField.getText());
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
