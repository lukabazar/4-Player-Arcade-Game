import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class Collectable extends GameObject {

    private final ImageView collectable = new ImageView();

    public Collectable(Fruit type, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.collectable.setFitWidth(width);
        this.collectable.setFitHeight(height);
        createCollectable(type);
    }

    private void createCollectable(Fruit type) {
        this.collectable.setPreserveRatio(true);
        this.collectable.setX(super.getX());
        this.collectable.setY(super.getY());
        if(type == Fruit.BANANA) {
            collectable.setImage(Fruit.BANANA.getImage());
        }
        else if(type == Fruit.CHERRY) {
            collectable.setImage(Fruit.CHERRY.getImage());
        }
        else if(type == Fruit.GUAVA) {
            collectable.setImage(Fruit.GUAVA.getImage());
        }
    }

    @Override
    public Node getGameObject() {
        return this.collectable;
    }
}
