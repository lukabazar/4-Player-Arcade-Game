import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private final int multi;
    private List<GameObject> platforms;
    private List<Rope> ropes;
    private List<Collectable> fruits;
    private List<Enemy> enemies;
    private Player player;
    boolean isPressed = false;

    /**
     * Places all game objects onto the level
     *
     * @param scene JavaFX scene
     * @param pane Pane to place assets
     * @param multi Multiple used to scale window
     */
    public Level(Scene scene, Pane pane, int multi) {
        this.scene = scene;
        this.pane = pane;
        this.multi = multi;
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
                if(now - last > 50_000_000) {
                    //createEnemy();
                }
                if (now - last > 17_500_000) {
                    if (player.getCycle() == 0) {
                        player.setCycle(1);
                    }
                    else {
                        player.setCycle(0);
                    }
                }
                update();
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
            player.setyVelocity(player.yVelocity() + 0.25 / 3.0 * multi);
        }

        player.setX(player.getX() + player.xVelocity());
        player.setY(player.getY() + player.yVelocity());

        snapToBounds();
        handleCollisions();

        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);
    }

    /**
     * Handles key release events
     *
     * @param event KeyEvent
     */
    private void keyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
            player.setxVelocity(0);
            player.setWalking(false);
            isPressed = false;
        }
        else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
            player.setyVelocity(0);
            player.isCycle(false);
            isPressed = false;
        }
    }

    /**
     * Handles key press events
     *
     * @param event KeyEvent
     */
    private void keyPressed(KeyEvent event) {
        if (player.isFalling()) {}
        else if (event.getCode() == KeyCode.LEFT && !isPressed) {
            if (player.isClimbingSpecial()) {
                player.getGameObject().setScaleX(-1);
                player.setX(player.getX() - 2.5 * multi);
                player.setClimbing(false);
                player.setClimbingSpecial(false);
                isPressed = true;
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
                player.setxVelocity(-2 / 3.0 * multi);
                player.setWalking(true);
                player.getGameObject().setScaleX(-1);
            }
        }
        else if (event.getCode() == KeyCode.RIGHT && !isPressed) {
            if (player.isClimbingSpecial()) {
                player.getGameObject().setScaleX(1);
                player.setX(player.getX() + 5.5 * multi);
                player.setClimbing(false);
                player.setClimbingSpecial(false);
                isPressed = true;
            }
            else if (player.isClimbing()) {
                if (player.getGameObject().getScaleX() == -1 && player.getX() + 3*player.getWidth()/2 < 256* multi) {
                    player.setClimbingSpecial(true);
                    player.setX(player.getX() + player.getWidth() / 2);
                    player.getGameObject().setScaleX(1);
                }
                player.getGameObject().setScaleX(-1);
            }
            else {
                player.setxVelocity(2 / 3.0 * multi);
                player.setWalking(true);
                player.getGameObject().setScaleX(1);
            }
        }
        else if (event.getCode() == KeyCode.UP && player.isClimbing()) {
            if (player.isClimbingSpecial()) {
                player.setyVelocity(-2 / 3.0 * multi);
            }
            else {
                player.setyVelocity(-1 / 3.0 * multi);
            }
            player.isCycle(true);
        }
        else if (event.getCode() == KeyCode.DOWN && player.isClimbing()) {
            if (player.isClimbingSpecial()) {
                player.setyVelocity(1 / 3.0 * multi);
            }
            else {
                player.setyVelocity(2 / 3.0 * multi);
            }
            player.isCycle(false);
        }
        else if (event.getCode() == KeyCode.SPACE && !player.isJumping() && !player.isClimbing()) {
            player.setyVelocity(-5 / 3.0 * multi);
            player.setJumping(true);
        }
    }

    /**
     * Handles all collision factors
     */
    private void handleCollisions() {
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
                        player.getX() + 2 * 9 * multi > platform.getX() + platform.getWidth()) {
                    player.setyVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                }
                else if (player.getGameObject().getScaleX() == -1 &&
                        player.getX() + player.getWidth() - 2 * 9 * multi < platform.getX()) {
                    player.setyVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                }
                else if(player.getX() > platform.getX() &&
                        player.getX() + player.getWidth() < platform.getX() + platform.getWidth()) {
                    player.setyVelocity(0);
                    player.setY(platform.getY() + platform.getHeight());
                }
                player.setGrounded(false);
            }
            else if(isCollision(player, platform) && player.isClimbing() && player.getY() < platform.getY()) {
                if(player.getGameObject().getScaleX() == -1 &&
                   player.getX() + player.getWidth() - 2 * 9 * multi < platform.getX()) {
                    player.setxVelocity(0);
                    player.setY(platform.getY() - player.getHeight());
                }
                else if(player.getGameObject().getScaleX() == 1 &&
                        player.getX() + 2 * 9 * multi > platform.getX() + platform.getWidth()) {
                    player.setyVelocity(0);
                    player.setY(platform.getY() - player.getHeight());
                }
            }
         }

        int numRopes = 0;
        for (Rope rope : ropes) {
            if (isCollision(player, rope) && player.getY() + player.getHeight() / 2 < rope.getY() + rope.getHeight() &&
                    player.getY() > rope.getY()) {
                if(rope.isWinner()) {
                    // TODO
                    System.out.println("WINNER!");
                }
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
                numRopes++;
            }
        }
        if(numRopes == 0) {
            player.setClimbing(false);
        }

        for (Collectable fruit : fruits) {
            if(isCollision(player, fruit)) {
                fruit.setFalling();
            }
            for(Enemy enemy : enemies) {
                if(isCollision(fruit, enemy)) {
                    System.out.println("when worlds collide");
                    enemy.getGameObject().relocate(256 * multi, 240 * multi);
                    pane.getChildren().remove(enemy.getGameObject());
                }
            }
            if(fruit.isFalling()) {
                fruit.setY(fruit.getY() + 2 * multi);
                if(fruit.getY() > 240 * multi) {
                    fruit.getGameObject().relocate(0,0);
                    pane.getChildren().remove(fruit.getGameObject());
                }
            }
        }
    }

    /**
     * Snap player in bounds
     */
    private void snapToBounds() {

        if (player.getX() < 0) {
            player.setX(0);
        }

        /*
        if (player.getX() < 24 * multi) {
            player.setX(24 * multi);
        }
        */

        else if (player.getX() > 256 * multi - player.getWidth()) {
            player.setX(256 * multi - player.getWidth());
        }

        /*
        else if (player.getX() > 232 * multi - player.getWidth()) {
            player.setX(232 * multi - player.getWidth());
        }
        */
        else if (player.getY() > 240 * multi + player.getHeight()) {
            player.setX(0);
            player.setY(200 * multi);
        }
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
        for(GameObject fruit : fruits) {
            pane.getChildren().add(fruit.getGameObject());
        }
        for(GameObject enemy : enemies) {
            pane.getChildren().add(enemy.getGameObject());
        }
        ropes.get(ropes.size() - 1).setWinner();
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
            int width = Integer.parseInt(scanner.next()) * multi;
            int height = Integer.parseInt(scanner.next()) * multi;
            int x = Integer.parseInt(scanner.next()) * multi;
            int y = Integer.parseInt(scanner.next()) * multi;
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
        fruits = new ArrayList<>();
        fruits.add(new Collectable(Collectable.Fruit.CHERRY, 96 * multi, 81 * multi, 16 * multi, 16 * multi));
        fruits.add(new Collectable(Collectable.Fruit.GUAVA, 32 * multi, 81 * multi, 16 * multi, 16 * multi));
        fruits.add(new Collectable(Collectable.Fruit.GUAVA, 153 * multi, 98 * multi, 16 * multi, 16 * multi));
        fruits.add(new Collectable(Collectable.Fruit.BANANA, 136 * multi, 145 * multi, 16 * multi, 16 * multi));
    }

    private void makeEnemies() {
        enemies = new ArrayList<>();
        Enemy enemy = new Enemy(100 * multi, (64 - 8) * multi, 16 * multi, 16 * multi);
        enemy.setEnemy();
        enemies.add(enemy);
    }

    /**
     * Creates player
     */
    private void makePlayer() {
        player = new Player(0, 200 * multi, 27 * multi, 16 * multi);
    }

}
