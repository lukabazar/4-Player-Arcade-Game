import java.io.Serializable;

/**
 * Data structure to contain player data
 * @author Samuel Dauk
 */
public class Data implements Serializable {
    private int x;
    private int y;
    private boolean isAlive;
    
    /**
     * Constructor for Data class
     * @param x player x coord
     * @param y player y coord
     * @param isAlive player alive status
     */
    public Data(int x, int y, boolean isAlive) {
        this.x = x;
        this.y = y;
        this.isAlive = isAlive;
    }

    /**
     * Get x coord
     * @return x val
     */
    public int getX() {
        return x;
    }

    /**
     * Set x coord
     * @param val new x val
     */
    public void setX(int val) {
        x = val;
    }

    /**
     * Get y coord
     * @return y val
     */
    public int getY() {
        return y;
    }

    /**
     * Set y coord
     * @param val new y val
     */
    public void setY(int val) {
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
}
