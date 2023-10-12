import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Collectable extends GameObject {

    /**
     * Enum containing all Fruit sprites
     */
    public enum Fruit {
        CHERRY("fruits/cherry.png"), BANANA("fruits/banana.png"), GUAVA("fruits/guava.png");

        private final ImageView img;

        Fruit(String imgStr) {
            ImageView tempImg = null;
            try {
                tempImg = new ImageView(imgStr);
            }
            catch (Exception e) {
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

    private final ImageView collectable = new ImageView();

    /**
     * Collectable GameObject to be used in the game
     *
     * @param type Fruit type
     * @param x initial x coordinate
     * @param y initial y coordinate
     * @param width width of collectable
     * @param height height of collectable
     */
    public Collectable(Fruit type, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.collectable.setFitWidth(width);
        this.collectable.setFitHeight(height);
        createCollectable(type);
    }

    /**
     * Create collectable and set corresponding image
     *
     * @param type Fruit type to set
     */
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

    /**
     * Get Node of the Collectable GameObject
     *
     * @return Node of GameObject
     */
    @Override
    public Node getGameObject() {
        return this.collectable;
    }
}
