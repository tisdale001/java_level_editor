import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FlightPathFileWriter {

    public void saveFlightPathsToFile(
            ArrayList<PlacedFlightPath> placedFlightPathArr,
            String fileName,
            String beastieName,
            int tileWidth,
            int tileHeight,
            String beastieFilePath) {

        fileName = fileName.replaceFirst("\\.lvl$", "");
        fileName += beastieName + ".txt";

        String filePath = beastieFilePath + beastieName + "/";
        File directory = new File(filePath);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Failed to create directories: " + filePath);
            return;
        }

        File file = new File(filePath + fileName);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write(placedFlightPathArr.size() + " " + tileWidth + " " + tileHeight);
            writer.newLine();

            for (PlacedFlightPath path : placedFlightPathArr) {
                writer.write(path.startX + " " + path.startY + " "
                        + path.controlX + " " + path.controlY + " "
                        + path.endX + " " + path.endY + " "
                        + path.id);
                writer.newLine();
            }

            System.out.println("Flight path data saved successfully to " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error saving flight path data: " + e.getMessage());
        }
    }
}
