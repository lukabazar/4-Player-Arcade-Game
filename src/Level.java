import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Level {

    private enum PlatformType {STANDARD, ROPE}
    private final Scene scene;
    private final Pane pane;
    private final int mult;
    private Collection<GameObject> gameObjects;
    private Player player;

    public Level(Scene scene, Pane pane, int mult) {
        this.scene = scene;
        this.pane = pane;
        this.mult = mult;
        makePlatforms();
        makeCollectables();
        makeEnemies();
        makePlayer();
        addAll();
        play();
    }

    private void play() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        timer.start();
    }

    private void update() {
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.LEFT) {

            }
            else if(event.getCode() == KeyCode.RIGHT) {

            }
            else if(event.getCode() == KeyCode.UP) {

            }
            else if (event.getCode() == KeyCode.DOWN) {

            }
            else if(event.getCode() == KeyCode.SPACE) {

            }
        });
    }


    private void addAll() {
        for(GameObject gameObject : gameObjects) {
            pane.getChildren().add(gameObject.getGameObject());
        }
        pane.getChildren().add(player.getGameObject());
    }

    private void platformsFromStream(InputStream in, PlatformType type) {
        InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(isr);
        scanner.useDelimiter(",|\\n");
        scanner.nextLine();
        while(scanner.hasNext()) {
            int width = Integer.parseInt(scanner.next()) * mult;
            int height = Integer.parseInt(scanner.next()) * mult;
            int x = Integer.parseInt(scanner.next()) * mult;
            int y = Integer.parseInt(scanner.next()) * mult;
            scanner.nextLine();
            if(type == PlatformType.STANDARD) {
                gameObjects.add(new GameObject(x, y, width, height));
            }
            else {
                gameObjects.add(new Rope(x, y, width, height));
            }
        }
    }

    private void makePlatforms() {
        gameObjects = new ArrayList<>();

        try (InputStream in = Level.class.getResourceAsStream("ropes-01.txt")) {
            platformsFromStream(in, PlatformType.ROPE);
        }
        catch (IOException e) {
            System.out.println("No File.");
        }

        try (InputStream in = Level.class.getResourceAsStream("platforms-01.txt")) {
            platformsFromStream(in, PlatformType.STANDARD);
        }
        catch (IOException e) {
            System.out.println("No File.");
        }
    }

    private void makeCollectables() {
        /*
        gameObjects.add(new Collectable(Fruit.CHERRY, 100 * mult, 100 * mult, 16 * mult, 16 * mult));
        gameObjects.add(new Collectable(Fruit.GUAVA, 200 * mult, 100 * mult, 16 * mult, 16 * mult));
        gameObjects.add(new Collectable(Fruit.GUAVA, 80 * mult, 100 * mult, 16 * mult, 16 * mult));
        gameObjects.add(new Collectable(Fruit.BANANA, 100 * mult, 200 * mult, 16 * mult, 16 * mult));
                 */
    }

    private void makeEnemies() {


    }

    private void makePlayer() {
        player = new Player(0, 200 * mult, 32 * mult, 16 * mult);
    }

}
