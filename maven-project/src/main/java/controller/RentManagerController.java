package controller;

import javafx.animation.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;
import loginPackage.DBConnection;
import model.DataPackage;
import model.ErrorMessageUtils;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;

public class RentManagerController implements Initializable {

    @FXML
    private AnchorPane subPane, cachePane, userSelectionPane, itemSelectionPane;
    @FXML
    private Button backBT;
    @FXML
    private ImageView backIV;
    @FXML
    private Text backText, forwardText, errorTxt;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Rectangle errorRec;

    private int actualPane;
    private FXMLLoader firstLoader, secondLoader;
    private DataPackage actualDataPackage;

    @FXML
    private void showNextPane() throws SQLException, ParseException {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setToX(-1200);
        translateTransition.setDuration(Duration.millis(1000));

        TranslateTransition secondTransition = new TranslateTransition();
        secondTransition.setFromX(1200);
        secondTransition.setToX(0);
        secondTransition.setDuration(Duration.millis(1000));

        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(progressBar.progressProperty(), ((double) 1));
        KeyFrame keyFrame = new KeyFrame(new Duration(1000), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        if (actualPane == 0) {
            actualDataPackage = ((UserSelectionController) firstLoader.getController()).getData();
            if (actualDataPackage == null) {
                errorRec.setFill(Color.web("#f06060"));
                errorRec.setStroke(Color.web("#f06060"));
                ErrorMessageUtils.showErrorMessage("Please select a User, StartDate and EndDate!", errorRec, errorTxt);
                return;
            }
            if (actualDataPackage.getStartDate().compareTo(actualDataPackage.getEndDate()) > 0) {
                errorRec.setFill(Color.web("#f06060"));
                errorRec.setStroke(Color.web("#f06060"));
                ErrorMessageUtils.showErrorMessage("EndDate is before StartDate", errorRec, errorTxt);
                return;
            }
            backBT.setVisible(true);
            backIV.setVisible(true);
            backText.setVisible(true);
            translateTransition.setNode(userSelectionPane);
            secondTransition.setNode(itemSelectionPane);
            cachePane.getChildren().add(itemSelectionPane);
            actualPane++;

            ((ItemSelectionController) secondLoader.getController()).refresh();
            translateTransition.setOnFinished(event -> {
                subPane.getChildren().clear();
                subPane.getChildren().add(itemSelectionPane);
            });

            secondTransition.setOnFinished(event -> cachePane.getChildren().clear());
            forwardText.setText("Finish");
            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().addAll(translateTransition, secondTransition, timeline);
            parallelTransition.play();
        } else if (actualPane == 1) {
            List<Product> products = ((ItemSelectionController) secondLoader.getController()).getSelectedItems();
            if (products == null || products.isEmpty()) {
                errorRec.setFill(Color.web("#f06060"));
                errorRec.setStroke(Color.web("#f06060"));
                ErrorMessageUtils.showErrorMessage("No Products selected!", errorRec, errorTxt);
                return;
            }

            for (Product p : products) {
                if (p.getStatusID() == 1) {
                    products.remove(p);
                }
            }

            DBConnection.getInstance().createRent(products, actualDataPackage);
            errorRec.setFill(Color.web("#00802b"));
            errorRec.setStroke(Color.web("#00802b"));
            ErrorMessageUtils.showErrorMessage("Successfully Inserted", errorRec, errorTxt);
            this.reset();
        }
    }

    private void reset() {
        this.initialize(null, null);
    }

    @FXML
    private void showPreviousPane() {
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

        if (actualPane == 1) {
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
        subPane.getChildren().clear();
        progressBar.setProgress(0);
        this.actualPane = 0;
        backIV.setVisible(false);
        backBT.setVisible(false);
        backText.setVisible(false);

        try {
            firstLoader = new FXMLLoader();
            firstLoader.setLocation(getClass().getClassLoader().getResource("fxml/UserSelectionScene.fxml"));
            userSelectionPane = firstLoader.load();

            Task<Parent> loadTask = new Task<Parent>() {
                @Override
                public Parent call() throws IOException {
                    secondLoader = new FXMLLoader();
                    secondLoader.setLocation(getClass().getClassLoader().getResource("fxml/ItemSelectionScene.fxml"));
                    itemSelectionPane = secondLoader.load();
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();
                    itemSelectionPane.setPrefWidth(bounds.getWidth() - 280);
                    itemSelectionPane.setPrefHeight(bounds.getHeight() - 300);
                    return itemSelectionPane;
                }
            };

            Thread thread = new Thread(loadTask);
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        userSelectionPane.setPrefWidth(bounds.getWidth() - 280);
        userSelectionPane.setPrefHeight(bounds.getHeight() - 300);
        subPane.getChildren().add(userSelectionPane);
    }
}