import java.awt.Image;
import java.util.ArrayList;

public class PlacedMovingColumnBorderBoxTile {
    int x;
    int y;
    int id;
    Image image;

    public PlacedMovingColumnBorderBoxTile(int x, int y, int id, Image image) {
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
        
        System.out.println(clickX >= this.x && clickX <= this.x + width &&
        clickY >= this.y && clickY <= this.y + height);
        return (clickX >= this.x && clickX <= this.x + width &&
                clickY >= this.y && clickY <= this.y + height);
    }
}