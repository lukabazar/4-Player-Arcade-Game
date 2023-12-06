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
    private boolean isJumping;
    private boolean isWalking;
    private boolean isGrounded;
    private boolean isClimbing;
    private boolean isClimbingSpecial;
    private boolean isCycle;
    private int direction;
    
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
        isJumping = false;
        isWalking = false;
        isGrounded = true;
        isClimbing = false;
        isClimbingSpecial = false;
        isCycle = false;
        direction = 1;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isCycle() {
        return isCycle;
    }

    public void setCycle(boolean cycle) {
        isCycle = cycle;
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

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public void setClimbing(boolean climbing) {
        isClimbing = climbing;
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public void setClimbingSpecial(boolean climbingSpecial) {
        isClimbingSpecial = climbingSpecial;
    }

    public boolean isClimbing() {
        return isClimbing;
    }

    public boolean isClimbingSpecial() {
        return isClimbingSpecial;
    }

    public boolean isGrounded() {
        return isGrounded;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isWalking() {
        return isWalking;
    }

}
