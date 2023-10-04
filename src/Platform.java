import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {
    private final Rectangle platform = new Rectangle();
    public Platform(int x, int y, int width, int height) {
        platform.setFill(Color.HONEYDEW);
        platform.setX(x);
        platform.setY(y);
        platform.setWidth(width);
        platform.setHeight(height);
    }

    public Rectangle getPlatform() {
        return this.platform;
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

    private int getX() {
        return (int) platform.getX();
    }

    private int getY() {
        return (int) platform.getY();
    }

    private int getWidth() {
        return (int) platform.getWidth();
    }

    private int getHeight() {
        return (int) platform.getHeight();
    }

}
