package loginPackage;

import com.jfoenix.controls.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.animation.*;
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
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.sql.SQLException;

public class LoginController implements Initializable {

    @FXML
    private Button closeButton;
    @FXML
    private JFXTextField userNameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private Text falseInputField;
    @FXML
    private JFXToggleButton toggleButton;
    @FXML
    private ImageView avaterIMG, imageVCancelBT;
    @FXML
    private AnchorPane loginPane;
    @FXML
    private JFXButton loginBT;

    private boolean loginSuccessful;

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loginAction(ActionEvent event) throws ClassNotFoundException, IOException, SQLException {

        if (!loginSuccessful){
            loginSuccessful = DBConnection.getInstance().login(userNameField.getText(), passwordField.getText());
            if (!loginSuccessful){
                falseInputField.setVisible(true);
                return;
            }

            FadeTransition rt = new FadeTransition(Duration.millis(1000), avaterIMG);
            rt.setFromValue(1.0);
            rt.setToValue(0.1);
            rt.setAutoReverse(true);
            rt.setOnFinished(e -> {

                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();

                Parent mainRoot = null;
                try {
                    mainRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/MainScene.fxml")));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Scene mainScene = new Scene(mainRoot);
                Stage newStage = new Stage();

                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();

                newStage.getIcons().add(new Image("https://raw.githubusercontent.com/stefanleithenmayr/Lagerverwaltung/master/maven-project/src/main/resources/icons/atom.png"));
                newStage.setX(bounds.getMinX());
                newStage.setY(bounds.getMinY());
                newStage.setWidth(bounds.getWidth());
                newStage.setHeight(bounds.getHeight());
                newStage.initStyle(StageStyle.TRANSPARENT);

                newStage.setScene(mainScene);
                //newStage.setResizable(false);
                mainScene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                newStage.show();

            });
            rt.play();
        }
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
        loginSuccessful = false;
    }
}