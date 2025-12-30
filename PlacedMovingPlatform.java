import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlacedMovingPlatform {
    private ArrayList<PlacedMovingPlatformTile> placedMovingPlatformTileArr = new ArrayList<>();
    private ArrayList<PlacedMovingPlatformBorderBoxTile> placedMovingPlatformBorderBoxTileArr = new ArrayList<>();

    public PlacedMovingPlatform() {}

    public void addPlacedMovingPlatformTile(PlacedMovingPlatformTile tile) {
        placedMovingPlatformTileArr.add(tile);
        placedMovingPlatformTileArr.sort(Comparator.comparingInt((PlacedMovingPlatformTile t) -> t.x));
    }

    public void addPlacedMovingPlatformBorderBoxTile(PlacedMovingPlatformBorderBoxTile tile) {
        if (placedMovingPlatformBorderBoxTileArr.size() < 2) {
            placedMovingPlatformBorderBoxTileArr.add(tile);
        }
        if (placedMovingPlatformBorderBoxTileArr.size() == 2) {
            PlacedMovingPlatformBorderBoxTile tile1 = placedMovingPlatformBorderBoxTileArr.get(0);
            PlacedMovingPlatformBorderBoxTile tile2 = placedMovingPlatformBorderBoxTileArr.get(1);
            if (!(tile1.x == tile2.x || tile1.y == tile2.y)) {
                placedMovingPlatformBorderBoxTileArr.clear();
            }
        }
        placedMovingPlatformBorderBoxTileArr.sort(
            Comparator
                .comparingInt((PlacedMovingPlatformBorderBoxTile t) -> t.x)
                .thenComparingInt(t -> t.y)
        );
    }

    public boolean hasClickedPlacedMovingPlatformTile(int clickX, int clickY) {
        for (PlacedMovingPlatformTile tile : placedMovingPlatformTileArr) {
            if (tile.isClicked(clickX, clickY)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasClickedPlacedMovingPlatformBorderBoxTile(int clickX, int clickY) {
        for (PlacedMovingPlatformBorderBoxTile tile : placedMovingPlatformBorderBoxTileArr) {
            if (tile.isClicked(clickX, clickY)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<PlacedMovingPlatformTile> getPlacedMovingPlatformTileArr() {
        return this.placedMovingPlatformTileArr;
    }

    public ArrayList<PlacedMovingPlatformBorderBoxTile> getPlacedMovingPlatformBorderBoxTileArr() {
        return this.placedMovingPlatformBorderBoxTileArr;
    }
}