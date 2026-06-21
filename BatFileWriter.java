import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BatFileWriter {

    public void saveBatsToFile(
            ArrayList<PlacedBat> placedBatArr,
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

            writer.write(placedBatArr.size() + " " + tileWidth + " " + tileHeight);
            writer.newLine();

            for (PlacedBat bat : placedBatArr) {
                writer.write(bat.x + " " + bat.y + " " + bat.id + " ");
                if (bat.hasFlightPath()) {
                    PlacedFlightPath path = bat.getFlightPath();
                    writer.write("1 " + path.startX + " " + path.startY + " "
                            + path.controlX + " " + path.controlY + " "
                            + path.endX + " " + path.endY + " "
                            + path.id);
                } else {
                    writer.write("0");
                }
                writer.newLine();
            }

            System.out.println("Bat data saved successfully to " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error saving bat data: " + e.getMessage());
        }
    }
}
