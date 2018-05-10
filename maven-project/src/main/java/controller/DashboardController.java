package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import model.Clock;

import java.net.URL;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class DashboardController implements Initializable, Observer {

    @FXML
    private Text timeOutput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Calendar time = Calendar.getInstance();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int min = time.get(Calendar.MINUTE);
        int sec = time.get(Calendar.SECOND);
        timeOutput.setText(String.format("%02d:%02d:%02d\n", hour,min,sec));
        Clock.getInstance().addObserver(this);
        Clock.getInstance().triggerObserver();
    }

    @Override
    public void update(Observable o, Object time) {
        Platform.runLater(() -> timeOutput.setText((String)time));
    }
}