import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BeastieFileWriter {

    public void saveBeastiesToFile(ArrayList<PlacedBeastieTile> placedBeastieTileArr, String fileName, String beastieName, int tileWidth, int tileHeight, ArrayList<Integer> beastieConstantArr) {
        // remove the ".lvl" from fileName
        fileName = fileName.replaceFirst("\\.lvl$", "");
        fileName += beastieName + ".txt";
        // Hard-coded filePath
        String filePath = "Assets/Beasties/BeastieLevelData/" + beastieName + "/";
        // Create the directory if it doesn't exist
        File directory = new File(filePath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directories created: " + filePath);
            } else {
                System.err.println("Failed to create directories: " + filePath);
                return;
            }
        }
        // Create the file object
        File file = new File(filePath + fileName);

        // count the number of Anemonies
        int count = 0;
        for (PlacedBeastieTile tile : placedBeastieTileArr) {
            Integer value = tile.id - LevelEditor.BEASTIE_PREFIX  - 100;
            if (beastieConstantArr.contains(value)) {
                count++;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write the metadata at the beginning of the file
            writer.write(count + " " + tileWidth + " " + tileHeight);
            writer.newLine();

            // Write the data for each anemone
            for (PlacedBeastieTile tile : placedBeastieTileArr) {
                Integer value = tile.id - LevelEditor.BEASTIE_PREFIX - 100;
                if (beastieConstantArr.contains(value)) {
                    writer.write(tile.x + " " + tile.y + " " + value);
                    writer.newLine();
                }
            }

            System.out.println("Beastie data saved successfully to " + filePath);

        } catch (IOException e) {
            System.err.println("An error occurred while saving the data to file: " + e.getMessage());
        }
    }

}
