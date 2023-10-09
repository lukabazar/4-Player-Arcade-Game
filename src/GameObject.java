import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameObject {
    private final Rectangle gameObject = new Rectangle();
    public GameObject(int x, int y, int width, int height) {
        gameObject.setFill(Color.WHITE);
        gameObject.setStroke(Color.BLACK);
        gameObject.setX(x);
        gameObject.setY(y);
        gameObject.setWidth(width);
        gameObject.setHeight(height);
    }

    public Node getGameObject() {
        return this.gameObject;
    }

    public int getMaxX() {
        return getX() + (getWidth()/2);
    }

    public int getMinX() {
        return getX() - (getWidth()/2);
    }

    public int getMaxY() {
        return getY() - (getHeight()/2);
    }

    public int getMinY() {
        return getY() + (getHeight()/2);
    }

    public int getX() {
        return (int) gameObject.getX();
    }

    public int getY() {
        return (int) gameObject.getY();
    }

    private int getWidth() {
        return (int) gameObject.getWidth();
    }

    private int getHeight() {
        return (int) gameObject.getHeight();
    }

}
