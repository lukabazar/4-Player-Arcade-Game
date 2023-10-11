import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends GameObject {

    private final ImageView dk = new ImageView();
    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        dk.setTranslateX(x);
        dk.setTranslateY(y);
        dk.setPreserveRatio(true);
        dk.setFitHeight(height);
    }

    @Override
    public Node getGameObject() {
        return dk;
    }

    @Override
    public double getX() {
        return dk.getTranslateX();
    }

    @Override
    public double getY() {
        return dk.getTranslateY();
    }

    @Override
    public void setX(double x) {
        dk.setTranslateX(x);
    }

    @Override
    public void setY(double y) {
        dk.setTranslateY(y);
    }

    public void setImage(String imageStr) {
        dk.setImage(new Image(imageStr));
    }
}
