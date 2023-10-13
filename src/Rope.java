public class Rope extends GameObject {
    private boolean isWinner = false;
    public Rope(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner() {
        this.isWinner = true;
    }
}
