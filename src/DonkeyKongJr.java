import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DonkeyKongJr extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        int multi = 1;
        int pixelWidth = 256;
        int pixelHeight = 240;

        for(int i = 1; pixelHeight * i < screenHeight; i++) {
            multi = i;
        }

        int width = pixelWidth * multi;
        int height = pixelHeight * multi;


        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, width, height);

        Level.Mode level = Level.Mode.LEVEL1;

        BackgroundSize backgroundSize = new BackgroundSize(width, height, false,
                                          false, false, false);
        if(level == Level.Mode.LEVEL1) {
            root.setBackground(new Background(new BackgroundImage(new Image("backgrounds/background-01.png"),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    null, backgroundSize)));
        }
        else if(level == Level.Mode.LEVEL2) {
            root.setBackground(new Background(new BackgroundImage(new Image("backgrounds/background-02.png"),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    null, backgroundSize)));
        }

        List<Label> labels = new ArrayList<>();
        if(level == Level.Mode.LEVEL1) {
            labels.add(new Label("Score: 5000"));
            labels.add(new Label("Lives: 3"));
        }
        else {
            labels.add(new Label("Score: 0"));
            labels.add(new Label("Lives: 1"));
        }

        for(Label label : labels) {
            label.setTextFill(Color.WHITE);
        }
        new Level(scene, root, labels, multi, level);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(labels);
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);

        root.setTop(hBox);
        primaryStage.setTitle("Donkey Kong Jr.");
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
