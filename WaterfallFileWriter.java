import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WaterfallFileWriter {

    public void saveWaterfallsToFile(
            ArrayList<PlacedBeastieTile> placedBeastieTileArr,
            String fileName,
            String beastieName,
            int tileWidth,
            int tileHeight,
            ArrayList<Integer> beastieConstantArr,
            String beastieFilePath) {

        fileName = fileName.replaceFirst("\\.lvl$", "");
        fileName += beastieName + ".txt";

        String filePath = beastieFilePath + beastieName + "/";
        File directory = new File(filePath);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Failed to create directories: " + filePath);
            return;
        }

        File file = new File(filePath, fileName);

        int count = 0;
        for (PlacedBeastieTile tile : placedBeastieTileArr) {
            if (beastieConstantArr.contains(tile.id)) {
                count++;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write(count + " " + tileWidth + " " + tileHeight);
            writer.newLine();

            for (PlacedBeastieTile tile : placedBeastieTileArr) {
                if (beastieConstantArr.contains(tile.id)) {
                    int width = tile.image.getWidth(null);
                    int height = tile.image.getHeight(null);
                    writer.write(tile.x + " " + tile.y + " " + width + " " + height + " " + tile.id);
                    writer.newLine();
                }
            }

            System.out.println("Waterfall data saved successfully to: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error saving Waterfall data: " + e.getMessage());
        }
    }
}
