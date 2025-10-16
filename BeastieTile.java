import java.awt.image.BufferedImage;


public class BeastieTile {
    private int ID;
    private BufferedImage image;

    public BeastieTile(BufferedImage scaledImage, int id) {
        this.image = scaledImage;
        this.ID = id;
    }

    public BeastieTile(BufferedImage scaledImage) {
        this(scaledImage, -1);
    }

    public void setID(int id) {
        this.ID = id;
    }

    public int getID() {
        return this.ID;
    }

    public BufferedImage getImage() {
        return this.image;
    }
}