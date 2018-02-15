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
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    private AnchorPane mainPane, addItemPane, subPane, showItemPane, rentsPane;
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
    String acutalPane;

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) cancelBT.getScene().getWindow();
        stage.close();
    }

    /**
     * Die zwei Methoden (mouseIsPressedEvent und mouseIsDraggedEvent)
     * sind dafür da, dass das MainPane verschiebar bleibt!
     * @param event
     */

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
    private void mouseHovered(MouseEvent event){
        Button button = (Button) event.getSource();
        String buttonName = button.getId();

        if (buttonName.equals("addItemBT")){
            addItemBT.setStyle("-fx-background-color:#3D4956");
        }else if(buttonName.equals("showItemsBT")){
            showItemsBT.setStyle("-fx-background-color:#3D4956");
        }else if(buttonName.equals("rentBT")){
            rentBT.setStyle("-fx-background-color:#3D4956");
        }
    }

    @FXML
    private void mouseNotHovered(MouseEvent event){
        Button button = (Button) event.getSource();
        String buttonName = button.getId();

        if (buttonName.equals("addItemBT") && !acutalPane.equals("addItemPane")){
            addItemBT.setStyle("-fx-background-color:transparent");
        }else if(buttonName.equals("showItemsBT") && !acutalPane.equals("showItemPane")){
            showItemsBT.setStyle("-fx-background-color:transparent");
        }else if(buttonName.equals("rentBT") && !acutalPane.equals("rentsPane")){
            rentBT.setStyle("-fx-background-color:transparent");
        }
    }

    /**
     * switcht das Theme
     * @param event
     */

    @FXML
    private void changeFont(ActionEvent event) {
        mainPane.getStylesheets().clear();
        addItemPane.getStylesheets().clear();
        showItemPane.getStylesheets().clear();
        rentsPane.getStylesheets().clear();

        if (changeThemeBT.isSelected()) {
            imageVCancelBT.setImage(new Image("icons/cancelmusic1.png"));
            mainPane.getStylesheets().add("css/mainPaneWHITE.css");
            addItemPane.getStylesheets().add("css/addItemWHITE.css");
            showItemPane.getStylesheets().add("css/showItemsWHITE.css");
            rentsPane.getStylesheets().add("css/rentsWHITE.css");
            theme = true;
        } else {
            imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
            imageAddElement.setImage(new Image("icons/add.png"));
            showItemsIV.setImage(new Image("icons/database.png"));
            mainPane.getStylesheets().add("css/mainPaneDARK.css");
            addItemPane.getStylesheets().add("css/addItemDARK.css");
            showItemPane.getStylesheets().add("css/showItemsDARK.css");
            rentsPane.getStylesheets().add("css/rentsDARK.css");
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

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        if (buttonName.equals("addItemBT")) {
            addItemBT.setStyle("-fx-background-color:#3D4956");
            subPane.getChildren().add(addItemPane);
            acutalPane = "addItemPane";
        } else if (buttonName.equals("showItemsBT")) {
            showItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ShowItemsScene.fxml"));
            showItemsBT.setStyle("-fx-background-color:#3D4956");
            subPane.getChildren().add(showItemPane);
            showItemPane.getStylesheets().clear();
            showItemPane.setPrefWidth(bounds.getWidth() - 280);
            showItemPane.setPrefHeight(bounds.getHeight() - 120);
            if (theme) {
                showItemPane.getStylesheets().add("css/showItemsWHITE.css");
            } else {
                showItemPane.getStylesheets().add("css/showItemsDARK.css");
            }
            acutalPane = "showItemPane";
        } else if(buttonName.equals("rentBT")){
            rentsPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Rents.fxml"));
            rentBT.setStyle("-fx-background-color:#3D4956");
            subPane.getChildren().add(rentsPane);
            rentsPane.setPrefWidth(bounds.getWidth() - 280);
            rentsPane.setPrefHeight(bounds.getHeight() - 120);
            rentsPane.getStylesheets().clear();

            if (theme) {
                rentsPane.getStylesheets().add("css/rentsWHITE.css");
            } else {
                rentsPane.getStylesheets().add("css/rentsDark.css");
            }
            acutalPane = "rentsPane";
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        theme = false;
        try {
            addItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AddItem.fxml"));
            showItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ShowItemsScene.fxml"));
            rentsPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Rents.fxml"));
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
        rentsPane.setPrefWidth(bounds.getWidth() - 280);
        rentsPane.setPrefHeight(bounds.getHeight() - 120);
        recLayout.setHeight(bounds.getHeight() - 71);
        acutalPane = "";
    }
}