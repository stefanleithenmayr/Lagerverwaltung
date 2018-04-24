package loginPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception{Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/LoginScene.fxml"));
        Scene scene = new Scene(root);

        stage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        stage.getIcons().add(new Image("https://raw.githubusercontent.com/stefanleithenmayr/Lagerverwaltung/master/maven-project/src/main/resources/icons/atom.png"));
        stage.setScene(scene);
        stage.setResizable(true);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
