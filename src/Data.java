import javafx.scene.image.ImageView;

import java.io.Serializable;

/**
 * Data structure to contain player data
 * @author Samuel Dauk
 */
public class Data implements Serializable {
    private double x;
    private double y;
    private boolean isAlive;
    private ImageView oppA;
    private ImageView oppB;
    private ImageView oppC;
    
    /**
     * Constructor for Data class
     * @param x player x coord
     * @param y player y coord
     * @param isAlive player alive status
     */
    public Data(double x, double y, boolean isAlive) {
        this.x = x;
        this.y = y;
        this.isAlive = isAlive;
        this. oppA = new ImageView();
        this. oppB = new ImageView();
        this. oppC = new ImageView();
    }

    /**
     * Get x coord
     * @return x val
     */
    public double getX() {
        return x;
    }

    /**
     * Set x coord
     * @param val new x val
     */
    public void setX(double val) {
        x = val;
    }

    /**
     * Get y coord
     * @return y val
     */
    public double getY() {
        return y;
    }

    /**
     * Set y coord
     * @param val new y val
     */
    public void setY(double val) {
        y = val;
    }

    /**
     * Get alive status
     * @return alive status
     */
    public boolean getIsAlive() {
        return isAlive;
    }

    /**
     * Set alive status
     * @param val new alive status
     */
    public void setIsAlive(boolean val) {
        isAlive = val;
    }

    public ImageView getOpponentA() { return oppA; }

    public void setOpponentA(ImageView opponent) { oppA = opponent; }

    public ImageView getOpponentB() { return oppB; }

    public void setOpponentB(ImageView opponent) { oppB = opponent; }

    public ImageView getOpponentC() { return oppC; }

    public void setOpponentC(ImageView opponent) { oppC = opponent; }
}
