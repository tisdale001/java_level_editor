import java.util.ArrayList;


public class TileSet {
    private ArrayList<Tile> tileArr = new ArrayList<>();

    public void addTile(Tile tile) {
        tileArr.add(tile);
    }

    public int getSize() {
        return tileArr.size();
    }

    public ArrayList<Tile> getTileArr() {
        return this.tileArr;
    }
}