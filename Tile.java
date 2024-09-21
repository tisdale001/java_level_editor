import java.awt.image.BufferedImage;


public class Tile {
    private int ID = -1;
    private BufferedImage image;

    public Tile(BufferedImage scaledImage) {
        this.image = scaledImage;
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