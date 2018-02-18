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
    private AnchorPane mainPane, addItemPane, subPane, showItemPane, rentsPane, statisticsPane;
    @FXML
    private Button cancelBT, addItemBT, showItemsBT, rentBT, exportBT, statisticsBT, userManagerBT;
    @FXML
    private JFXToggleButton changeThemeBT;
    @FXML
    private ImageView imageVCancelBT;
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
        }else if(buttonName.equals("exportBT")){
            exportBT.setStyle("-fx-background-color:#3D4956");
        }else if(buttonName.equals("statisticsBT")){
            statisticsBT.setStyle("-fx-background-color:#3D4956");
        }else if(buttonName.equals("userManagerBT")){
            userManagerBT.setStyle("-fx-background-color:#3D4956");
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
        }else if(buttonName.equals("exportBT") && !acutalPane.equals("exportPane")){
            exportBT.setStyle("-fx-background-color:transparent");
        }else if(buttonName.equals("statisticsBT") && !acutalPane.equals("statisticsPane")){
            statisticsBT.setStyle("-fx-background-color:transparent");
        }else if(buttonName.equals("userManagerBT") && !acutalPane.equals("userManagerPane")){
            userManagerBT.setStyle("-fx-background-color:transparent");
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
        statisticsPane.getStylesheets().clear();

        if (changeThemeBT.isSelected()) {
            imageVCancelBT.setImage(new Image("icons/cancelmusic1.png"));
            mainPane.getStylesheets().add("css/mainPaneWHITE.css");
            addItemPane.getStylesheets().add("css/addItemWHITE.css");
            showItemPane.getStylesheets().add("css/showItemsWHITE.css");
            rentsPane.getStylesheets().add("css/rentsWHITE.css");
            statisticsPane.getStylesheets().add("css/statisticsWHITE.css");
            theme = true;
        } else {
            imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
            mainPane.getStylesheets().add("css/mainPaneDARK.css");
            addItemPane.getStylesheets().add("css/addItemDARK.css");
            showItemPane.getStylesheets().add("css/showItemsDARK.css");
            rentsPane.getStylesheets().add("css/rentsDARK.css");
            statisticsPane.getStylesheets().add("css/statisticsDARK.css");
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
        }else if(buttonName.equals("statisticsBT")){
            statisticsPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/StatisticsScene.fxml"));
            statisticsBT.setStyle("-fx-background-color:#3D4956");
            subPane.getChildren().add(statisticsPane);
            statisticsPane.setPrefWidth(bounds.getWidth() - 280);
            statisticsPane.setPrefHeight(bounds.getHeight() - 120);
            statisticsPane.getStylesheets().clear();

            if (theme) {
                statisticsPane.getStylesheets().add("css/statisticsWHITE.css");
            } else {
                statisticsPane.getStylesheets().add("css/statisticsDARK.css");
            }
            acutalPane = "statisticsPane";
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        theme = false;
        try {
            addItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AddItem.fxml"));
            showItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ShowItemsScene.fxml"));
            rentsPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Rents.fxml"));
            statisticsPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/StatisticsScene.fxml"));
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

        statisticsPane.setPrefWidth(bounds.getWidth() - 280);
        statisticsPane.setPrefHeight(bounds.getHeight() - 120);

        recLayout.setHeight(bounds.getHeight() - 71);
        acutalPane = "";
    }
}