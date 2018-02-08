package controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    private AnchorPane mainPane, addItemPane, subPane;
    @FXML
    private Button cancelBT, addItemBT;
    @FXML
    private JFXToggleButton changeThemeBT;
    @FXML
    private ImageView imageVCancelBT, imageAddElement;
    @FXML
    private Line designLine;

    private double xOffset;
    private double yOffset;

    @FXML
    private void closeWindow(ActionEvent event){
        Stage stage = (Stage) cancelBT.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void mouseIsPressedEvent(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    private void mouseIsDraggedEvent(MouseEvent event) {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    private void changeFont(ActionEvent event){
        mainPane.getStylesheets().clear();
        addItemPane.getStylesheets().clear();

        if(changeThemeBT.isSelected()){
            imageVCancelBT.setImage(new Image("icons/cancelmusic1.png"));
            imageAddElement.setImage(new Image("icons/add1.png"));
            mainPane.getStylesheets().add("css/mainPaneWHITE.css");
            addItemPane.getStylesheets().add("css/addItemWHITE.css");
        } else{
            imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
            imageAddElement.setImage(new Image("icons/add.png"));
            mainPane.getStylesheets().add("css/mainPaneDARK.css");
            addItemPane.getStylesheets().add("css/addItemDARK.css");
        }
    }

    @FXML
    public void addElement(ActionEvent actionEvent) {


    }
    
    @FXML
    private void switchPane(ActionEvent event) {
        Button button = (Button) event.getSource();
        String buttonName = button.getId();
        subPane.getChildren().clear();
        if (buttonName.equals("addItemBT")) {
            subPane.getChildren().add(addItemPane);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            addItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AddItem.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        designLine.setEndX(bounds.getWidth() - 100);
        subPane.setPrefWidth(bounds.getWidth() - 40);
        subPane.setPrefHeight(bounds.getHeight() - 120);
        addItemPane.setPrefWidth(bounds.getWidth() - 40);
        addItemPane.setPrefHeight(bounds.getHeight() - 120);
    }
}