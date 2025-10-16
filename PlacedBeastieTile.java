import java.awt.Image;

public class PlacedBeastieTile {
    int x;
    int y;
    int id;
    Image image;

    PlacedBeastieTile(int x, int y, int id, Image image) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.image = image;
    }

    public boolean isClicked(int clickX, int clickY) {
        if (image == null) {
            return false;
        }
    
        int width = image.getWidth(null);
        int height = image.getHeight(null);
    
        return (clickX >= this.x && clickX <= this.x + width &&
                clickY >= this.y && clickY <= this.y + height);
    }
}