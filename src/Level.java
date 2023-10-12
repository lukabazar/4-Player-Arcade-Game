import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Level {

    private enum PlatformType {STANDARD, ROPE}
    private final Scene scene;
    private final Pane pane;
    private final int mult;
    private List<GameObject> platforms;
    private List<GameObject> ropes;
    private Player player;

    /**
     * Places all game objects onto the level
     *
     * @param scene JavaFX scene
     * @param pane Pane to place assets
     * @param mult Multiple used to scale window
     */
    public Level(Scene scene, Pane pane, int mult) {
        this.scene = scene;
        this.pane = pane;
        this.mult = mult;
        makeGameObjects();
        makeCollectables();
        makeEnemies();
        makePlayer();
        addAll();
        play();
    }

    /**
     * Holds animation timer in order to update the game
     */
    private void play() {
        AnimationTimer timer = new AnimationTimer() {
            long last = 0;

            @Override
            public void handle(long now) {
                update();
                if (now - last > 17_500_000) {
                    if (player.getCycle() == 0) {
                        player.setCycle(1);
                    }
                    else {
                        player.setCycle(0);
                    }
                }
                last = now;
            }
        };
        timer.start();
    }

    /**
     * Updates the screen based on user input and gravity
     */
    private void update() {

        player.changeSprite();

        if (!player.isClimbing()) {
            player.setyVelocity(player.yVelocity() + 0.25 / 3.0 * mult);
        }

        player.setX(player.getX() + player.xVelocity());
        player.setY(player.getY() + player.yVelocity());

        for (GameObject platform : platforms) {
            if (isCollision(player, platform) && onPlatform(player, platform) && player.getY() < platform.getY()) {
                player.setyVelocity(0);
                player.setJumping(false);
                player.setClimbing(false);
                player.setClimbingSpecial(false);
                player.setGrounded(true);
                player.setY(platform.getY() - player.getHeight());
            }
            else if (isCollision(player, platform) && player.isClimbing() && player.getY() > platform.getY()) {
                if (player.isClimbingSpecial()) {
                    player.setyVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                }
                else if (player.getGameObject().getScaleX() == 1 &&
                         player.getX() + 2 * 9 * mult > platform.getX() + platform.getWidth()) {
                    player.setyVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                }
                else if (player.getGameObject().getScaleX() == -1 &&
                         player.getX() + player.getWidth() - 2 * 9 * mult < platform.getX()) {
                    player.setyVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                }
                player.setGrounded(false);
            }
        }

        for (GameObject rope : ropes) {
            if (isCollision(player, rope) && player.getY() + player.getHeight() / 2 < rope.getY() + rope.getHeight() &&
                player.getY() > rope.getY()) {
                if (player.isJumping()) {
                    player.setyVelocity(0);
                    player.setJumping(false);
                }
                if (!player.isClimbing()) {
                    player.setyVelocity(0);
                    player.setX(rope.getX() - player.getWidth() / 2 - rope.getWidth());
                }
                player.setxVelocity(0);
                player.setClimbing(true);
                player.setGrounded(false);
            }
        }

        if (player.getX() < 0) {
            player.setX(0);
        }
        else if (player.getX() > 256 * mult - player.getWidth()) {
            player.setX(256 * mult - player.getWidth());
        }
        else if (player.getY() > 240 * mult + player.getHeight()) {
            player.setX(0);
            player.setY(200 * mult);
        }

        scene.setOnKeyPressed(event -> {
            if (player.isFalling()) {}
            else if (event.getCode() == KeyCode.LEFT) {
                if (player.isClimbingSpecial()) {
                    player.getGameObject().setScaleX(-1);
                    player.setX(player.getX() - 2.5 *  mult);
                    player.setClimbing(false);
                    player.setClimbingSpecial(false);
                }
                else if (player.isClimbing()) {
                    if (player.getGameObject().getScaleX() == 1 && player.getX() - player.getWidth()/2 > 0) {
                        player.setClimbingSpecial(true);
                        player.setX(player.getX() - player.getWidth() / 2 + ropes.get(0).getWidth());
                        player.getGameObject().setScaleX(-1);
                    }
                    player.getGameObject().setScaleX(1);
                }
                else {
                    player.setxVelocity(-2 / 3.0 * mult);
                    player.setWalking(true);
                    player.getGameObject().setScaleX(-1);
                }
            }
            else if (event.getCode() == KeyCode.RIGHT) {
                if (player.isClimbingSpecial()) {
                    player.getGameObject().setScaleX(1);
                    player.setX(player.getX() + 5.5 * mult);
                    player.setClimbing(false);
                    player.setClimbingSpecial(false);
                }
                else if (player.isClimbing()) {
                    if (player.getGameObject().getScaleX() == -1 && player.getX() + 3*player.getWidth()/2 < 256*mult) {
                        player.setClimbingSpecial(true);
                        player.setX(player.getX() + player.getWidth() / 2);
                        player.getGameObject().setScaleX(1);
                    }
                    player.getGameObject().setScaleX(-1);
                }
                else {
                    player.setxVelocity(2 / 3.0 * mult);
                    player.setWalking(true);
                    player.getGameObject().setScaleX(1);
                }
            }
            else if (event.getCode() == KeyCode.UP && player.isClimbing()) {
                if (player.isClimbingSpecial()) {
                    player.setyVelocity(-2 / 3.0 * mult);
                }
                else {
                    player.setyVelocity(-1 / 3.0 * mult);
                }
                player.isCycle(true);
            }
            else if (event.getCode() == KeyCode.DOWN && player.isClimbing()) {
                if (player.isClimbingSpecial()) {
                    player.setyVelocity(1 / 3.0 * mult);
                }
                else {
                    player.setyVelocity(2 / 3.0 * mult);
                }
                player.isCycle(false);
            }
            else if (event.getCode() == KeyCode.SPACE && !player.isJumping() && !player.isClimbing()) {
                player.setyVelocity(-5 / 3.0 * mult);
                player.setJumping(true);
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                player.setxVelocity(0);
                player.setWalking(false);
            }
            else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                player.setyVelocity(0);
                player.isCycle(false);
            }
        });
    }

    /**
     * Checks for collision between two GameObjects
     *
     * @param object1 First object
     * @param object2 Second object
     * @return true if they collide, false otherwise
     */
    private boolean isCollision(GameObject object1, GameObject object2) {
        return object1.getGameObject().getBoundsInParent().intersects(object2.getGameObject().getBoundsInParent());
    }

    /**
     * Checks if one GameObject is on top of another GameObject
     *
     * @param object1 First object
     * @param object2 Second object
     * @return true if one is on top of the second, false otherwise
     */
    private boolean onPlatform(GameObject object1, GameObject object2) {
        double upperBound = object2.getX() + object2.getWidth() - object1.getWidth() / 2;
        double lowerBound = object2.getX() - object1.getWidth() / 2;
        return object1.getX() > lowerBound && object1.getX() < upperBound;
    }

    /**
     * Adds all GameObjects to screen
     */
    private void addAll() {
        for (GameObject rope : ropes) {
            pane.getChildren().add(rope.getGameObject());
        }
        for (GameObject platform : platforms) {
            pane.getChildren().add(platform.getGameObject());
        }
        pane.getChildren().add(player.getGameObject());
    }

    /**
     * Creates GameObjects from stream
     *
     * @param in input stream
     * @param type current GameObject type to create
     */
    private void platformsFromStream(InputStream in, PlatformType type) {
        InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(isr);
        scanner.useDelimiter(",|\\n");
        scanner.nextLine();
        while (scanner.hasNext()) {
            int width = Integer.parseInt(scanner.next()) * mult;
            int height = Integer.parseInt(scanner.next()) * mult;
            int x = Integer.parseInt(scanner.next()) * mult;
            int y = Integer.parseInt(scanner.next()) * mult;
            scanner.nextLine();
            if (type == PlatformType.STANDARD) {
                platforms.add(new GameObject(x, y, width, height));
            }
            else {
                ropes.add(new Rope(x, y, width, height));
            }
        }
    }

    /**
     * Creates GameObjects, and reads text files
     */
    private void makeGameObjects() {
        platforms = new ArrayList<>();
        ropes = new ArrayList<>();

        try (InputStream in = Level.class.getResourceAsStream("ropes-01.txt")) {
            platformsFromStream(in, PlatformType.ROPE);
        } catch (IOException e) {
            System.out.println("No File.");
        }

        try (InputStream in = Level.class.getResourceAsStream("platforms-01.txt")) {
            platformsFromStream(in, PlatformType.STANDARD);
        } catch (IOException e) {
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

    /**
     * Creates player
     */
    private void makePlayer() {
        player = new Player(0, 200 * mult, 27 * mult, 16 * mult);
    }

}
