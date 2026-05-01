import java.util.ArrayList;


public class MovingColumnTileSet {
    private ArrayList<MovingColumnTile> mcTileArr = new ArrayList<>();

    public void addTile(MovingColumnTile tile) {
        mcTileArr.add(tile);
    }

    public int getSize() {
        return mcTileArr.size();
    }

    public ArrayList<MovingColumnTile> getMovingColumnTileArr() {
        return this.mcTileArr;
    }
}