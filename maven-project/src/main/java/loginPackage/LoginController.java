package loginPackage;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.soap.Text;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginController implements Initializable {

    @FXML
    private Button closeButton;
    @FXML
    private JFXTextField userNameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXTextField falseInputField;
    @FXML
    private JFXToggleButton toggleButton;
    @FXML
    private ImageView avaterIMG;
    @FXML
    private AnchorPane loginPane;

    public static Stage mainStage;

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loginAction(ActionEvent event) throws ClassNotFoundException, IOException, SQLException {
        boolean loginSuccessful;
        loginSuccessful = DBConnection.getInstance().login(userNameField.getText(), passwordField.getText());
        if (!loginSuccessful){
            falseInputField.setVisible(true);
            return;
        }
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

        Parent mainRoot = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainScene.fxml"));
        Scene mainScene = new Scene(mainRoot);
        Stage newStage = new Stage();
        newStage.initStyle(StageStyle.TRANSPARENT);

        newStage.setScene(mainScene);
        newStage.setResizable(false);
        mainScene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        mainStage = newStage;
        newStage.show();
    }

    @FXML
    private void changeFont(ActionEvent event){
        loginPane.getStylesheets().clear();
        if(toggleButton.isSelected()){
            avaterIMG.setImage(new Image("icons/user1.png"));
            loginPane.getStylesheets().add("css/loginPaneWHITE.css");
        }else{
            avaterIMG.setImage(new Image("icons/user.png"));
            loginPane.getStylesheets().add("css/loginPaneDARK.css");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}