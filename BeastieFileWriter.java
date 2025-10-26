import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.util.ArrayList;

public class BeastieFileWriter {

    public void saveBeastiesToFile(
            ArrayList<PlacedBeastieTile> placedBeastieTileArr,
            String fileName,
            String beastieName,
            int tileWidth,
            int tileHeight,
            ArrayList<Integer> beastieConstantArr) {

        // Ensure base file name ends properly
        fileName = fileName.replaceFirst("\\.lvl$", "");
        fileName += beastieName + ".txt";

        // Directory path
        String filePath = "Assets/Beasties/BeastieLevelData/" + beastieName + "/";
        File directory = new File(filePath);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Failed to create directories: " + filePath);
            return;
        }

        File file = new File(filePath, fileName);

        // Count matching beasties
        int count = 0;
        for (PlacedBeastieTile tile : placedBeastieTileArr) {
            int value = tile.id;
            if (beastieConstantArr.contains(value)) {
                count++;
            }
        }

        // Write data safely using UTF-8
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            // Metadata line
            writer.write(count + " " + tileWidth + " " + tileHeight);
            writer.newLine();

            // Write beastie entries
            for (PlacedBeastieTile tile : placedBeastieTileArr) {
                int value = tile.id;
                if (beastieConstantArr.contains(value)) {
                    writer.write(tile.x + " " + tile.y + " " + value);
                    writer.newLine();
                }
            }

            System.out.println("Beastie data saved successfully to: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error saving Beastie data: " + e.getMessage());
        }
    }
}
