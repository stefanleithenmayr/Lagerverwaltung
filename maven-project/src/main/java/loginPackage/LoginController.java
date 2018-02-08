package loginPackage;

import com.jfoenix.controls.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.stage.Screen;
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
    private ImageView avaterIMG, imageVCancelBT;
    @FXML
    private AnchorPane loginPane;
    @FXML
    private JFXButton loginBT;

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loginAction(ActionEvent event) throws ClassNotFoundException, IOException, SQLException {
        boolean loginSuccessful;
        //loginSuccessful = DBConnection.getInstance().login(userNameField.getText(), passwordField.getText());
        loginSuccessful = DBConnection.getInstance().login("renedeicker", "12345");
        if (!loginSuccessful){
            falseInputField.setVisible(true);
            return;
        }
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

        Parent mainRoot = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainScene.fxml"));
        Scene mainScene = new Scene(mainRoot);
        Stage newStage = new Stage();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        newStage.setX(bounds.getMinX());
        newStage.setY(bounds.getMinY());
        newStage.setWidth(bounds.getWidth());
        newStage.setHeight(bounds.getHeight());
        newStage.initStyle(StageStyle.TRANSPARENT);

        newStage.setScene(mainScene);
        newStage.setResizable(false);
        mainScene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        newStage.show();
    }

    @FXML
    private void changeFont(ActionEvent event){
        loginPane.getStylesheets().clear();
        if(toggleButton.isSelected()){
            avaterIMG.setImage(new Image("icons/user1.png"));
            imageVCancelBT.setImage(new Image("icons/cancelmusic1.png"));
            loginPane.getStylesheets().add("css/loginPaneWHITE.css");
        }else{
            avaterIMG.setImage(new Image("icons/user.png"));
            imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
            loginPane.getStylesheets().add("css/loginPaneDARK.css");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}