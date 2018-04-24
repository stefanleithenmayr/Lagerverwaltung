package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import model.Clock;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class DashboardController implements Initializable, Observer {

    @FXML
    private Text timeOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Clock.getInstance().addObserver(this);
        Clock.getInstance().triggerObserver();
    }

    @Override
    public void update(Observable o, Object time) {
        Platform.runLater(() -> timeOutput.setText((String)time));
    }
}

