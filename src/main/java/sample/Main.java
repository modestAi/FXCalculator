package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;


public class Main extends Application{
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("main.fxml")));
        root.getStylesheets().add("Style.css");
        stage.setTitle("FXCalculator");
        setIcon(stage, "src/main/resources/icon.png");
        stage.setResizable(false);
        fixDrag(stage, root);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

    }

    private void fixDrag(Stage stage, Parent root) {
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }


    private void setIcon(Stage stage, String fileDirectory) throws FileNotFoundException {
        Image img = new Image(new FileInputStream(fileDirectory));
        stage.getIcons().add(img);
    }

    public static void main(String[] args) {
        launch(args);
    }
}