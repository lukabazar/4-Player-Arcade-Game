import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DonkeyKongJr extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int mult = 3;
        int width = 256 * mult;
        int height = 240 * mult;

        Pane root = new Pane();
        Scene scene = new Scene(root, width, height);

        BackgroundSize backgroundSize = new BackgroundSize(width, height, false,
                                          false, false, false);
        root.setBackground(new Background(new BackgroundImage(new Image("background.png"),
                                          BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                  null, backgroundSize)));
        new Level(root, mult);

        primaryStage.setTitle("Donkey Kong Jr.");
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
