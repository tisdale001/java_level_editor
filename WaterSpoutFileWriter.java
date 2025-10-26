import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class WaterSpoutFileWriter {

    public void saveWaterSpoutsToFile(
        ArrayList<PlacedWaterSpoutTile> placedWaterSpoutTileArr,
        String fileName,
        String beastieName,
        int tileWidth,
        int tileHeight,
        ArrayList<Integer> waterSpoutConstantArr) {

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

        // Count valid water spouts
        int count = 0;
        for (PlacedWaterSpoutTile tile : placedWaterSpoutTileArr) {
            int value = tile.id - LevelEditor.BEASTIE_PREFIX - 100;
            System.out.println("value: " + value);
            if (waterSpoutConstantArr.contains(value)) {
                count++;
            }
        }
        System.out.println("count of WaterSpouts: " + count);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            // Header: number of spouts, width, height
            writer.write(count + " " + tileWidth + " " + tileHeight);
            writer.newLine();

            // Write each WaterSpout
            System.out.println("placedWaterSpoutTileArr.size() = " + placedWaterSpoutTileArr.size());
            for (PlacedWaterSpoutTile tile : placedWaterSpoutTileArr) {
                int value = tile.id - LevelEditor.BEASTIE_PREFIX - 100;
                if (waterSpoutConstantArr.contains(value)) {
                    System.out.println("value: " + value);
                    ArrayList<PlacedWaterCurrentTile> currents = tile.getPlacedWaterCurrentTileArr();
                    int numBoxes = (currents != null) ? currents.size() : 0;

                    // WaterSpout metadata
                    writer.write(tile.x + " " + tile.y + " " + value);
                    writer.newLine();

                    // Number of boxes
                    writer.write(Integer.toString(numBoxes));
                    writer.newLine();

                    // Write each current
                    if (currents != null) {
                        for (PlacedWaterCurrentTile pwcTile : currents) {
                            int pwcValue = pwcTile.id - LevelEditor.BEASTIE_PREFIX - 100;
                            writer.write(pwcTile.x + " " + pwcTile.y + " " + pwcValue);
                            writer.newLine();
                        }
                    }
                }
            }

            System.out.println("✅ Water Spout data saved successfully to " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error saving Water Spout data: " + e.getMessage());
        }
    }
}
