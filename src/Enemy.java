import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Enemy extends GameObject {
    private enum EnemySprites {
        RED1("enemies/red-01.png"), RED2("enemies/red-02.png"),
        BLUE1("enemies/blue-01.png"), BLUE2("enemies/blue-02.png");
        private final ImageView img;
        EnemySprites(String imgStr) {
            ImageView tempImg = null;
            try {
                tempImg = new ImageView(imgStr);
            } catch (Exception e) {
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

    private final ImageView enemy = new ImageView();
    private double spawnX = 0;
    private double spawnY = 0;

    public Enemy(int x, int y, int width, int height) {
        super(x, y, width, height);
        enemy.setTranslateX(x);
        enemy.setTranslateY(y);
        enemy.setPreserveRatio(true);
        enemy.setFitWidth(width);
    }

    public void setEnemy() {
        enemy.setImage(EnemySprites.RED1.getImage());
    }

    public void setSpawn(double spawnX, double spawnY) {
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    @Override
    public Node getGameObject() {
        return enemy;
    }

}
