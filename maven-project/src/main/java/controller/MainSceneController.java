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
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @FXML
    private AnchorPane mainPane, addItemPane, subPane, showItemPane, rentsPane, returnProductPane, userManagerPane, exportDatasPane, dashboardPane,
            rentManagerPane, setsManagerPane, showSetsPane, deleteItemPane, deleteSetsPane, actualShowingPane;
    @FXML
    private Button cancelBT, btProductManager, showItemsBT, rentBT, exportDatasBT, returnProductBT, userManagerBT, btSetsManager, btShowSets,
            btCreateNewSet, btDeleteItem, btAddItem, dashboardBT, btDeleteSets;
    @FXML
    private JFXToggleButton changeThemeBT;
    @FXML
    private ImageView imageVCancelBT, statIV, expIV, userIV, rentIV, productManagerIV, setsManagerIV;
    @FXML
    private Rectangle recLayout;
    @FXML
    private Text showRentsTEXT, createRentTEXT, productManagerText, createNewSetTEXT, showSetsTEXT, addItemTEXT, deleteItemTEXT;

    private double xOffset;
    private double yOffset;

    private boolean theme, isDownRents, isDownSetsManager, isDownProductManager; //false = dark, true = white
    private String acutalPane;

    @FXML
    public void dropDownProductManager() {
        TranslateTransition transation1 = new TranslateTransition();
        transation1.setDuration(Duration.millis(500));
        transation1.setNode(setsManagerIV);

        TranslateTransition transation2 = new TranslateTransition();
        transation2.setDuration(Duration.millis(500));
        transation2.setNode(btAddItem);

        TranslateTransition transation3 = new TranslateTransition();
        transation3.setDuration(Duration.millis(500));
        transation3.setNode(btDeleteItem);

        TranslateTransition transation4 = new TranslateTransition();
        transation4.setDuration(Duration.millis(500));
        transation4.setNode(btCreateNewSet);

        TranslateTransition transation5 = new TranslateTransition();
        transation5.setDuration(Duration.millis(500));
        transation5.setNode(btShowSets);

        TranslateTransition transation6 = new TranslateTransition();
        transation6.setDuration(Duration.millis(500));
        transation6.setNode(btSetsManager);

        TranslateTransition transation7 = new TranslateTransition();
        transation7.setDuration(Duration.millis(500));
        transation7.setNode(btDeleteSets);


        if (!isDownProductManager) {
            if (isDownRents) {
                transation1.setToY(250);
                transation6.setToY(250);
                transation4.setToY(250);
                transation5.setToY(250);
                transation7.setToY(250);
            } else {
                transation1.setToY(100);
                transation6.setToY(100);
                transation4.setToY(100);
                transation5.setToY(100);
                transation7.setToY(100);
            }

            transation2.setFromX(-100);
            transation2.setToX(0);
            transation3.setFromX(-100);
            transation3.setToX(0);

            ParallelTransition pT = new ParallelTransition();
            pT.getChildren().addAll(transation1, transation2, transation3, transation4,
                    transation5, transation6, transation7);
            btAddItem.setVisible(true);
            btDeleteItem.setVisible(true);
            isDownProductManager = true;
            addItemTEXT.setVisible(true);
            deleteItemTEXT.setVisible(true);
            pT.play();
        } else {
            if (isDownRents) {
                transation1.setToY(150);
                transation4.setToY(150);
                transation5.setToY(150);
                transation6.setToY(150);
                transation7.setToY(150);
            } else {
                transation1.setToY(0);
                transation4.setToY(0);
                transation5.setToY(0);
                transation6.setToY(0);
                transation7.setToY(0);
            }
            transation2.setToX(-100);
            transation3.setToX(-100);

            ParallelTransition pT = new ParallelTransition();
            pT.getChildren().addAll(transation1, transation2, transation3, transation4,
                    transation5, transation6, transation7);
            btAddItem.setVisible(false);
            btDeleteItem.setVisible(false);
            isDownProductManager = false;
            addItemTEXT.setVisible(false);
            deleteItemTEXT.setVisible(false);
            pT.play();
        }
    }

    @FXML
    private void dropDownRents() {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.millis(500));
        translateTransition.setNode(productManagerIV);

        TranslateTransition secondTransition = new TranslateTransition();
        secondTransition.setDuration(Duration.millis(500));
        secondTransition.setNode(btProductManager);


        TranslateTransition sixthTransition = new TranslateTransition();
        sixthTransition.setDuration(Duration.millis(500));
        sixthTransition.setNode(btSetsManager);

        TranslateTransition seventhTranisiton = new TranslateTransition();
        seventhTranisiton.setDuration(Duration.millis(500));
        seventhTranisiton.setNode(setsManagerIV);

        TranslateTransition Transition8 = new TranslateTransition();
        Transition8.setDuration(Duration.millis(500));
        Transition8.setNode(btCreateNewSet);

        TranslateTransition Transition16 = new TranslateTransition();
        Transition16.setDuration(Duration.millis(500));
        Transition16.setNode(btDeleteSets);

        TranslateTransition Transition9 = new TranslateTransition();
        Transition9.setDuration(Duration.millis(500));
        Transition9.setNode(btShowSets);

        TranslateTransition Transition12 = new TranslateTransition();
        Transition12.setDuration(Duration.millis(500));
        Transition12.setNode(btAddItem);

        TranslateTransition Transition15 = new TranslateTransition();
        Transition15.setDuration(Duration.millis(500));
        Transition15.setNode(btDeleteItem);
/*
        TranslateTransition Transition16 = new TranslateTransition();
        Transition15.setDuration(Duration.millis(500));
        Transition15.setNode(returnProductBT);
        TranslateTransition Transition17 = new TranslateTransition();
        Transition15.setDuration(Duration.millis(500));
        Transition15.setNode(rentBT);
        TranslateTransition Transition18 = new TranslateTransition();
        Transition15.setDuration(Duration.millis(500));
        Transition15.setNode(showItemsBT);
*/
        if (!isDownRents) {
            translateTransition.setToY(150);
            secondTransition.setToY(150);
            if (isDownProductManager) {
                sixthTransition.setToY(250);
                seventhTranisiton.setToY(250);
                Transition9.setToY(250);
                Transition8.setToY(250);
                Transition16.setToY(250);
            } else {
                sixthTransition.setToY(150);
                seventhTranisiton.setToY(150);
                Transition8.setToY(150);
                Transition9.setToY(150);
                Transition16.setToY(150);
            }
            Transition12.setToY(150);
            Transition15.setToY(150);

            ParallelTransition pT = new ParallelTransition();
            pT.getChildren().addAll(translateTransition, secondTransition,
                    sixthTransition, seventhTranisiton, Transition8, Transition9, Transition12, Transition15, Transition16);
            createRentTEXT.setVisible(true);
            showRentsTEXT.setVisible(true);
            showItemsBT.setVisible(true);
            rentBT.setVisible(true);
            returnProductBT.setVisible(true);
            isDownRents = true;
            pT.play();
        } else {
            if (isDownProductManager) {
                sixthTransition.setToY(100);
                seventhTranisiton.setToY(100);
                Transition8.setToY(100);
                Transition9.setToY(100);
                Transition16.setToY(100);
            } else {
                sixthTransition.setToY(0);
                seventhTranisiton.setToY(0);
                Transition8.setToY(0);
                Transition9.setToY(0);
                Transition16.setToY(0);
            }
            translateTransition.setToY(0);
            secondTransition.setToY(0);
            Transition12.setToY(0);
            Transition15.setToY(0);

            ParallelTransition pT = new ParallelTransition();
            pT.getChildren().addAll(translateTransition, secondTransition, sixthTransition, seventhTranisiton,
                    Transition8, Transition9, Transition12, Transition15, Transition16);
            pT.play();
            createRentTEXT.setVisible(false);
            showRentsTEXT.setVisible(false);
            showItemsBT.setVisible(false);
            rentBT.setVisible(false);
            returnProductBT.setVisible(false);
            isDownRents = false;
        }
    }

    @FXML
    public void dropDownSetsManager() {
        TranslateTransition Transition1 = new TranslateTransition();
        Transition1.setDuration(Duration.millis(500));
        Transition1.setNode(btShowSets);

        TranslateTransition Transition2 = new TranslateTransition();
        Transition2.setDuration(Duration.millis(500));
        Transition2.setNode(btCreateNewSet);

        TranslateTransition Transition3 = new TranslateTransition();
        Transition3.setDuration(Duration.millis(500));
        Transition3.setNode(btDeleteSets);


        if (!isDownSetsManager) {
            btShowSets.setVisible(true);
            btDeleteSets.setVisible(true);
            btCreateNewSet.setVisible(true);
            createNewSetTEXT.setVisible(true);
            showSetsTEXT.setVisible(true);
            isDownSetsManager = true;

            Transition1.setFromX(-100);
            Transition1.setToX(0);
            Transition2.setFromX(-100);
            Transition2.setToX(0);
            Transition3.setFromX(-100);
            Transition3.setToX(0);

            ParallelTransition pT = new ParallelTransition();
            pT.getChildren().addAll(Transition1, Transition2, Transition3);
            pT.play();
        } else {
            btShowSets.setVisible(false);
            btDeleteSets.setVisible(false);
            btCreateNewSet.setVisible(false);
            createNewSetTEXT.setVisible(false);
            showSetsTEXT.setVisible(false);
            isDownSetsManager = false;
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
            case "showItemsBT":
                showItemsBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "btDeleteItem":
                btDeleteItem.setStyle("-fx-background-color:#3D4956");
                break;
            case "btAddItem":
                btAddItem.setStyle("-fx-background-color:#3D4956");
                break;
            case "rentBT":
                rentBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "exportBT":
                exportDatasBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "returnProductBT":
                returnProductBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "userManagerBT":
                userManagerBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "exportDatasBT":
                exportDatasBT.setStyle("-fx-background-color:#3D4956");
                break;
            case "btShowSets":
                btShowSets.setStyle("-fx-background-color:#3D4956");
                break;
            case "btCreateNewSet":
                btCreateNewSet.setStyle("-fx-background-color:#3D4956");
                break;
            case "btDeleteSets":
                btDeleteSets.setStyle("-fx-background-color:#3D4956");
                break;
        }
    }

    @FXML
    private void mouseNotHovered(MouseEvent event) {
        Button button = (Button) event.getSource();
        String buttonName = button.getId();

        if (buttonName.equals("showItemsBT") && !acutalPane.equals("rentsManagerPane")) {
            showItemsBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("rentBT") && !acutalPane.equals("rentsPane")) {
            rentBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("exportBT") && !acutalPane.equals("exportPane")) {
            exportDatasBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("returnProductBT") && !acutalPane.equals("returnProductPane")) {
            returnProductBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("userManagerBT") && !acutalPane.equals("userManagerPane")) {
            userManagerBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("exportDatasBT") && !acutalPane.equals("exportDatasPane")) {
            exportDatasBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("btCreateNewSet") && !acutalPane.equals("setsManagerPane")) {
            btCreateNewSet.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("btShowSets") && !acutalPane.equals("showSetsPane")) {
            btShowSets.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("btAddItem") && !acutalPane.equals("addItemPane")) {
            btAddItem.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("btDeleteItem") && !acutalPane.equals("deleteItemPane")) {
            btDeleteItem.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("returnProductBT") && !acutalPane.equals("returnProductPane")) {
            returnProductBT.setStyle("-fx-background-color:transparent");
        } else if (buttonName.equals("btDeleteSets") && !acutalPane.equals("deleteSetsPane")) {
            btDeleteSets.setStyle("-fx-background-color:transparent");
        }
    }

    /**
     * Switcht das Theme!
     */

    @FXML
    private synchronized void changeFont() throws IOException {
        if (actualShowingPane == null) return;
        actualShowingPane.getStylesheets().clear();
        mainPane.getStylesheets().clear();

        if (changeThemeBT.isSelected()) {
            imageVCancelBT.setImage(new Image("icons/cancelmusic1.png"));
            mainPane.getStylesheets().add("css/mainPaneWHITE.css");
            if (actualShowingPane.getId().equals("exportDatasPane")) {
                actualShowingPane.getStylesheets().add("css/exportDatasWHITE.css");
            } else if (actualShowingPane.getId().equals("addItemPane")) {
                actualShowingPane.getStylesheets().add("css/addItemWHITE.css");
            } else if (actualShowingPane.getId().equals("rentsPane")) {
                actualShowingPane.getStylesheets().add("css/rentsWHITE.css");
            } else if (actualShowingPane.getId().equals("returnProductPane")) {
                actualShowingPane.getStylesheets().add("css/returnProductWHITE.css");
            } else if (actualShowingPane.getId().equals("userManagerPane")) {
                actualShowingPane.getStylesheets().add("css/userManagerWHITE.css");
            } else if (actualShowingPane.getId().equals("rentManagerPane")) {
                actualShowingPane.getStylesheets().add("css/rentsManagerWHITE.css");
            } else if (actualShowingPane.getId().equals("setsManagerPane")) {
                actualShowingPane.getStylesheets().add("css/setsManagerWHITE.css");
            } else if (actualShowingPane.getId().equals("showSetsPane")) {
                actualShowingPane.getStylesheets().add("css/showSetsWHITE.css");
            } else if (actualShowingPane.getId().equals("deleteItemPane")) {
                actualShowingPane.getStylesheets().add("css/deleteItemWHITE.css");
            } else if (actualShowingPane.getId().equals("deleteSetsPane")) {
                actualShowingPane.getStylesheets().add("css/deleteSetsWHITE.css");
            }
            theme = true;
        } else {
            imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
            mainPane.getStylesheets().add("css/mainPaneDARK.css");

            if (actualShowingPane.getId().equals("exportDatasPane")) {
                actualShowingPane.getStylesheets().add("css/exportDatasDARK.css");
            } else if (actualShowingPane.getId().equals("addItemPane")) {
                actualShowingPane.getStylesheets().add("css/addItemDARK.css");
            } else if (actualShowingPane.getId().equals("rentsPane")) {
                actualShowingPane.getStylesheets().add("css/rentsDARK.css");
            } else if (actualShowingPane.getId().equals("returnProductPane")) {
                actualShowingPane.getStylesheets().add("css/returnProductDARK.css");
            } else if (actualShowingPane.getId().equals("userManagerPane")) {
                actualShowingPane.getStylesheets().add("css/userManagerDARK.css");
            } else if (actualShowingPane.getId().equals("rentManagerPane")) {
                actualShowingPane.getStylesheets().add("css/rentsManagerDARK.css");
            } else if (actualShowingPane.getId().equals("setsManagerPane")) {
                actualShowingPane.getStylesheets().add("css/setsManagerDARK.css");
            } else if (actualShowingPane.getId().equals("showSetsPane")) {
                actualShowingPane.getStylesheets().add("css/showSetsDARK.css");
            } else if (actualShowingPane.getId().equals("deleteItemPane")) {
                actualShowingPane.getStylesheets().add("css/deleteItemDARK.css");
            } else if (actualShowingPane.getId().equals("deleteSetsPane")) {
                actualShowingPane.getStylesheets().add("css/deleteSetsDARK.css");
            }
            theme = false;
        }
    }

    /**
     * Wechselt zwischen den verschiedenen Panes hin und her
     *
     * @param event Event in welchen steht, welcher Button gedrückt wurde!
     * @throws IOException IOException
     */
    @FXML
    private void switchPane(ActionEvent event) throws IOException {
        changeThemeBT.setVisible(true);
        Button button = (Button) event.getSource();
        String buttonName = button.getId();

        btProductManager.setStyle("-fx-background-color:transparent");
        showItemsBT.setStyle("-fx-background-color:transparent");
        rentBT.setStyle("-fx-background-color:transparent");
        userManagerBT.setStyle("-fx-background-color:transparent");
        returnProductBT.setStyle("-fx-background-color:transparent");
        exportDatasBT.setStyle("-fx-background-color:transparent");
        btSetsManager.setStyle("-fx-background-color:transparent");
        btCreateNewSet.setStyle("-fx-background-color:transparent");
        btShowSets.setStyle("-fx-background-color:transparent");
        btAddItem.setStyle("-fx-background-color:transparent");
        btDeleteItem.setStyle("-fx-background-color:transparent");
        btDeleteSets.setStyle("-fx-background-color:transparent");

        subPane.getChildren().clear();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        switch (buttonName) {

            case "btAddItem":
                addItemPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/AddItem.fxml")));
                actualShowingPane = addItemPane;
                btAddItem.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(addItemPane);
                addItemPane.getStylesheets().clear();
                addItemPane.setPrefWidth(bounds.getWidth() - 280);
                addItemPane.setPrefHeight(bounds.getHeight() - 120);
                if (theme) {
                    addItemPane.getStylesheets().add("css/addItemWHITE.css");
                } else {
                    addItemPane.getStylesheets().add("css/addItemDARK.css");
                }
                acutalPane = "addItemPane";
                break;
            case "btDeleteItem":
                deleteItemPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/DeleteItem.fxml")));
                actualShowingPane = deleteItemPane;
                btDeleteItem.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(deleteItemPane);
                deleteItemPane.getStylesheets().clear();
                deleteItemPane.setPrefWidth(bounds.getWidth() - 280);
                deleteItemPane.setPrefHeight(bounds.getHeight() - 120);
                if (theme) {
                    deleteItemPane.getStylesheets().add("css/deleteItemWHITE.css");
                } else {
                    deleteItemPane.getStylesheets().add("css/deleteItemDARK.css");
                }
                acutalPane = "deleteItemPane";
                break;
            case "showItemsBT":
                changeThemeBT.setVisible(false);
                rentManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/RentManagerScene.fxml")));
                actualShowingPane = rentManagerPane;
                showItemsBT.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(rentManagerPane);
                rentManagerPane.getStylesheets().clear();
                rentManagerPane.setPrefWidth(bounds.getWidth() - 280);
                rentManagerPane.setPrefHeight(bounds.getHeight() - 120);

                rentManagerPane.getStylesheets().add("css/rentsManagerDARK.css");
                mainPane.getStylesheets().clear();
                imageVCancelBT.setImage(new Image("/icons/cancelmusic.png"));
                mainPane.getStylesheets().add("css/mainPaneDARK.css");
                acutalPane = "rentsManagerPane";
                changeThemeBT.setSelected(false);
                theme = false;
                break;
            case "rentBT":
                rentsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Rents.fxml")));
                actualShowingPane = rentsPane;
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
            case "returnProductBT":
                returnProductPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ReturnProductScene.fxml")));
                actualShowingPane = returnProductPane;
                returnProductBT.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(returnProductPane);
                returnProductPane.setPrefWidth(bounds.getWidth() - 280);
                returnProductPane.setPrefHeight(bounds.getHeight() - 120);
                returnProductPane.getStylesheets().clear();

                if (theme) {
                    returnProductPane.getStylesheets().add("css/returnProductWHITE.css");
                } else {
                    returnProductPane.getStylesheets().add("css/returnProductDARK.css");
                }
                acutalPane = "returnProductPane";
                break;
            case "userManagerBT":
                userManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/UserManagerScene.fxml")));
                actualShowingPane = userManagerPane;
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
                actualShowingPane = exportDatasPane;
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
            case "btCreateNewSet":
                setsManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/SetsManager.fxml")));
                actualShowingPane = setsManagerPane;
                btCreateNewSet.setStyle("-fx-background-color:#3D4956");
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
            case "btShowSets":
                showSetsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ShowSets.fxml")));
                actualShowingPane = showSetsPane;
                btShowSets.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(showSetsPane);
                showSetsPane.setPrefWidth(bounds.getWidth() - 280);
                showSetsPane.setPrefHeight(bounds.getHeight() - 120);
                showSetsPane.getStylesheets().clear();

                if (theme) {
                    showSetsPane.getStylesheets().add("css/showSetsWHITE.css");
                } else {
                    showSetsPane.getStylesheets().add("css/showSetsDARK.css");
                }
                acutalPane = "showSetsPane";
                break;
            case "btDeleteSets":
                deleteSetsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/DeleteSets.fxml")));
                actualShowingPane = deleteSetsPane;
                btDeleteSets.setStyle("-fx-background-color:#3D4956");
                subPane.getChildren().add(deleteSetsPane);
                deleteSetsPane.setPrefWidth(bounds.getWidth() - 280);
                deleteSetsPane.setPrefHeight(bounds.getHeight() - 120);
                deleteSetsPane.getStylesheets().clear();

                if (theme) {
                    deleteSetsPane.getStylesheets().add("css/deleteSetsWHITE.css");
                } else {
                    deleteSetsPane.getStylesheets().add("css/deleteSetsDARK.css");
                }
                acutalPane = "deleteSetsPane";
                break;
            case "dashboardBT":
                changeThemeBT.setVisible(false);
                dashboardPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/DashboardScene.fxml")));
                actualShowingPane = dashboardPane;
                subPane.getChildren().add(dashboardPane);
                dashboardPane.setPrefWidth(bounds.getWidth() - 280);
                dashboardPane.setPrefHeight(bounds.getHeight() - 120);
                acutalPane = "dashBoardPane";
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeThemeBT.setVisible(false);
        theme = false;
        try {
            //addItemPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/AddItem.fxml")));
            //showItemPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ShowItemsScene.fxml")));
            //rentsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/Rents.fxml")));
            //returnProductPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ReturnProductScene.fxml")));
            //userManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/UserManagerScene.fxml")));
            //exportDatasPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ExportDatas.fxml")));
            dashboardPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/DashboardScene.fxml")));
            //rentManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/RentManagerScene.fxml")));
            //setsManagerPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/SetsManager.fxml")));
            //showSetsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ShowSets.fxml")));
            //deleteItemPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/DeleteItem.fxml")));
            deleteSetsPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/DeleteSets.fxml")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        subPane.setPrefWidth(bounds.getWidth() - 280);
        subPane.setPrefHeight(bounds.getHeight() - 120);

        /*addItemPane.setPrefWidth(bounds.getWidth() - 280);
        addItemPane.setPrefHeight(bounds.getHeight() - 120);

        showItemPane.setPrefWidth(bounds.getWidth() - 280);
        showItemPane.setPrefHeight(bounds.getHeight() - 120);

        rentsPane.setPrefWidth(bounds.getWidth() - 280);
        rentsPane.setPrefHeight(bounds.getHeight() - 120);

        returnProductPane.setPrefWidth(bounds.getWidth() - 280);
        returnProductPane.setPrefHeight(bounds.getHeight() - 120);

        userManagerPane.setPrefWidth(bounds.getWidth() - 280);
        userManagerPane.setPrefHeight(bounds.getHeight() - 120);

        exportDatasPane.setPrefWidth(bounds.getWidth() - 280);
        exportDatasPane.setPrefHeight(bounds.getHeight() - 120);*/

        dashboardPane.setPrefWidth(bounds.getWidth() - 280);
        dashboardPane.setPrefHeight(bounds.getHeight() - 120);

        /*rentManagerPane.setPrefWidth(bounds.getWidth() - 280);
        rentManagerPane.setPrefHeight(bounds.getHeight() - 120);

        setsManagerPane.setPrefWidth(bounds.getWidth() - 280);
        setsManagerPane.setPrefHeight(bounds.getHeight() - 120);

        showSetsPane.setPrefWidth(bounds.getWidth() - 280);
        showSetsPane.setPrefHeight(bounds.getHeight() - 120);*/

        deleteSetsPane.setPrefWidth(bounds.getWidth() - 280);
        deleteSetsPane.setPrefHeight(bounds.getHeight() - 120);


        subPane.getChildren().add(dashboardPane);
        recLayout.setHeight(bounds.getHeight() - 71);

        if (!DBConnection.getInstance().getActualUser().equals("stuetz") && !DBConnection.getInstance().getActualUser().equals("renedeicker")
                && !DBConnection.getInstance().getActualUser().equals("stefanleithenmayr") && !DBConnection.getInstance().getActualUser().equals("maxhofer")) {
            exportDatasBT.setVisible(false);
            returnProductBT.setVisible(false);
            userManagerBT.setVisible(false);
            statIV.setVisible(false);
            expIV.setVisible(false);
            userIV.setVisible(false);
        }
        isDownRents = false;
        isDownSetsManager = false;
        acutalPane = "";
        /*try {
        try {
            DBConnection.getInstance().InsertTestDatas();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
}