import java.awt.Image;
import java.util.ArrayList;

public class PlacedWaterSpoutTile {
    int x;
    int y;
    int id;
    Image image;
    ArrayList<PlacedWaterCurrentTile> placedWaterCurrentTileArr = new ArrayList<>();

    PlacedWaterSpoutTile(int x, int y, int id, Image image) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.image = image;
    }

    public boolean isClicked(int clickX, int clickY) {
        System.out.println("WaterSpout isClicked()");
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

    public void addPlacedWaterCurrentTile(PlacedWaterCurrentTile tile) {
        placedWaterCurrentTileArr.add(tile);
    }

    public boolean hasClickedPlacedWaterCurrentTile(int clickX, int clickY) {
        for (PlacedWaterCurrentTile tile : placedWaterCurrentTileArr) {
            if (tile.isClicked(clickX, clickY)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<PlacedWaterCurrentTile> getPlacedWaterCurrentTileArr() {
        return this.placedWaterCurrentTileArr;
    }
}