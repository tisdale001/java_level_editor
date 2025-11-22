import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;


public class SandTileFileWriter {

    public void saveSandTilesToFile(
        ArrayList<PlacedSandTile> placedSandTilesArr,
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

        // sort by y, then by x
        Collections.sort(placedSandTilesArr, (a, b) -> {
            int yDiff = a.y - b.y;
        
            // Treat y as equal if within ±1 pixel
            if (Math.abs(yDiff) <= 1) {
                return Integer.compare(a.x, b.x); // sort by x when y is effectively the same
            }
        
            return Integer.compare(a.y, b.y); // otherwise, sort normally by y
        });

        // count left borders
        int count = 0;
        for (PlacedSandTile tile : placedSandTilesArr) {
            int value = tile.id;
            if (value == LevelEditor.SAND_LEFT_BORDER) {
                count++;
            }
        }
        System.out.println("count of Left Border Sand Tiles: " + count);
        
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            // Header: number of WormManagers, width, height
            writer.write(count + " " + tileWidth + " " + tileHeight);
            writer.newLine();

            // Write each LEFT_BORDER_TILE, x and y coordinates, then the count of the string of tiles
            int runCount = 0;
            int x = 0;
            int y = 0;
            boolean movingRight = true;
            for (PlacedSandTile tile : placedSandTilesArr) {
                int value = tile.id;
                if (value == LevelEditor.SAND_RIGHT_BORDER) {
                    runCount++;
                    // x, y, movingRight, runCount
                    writer.write(x + " " + y + " " + String.valueOf(movingRight ? 1: 0) + " " + runCount);
                    writer.newLine();
                    movingRight = true;
                    runCount = 0;
                } else if (value == LevelEditor.SAND_LEFT_BORDER) {
                    x = tile.x;
                    y = tile.y;
                    runCount++;
                } else {
                    if (value == LevelEditor.SAND_LEFT) {
                        movingRight = false;
                    }
                    runCount++;
                }
            }
            System.out.println("✅ Sand Tile data saved successfully to " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error saving Sand Tile data: " + e.getMessage());
        }
    }
}
