import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MovingPlatformFileWriter {

    public void saveMovingPlatformsToFile(
        ArrayList<PlacedMovingPlatform> placedMovingPlatformArr,
        String fileName,
        String beastieName,
        int tileWidth,
        int tileHeight) {

        // Remove the ".lvl" extension if present
        fileName = fileName.replaceFirst("\\.lvl$", "");
        fileName += beastieName + ".txt";

        // Construct the output directory
        String filePath = "Assets/Beasties/BeastieLevelData/" + beastieName + "/";
        File directory = new File(filePath);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Failed to create directories: " + filePath);
            return;
        }

        File file = new File(filePath + fileName);

        // Count valid moving platforms
        int count = placedMovingPlatformArr.size();
        
        System.out.println("count of MovingPlatforms: " + count);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            // Header: number of moving platforms, width, height
            writer.write(count + " " + tileWidth + " " + tileHeight);
            writer.newLine();

            // Write each MovingPlatform
            System.out.println("placedMovingPlatformArr.size() = " + placedMovingPlatformArr.size());
            for (PlacedMovingPlatform pmp : placedMovingPlatformArr) {
                //int mpX, mpY, mpNumTiles, mpId, minX, minY, maxX, maxY, bbId;
                ArrayList<PlacedMovingPlatformTile> placedMovingPlatformTileArr = pmp.getPlacedMovingPlatformTileArr();
                if (placedMovingPlatformTileArr == null || placedMovingPlatformTileArr.isEmpty()) {
                    System.out.println("placedMovingPlatformTileArr is Empty");
                    break;
                }
                PlacedMovingPlatformTile pmpTile = placedMovingPlatformTileArr.get(0);
                int mpX = pmpTile.x;
                int mpY = pmpTile.y;
                int mpNumTiles = placedMovingPlatformTileArr.size();
                int mpId = pmpTile.id;

                ArrayList<PlacedMovingPlatformBorderBoxTile> placedMovingPlatformBorderBoxTileArr = pmp.getPlacedMovingPlatformBorderBoxTileArr();
                if (placedMovingPlatformBorderBoxTileArr == null || placedMovingPlatformBorderBoxTileArr.isEmpty()) {
                    System.out.println("placedMovingPlatformBorderBoxTileArr is Empty");
                    break;
                } else if (placedMovingPlatformBorderBoxTileArr.size() != 2) {
                    System.out.println("placedMovingPlatformBorderBoxTileArr is not of size 2");
                    break;
                }
                PlacedMovingPlatformBorderBoxTile bbMinTile = placedMovingPlatformBorderBoxTileArr.get(0);
                PlacedMovingPlatformBorderBoxTile bbMaxTile = placedMovingPlatformBorderBoxTileArr.get(1);
                int minX = bbMinTile.x;
                int minY = bbMinTile.y;
                int maxX = bbMaxTile.x;
                int maxY = bbMaxTile.y;
                int bbId = bbMinTile.id;

                writer.write(mpX + " " + mpY + " " + mpNumTiles + " " + mpId + " " + minX + " " + minY + " " + maxX + " " + maxY + " " + bbId);
                writer.newLine();
            }

            System.out.println("Moving Platform data saved successfully to " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error saving Moving Platform data: " + e.getMessage());
        }
    }
}
