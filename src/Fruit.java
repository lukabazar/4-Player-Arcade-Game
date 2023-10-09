import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    public Image getImage() {
        return this.img.getImage();
    }
}
