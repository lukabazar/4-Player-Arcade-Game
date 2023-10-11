import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Level {

    public enum PlatformType {STANDARD, ROPE}
    private final Scene scene;
    private final Pane pane;
    private final int mult;
    private Collection<GameObject> platforms;
    private Collection<GameObject> ropes;
    private Player player;

    private double xVelocity = 0;
    private double yVelocity = 0;
    private boolean isJumping = false;
    private boolean isWalking = false;
    private boolean isClimbing = false;
    private boolean isCycle = false;
    private int cycle = 0;

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
            long last = 0;
            @Override
            public void handle(long now) {
                update();
                if(now - last > 17_500_000) {
                    if(cycle == 0) {
                        cycle = 1;
                    }
                    else {
                        cycle = 0;
                    }
                }
                last = now;
            }
        };
        timer.start();
    }

    private void update() {

        if(!isClimbing) {
            yVelocity += 0.25 / 3.0 * mult;
        }

        changeSprite();

        player.setX(player.getX() + xVelocity);
        player.setY(player.getY() + yVelocity);

        for(GameObject platform : platforms) {
            if (isCollision(player, platform) && onPlatform(player, platform) && player.getY() < platform.getY()) {
                yVelocity = 0;
                isJumping = false;
                isClimbing = false;
                player.setY(platform.getY() - player.getHeight());
            }
            else if(isCollision(player, platform) && isClimbing && player.getY() > platform.getY()) {
                yVelocity = 0;
                player.setY(platform.getY() + platform.getHeight());
            }
        }

        for(GameObject rope : ropes) {
            if (isCollision(player, rope) && player.getY() + player.getHeight()/2 < rope.getY() + rope.getHeight()) {
                if(isJumping) {
                    yVelocity = 0;
                    isJumping = false;
                }
                if(!isClimbing) {
                    player.setX(rope.getX() - player.getWidth()/2 - rope.getWidth());
                }
                isClimbing = true;
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
            if (event.getCode() == KeyCode.LEFT) {
                if(isClimbing) {
                    player.getGameObject().setScaleX(1);
                }
                else {
                    xVelocity = -2/3.0 * mult;
                    isWalking = true;
                    player.getGameObject().setScaleX(-1);
                }
            }
            else if (event.getCode() == KeyCode.RIGHT) {
                if(isClimbing) {
                    player.getGameObject().setScaleX(-1);
                }
                else {
                    xVelocity = 2/3.0 * mult;
                    isWalking = true;
                    player.getGameObject().setScaleX(1);
                }
            }
            else if(event.getCode() == KeyCode.UP && isClimbing) {
                yVelocity = -1/3.0 * mult;
                isCycle = true;
            }
            else if(event.getCode() == KeyCode.DOWN && isClimbing) {
                yVelocity = 2/3.0 * mult;
                isCycle = true;
            }
            else if(event.getCode() == KeyCode.SPACE && !isJumping && !isClimbing) {
                yVelocity = -5/3.0 * mult;
                isJumping = true;
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                xVelocity = 0;
                isWalking = false;
            }
            else if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                yVelocity = 0;
                isCycle = false;
            }
        });
    }

    private void changeSprite() {
        if(isClimbing) {
            if(cycle == 0 && isCycle) {
                player.setImage("/sprites/climb-01.png");
            }
            else {
                player.setImage("/sprites/climb-02.png");
            }
        }
        else if(isWalking) {
            if(cycle == 0) {
                player.setImage("/sprites/walk-01.png");
            }
            else {
                player.setImage("/sprites/walk-03.png");
            }
        }
        else if(isJumping) {
            player.setImage("/sprites/jump-01.png");
        }
        else {
            player.setImage("/sprites/walk-02.png");
        }
    }

    private boolean isCollision(GameObject object1, GameObject object2) {
        return object1.getGameObject().getBoundsInParent().intersects(object2.getGameObject().getBoundsInParent());
    }

    private boolean onPlatform(GameObject object1, GameObject object2) {
        double upperBound = object2.getX() + object2.getWidth() - object1.getWidth()/2;
        double lowerBound = object2.getX() - object1.getWidth()/2;

        return object1.getX() > lowerBound && object1.getX() < upperBound;
    }

    private void addAll() {
        for(GameObject rope : ropes) {
            pane.getChildren().add(rope.getGameObject());
        }
        for(GameObject platform : platforms) {
            pane.getChildren().add(platform.getGameObject());
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
                platforms.add(new GameObject(x, y, width, height));
            }
            else {
                ropes.add(new Rope(x, y, width, height));
            }
        }
    }

    private void makePlatforms() {
        platforms = new ArrayList<>();
        ropes = new ArrayList<>();

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
        player = new Player(0, 200 * mult, 27 * mult, 16 * mult);
    }

}
