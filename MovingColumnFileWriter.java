import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MovingColumnFileWriter {

    public void saveMovingColumnsToFile(
        ArrayList<PlacedMovingColumn> placedMovingColumnArr,
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

        File file = new File(filePath, fileName);

        // Count valid moving platforms
        int count = placedMovingColumnArr.size();
        
        System.out.println("count of MovingColumns: " + count);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            // Header: number of moving platforms, width, height
            writer.write(count + " " + tileWidth + " " + tileHeight);
            writer.newLine();

            // Write each MovingPlatform
            System.out.println("placedMovingColumnArr.size() = " + placedMovingColumnArr.size());
            for (PlacedMovingColumn pmc : placedMovingColumnArr) {
                
                ArrayList<PlacedMovingColumnBorderBoxTile> placedMovingColumnBorderBoxTileArr = pmc.getPlacedMovingColumnBorderBoxTileArr();
                if (placedMovingColumnBorderBoxTileArr == null || placedMovingColumnBorderBoxTileArr.isEmpty()) {
                    System.out.println("placedMovingColumnsBorderBoxTileArr is Empty");
                    break;
                } else if (placedMovingColumnBorderBoxTileArr.size() != 2) {
                    System.out.println("placedMovingColumnBorderBoxTileArr is not of size 2");
                    break;
                }
                PlacedMovingColumnBorderBoxTile bbMinTile = placedMovingColumnBorderBoxTileArr.get(0);
                PlacedMovingColumnBorderBoxTile bbMaxTile = placedMovingColumnBorderBoxTileArr.get(1);
                int minX = bbMinTile.x;
                int minY = bbMinTile.y;
                int maxX = bbMaxTile.x;
                int maxY = bbMaxTile.y;
                int bbMinId = bbMinTile.id;
                int bbMaxId = bbMaxTile.id;

                writer.write(minX + " " + minY + " " + maxX + " " + maxY + " " + bbMinId + " " + bbMaxId);
                writer.newLine();
            }

            System.out.println("Moving Column data saved successfully to " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error saving Moving Column data: " + e.getMessage());
        }
    }
}
