package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import loginPackage.DBConnection;
import model.Clock;
import model.Rent;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class DashboardController implements Initializable, Observer {

    //FXML variables
    @FXML
    private Text timeOutput;
    @FXML
    private TableView<Rent> tableViewRents;
    @FXML
    private TableColumn<?, ?> userNameCol, fromCol, untilCol;

    //No FXML Methods, but two "Override Methods" instead
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Calendar time = Calendar.getInstance();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int min = time.get(Calendar.MINUTE);
        int sec = time.get(Calendar.SECOND);
        timeOutput.setText(String.format("%02d:%02d:%02d\n", hour, min, sec));
        Clock.getInstance().addObserver(this);
        Clock.getInstance().triggerObserver();

        userNameCol.setCellValueFactory(
                new PropertyValueFactory<>("UserName"));

        fromCol.setCellValueFactory(
                new PropertyValueFactory<>("From"));

        untilCol.setCellValueFactory(
                new PropertyValueFactory<>("Until"));

        List<Rent> criticalRents = null;
        try {
            criticalRents = FXCollections.observableArrayList(DBConnection.getInstance().getCriticalRents());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewRents.setItems((ObservableList<Rent>) criticalRents);
    }

    @Override
    public void update(Observable o, Object time) {
        Platform.runLater(() -> timeOutput.setText((String) time));
    }
}