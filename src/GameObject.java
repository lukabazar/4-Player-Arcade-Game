import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameObject {
    private final Rectangle gameObject = new Rectangle();
    public GameObject(int x, int y, int width, int height) {
        gameObject.setFill(Color.TRANSPARENT);
        gameObject.setStroke(Color.RED);
        gameObject.setTranslateX(x);
        gameObject.setTranslateY(y);
        gameObject.setWidth(width);
        gameObject.setHeight(height);
    }

    public Node getGameObject() {
        return this.gameObject;
    }

    public double getX() {
        return gameObject.getTranslateX();
    }

    public void setX(double x) {
        gameObject.setTranslateX(x);
    }

    public void setY(double y) {
        gameObject.setTranslateY(y);
    }

    public double getY() {
        return gameObject.getTranslateY();
    }

    public double getWidth() {
        return gameObject.getWidth();
    }

    public double getHeight() {
        return gameObject.getHeight();
    }

}
