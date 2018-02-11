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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    private AnchorPane mainPane, addItemPane, subPane, showItemPane;
    @FXML
    private Button cancelBT, addItemBT, showItemsBT, rentBT;
    @FXML
    private JFXToggleButton changeThemeBT;
    @FXML
    private ImageView imageVCancelBT, imageAddElement, showItemsIV;
    @FXML
    private Rectangle recLayout;

    private double xOffset;
    private double yOffset;

    boolean theme; //false = dark, true = white

    @FXML
    private void closeWindow(ActionEvent event) {
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
    private void changeFont(ActionEvent event) {
        mainPane.getStylesheets().clear();
        addItemPane.getStylesheets().clear();
        showItemPane.getStylesheets().clear();

        if (changeThemeBT.isSelected()) {
            imageVCancelBT.setImage(new Image("icons/cancelmusic1.png"));
            mainPane.getStylesheets().add("css/mainPaneWHITE.css");
            addItemPane.getStylesheets().add("css/addItemWHITE.css");
            showItemPane.getStylesheets().add("css/showItemsWHITE.css");
            theme = true;
        } else {
            imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
            imageAddElement.setImage(new Image("icons/add.png"));
            showItemsIV.setImage(new Image("icons/database.png"));
            mainPane.getStylesheets().add("css/mainPaneDARK.css");
            addItemPane.getStylesheets().add("css/addItemDARK.css");
            showItemPane.getStylesheets().add("css/showItemsDARK.css");
            theme = false;
        }
    }

    /**
     * Wechselt zwischen den verschiedenen Panes hin und her
     * @param event
     * @throws IOException
     */
    @FXML
    private void switchPane(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        addItemBT.setStyle("-fx-background-color:transparent");
        showItemsBT.setStyle("-fx-background-color:transparent");
        rentBT.setStyle("-fx-background-color:transparent");
        String buttonName = button.getId();
        subPane.getChildren().clear();

        if (buttonName.equals("addItemBT")) {
            addItemBT.setStyle("-fx-background-color:#3D4956");
            subPane.getChildren().add(addItemPane);
        } else if (buttonName.equals("showItemsBT")) {
            showItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ShowItemsScene.fxml"));
            showItemsBT.setStyle("-fx-background-color:#3D4956");
            subPane.getChildren().add(showItemPane);
            showItemPane.getStylesheets().clear();
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            showItemPane.setPrefWidth(bounds.getWidth() - 280);
            showItemPane.setPrefHeight(bounds.getHeight() - 120);
            if (theme) {
                showItemPane.getStylesheets().add("css/showItemsWHITE.css");
            } else {
                showItemPane.getStylesheets().add("css/showItemsDARK.css");
            }
        } else if(buttonName.equals("rentBT")){
            rentBT.setStyle("-fx-background-color:#3D4956");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        theme = false;
        try {
            addItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AddItem.fxml"));
            showItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ShowItemsScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        subPane.setPrefWidth(bounds.getWidth() - 280);
        subPane.setPrefHeight(bounds.getHeight() - 120);
        addItemPane.setPrefWidth(bounds.getWidth() - 280);
        addItemPane.setPrefHeight(bounds.getHeight() - 120);
        showItemPane.setPrefWidth(bounds.getWidth() - 280);
        showItemPane.setPrefHeight(bounds.getHeight() - 120);
        recLayout.setHeight(bounds.getHeight() - 71);
    }
}