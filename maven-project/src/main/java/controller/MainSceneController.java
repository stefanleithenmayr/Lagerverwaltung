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
    private AnchorPane mainPane, addItemPane, subPane, showItemPane, rentsPane, statisticsPane, userManagerPane, exportDatasPane;
    @FXML
    private Button cancelBT, addItemBT, showItemsBT, rentBT, exportDatasBT, statisticsBT, userManagerBT;
    @FXML
    private JFXToggleButton changeThemeBT;
    @FXML
    private ImageView imageVCancelBT;
    @FXML
    private Rectangle recLayout;

    private double xOffset;
    private double yOffset;

    private boolean theme; //false = dark, true = white
    private String acutalPane;

    @FXML
    private void closeWindow() {
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

        switch (buttonName) {
            case "addItemBT":
                addItemBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "showItemsBT":
                showItemsBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "rentBT":
                rentBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "exportBT":
                exportDatasBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "statisticsBT":
                statisticsBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "userManagerBT":
                userManagerBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "exportDatasBT":
                exportDatasBT.setStyle("-fx-background-color:#3D4956");
                break;
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
            exportDatasBT.setStyle("-fx-background-color:transparent");
        }else if(buttonName.equals("statisticsBT") && !acutalPane.equals("statisticsPane")){
            statisticsBT.setStyle("-fx-background-color:transparent");
        }else if(buttonName.equals("userManagerBT") && !acutalPane.equals("userManagerPane")){
            userManagerBT.setStyle("-fx-background-color:transparent");
        }else if(buttonName.equals("exportDatasBT") && !acutalPane.equals("exportDatasPane")){
            exportDatasBT.setStyle("-fx-background-color:transparent");
        }
    }

    /**
     * switcht das Theme
     */

    @FXML
    private void changeFont() throws IOException {
        mainPane.getStylesheets().clear();
        addItemPane.getStylesheets().clear();
        statisticsPane.getStylesheets().clear();
        userManagerPane.getStylesheets().clear();
        exportDatasPane.getStylesheets().clear();

        if (changeThemeBT.isSelected()) {
            imageVCancelBT.setImage(new Image("icons/cancelmusic1.png"));
            mainPane.getStylesheets().add("css/mainPaneWHITE.css");
            addItemPane.getStylesheets().add("css/addItemWHITE.css");
            rentsPane.getStylesheets().add("css/rentsWHITE.css");
            statisticsPane.getStylesheets().add("css/statisticsWHITE.css");
            userManagerPane.getStylesheets().add("css/userManagerWHITE.css");
            exportDatasPane.getStylesheets().add("css/exportDatasWHITE.css");
            theme = true;
        } else {
            imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
            mainPane.getStylesheets().add("css/mainPaneDARK.css");
            addItemPane.getStylesheets().add("css/addItemDARK.css");
            rentsPane.getStylesheets().add("css/rentsDARK.css");
            statisticsPane.getStylesheets().add("css/statisticsDARK.css");
            userManagerPane.getStylesheets().add("css/userManagerDARK.css");
            exportDatasPane.getStylesheets().add("css/exportDatasDARK.css");
            theme = false;
        }

        if (this.acutalPane.equals("showItemPane")){
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            subPane.getChildren().clear();
            showItemPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ShowItemsScene.fxml"));
            subPane.getChildren().add(showItemPane);
            showItemPane.getStylesheets().clear();
            showItemPane.setPrefWidth(bounds.getWidth() - 280);
            showItemPane.setPrefHeight(bounds.getHeight() - 120);
            if (theme) {
                showItemPane.getStylesheets().add("css/showItemsWHITE.css");
            } else {
                showItemPane.getStylesheets().add("css/showItemsDARK.css");
            }
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
        userManagerBT.setStyle("-fx-background-color:transparent");
        statisticsBT.setStyle("-fx-background-color:transparent");
        exportDatasBT.setStyle("-fx-background-color:transparent");
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
        }else if(buttonName.equals("userManagerBT")){
            userManagerPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/UserManagerScene.fxml"));
            userManagerBT.setStyle("-fx-background-color:#3D4956");
            subPane.getChildren().add(userManagerPane);
            userManagerPane.setPrefWidth(bounds.getWidth() - 280);
            userManagerPane.setPrefHeight(bounds.getHeight() - 120);
            userManagerPane.getStylesheets().clear();

            if (theme) {
                userManagerPane.getStylesheets().add("css/userManagerWHITE.css");
            } else {
                userManagerPane.getStylesheets().add("css/userManagerDARK.css");
            }
            acutalPane = "userManagerPane";
        }
        else if(buttonName.equals("exportDatasBT")){
            exportDatasPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ExportDatas.fxml"));
            exportDatasBT.setStyle("-fx-background-color:#3D4956");
            subPane.getChildren().add(exportDatasPane);
            exportDatasPane.setPrefWidth(bounds.getWidth() - 280);
            exportDatasPane.setPrefHeight(bounds.getHeight() - 120);
            exportDatasPane.getStylesheets().clear();

            if (theme) {
                userManagerPane.getStylesheets().add("css/exportDatasWHITE.css");
            } else {
                userManagerPane.getStylesheets().add("css/exportDatasDARK.css");
            }
            acutalPane = "exportDatasPane";
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
            userManagerPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/UserManagerScene.fxml"));
            exportDatasPane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ExportDatas.fxml"));
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

        userManagerPane.setPrefWidth(bounds.getWidth() - 280);
        userManagerPane.setPrefHeight(bounds.getHeight() - 120);

        exportDatasPane.setPrefWidth(bounds.getWidth() - 280);
        exportDatasPane.setPrefHeight(bounds.getHeight() - 120);

        recLayout.setHeight(bounds.getHeight() - 71);
        acutalPane = "";
    }
}