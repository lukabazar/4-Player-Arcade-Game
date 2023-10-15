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
    private int cycle = 0;
    private int dir = 0;
    private EnemySprites startingSprite = EnemySprites.RED1;

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

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public void switchColor() {
        this.startingSprite = EnemySprites.BLUE1;
    }

    public int getCycle() {
        return cycle;
    }

    public void changeSprite() {
        if(cycle == 0) {
            if(startingSprite == EnemySprites.RED1) {
                enemy.setImage(EnemySprites.RED2.getImage());
            }
            else {
                enemy.setImage(EnemySprites.BLUE2.getImage());
            }
        }
        else {
            if(startingSprite == EnemySprites.RED1) {
                enemy.setImage(EnemySprites.RED1.getImage());
            }
            else {
                enemy.setImage(EnemySprites.BLUE2.getImage());
            }
        }
    }

    public void changeDirection() {
        if(dir == 0) {
            dir = 90;
            enemy.setRotate(dir);
        }
        else {
            dir = 0;
            enemy.setRotate(dir);
        }
    }

    @Override
    public Node getGameObject() {
        return enemy;
    }

}
