import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Level {

    private final Pane pane;
    private final int mult;
    private Collection<Platform> platforms;
    private Collection<Collectable> collectables;
    private Collection<Enemy> enemies;

    private Player player;

    public Level(Pane pane, int mult) {
        this.pane = pane;
        this.mult = mult;
        makePlatforms();
        makeCollectables();
        makeEnemies();
        addAll();
    }

    private void addAll() {
        for(Platform platform : platforms) {
            pane.getChildren().add(platform.getPlatform());
        }
    }

    private void platformsFromStream(InputStream in) {
        assert in != null;
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
            platforms.add(new Platform(x, y, width, height));
        }
    }

    private void makePlatforms() {
        platforms = new ArrayList<>();

        try (InputStream in = Level.class.getResourceAsStream("platforms-01.txt")) {
            platformsFromStream(in);
        }
        catch (IOException e) {
            System.out.println("No File.");
        }

        try (InputStream in = Level.class.getResourceAsStream("ropes-01.txt")) {
            platformsFromStream(in);
        }
        catch (IOException e) {
            System.out.println("No File.");
        }
    }

    private void makeCollectables() {
        collectables = new ArrayList<>();

    }

    private void makeEnemies() {
        enemies = new ArrayList<>();

    }

}
