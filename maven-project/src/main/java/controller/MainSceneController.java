package controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button cancelBT;
    @FXML
    private JFXToggleButton changeThemeBT;
    @FXML
    private ImageView imageVCancelBT;

    @FXML
    private void closeWindow(ActionEvent event){
        Stage stage = (Stage) cancelBT.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void changeFont(ActionEvent event){
        mainPane.getStylesheets().clear();
        if(changeThemeBT.isSelected()){
            imageVCancelBT.setImage(new Image("icons/cancelmusic1.png"));
            mainPane.getStylesheets().add("css/mainPaneWHITE.css");
        }else{
            imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
            mainPane.getStylesheets().add("css/mainPaneDARK.css");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}