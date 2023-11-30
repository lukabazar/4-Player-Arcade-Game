import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Name: Luka Bazar
 * <p>
 * Title Screen and handling
 */
public class Title {

    /**
     * Title screen backgrounds
     */
    private enum Titles {
        TITLE1("backgrounds/title-01.png"), TITLE2("backgrounds/title-02.png");

        private final ImageView img;

        Titles(String imgStr) {
            ImageView tempImg = null;
            try {
                tempImg = new ImageView(imgStr);
            } catch (Exception e) {
                System.out.println("File not found.");
            }
            img = tempImg;
        }

        /**
         * Get image of Sprite
         *
         * @return Image
         */
        public Image getImage() {
            return this.img.getImage();
        }
    }

    private final Scene scene;
    private final BorderPane pane;
    private Level currentLevel;
    private Titles currentTitle = Titles.TITLE1;

    /**
     * Title screen
     *
     * @param width  width of window
     * @param height height of window
     */
    public Title(int width, int height) {
        this.pane = new BorderPane();
        this.scene = new Scene(this.pane, width, height);
        changeBackground(currentTitle.getImage());
    }

    /**
     * Change background
     *
     * @param backgroundImg image to change to
     */
    private void changeBackground(Image backgroundImg) {
        BackgroundSize backgroundSize = new BackgroundSize(scene.getWidth(), scene.getHeight(), false,
                false, false, false);
        pane.setBackground(new Background(new BackgroundImage(backgroundImg, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, null, backgroundSize)));
        handlePress();
    }

    /**
     * Handles key presses for Title screen
     */
    private void handlePress() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP) {
                changeBackground(Titles.TITLE1.getImage());
                currentTitle = Titles.TITLE1;
            }
            else if (event.getCode() == KeyCode.DOWN) {
                changeBackground(Titles.TITLE2.getImage());
                currentTitle = Titles.TITLE2;
            }
            else if (event.getCode() == KeyCode.ENTER) {
                List<Label> labels = makeLabels();
                int portNum = 20;   // not sure what the right port num is, this is just what I usually use, may need to play around with it
                String hostname = ""; // edit to proper hostname, TODO: add input boxes to title screen to add hostname and port number before player selection

                int multi = (int) scene.getHeight() / 240;
                Data thisData = new Data(24 * multi, 200 * multi, true);
                Data otherData = new Data(24 * multi, 200 * multi, true);

                //TODO: create list of 3 sockets for 4 player setup, wait for all 4 to connect before continuing

                if (currentTitle == Titles.TITLE1) {
                    // player1, start server
                    try (ServerSocket serverSocket = new ServerSocket(portNum)) {
                        Socket player2 = serverSocket.accept();

                        currentLevel = new Level(scene, pane, labels, multi, Level.Mode.LEVEL2);
                        changeBackground(new Image("backgrounds/background-02.png"));

                        Server server = new Server(player2, currentLevel, otherData, thisData);

                        Thread serverThread = new Thread(server);
                        serverThread.start();
                    } catch (IOException e) {}
                }
                else {
                    // player2, client connect
                    // TODO: when 4 player is implemented, wait for all 4 players to connect
                    try (Socket player = new Socket(hostname, portNum)) {

                        currentLevel = new Level(scene, pane, labels, (int) scene.getHeight() / 240, Level.Mode.LEVEL2);
                        changeBackground(new Image("backgrounds/background-02.png"));

                        Client client = new Client(player, currentLevel, thisData, otherData);

                        Thread clientThread = new Thread(client);
                        clientThread.start();
                    } catch (IOException e) {}
                }
                HBox hBox = new HBox();
                hBox.getChildren().addAll(labels);
                hBox.setPadding(new Insets(10));
                hBox.setSpacing(10);
                pane.setTop(hBox);
            }
        });
    }

    /**
     * Create labels based on Game Mode
     *
     * @return List of labels
     */
    private List<Label> makeLabels() {
        List<Label> labels = new ArrayList<>();
        if (currentTitle == Titles.TITLE1) {
            labels.add(new Label("Score: 5000"));
            labels.add(new Label("Lives: 3"));
        }
        else {
            labels.add(new Label("Score: 0"));
            labels.add(new Label("Lives: 1"));
        }

        for (Label label : labels) {
            label.setTextFill(Color.WHITE);
            label.setFont(new Font("Stencil", 5 * (scene.getHeight() / 240)));
        }
        return labels;
    }

    /**
     * Get the current level
     *
     * @return current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Get the scene
     *
     * @return scene
     */
    public Scene getScene() {
        return scene;
    }

}
