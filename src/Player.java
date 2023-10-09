import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends GameObject {

    private final ImageView dk = new ImageView();
    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.dk.setFitWidth(width);
        this.dk.setFitHeight(height);
        createPlayer();
    }

    private void createPlayer() {
        this.dk.setPreserveRatio(true);
        this.dk.setX(super.getX());
        this.dk.setY(super.getY());
        this.dk.setImage(new Image("sprites/walk-01.png"));
    }

    @Override
    public Node getGameObject() {
        return this.dk;
    }
}
