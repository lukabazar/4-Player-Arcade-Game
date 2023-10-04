import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Collectable {

    private enum Fruit {BANANA, CHERRY, MANGO}

    private final ImageView collectable = new ImageView();
    private final int x;
    private final int y;

    public Collectable(Pane pane, Fruit type, int x, int y) {
        this.x = x;
        this.y = y;
        createCollectable(pane, type);
    }

    private void createCollectable(Pane pane, Fruit type) {
        this.collectable.setPreserveRatio(true);
        this.collectable.setX(x);
        this.collectable.setY(y);
        if(type == Fruit.BANANA) {
            //collectable.setImage();
        }
        else if(type == Fruit.CHERRY) {
            //collectable.setImage();
        }
        else if(type == Fruit.MANGO) {
            //collectable.setImage();
        }
        pane.getChildren().add(this.collectable);
    }
}
