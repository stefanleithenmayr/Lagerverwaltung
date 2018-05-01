package controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import loginPackage.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    private AnchorPane mainPane, addItemPane, subPane, showItemPane, rentsPane, statisticsPane, userManagerPane, exportDatasPane, dashboardPane, rentManagerPane, setsManagerPane;
    @FXML
    private Button cancelBT, addItemBT, showItemsBT, rentBT, exportDatasBT, statisticsBT, userManagerBT, btSetsManager;
    @FXML
    private JFXToggleButton changeThemeBT;
    @FXML
    private ImageView imageVCancelBT, statIV, expIV, userIV, rentIV, productManagerIV;
    @FXML
    private Rectangle recLayout;
    @FXML
    private Text showRentsTEXT, createRentTEXT,productManagerText;

    private double xOffset;
    private double yOffset;

    private boolean theme, isDown; //false = dark, true = white
    private String acutalPane;

    @FXML
    private void dropDown(){
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.millis(500));
        translateTransition.setNode(productManagerIV);

        TranslateTransition secondTransition = new TranslateTransition();
        secondTransition.setDuration(Duration.millis(500));
        secondTransition.setNode(addItemBT);

        /*TranslateTransition thirdTransition = new TranslateTransition();
        thirdTransition.setDuration(Duration.millis(500));
        thirdTransition.setNode(productManagerText);*/

        TranslateTransition tetraTransition = new TranslateTransition();
        tetraTransition.setDuration(Duration.millis(500));
        tetraTransition.setNode(createRentTEXT);

        TranslateTransition fifthTransition = new TranslateTransition();
        fifthTransition.setDuration(Duration.millis(500));
        fifthTransition.setNode(showRentsTEXT);


        if (!isDown){
            translateTransition.setToY(100);
            secondTransition.setToY(100);
            //thirdTransition.setToY(100);
            tetraTransition.setFromX(-100);
            tetraTransition.setToX(0);
            fifthTransition.setFromX(-100);
            fifthTransition.setToX(0);

            ParallelTransition pT = new ParallelTransition();
            pT.getChildren().addAll(translateTransition, secondTransition, tetraTransition, fifthTransition);
            createRentTEXT.setVisible(true);
            showRentsTEXT.setVisible(true);
            showItemsBT.setVisible(true);
            rentBT.setVisible(true);
            isDown = true;
            pT.play();
        }else{
            translateTransition.setToY(0);
            secondTransition.setToY(0);
            //thirdTransition.setToY(0);
            tetraTransition.setToX(-100);
            fifthTransition.setToX(-100);

            ParallelTransition pT = new ParallelTransition();
            pT.getChildren().addAll(translateTransition, secondTransition);
            pT.play();
            createRentTEXT.setVisible(false);
            showRentsTEXT.setVisible(false);
            showItemsBT.setVisible(false);
            rentBT.setVisible(false);
            isDown = false;
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) cancelBT.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

    /**
     * Die zwei Methoden (mouseIsPressedEvent und mouseIsDraggedEvent)
     * sind dafür da, dass das MainPane verschiebar bleibt!
     *
     * @param event MouseEvent
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
    private void mouseHovered(MouseEvent event) {
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
            case "btSetsManager":
                btSetsManager.setStyle("-fx-background-color:#3D4956");
                break;
        }
    }

    @FXML
    private void mouseNotHovered(MouseEvent event) {
        Button button = (Button) event.getSource();
        String buttonName = button.getId();

        if (buttonName.equals("addItemBT") && !acutalPane.equals("addItemPane")) {
            addItemBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("showItemsBT") && !acutalPane.equals("showItemPane")) {
            showItemsBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("rentBT") && !acutalPane.equals("rentsPane")) {
            rentBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("exportBT") && !acutalPane.equals("exportPane")) {
            exportDatasBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("statisticsBT") && !acutalPane.equals("statisticsPane")) {
            statisticsBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("userManagerBT") && !acutalPane.equals("userManagerPane")) {
            userManagerBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("exportDatasBT") && !acutalPane.equals("exportDatasPane")) {
            exportDatasBT.setStyle("-fx-background-color:transparent");
        }else if (buttonName.equals("btSetsManager") && !acutalPane.equals("setsManagerPane")) {
            btSetsManager.setStyle("-fx-background-color:transparent");
        }
    }

    /**
     * Switcht das Theme!
     */

    @FXML
    private void changeFont() throws IOException {
        mainPane.getStylesheets().clear();
        addItemPane.getStylesheets().clear();
        statisticsPane.getStylesheets().clear();
        userManagerPane.getStylesheets().clear();
        exportDatasPane.getStylesheets().clear();
        rentsPane.getStylesheets().clear();
        rentManagerPane.getStylesheets().clear();
        setsManagerPane.getStylesheets().clear();

        if (changeThemeBT.isSelected()) {
            imageVCancelBT.setImage(new Image("icons/cancelmusic1.png"));
            mainPane.getStylesheets().add("css/mainPaneWHITE.css");
            addItemPane.getStylesheets().add("css/addItemWHITE.css");
            rentsPane.getStylesheets().add("css/rentsWHITE.css");
            statisticsPane.getStylesheets().add("css/statisticsWHITE.css");
            userManagerPane.getStylesheets().add("css/userManagerWHITE.css");
            exportDatasPane.getStylesheets().add("css/exportDatasWHITE.css");
            rentManagerPane.getStylesheets().add("css/rentsManagerWHITE.css");
            setsManagerPane.getStylesheets().add("css/setsManagerWHITE.css");
            theme = true;
        } else {
            imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
            mainPane.getStylesheets().add("css/mainPaneDARK.css");
            addItemPane.getStylesheets().add("css/addItemDARK.css");
            rentsPane.getStylesheets().add("css/rentsDARK.css");
            statisticsPane.getStylesheets().add("css/statisticsDARK.css");
            userManagerPane.getStylesheets().add("css/userManagerDARK.css");
            exportDatasPane.getStylesheets().add("css/exportDatasDARK.css");
            rentManagerPane.getStylesheets().add("css/rentsManagerDARK.css");
            setsManagerPane.getStylesheets().add("css/setsManagerDARK.css");
            theme = false;
        }

        /*if (this.acutalPane.equals("showItemPane")) {
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            subPane.getChildren().clear();
            showItemPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ShowItemsScene.fxml")));
            subPane.getChildren().add(showItemPane);
            showItemPane.getStylesheets().clear();
            showItemPane.setPrefWidth(bounds.getWidth() - 280);
            showItemPane.setPrefHeight(bounds.getHeight() - 120);
            if (theme) {
                showItemPane.getStylesheets().add("css/showItemsWHITE.css");
            } else {
                showItemPane.getStylesheets().add("css/showItemsDARK.css");
            }
        }*/
    }

    /**
     * Wechselt zwischen den verschiedenen Panes hin und her
     *
     * @param event Event in welchen steht, welcher Button gedrückt wurde!
     * @throws IOException IOException
     */
    @FXML
    private void switchPane(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        String buttonName = button.getId();

        addItemBT.setStyle("-fx-background-color:transparent");
        showItemsBT.setStyle("-fx-background-color:transparent");
        rentBT.setStyle("-fx-background-color:transparent");
        userManagerBT.setStyle("-fx-background-color:transparent");
        statisticsBT.setStyle("-fx-background-color:transparent");
        exportDatasBT.setStyle("-fx-background-color:transparent");
        btSetsManager.setStyle("-fx-background-color:transparent");

        subPane.getChildren().clear();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        switch (buttonName) {
            case "addItemBT":
                addItemBT.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(addItemPane);
                acutalPane = "addItemPane";
                break;
            case "showItemsBT":
                rentManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/RentManagerScene.fxml")));
                showItemsBT.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(rentManagerPane);
                rentManagerPane.getStylesheets().clear();
                rentManagerPane.setPrefWidth(bounds.getWidth() - 280);
                rentManagerPane.setPrefHeight(bounds.getHeight() - 120);
                if (theme) {
                    rentManagerPane.getStylesheets().add("css/rentsManagerWHITE.css");
                } else {
                    rentManagerPane.getStylesheets().add("css/rentsManagerDARK.css");
                }
                acutalPane = "rentsManagerPane";
                break;
            case "rentBT":
                rentsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Rents.fxml")));
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
                break;
            case "statisticsBT":
                statisticsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/StatisticsScene.fxml")));
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
                break;
            case "userManagerBT":
                userManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/UserManagerScene.fxml")));
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
                break;
            case "exportDatasBT":
                exportDatasPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ExportDatas.fxml")));
                exportDatasBT.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(exportDatasPane);
                exportDatasPane.setPrefWidth(bounds.getWidth() - 280);
                exportDatasPane.setPrefHeight(bounds.getHeight() - 120);
                exportDatasPane.getStylesheets().clear();

                if (theme) {
                    exportDatasPane.getStylesheets().add("css/exportDatasWHITE.css");
                } else {
                    exportDatasPane.getStylesheets().add("css/exportDatasDARK.css");
                }
                acutalPane = "exportDatasPane";
                break;
            case "btSetsManager":
                setsManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/SetsManager.fxml")));
                btSetsManager.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(setsManagerPane);
                setsManagerPane.setPrefWidth(bounds.getWidth() - 280);
                setsManagerPane.setPrefHeight(bounds.getHeight() - 120);
                setsManagerPane.getStylesheets().clear();

                if (theme) {
                    setsManagerPane.getStylesheets().add("css/setsManagerWHITE.css");
                } else {
                    setsManagerPane.getStylesheets().add("css/setsManagerDARK.css");
                }
                acutalPane = "setsManagerPane";
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        theme = false;
        try {
            addItemPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/AddItem.fxml")));
            showItemPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ShowItemsScene.fxml")));
            rentsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Rents.fxml")));
            statisticsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/StatisticsScene.fxml")));
            userManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/UserManagerScene.fxml")));
            exportDatasPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ExportDatas.fxml")));
            dashboardPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/DashboardScene.fxml")));
            rentManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/RentManagerScene.fxml")));
            setsManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/SetsManager.fxml")));

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

        dashboardPane.setPrefWidth(bounds.getWidth() - 280);
        dashboardPane.setPrefHeight(bounds.getHeight() - 120);

        rentManagerPane.setPrefWidth(bounds.getWidth() - 280);
        rentManagerPane.setPrefHeight(bounds.getHeight() - 120);

        setsManagerPane.setPrefWidth(bounds.getWidth() - 280);
        setsManagerPane.setPrefHeight(bounds.getHeight() - 120);

        subPane.getChildren().add(dashboardPane);
        recLayout.setHeight(bounds.getHeight() - 71);

        if (!DBConnection.getInstance().getActualUser().equals("stuetz") && !DBConnection.getInstance().getActualUser().equals("renedeicker")
                &&!DBConnection.getInstance().getActualUser().equals("stefanleithenmayr") && !DBConnection.getInstance().getActualUser().equals("maxhofer")) {
            exportDatasBT.setVisible(false);
            statisticsBT.setVisible(false);
            userManagerBT.setVisible(false);
            statIV.setVisible(false);
            expIV.setVisible(false);
            userIV.setVisible(false);
        }
        isDown = false;
        acutalPane = "";
    }
}