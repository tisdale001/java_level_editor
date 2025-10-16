import java.util.ArrayList;


public class BeastieTileSet {
    private ArrayList<BeastieTile> beastieTileArr = new ArrayList<>();

    public void addBeastieTile(BeastieTile tile) {
        beastieTileArr.add(tile);
    }

    public int getSize() {
        return beastieTileArr.size();
    }

    public ArrayList<BeastieTile> getBeastieTileArr() {
        return this.beastieTileArr;
    }
}