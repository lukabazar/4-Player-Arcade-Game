import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Level {

    public enum Mode {LEVEL1, LEVEL2};
    private enum PlatformType {STANDARD, ROPE}
    private final Scene scene;
    private final Pane pane;
    private final List<Label> labels;
    private final int multi;
    private final Mode level;
    private List<GameObject> platforms;
    private List<Rope> ropes;
    private List<Collectable> fruits;
    private List<Enemy> enemies;
    private Player player;
    private boolean isWin = false;
    boolean isPressed = false;
    int numRopes = 0;

    /**
     * Places all game objects onto the level
     *
     * @param scene JavaFX scene
     * @param pane Pane to place assets
     * @param multi Multiple used to scale window
     */
    public Level(Scene scene, Pane pane, List<Label> labels, int multi, Mode level) {
        this.scene = scene;
        this.pane = pane;
        this.multi = multi;
        this.level = level;
        this.labels = labels;
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
            int count = 0;
            @Override
            public void handle(long now) {
                if(isWin) {
                    this.stop();
                }
                if(count % 120 == 0) {
                    if(Integer.parseInt(labels.get(0).getText().substring(7)) == 0) {
                        labels.get(0).setText("Score: 0");
                        player.setLives(0);
                    }
                    else {
                        int currentScore = Integer.parseInt(labels.get(0).getText().substring(7));
                        if(level == Mode.LEVEL1) {
                            labels.get(0).setText("Score: " + (currentScore - 100));
                        }
                        else {
                            labels.get(0).setText("Score: " + (currentScore + 100));
                        }
                    }
                }
                if(count % 600 == 0) {
                    if(level == Mode.LEVEL1) {
                        enemies.add(new Enemy(80 * multi,56 * multi,16 * multi, 8 * multi));
                    }
                    else if(level == Mode.LEVEL2) {
                        enemies.add(new Enemy(48 * multi,56 * multi,16 * multi, 8 * multi));
                    }
                }
                if (count % 8 == 0) {
                    if (player.getCycle() == 0) {
                        player.setCycle(1);
                        for(Enemy enemy : enemies) {
                            enemy.setCycle(1);
                        }
                    }
                    else if (player.getCycle() == 1) {
                        player.setCycle(2);
                        for(Enemy enemy : enemies) {
                            enemy.setCycle(0);
                        }
                    }
                    else if (player.getCycle() == 2){
                        player.setCycle(3);
                    }
                    else {
                        player.setCycle(0);
                    }
                }
                update();
                count = (count + 1) % 3600;
            }
        };
        timer.start();
    }

    /**
     * Updates the screen based on user input and gravity
     */
    private void update() {
        if(player.getFallCount() >= 8 * multi) {
            if(player.isGrounded()) {
                if(level == Mode.LEVEL1) {
                    labels.get(0).setText("Score: 5000");
                }
                player.respawn(labels.get(1));
            }
        }

        player.changeSprite();

        for(Enemy enemy : enemies) {
            pane.getChildren().remove(enemy.getGameObject());
            pane.getChildren().add(enemy.getGameObject());
            enemy.changeSprite();
        }

        player.setX(player.getX() + player.xVelocity());
        player.setY(player.getY() + player.yVelocity());

        // Gravity
        if (!player.isClimbing()) {
            player.setyVelocity(player.yVelocity() + 0.25 / 3.0 * multi);
        }

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
                if(numRopes == 1) {
                    player.setyVelocity(0);
                    player.setClimbing(false);
                    player.setClimbingSpecial(false);
                }
                else {
                    player.setyVelocity(-2 / 3.0 * multi);
                }
            }
            else {
                player.setyVelocity(-0.75 / 3.0 * multi);
            }
            player.isCycle(true);
        }
        else if (event.getCode() == KeyCode.DOWN && player.isClimbing()) {
            if (player.isClimbingSpecial()) {
                if(numRopes == 1) {
                    player.setyVelocity(0);
                    player.setClimbing(false);
                    player.setClimbingSpecial(false);
                }
                else {
                    player.setyVelocity(1 / 3.0 * multi);
                }
                player.isCycle(true);
            }
            else {
                if(numRopes == 0) {
                    player.setClimbing(false);
                }
                player.setyVelocity(2 / 3.0 * multi);
            }
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

        player.setGrounded(false);

        platformCollision();

        numRopes = 0;
        for (Rope rope : ropes) {
            if (isCollision(player, rope) && player.getY() + player.getHeight() / 2 < rope.getY() + rope.getHeight() &&
                    player.getY() > rope.getY()) {
                if(rope.isWinner()) {
                    isWin = true;
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

        for (Collectable fruit : fruits) {
            if(player.getGameObject().getBoundsInParent().intersects(fruit.getHitBox().getBoundsInParent())) {
                fruit.setFalling();
                fruit.getHitBox().relocate(0,0);
                int scoreInt = Integer.parseInt(labels.get(0).getText().substring(7));
                labels.get(0).setText("Score: " + (scoreInt + 400));
            }
            for(Enemy enemy : enemies) {
                if(isCollision(fruit, enemy)) {
                    enemy.getGameObject().relocate(256 * multi, 240 * multi);
                    pane.getChildren().remove(enemy.getGameObject());
                }
            }
            if(fruit.isFalling()) {
                fruit.setY(fruit.getY() + 2 * multi);
                if(fruit.getY() > 240 * multi) {
                    fruit.getGameObject().relocate(0,0);
                    pane.getChildren().remove(fruit.getGameObject());
                    pane.getChildren().remove(fruit.getHitBox());
                }
            }
        }
        for(Enemy enemy : enemies) {
            if(isCollision(player, enemy)) {
                if(level == Mode.LEVEL1) {
                    labels.get(0).setText("Score: 5000");
                }
                player.respawn(labels.get(1));
            }
        }
    }

    /**
     * Handles platform collisions
     */
    private void platformCollision() {
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
                    player.setyVelocity(0);
                    player.setY(platform.getY() - player.getHeight());
                }
                else if(player.getGameObject().getScaleX() == 1 &&
                        player.getX() + 2 * 9 * multi > platform.getX() + platform.getWidth()) {
                    player.setyVelocity(0);
                    player.setY(platform.getY() - player.getHeight());
                }
                player.setGrounded(false);
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

        if(level == Mode.LEVEL1) {
            if (player.getX() > pane.getWidth() - player.getWidth()) {
                player.setX(pane.getWidth() - player.getWidth());
            }

            else if (player.getY() > pane.getHeight() + player.getHeight()) {
                if(level == Mode.LEVEL1) {
                    labels.get(0).setText("Score: 5000");
                }
                player.respawn(labels.get(1));
            }
        }

        else if(level == Mode.LEVEL2) {
            if (player.getX() < 24 * multi) {
                player.setX(24 * multi);
            }
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
        for(Collectable fruit : fruits) {
            pane.getChildren().add(fruit.getGameObject());
            pane.getChildren().add(fruit.getHitBox());
        }
        for(GameObject enemy : enemies) {
            pane.getChildren().add(enemy.getGameObject());
        }
        if(level == Mode.LEVEL1) {
            ropes.get(ropes.size() - 1).setWinner();
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

        if(level == Mode.LEVEL1) {
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

        if(level == Mode.LEVEL2) {
            try (InputStream in = Level.class.getResourceAsStream("ropes-02.txt")) {
                platformsFromStream(in, PlatformType.ROPE);
            } catch (IOException e) {
                System.out.println("No File.");
            }

            try (InputStream in = Level.class.getResourceAsStream("platforms-02.txt")) {
                platformsFromStream(in, PlatformType.STANDARD);
            } catch (IOException e) {
                System.out.println("No File.");
            }
        }
    }


    private void makeCollectables() {
        fruits = new ArrayList<>();
        if(level == Mode.LEVEL1) {
            int size = 16 * multi;
            fruits.add(new Collectable(Collectable.Fruit.CHERRY, 96 * multi, 81 * multi, size, size));
            fruits.add(new Collectable(Collectable.Fruit.GUAVA, 32 * multi, 81 * multi, size, size));
            fruits.add(new Collectable(Collectable.Fruit.GUAVA, 153 * multi, 98 * multi, size, size));
            fruits.add(new Collectable(Collectable.Fruit.BANANA, 136 * multi, 145 * multi, size, size));
        }
    }

    private void makeEnemies() {
        enemies = new ArrayList<>();
    }

    /**
     * Creates player
     */
    private void makePlayer() {
        player = new Player(0, 200 * multi, 27 * multi, 16 * multi, 3);
    }

}
