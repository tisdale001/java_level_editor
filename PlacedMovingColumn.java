import java.util.ArrayList;
import java.util.Comparator;


public class PlacedMovingColumn {
    private ArrayList<PlacedMovingColumnBorderBoxTile> placedMovingColumnBorderBoxTileArr = new ArrayList<>();

    public PlacedMovingColumn() {}

    public void addPlacedMovingColumnBorderBoxTile(PlacedMovingColumnBorderBoxTile tile) {
        if (placedMovingColumnBorderBoxTileArr.size() >= 2) {
            placedMovingColumnBorderBoxTileArr.clear();
            return;
        } else {
            // array size is less than 2
            placedMovingColumnBorderBoxTileArr.add(tile);
        }
        // check for validity: x's are the same or y's are the same
        if (placedMovingColumnBorderBoxTileArr.size() == 2) {
            PlacedMovingColumnBorderBoxTile tile1 = placedMovingColumnBorderBoxTileArr.get(0);
            PlacedMovingColumnBorderBoxTile tile2 = placedMovingColumnBorderBoxTileArr.get(1);
            if (!(tile1.x == tile2.x || tile1.y == tile2.y)) {
                placedMovingColumnBorderBoxTileArr.clear();
            }
        }
        placedMovingColumnBorderBoxTileArr.sort(
            Comparator
                .comparingInt((PlacedMovingColumnBorderBoxTile t) -> t.x)
                .thenComparingInt(t -> t.y)
        );
        // check that id's are 1 apart after being sorted
        if (placedMovingColumnBorderBoxTileArr.size() == 2) {
            PlacedMovingColumnBorderBoxTile tile1 = placedMovingColumnBorderBoxTileArr.get(0);
            PlacedMovingColumnBorderBoxTile tile2 = placedMovingColumnBorderBoxTileArr.get(1);
            if (!(tile2.id - tile1.id == 1)) {
                placedMovingColumnBorderBoxTileArr.clear();
            }
        }
    }

    public boolean hasClickedPlacedMovingColumnBorderBoxTile(int clickX, int clickY) {
        for (PlacedMovingColumnBorderBoxTile tile : placedMovingColumnBorderBoxTileArr) {
            if (tile.isClicked(clickX, clickY)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<PlacedMovingColumnBorderBoxTile> getPlacedMovingColumnBorderBoxTileArr() {
        return this.placedMovingColumnBorderBoxTileArr;
    }

}