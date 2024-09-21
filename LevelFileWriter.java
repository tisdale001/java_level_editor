import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LevelFileWriter {

    private ArrayList<ArrayList<Integer>> levelArr;
    private int numRows;
    private int numCols;

    public LevelFileWriter(ArrayList<ArrayList<Integer>> levelArr, int numRows, int numCols) {
        this.levelArr = levelArr;
        this.numRows = numRows;
        this.numCols = numCols;
    }

    // "filePath" is path like "Assets/Levels/First_Try/"; "fileName" is like "level1.lvl";
    // "tileSetFolderName" is the folder and the name of your tileset, 
    // this is how your level will be found and ensure the correct settings
    public void saveLevelToFile(String filePath, String fileName, String tileSetFolderName) {
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write the metadata (rows and columns) at the beginning of the file
            writer.write(numRows + " " + numCols + " " + tileSetFolderName);
            writer.newLine();

            // Write the level data (tile IDs)
            for (ArrayList<Integer> row : levelArr) {
                for (int i = 0; i < row.size(); i++) {
                    writer.write(row.get(i) + (i < row.size() - 1 ? " " : ""));
                }
                writer.newLine(); // Move to the next line after each row
            }

            System.out.println("Level saved successfully to " + filePath);

        } catch (IOException e) {
            System.err.println("An error occurred while saving the level to file: " + e.getMessage());
        }
    }

}
