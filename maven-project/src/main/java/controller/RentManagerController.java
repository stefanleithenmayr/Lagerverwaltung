package controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Observable;
import java.util.ResourceBundle;

public class RentManagerController extends Observable implements Initializable {

    @FXML
    private AnchorPane subPane, cachePane, userSelectionPane, itemSelectionPane;

    @FXML
    private Button backBT, forwardBT;

    @FXML
    private ImageView backIV, forwardIV;

    @FXML
    private Text backText, forwardText;

    @FXML
    private ProgressBar progressBar;

    private int actualPane;

    @FXML
    private void showNextPane() throws InterruptedException {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setToX(-1200);
        translateTransition.setDuration(Duration.millis(1000));

        TranslateTransition secondTransition = new TranslateTransition();
        secondTransition.setFromX(1200);
        secondTransition.setToX(0);
        secondTransition.setDuration(Duration.millis(1000));

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(progressBar.progressProperty(), ((double)1));
        KeyFrame keyFrame = new KeyFrame(new Duration(1000), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        if (actualPane == 0){
            backBT.setVisible(true);
            backIV.setVisible(true);
            backText.setVisible(true);
            translateTransition.setNode(userSelectionPane);
            secondTransition.setNode(itemSelectionPane);
            cachePane.getChildren().add(itemSelectionPane);
            actualPane++;
            translateTransition.setOnFinished(event -> {
                subPane.getChildren().clear();
                subPane.getChildren().add(itemSelectionPane);
            });
            secondTransition.setOnFinished(event -> cachePane.getChildren().clear());
            forwardText.setText("Finish");
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().addAll(translateTransition, secondTransition, timeline);
            parallelTransition.play();
        }

    }

    @FXML
    private void showPreviousPane(){
        forwardText.setText("Previous");
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setFromX(0);
        translateTransition.setToX(1800);
        translateTransition.setDuration(Duration.millis(1000));

        TranslateTransition secondTransition = new TranslateTransition();
        secondTransition.setFromX(-1200);
        secondTransition.setToX(0);
        secondTransition.setDuration(Duration.millis(1000));

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(progressBar.progressProperty(), 0);
        KeyFrame keyFrame = new KeyFrame(new Duration(1000), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        if (actualPane == 1){
            forwardText.setText("Previous");
            backBT.setVisible(false);
            backIV.setVisible(false);
            backText.setVisible(false);
            translateTransition.setNode(itemSelectionPane);
            secondTransition.setNode(userSelectionPane);
            cachePane.getChildren().add(userSelectionPane);
            actualPane--;
            translateTransition.setOnFinished(event -> {
                subPane.getChildren().clear();
                subPane.getChildren().add(userSelectionPane);
            });
            secondTransition.setOnFinished(event -> cachePane.getChildren().clear());
        }
        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(translateTransition, secondTransition, timeline);
        parallelTransition.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.actualPane = 0;
        backIV.setVisible(false);
        backBT.setVisible(false);
        backText.setVisible(false);

        try {
            userSelectionPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/UserSelectionScene.fxml")));
            itemSelectionPane = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/ItemSelectionScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        userSelectionPane.setPrefWidth(bounds.getWidth() - 280);
        userSelectionPane.setPrefHeight(bounds.getHeight() - 470);

        itemSelectionPane.setPrefWidth(bounds.getWidth() - 280);
        itemSelectionPane.setPrefHeight(bounds.getHeight() - 470);

        subPane.getChildren().add(userSelectionPane);
    }
}