package model;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ErrorMessageUtils {
    public static void showErrorMessage(String msg, Rectangle rec, Text txt){
        txt.setText(msg);
        FadeTransition fadeTransition =
                new FadeTransition(Duration.millis(1500), rec );
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(0.7);
        fadeTransition.setAutoReverse(false);

        FadeTransition fadeTransition2 =
                new FadeTransition(Duration.millis(1500), txt );
        fadeTransition2.setFromValue(0);
        fadeTransition2.setToValue(0.7);
        fadeTransition2.setAutoReverse(false);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                fadeTransition2
        );
        parallelTransition.play();
    }
}