package model;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ErrorMessageUtils {
    public static void showErrorMessage(String msg, Rectangle rec, Text txt) {
        txt.setText(msg);
        FadeTransition fadeTransition =
                new FadeTransition(Duration.millis(1000), rec);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(0.7);

        FadeTransition fadeTransition2 =
                new FadeTransition(Duration.millis(1000), txt);
        fadeTransition2.setFromValue(0);
        fadeTransition2.setToValue(1);

        ParallelTransition firstParallelTransition = new ParallelTransition();
        firstParallelTransition.getChildren().addAll(
                fadeTransition,
                fadeTransition2
        );
        firstParallelTransition.play();

        firstParallelTransition.setOnFinished(e -> {
            fadeTransition.setFromValue(0.7);
            fadeTransition.setToValue(0.7);
            fadeTransition.setDuration(Duration.millis(2000));
            fadeTransition2.setFromValue(1);
            fadeTransition2.setToValue(1);
            fadeTransition2.setDuration(Duration.millis(2000));

            ParallelTransition secondParallelTransition = new ParallelTransition();
            secondParallelTransition.getChildren().addAll(
                    fadeTransition,
                    fadeTransition2
            );
            secondParallelTransition.play();
            secondParallelTransition.setOnFinished(event -> {
                fadeTransition.setFromValue(0.7);
                fadeTransition.setToValue(0);
                fadeTransition.setDuration(Duration.millis(1000));
                fadeTransition2.setFromValue(1);
                fadeTransition2.setToValue(0);
                fadeTransition2.setDuration(Duration.millis(1000));

                ParallelTransition thirdParallelTransition = new ParallelTransition();
                thirdParallelTransition.getChildren().addAll(
                        fadeTransition,
                        fadeTransition2
                );
                thirdParallelTransition.play();
            });
        });
    }
}