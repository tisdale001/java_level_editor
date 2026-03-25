import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;




public class LevelTileGridPanel extends JPanel {
    private int tileWidth;
    private int tileHeight;
    private int numRows;
    private int numCols;
    private LevelEditor levelEditor;
    private ArrayList<ArrayList<Integer>> levelArr = new ArrayList<>();
    private boolean fastEntryMode = false;
    private boolean fastEraseMode = false;
    private TilemapOverview overview = TilemapOverview.getInstance();
    private int curRow = 0;
    private int curCol = 0;
    private ArrayList<PlacedBeastieTile> placedBeastieTileArr = new ArrayList<>();
    public ArrayList<PlacedWaterSpoutTile> placedWaterSpoutTileArr = new ArrayList<>();
    public ArrayList<PlacedSandTile> placedSandTilesArr = new ArrayList<>();
    public ArrayList<PlacedMovingPlatform> placedMovingPlatformArr = new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> beastieNamesToConstantsMap = new HashMap<>();

    

    public LevelTileGridPanel(LevelEditor levelEditor, int numRows, int numCols, int tileWidth, int tileHeight) {
        this.levelEditor = levelEditor;
        this.numRows = numRows;
        this.numCols = numCols;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        createLevelArr();
        createBeastieNamesToConstantsMap();

        setPreferredSize(new Dimension(this.numCols * this.tileWidth, this.numRows * this.tileHeight));
        overview.setPreferredSize();

        // Add a mouse listener to handle click events
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Handle double click event
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        // Left double click
                        if (fastEntryMode) {
                            fastEntryMode = false;
                            fastEraseMode = false;
                        } else {
                            fastEntryMode = true;
                            fastEraseMode = false;
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        // Right double click
                        if (fastEraseMode) {
                            fastEraseMode = false;
                            fastEntryMode = false;
                        } else {
                            fastEraseMode = true;
                            fastEntryMode = false;
                        }
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() > 1) return;
                // Get the x and y coordinates of the click
                int x = e.getX();
                int y = e.getY();

                // Display the coordinates
                if (SwingUtilities.isLeftMouseButton(e)) {
                    System.out.println("Left clicked at: (" + x + ", " + y + ")");
                    int tileID = LevelTileGridPanel.this.levelEditor.getCurTile();
                    LevelTileGridPanel.this.placeTileInLevel(x, y, tileID);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    System.out.println("Right clicked at: (" + x + ", " + y + ")");
                    if (!LevelTileGridPanel.this.hasEndedWaterSpoutSetup(x, y) && !LevelTileGridPanel.this.hasEndedMovingPlatformSetup(x, y)) {
                        if (!LevelTileGridPanel.this.hasClickedOnAWaterSpoutElement(x, y) && !LevelTileGridPanel.this.hasClickedOnAMovingPlatformElement(x, y)) {
                            if (!LevelTileGridPanel.this.hasClickedOnBeastieTile(x, y)) {
                                LevelTileGridPanel.this.placeTileInLevel(x, y, -1);
                            }
                        }
                    }
                }

                // Optionally, you can trigger a repaint or other actions based on the click
                repaint();
                overview.repaint();
            }
        });

        // Add MouseMotionListener to handle mouse move events
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                // Handle mouse move event
                if (fastEntryMode) {
                    int tileID = LevelTileGridPanel.this.levelEditor.getCurTile();
                    LevelTileGridPanel.this.placeTileInLevel(x, y, tileID);
                    repaint();
                    overview.repaint();
                } else if (fastEraseMode) {
                    LevelTileGridPanel.this.placeTileInLevel(x, y, -1);
                    repaint();
                    overview.repaint();
                } else {
                    // Show tile row, col if new
                    int newRow = y / LevelTileGridPanel.this.tileHeight;
                    int newCol = x / LevelTileGridPanel.this.tileWidth;
                    if (newRow != LevelTileGridPanel.this.curRow || newCol != LevelTileGridPanel.this.curCol) {
                        System.out.println("(Row, Column) = (" + newRow + ", " + newCol + ")");
                        LevelTileGridPanel.this.curRow = newRow;
                        LevelTileGridPanel.this.curCol = newCol;
                    }
                }
            }
        });
    }

    private void createBeastieNamesToConstantsMap() {
        ArrayList<Integer> anemonesArr = new ArrayList<>(Arrays.asList(levelEditor.ANEMONE_FLOOR, levelEditor.ANEMONE_LEFT_WALL, levelEditor.ANEMONE_CEILING,
            levelEditor.ANEMONE_RIGHT_WALL));
        this.beastieNamesToConstantsMap.put("Anemones", anemonesArr);
        ArrayList<Integer> piranhaArr = new ArrayList<>(Arrays.asList(levelEditor.PIRANHA_RIGHT, levelEditor.PIRANHA_LEFT));
        this.beastieNamesToConstantsMap.put("Piranha", piranhaArr);
        ArrayList<Integer> alligatorsArr = new ArrayList<>(Arrays.asList(levelEditor.ALLIGATOR_RIGHT, levelEditor.ALLIGATOR_LEFT));
        this.beastieNamesToConstantsMap.put("Alligators", alligatorsArr);
        ArrayList<Integer> magicFishArr = new ArrayList<>(Arrays.asList(levelEditor.MAGIC_FISH_RIGHT, levelEditor.MAGIC_FISH_LEFT));
        this.beastieNamesToConstantsMap.put("MagicFish", magicFishArr);
        ArrayList<Integer> pufferfishArr = new ArrayList<>(Arrays.asList(levelEditor.PUFFERFISH));
        this.beastieNamesToConstantsMap.put("Pufferfish", pufferfishArr);
        ArrayList<Integer> switchArr = new ArrayList<>(Arrays.asList(levelEditor.SWITCH_ON, levelEditor.SWITCH_OFF));
        this.beastieNamesToConstantsMap.put("Switches", switchArr);
        ArrayList<Integer> toggleDoorsArr = new ArrayList<>(Arrays.asList(levelEditor.TOGGLE_DOOR_OPEN, levelEditor.TOGGLE_DOOR_CLOSED));
        this.beastieNamesToConstantsMap.put("ToggleDoors", toggleDoorsArr);
        ArrayList<Integer> spidersArr = new ArrayList<>(Arrays.asList(levelEditor.SPIDER_FLOOR_RIGHT, levelEditor.SPIDER_FLOOR_LEFT, levelEditor.SPIDER_CEILING_RIGHT,
            levelEditor.SPIDER_CEILING_LEFT, levelEditor.SPIDER_LEFT_WALL_UP, levelEditor.SPIDER_LEFT_WALL_DOWN, levelEditor.SPIDER_RIGHT_WALL_UP, levelEditor.SPIDER_RIGHT_WALL_DOWN));
        this.beastieNamesToConstantsMap.put("Spiders", spidersArr);
        ArrayList<Integer> spiderBorderBoxArr = new ArrayList<>(Arrays.asList(levelEditor.SPIDER_BORDER_BOX));
        this.beastieNamesToConstantsMap.put("SpiderBorderBoxes", spiderBorderBoxArr);
        ArrayList<Integer> ratsArr = new ArrayList<>(Arrays.asList(levelEditor.RAT_RIGHT, levelEditor.RAT_LEFT));
        this.beastieNamesToConstantsMap.put("Rats", ratsArr);
        ArrayList<Integer> ratBorderBoxArr = new ArrayList<>(Arrays.asList(levelEditor.RAT_BORDER_BOX_RIGHT, levelEditor.RAT_BORDER_BOX_LEFT));
        this.beastieNamesToConstantsMap.put("RatBorderBoxes", ratBorderBoxArr);
        ArrayList<Integer> waterSpoutArr = new ArrayList<>(Arrays.asList(levelEditor.WATER_SPOUT_RIGHT, levelEditor.WATER_SPOUT_LEFT, levelEditor.WATER_SPOUT_UP,
            levelEditor.WATER_SPOUT_DOWN));
        this.beastieNamesToConstantsMap.put("WaterSpouts", waterSpoutArr);
        ArrayList<Integer> beePotArr = new ArrayList<>(Arrays.asList(levelEditor.BEE_POT));
        this.beastieNamesToConstantsMap.put("BeePots", beePotArr);
        ArrayList<Integer> spikesArr = new ArrayList<>(Arrays.asList(levelEditor.SPIKES_UP, levelEditor.SPIKES_DOWN));
        this.beastieNamesToConstantsMap.put("Spikes", spikesArr);
        ArrayList<Integer> sandTilesArr = new ArrayList<>(Arrays.asList(levelEditor.SAND_LEFT_BORDER, levelEditor.SAND_LEFT, levelEditor.SAND_RIGHT, levelEditor.SAND_RIGHT_BORDER));
        this.beastieNamesToConstantsMap.put("SandTiles", sandTilesArr);
        ArrayList<Integer> movingPlatformArr = new ArrayList<>(Arrays.asList(levelEditor.MOVING_PLATFORM_TILE, levelEditor.MOVING_PLATFORM_BORDER_BOX_TILE));
        this.beastieNamesToConstantsMap.put("MovingPlatforms", movingPlatformArr);
        ArrayList<Integer> dogArr = new ArrayList<>(Arrays.asList(levelEditor.DOG_RIGHT, levelEditor.DOG_LEFT));
        this.beastieNamesToConstantsMap.put("Dogs", dogArr);
        ArrayList<Integer> dogBorderBoxArr = new ArrayList<>(Arrays.asList(levelEditor.DOG_BORDER_BOX_LEFT, levelEditor.DOG_BORDER_BOX_RIGHT));
        this.beastieNamesToConstantsMap.put("DogBorderBoxes", dogBorderBoxArr);
    }

    private boolean hasClickedOnBeastieTile(int x, int y) {
        for (int i = 0; i < placedBeastieTileArr.size(); ++i) {
            PlacedBeastieTile tile = placedBeastieTileArr.get(i);
            if (tile.isClicked(x, y)) {
                placedBeastieTileArr.remove(i);
                return true;
            }
        }
        // also check sand tiles
        for (int i = 0; i < placedSandTilesArr.size(); ++i) {
            PlacedSandTile tile = placedSandTilesArr.get(i);
            if (tile.isClicked(x, y)) {
                placedSandTilesArr.remove(i);
                return true;
            }
        }
        return false;
    }

    private boolean hasEndedWaterSpoutSetup(int x, int y) {
        return levelEditor.hasEndedWaterSpoutSetup(x, y);
    }

    private boolean hasClickedOnAWaterSpoutElement(int x, int y) {
        return levelEditor.hasClickedOnAWaterSpoutElement(x, y);
    }

    private boolean hasEndedMovingPlatformSetup(int x, int y) {
        return levelEditor.hasEndedMovingPlatformSetup(x, y);
    }

    private boolean hasClickedOnAMovingPlatformElement(int x, int y) {
        return levelEditor.hasClickedOnAMovingPlatformElement(x, y);
    }

    public void placeBeastieTile(int x, int y, int id, Image tileImage) {
        placedBeastieTileArr.add(new PlacedBeastieTile(x, y, id, tileImage));
        repaint();
    }

    public void placeWaterSpoutTile(PlacedWaterSpoutTile tile) {
        placedWaterSpoutTileArr.add(tile);
        repaint();
    }

    public void placeMovingPlatform(PlacedMovingPlatform pmp) {
        placedMovingPlatformArr.add(pmp);
        repaint();
    }

    public void placeSandTile(PlacedSandTile tile) {
        placedSandTilesArr.add(tile);
    }

    private void placeTileInLevel(int x, int y, int tileID) {
        int col = x / this.tileWidth;
        int row = y / this.tileHeight;
        if (row >= 0 && row < this.levelArr.size() && col >= 0 && col < this.levelArr.get(row).size()) {
            this.levelArr.get(row).set(col, tileID);
        }
        overview.placeTileInLevel(x, y, tileID);
    }

    private void createLevelArr() {
        for (int i = 0; i < this.numRows; i++) {
            ArrayList<Integer> innerList = new ArrayList<>();
            for (int j = 0; j < this.numCols; j++) {
                innerList.add(-1);
            }
            this.levelArr.add(innerList);
        }
        overview.initiateOverviewMap(this.levelEditor, this.levelArr, this.numRows, this.numCols, this.tileWidth, this.tileHeight);
    }

    public void refreshRowsCols(String topBottomSelection, String leftRightSelection, int deltaRows, int deltaCols) {
        System.out.println(String.format("refreshRowsCols(), deltaRows = %d, deltaCols = %d", deltaRows, deltaCols));
        if (topBottomSelection == "Top") {
            // Top
            if (deltaRows > 0) {
                // add rows to top
                System.out.println("add rows to top");
                for (int i = 0; i < deltaRows; i++) {
                    ArrayList<Integer> newArr = new ArrayList<>(Collections.nCopies(this.numCols, -1));
                    this.levelArr.add(0, newArr);
                }
            } else if (deltaRows < 0) {
                // subtract rows from top
                System.out.println("subtract rows from top");
                for (int i = 0; i < -deltaRows; i++) {
                    if (!this.levelArr.isEmpty()) {
                        this.levelArr.remove(0);
                    }
                }
            }
        } else {
            // Bottom
            if (deltaRows > 0) {
                // add rows to bottom
                System.out.println("add rows to bottom");
                for (int i = 0; i < deltaRows; i++) {
                    ArrayList<Integer> newArr = new ArrayList<>(Collections.nCopies(this.numCols, -1));
                    System.out.println(newArr);
                    this.levelArr.add(newArr);
                }
            } else if (deltaRows < 0) {
                // subtract rows from bottom
                System.out.println("subtract rows from bottom");
                for (int i = 0; i < -deltaRows; i++) {
                    if (!this.levelArr.isEmpty()) {
                        this.levelArr.remove(this.levelArr.size() - 1);
                    }
                }
            }
        }
        if (leftRightSelection == "Left") {
            // Left
            if (deltaCols > 0) {
                // add cols to left
                System.out.println("add cols to left");
                for (int j = 0; j < this.levelArr.size(); j++) {
                    for (int i = 0; i < deltaCols; i++) {
                        this.levelArr.get(j).add(0, -1);
                    }
                }
            } else if (deltaCols < 0) {
                // subtract cols from left
                System.out.println("subtract cols from left");
                for (int j = 0; j < this.levelArr.size(); j++) {
                    for (int i = 0; i < -deltaCols; i++) {
                        if (!this.levelArr.get(j).isEmpty()) {
                            this.levelArr.get(j).remove(0);
                        }
                    }
                }
            }
        } else {
            // Right
            if (deltaCols > 0) {
                // add cols to right
                System.out.println("add cols to right");
                for (int j = 0; j < this.levelArr.size(); j++) {
                    for (int i = 0; i < deltaCols; i++) {
                        this.levelArr.get(j).add(-1);
                    }
                }
            } else if (deltaCols < 0) {
                // subtract cols from right
                System.out.println("subtract cols from right");
                for (int j = 0; j < this.levelArr.size(); j++) {
                    for (int i = 0; i < -deltaCols; i++) {
                        if (!this.levelArr.get(j).isEmpty()) {
                            this.levelArr.get(j).remove(this.levelArr.get(j).size() - 1);
                        }
                    }
                }
            }
        }
        // change numRows, numCols
        this.numRows = this.levelArr.size();
        this.numCols = this.levelArr.isEmpty() ? 0 : this.levelArr.get(0).size();
        setPreferredSize(new Dimension(this.numCols * this.tileWidth, this.numRows * this.tileHeight));
        revalidate();
        repaint();
        overview.initiateOverviewMap(this.levelEditor, this.levelArr, this.numRows, this.numCols, this.tileWidth, this.tileHeight);
        overview.setPreferredSize();
        overview.revalidate();
        overview.repaint();
    }

    public void saveGridAsLevel(String filePath, String fileName, String tileSetFolderName) {
        LevelFileWriter lfw = new LevelFileWriter(this.levelArr, this.levelArr.size(), this.levelArr.get(0).size());
        lfw.saveLevelToFile(filePath, fileName, tileSetFolderName);
    }

    public void saveBeastiesToLevel(String fileName) {
        // public void saveBeastiesToFile(ArrayList<PlacedBeastieTile> placedBeastieTileArr, String fileName, String beastieName, ArrayList<Integer> beastieConstantArr) {
        BeastieFileWriter bfw = new BeastieFileWriter();
        for (HashMap.Entry<String, ArrayList<Integer>> entry : beastieNamesToConstantsMap.entrySet()) {
            String key = entry.getKey();
            ArrayList<Integer> value = entry.getValue();
            if ((key != "WaterSpouts") && (key != "WaterCurrents") && (key != "MovingPlatforms")) {
                bfw.saveBeastiesToFile(placedBeastieTileArr, fileName, key, tileWidth, tileHeight, value);
            }
        }
    }

    public void saveWaterSpoutsToLevel(String fileName) {
        WaterSpoutFileWriter wsfw = new WaterSpoutFileWriter();
        for (HashMap.Entry<String, ArrayList<Integer>> entry : beastieNamesToConstantsMap.entrySet()) {
            String key = entry.getKey();
            ArrayList<Integer> value = entry.getValue();
            if (key == "WaterSpouts") {
                wsfw.saveWaterSpoutsToFile(this.placedWaterSpoutTileArr, fileName, key, tileWidth, tileHeight, value);
            }
        }
    }

    public void saveMovingPlatformsToLevel(String fileName) {
        MovingPlatformFileWriter mpfw = new MovingPlatformFileWriter();
        for (HashMap.Entry<String, ArrayList<Integer>> entry : beastieNamesToConstantsMap.entrySet()) {
            String key = entry.getKey();
            ArrayList<Integer> value = entry.getValue();
            if (key == "MovingPlatforms") {
                mpfw.saveMovingPlatformsToFile(this.placedMovingPlatformArr, fileName, key, tileWidth, tileHeight);
            }
        }
    }

    public void saveSandTilesToLevel(String fileName) {
        SandTileFileWriter stfw = new SandTileFileWriter();
        String key = "SandTiles";
        stfw.saveSandTilesToFile(this.placedSandTilesArr, fileName, key, tileWidth, tileHeight);
    }

    public void loadBeastiesFromFiles(String filePath, String fileName) {
        System.out.println("loadBeastiesFromFile");
        this.placedBeastieTileArr.clear();
        for (String key : this.beastieNamesToConstantsMap.keySet()) {
            if (key != "WaterSpouts" && key != "WaterCurrents" && key != "MovingPlatforms") {
                this.loadBeastiesFromFile(filePath, fileName, key);
            }
        }
        repaint();
    }

    private void loadBeastiesFromFile(String filePath, String fileName, String beastieNamePlural) {
        System.out.println("beastieNamePlural: " + beastieNamePlural);
        filePath += beastieNamePlural + "/";
        fileName = fileName.replaceFirst("\\.lvl$", "");
        fileName += beastieNamePlural + ".txt";
        File file = new File(filePath + fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read the metadata (number of Anemones)
            String metadataLine = reader.readLine();
            if (metadataLine == null || metadataLine.isEmpty()) {
                throw new IOException("Invalid level file: metadata missing");
            }
            String[] metadataParts = metadataLine.split(" ");
            // if (metadataParts.length != 1) {
            //     throw new IOException("Invalid level file: incorrect metadata format");
            // }
            int numBeasties = Integer.parseInt(metadataParts[0]);
            // Read the beastie data
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // skip blank lines

                String[] parts = line.split("\\s+"); // split by one or more spaces
                if (parts.length != 3) {
                    throw new IOException("Invalid beastie line format: " + line);
                }

                int xPos = Integer.parseInt(parts[0]);
                int yPos = Integer.parseInt(parts[1]);
                int beastieType = Integer.parseInt(parts[2]);
                int tileId = beastieType;
                Image image;
                if (levelEditor.enlargeToFourByFourBeasties.contains(beastieType)) {
                    int scaledWidth = this.tileWidth * 4;
                    int scaledHeight = this.tileHeight * 4;
                    Image tileImage = levelEditor.beastieGridPanel.tileArr.get(beastieType).getImage();
                    // Create a new BufferedImage to hold the scaled image
                    BufferedImage scaledBuffered = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = scaledBuffered.createGraphics();

                    // For pixel art, use NEAREST_NEIGHBOR; for smoother scaling, use BILINEAR
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    g2.drawImage(tileImage, 0, 0, scaledWidth, scaledHeight, null);
                    g2.dispose();

                    // Replace the current image with the scaled one
                    image = scaledBuffered;
                } else if (levelEditor.enlargeByOnePointTwentyFiveBeasties.contains(beastieType)) {
                    // Compute scaled dimensions
                    int scaledWidth = (int)(this.tileWidth * 1.25);
                    int scaledHeight = (int)(this.tileHeight * 1.25);
                    Image tileImage = levelEditor.beastieGridPanel.tileArr.get(beastieType).getImage();
                    // Create a new BufferedImage to hold the scaled image
                    BufferedImage scaledBuffered = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = scaledBuffered.createGraphics();

                    // For pixel art, use NEAREST_NEIGHBOR; for smoother scaling, use BILINEAR
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    g2.drawImage(tileImage, 0, 0, scaledWidth, scaledHeight, null);
                    g2.dispose();

                    // Replace the current image with the scaled one
                    image = scaledBuffered;
                } else if (levelEditor.enlargeToOneByFourVerticallyBeasties.contains(beastieType)) {
                    int scaledWidth = this.tileWidth;
                    int scaledHeight = this.tileHeight * 4;
                    Image tileImage = levelEditor.beastieGridPanel.tileArr.get(beastieType).getImage();
                    // Create a new BufferedImage to hold the scaled image
                    BufferedImage scaledBuffered = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = scaledBuffered.createGraphics();

                    // For pixel art, use NEAREST_NEIGHBOR; for smoother scaling, use BILINEAR
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    g2.drawImage(tileImage, 0, 0, scaledWidth, scaledHeight, null);
                    g2.dispose();

                    // Replace the current image with the scaled one
                    image = scaledBuffered;
                } else if (levelEditor.enlargeToFourByOneHorizontallyBeasties.contains(beastieType)) {
                    int scaledWidth = this.tileWidth * 4;
                    int scaledHeight = this.tileHeight;
                    Image tileImage = levelEditor.beastieGridPanel.tileArr.get(beastieType).getImage();
                    // Create a new BufferedImage to hold the scaled image
                    BufferedImage scaledBuffered = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = scaledBuffered.createGraphics();

                    // For pixel art, use NEAREST_NEIGHBOR; for smoother scaling, use BILINEAR
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    g2.drawImage(tileImage, 0, 0, scaledWidth, scaledHeight, null);
                    g2.dispose();

                    // Replace the current image with the scaled one
                    image = scaledBuffered;
                } else {
                    image = levelEditor.beastieGridPanel.tileArr.get(beastieType).getImage();
                }
                // Place PlacedBeastieTile
                this.placedBeastieTileArr.add(new PlacedBeastieTile(xPos, yPos, tileId, image));
            }
        } catch (IOException e) {
            System.err.println("An error occurred while loading the beasties from file: " + e.getMessage());
            return;
        }
    }

    public void loadWaterSpoutsFromFile(String filePath, String fileName) {
        this.placedWaterSpoutTileArr.clear();
    
        String beastieNamePlural = "WaterSpouts";
        filePath += beastieNamePlural + "/";
        fileName = fileName.replaceFirst("\\.lvl$", "");
        fileName += beastieNamePlural + ".txt";
        File file = new File(filePath + fileName);
        System.out.println("filePath + fileName: " + filePath + fileName);
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            AtomicInteger lineNumber = new AtomicInteger(0);
    
            // Helper lambda to read the next non-empty line (or null if EOF)
            java.util.function.Supplier<String> readNonEmptyLine = () -> {
                try {
                    String ln;
                    while ((ln = reader.readLine()) != null) {
                        lineNumber.incrementAndGet();
                        if (!ln.trim().isEmpty()) return ln;
                        // else skip blank line and continue
                    }
                    return null;
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            };
    
            // Read metadata (num water spouts)
            String metadataLine = readNonEmptyLine.get();
            if (metadataLine == null) {
                throw new IOException("Invalid level file: metadata missing (line " + lineNumber + ")");
            }
            String[] metadataParts = metadataLine.trim().split("\\s+");
            int numWaterSpouts;
            try {
                numWaterSpouts = Integer.parseInt(metadataParts[0]);
            } catch (NumberFormatException nfe) {
                throw new IOException("Invalid metadata number on line " + lineNumber + ": '" + metadataLine + "'", nfe);
            }
            System.out.println("numWaterSpouts: " + numWaterSpouts);
    
            for (int i = 0; i < numWaterSpouts; i++) {
                // read the water spout line
                String metaLine = readNonEmptyLine.get();
                if (metaLine == null) {
                    throw new IOException("Unexpected end of file while reading water spout metadata (expected " + numWaterSpouts + " entries, got " + i + ")");
                }
                System.out.println("metaLine (line " + lineNumber + "): " + metaLine);
    
                String[] parts = metaLine.trim().split("\\s+");
                if (parts.length < 3) {
                    throw new IOException("Invalid water spout metadata (line " + lineNumber + "): " + metaLine);
                }
    
                int wsX, wsY, wsId, tileId;
                try {
                    wsX = Integer.parseInt(parts[0]);
                    wsY = Integer.parseInt(parts[1]);
                    wsId = Integer.parseInt(parts[2]);
                    tileId = wsId;
                } catch (NumberFormatException nfe) {
                    throw new IOException("Invalid integer in water spout metadata (line " + lineNumber + "): " + metaLine, nfe);
                }
    
                Image wsImage = levelEditor.beastieGridPanel.tileArr.get(wsId).getImage();
                PlacedWaterSpoutTile placedWaterSpoutTile = new PlacedWaterSpoutTile(wsX, wsY, tileId, wsImage);
    
                // Read number of boxes (skip blank lines)
                String metaLineBoxes = readNonEmptyLine.get();
                if (metaLineBoxes == null) {
                    throw new IOException("Unexpected end of file while reading number of boxes for water spout at index " + i + " (line " + lineNumber + ")");
                }
                System.out.println("metaLineBoxes (line " + lineNumber + "): " + metaLineBoxes);
                int numBoxes;
                try {
                    numBoxes = Integer.parseInt(metaLineBoxes.trim());
                } catch (NumberFormatException nfe) {
                    throw new IOException("Invalid number-of-boxes (line " + lineNumber + "): " + metaLineBoxes, nfe);
                }
    
                for (int b = 0; b < numBoxes; b++) {
                    String metaLineBox = readNonEmptyLine.get();
                    if (metaLineBox == null) {
                        throw new IOException("Unexpected end of file while reading water spout box data for water spout index " + i + ", box " + b + " (line " + lineNumber + ")");
                    }
                    System.out.println("metaLineBox (line " + lineNumber + "): " + metaLineBox);
    
                    String[] partsBoxes = metaLineBox.trim().split("\\s+");
                    if (partsBoxes.length < 3) {
                        throw new IOException("Invalid box data line (line " + lineNumber + "): " + metaLineBox);
                    }
    
                    int boxX, boxY, boxId;
                    try {
                        boxX = Integer.parseInt(partsBoxes[0]);
                        boxY = Integer.parseInt(partsBoxes[1]);
                        boxId = Integer.parseInt(partsBoxes[2]);
                    } catch (NumberFormatException nfe) {
                        throw new IOException("Invalid integer in box data (line " + lineNumber + "): " + metaLineBox, nfe);
                    }
    
                    Image boxImage = levelEditor.beastieGridPanel.tileArr.get(boxId).getImage();
                    PlacedWaterCurrentTile placedWaterCurrentTile =
                            new PlacedWaterCurrentTile(boxX, boxY, boxId, boxImage);
    
                    placedWaterSpoutTile.addPlacedWaterCurrentTile(placedWaterCurrentTile);
                }
                this.placedWaterSpoutTileArr.add(placedWaterSpoutTile);
            }
        } catch (Exception e) {
            // Catch Exception so both IO and format problems are visible and you get a stack trace.
            System.err.println("An error occurred while loading the water spouts from file: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    public void loadMovingPlatformsFromFile(String filePath, String fileName) {
        this.placedMovingPlatformArr.clear();
    
        String beastieNamePlural = "MovingPlatforms";
        filePath += beastieNamePlural + "/";
        fileName = fileName.replaceFirst("\\.lvl$", "");
        fileName += beastieNamePlural + ".txt";
        File file = new File(filePath + fileName);
        System.out.println("filePath + fileName: " + filePath + fileName);
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            AtomicInteger lineNumber = new AtomicInteger(0);
    
            // Helper lambda to read the next non-empty line (or null if EOF)
            java.util.function.Supplier<String> readNonEmptyLine = () -> {
                try {
                    String ln;
                    while ((ln = reader.readLine()) != null) {
                        lineNumber.incrementAndGet();
                        if (!ln.trim().isEmpty()) return ln;
                        // else skip blank line and continue
                    }
                    return null;
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            };
    
            // Read metadata (num moving platforms) (will also have tileWidth and tileHeight, but not read here)
            String metadataLine = readNonEmptyLine.get();
            if (metadataLine == null) {
                throw new IOException("Invalid level file: metadata missing (line " + lineNumber + ")");
            }
            String[] metadataParts = metadataLine.trim().split("\\s+");
            int numMovingPlatforms;
            try {
                numMovingPlatforms = Integer.parseInt(metadataParts[0]);
            } catch (NumberFormatException nfe) {
                throw new IOException("Invalid metadata number on line " + lineNumber + ": '" + metadataLine + "'", nfe);
            }
            System.out.println("numMovingPlatforms: " + numMovingPlatforms);
    
            for (int i = 0; i < numMovingPlatforms; i++) {
                // read the moving platform line (x, y, numTiles)
                // then min border box (x, y), then max border box (x, y)
                String metaLine = readNonEmptyLine.get();
                if (metaLine == null) {
                    throw new IOException("Unexpected end of file while reading moving platform metadata (expected " + numMovingPlatforms + " entries, got " + i + ")");
                }
                // System.out.println("metaLine (line " + lineNumber + "): " + metaLine);
    
                String[] parts = metaLine.trim().split("\\s+");
                if (parts.length < 9) {
                    throw new IOException("Invalid moving platform metadata (line " + lineNumber + "): " + metaLine);
                }
    
                int mpX, mpY, mpNumTiles, mpId, minX, minY, maxX, maxY, bbId;
                try {
                    mpX = Integer.parseInt(parts[0]);
                    mpY = Integer.parseInt(parts[1]);
                    mpNumTiles = Integer.parseInt(parts[2]);
                    mpId = Integer.parseInt(parts[3]);
                    minX = Integer.parseInt(parts[4]);
                    minY = Integer.parseInt(parts[5]);
                    maxX = Integer.parseInt(parts[6]);
                    maxY = Integer.parseInt(parts[7]);
                    bbId = Integer.parseInt(parts[8]);
                } catch (NumberFormatException nfe) {
                    throw new IOException("Invalid integer in moving platform metadata (line " + lineNumber + "): " + metaLine, nfe);
                }

                Image mpImage = levelEditor.beastieGridPanel.tileArr.get(mpId).getImage();
                Image bbImage = levelEditor.beastieGridPanel.tileArr.get(bbId).getImage();
                PlacedMovingPlatform placedMovingPlatform = new PlacedMovingPlatform();
                for (int j = 0; j < mpNumTiles; j++) {
                    PlacedMovingPlatformTile placedMovingPlatformTile = new PlacedMovingPlatformTile(mpX + j * this.tileWidth, mpY, mpId, mpImage);
                    placedMovingPlatform.addPlacedMovingPlatformTile(placedMovingPlatformTile);
                }
                PlacedMovingPlatformBorderBoxTile pmpbbMinTile = new PlacedMovingPlatformBorderBoxTile(minX, minY, bbId, bbImage);
                PlacedMovingPlatformBorderBoxTile pmpbbMaxTile = new PlacedMovingPlatformBorderBoxTile(maxX, maxY, bbId, bbImage);
                placedMovingPlatform.addPlacedMovingPlatformBorderBoxTile(pmpbbMinTile);
                placedMovingPlatform.addPlacedMovingPlatformBorderBoxTile(pmpbbMaxTile);
                
                this.placedMovingPlatformArr.add(placedMovingPlatform);
            }
        } catch (Exception e) {
            // Catch Exception so both IO and format problems are visible and you get a stack trace.
            System.err.println("An error occurred while loading the moving platforms from file: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    public void loadSandTilesFromFile(String filePath, String fileName) {
        this.placedSandTilesArr.clear();
    
        String beastieNamePlural = "SandTiles";
        filePath += beastieNamePlural + "/";
        fileName = fileName.replaceFirst("\\.lvl$", "");
        fileName += beastieNamePlural + ".txt";
        File file = new File(filePath + fileName);
        System.out.println("filePath + fileName: " + filePath + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            AtomicInteger lineNumber = new AtomicInteger(0);
    
            // Helper lambda to read the next non-empty line (or null if EOF)
            java.util.function.Supplier<String> readNonEmptyLine = () -> {
                try {
                    String ln;
                    while ((ln = reader.readLine()) != null) {
                        lineNumber.incrementAndGet();
                        if (!ln.trim().isEmpty()) return ln;
                        // else skip blank line and continue
                    }
                    return null;
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            };
            // read the sand tiles line
            String metaLine = readNonEmptyLine.get();
            if (metaLine == null) {
                throw new IOException("Unexpected end of file while reading sand tiles");
            }
            System.out.println("metaLine (line " + lineNumber + "): " + metaLine);

            String[] parts = metaLine.trim().split("\\s+");
            if (parts.length < 3) {
                throw new IOException("Invalid sand tile metadata (line " + lineNumber + "): " + metaLine);
            }

            int numWormManagers;
            try {
                numWormManagers = Integer.parseInt(parts[0]);
                // I don't need the tileWidth and tileHeight here
            } catch (NumberFormatException nfe) {
                throw new IOException("Invalid integer in sand tile metadata (line " + lineNumber + "): " + metaLine, nfe);
            }

            for (int j = 0; j < numWormManagers; j++) {
                // x, y, movingRight, runCount
                String metaLineWormManagers = readNonEmptyLine.get();
                if (metaLine == null) {
                    throw new IOException("Unexpected end of file while reading sand tiles");
                }
                System.out.println("metaLine (line " + lineNumber + "): " + metaLineWormManagers);

                String[] parts2 = metaLineWormManagers.trim().split("\\s+");
                if (parts2.length < 4) {
                    throw new IOException("Invalid sand tile metadata (line " + lineNumber + "): " + metaLineWormManagers);
                }
                int stX, stY, runCount;
                boolean movingRight;
                try {
                    stX = Integer.parseInt(parts2[0]);
                    stY = Integer.parseInt(parts2[1]);
                    movingRight = Integer.parseInt(parts2[2]) == 1;
                    runCount = Integer.parseInt(parts2[3]);
                } catch (NumberFormatException nfe) {
                    throw new IOException("Invalid integer in sand tile metadata (line " + lineNumber + "): " + metaLineWormManagers, nfe);
                }
                // reconstruct placedSandTiles
                int x;
                int y;
                int tileId;
                Image image;
                for (int i = 0; i < runCount; i++) {
                    PlacedSandTile pst;
                    if (i == 0) {
                        // SAND_LEFT_BORDER
                        x = stX;
                        y = stY;
                        tileId = levelEditor.SAND_LEFT_BORDER;
                        image = levelEditor.beastieGridPanel.tileArr.get(tileId).getImage();
                        pst = new PlacedSandTile(x, y, tileId, image);
                    } else if (i == runCount - 1) {
                        // SAND_RIGHT_BORDER
                        x = stX + tileWidth * i;
                        y = stY;
                        tileId = levelEditor.SAND_RIGHT_BORDER;
                        image = levelEditor.beastieGridPanel.tileArr.get(tileId).getImage();
                        pst = new PlacedSandTile(x, y, tileId, image);
                    } else {
                        if (movingRight) {
                            // SAND_RIGHT
                            x = stX + tileWidth * i;
                            y = stY;
                            tileId = levelEditor.SAND_RIGHT;
                            image = levelEditor.beastieGridPanel.tileArr.get(tileId).getImage();
                            pst = new PlacedSandTile(x, y, tileId, image);
                        } else {
                            // SAND_LEFT
                            x = stX + tileWidth * i;
                            y = stY;
                            tileId = levelEditor.SAND_LEFT;
                            image = levelEditor.beastieGridPanel.tileArr.get(tileId).getImage();
                            pst = new PlacedSandTile(x, y, tileId, image);
                        }
                    }
                    this.placedSandTilesArr.add(pst);
                }
            }

        } catch (Exception e) {
            // Catch Exception so both IO and format problems are visible and you get a stack trace.
            System.err.println("An error occurred while loading the sand tiles from file: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }
    

    public LevelData loadLevelFromFile(String filePath, String fileName) {
        // Create the file object
        File file = new File(filePath + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read the metadata (rows, columns, and tileset folder name)
            String metadataLine = reader.readLine();
            if (metadataLine == null || metadataLine.isEmpty()) {
                throw new IOException("Invalid level file: metadata missing");
            }

            String[] metadataParts = metadataLine.split(" ");
            if (metadataParts.length != 3) {
                throw new IOException("Invalid level file: incorrect metadata format");
            }

            int numR = Integer.parseInt(metadataParts[0]);
            int numC = Integer.parseInt(metadataParts[1]);
            String tileSetFolderName = metadataParts[2];

            // Initialize the level array
            this.levelArr = new ArrayList<>(numR);

            // Read the level data (tile IDs)
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tileIDs = line.trim().split(" ");
                ArrayList<Integer> row = new ArrayList<>(numC);
                for (String tileID : tileIDs) {
                    row.add(Integer.parseInt(tileID));
                }
                levelArr.add(row);
            }

            // Verify the loaded data matches the expected dimensions
            if (levelArr.size() != numR) {
                throw new IOException("Invalid level file: row count does not match metadata");
            }
            for (ArrayList<Integer> row : levelArr) {
                if (row.size() != numC) {
                    throw new IOException("Invalid level file: column count does not match metadata");
                }
            }

            // Set necessary data
            this.numRows = numR;
            this.numCols = numC;
            revalidate();
            repaint();
            // set TilemapOverview
            overview.initiateOverviewMap(this.levelEditor, levelArr, numRows, numCols, tileWidth, tileHeight);
            overview.setPreferredSize();
            overview.revalidate();
            overview.repaint();
            // Return the loaded level data
            return new LevelData(numR, numC, tileSetFolderName);

        } catch (IOException e) {
            System.err.println("An error occurred while loading the level from file: " + e.getMessage());
            return null;
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // System.out.println("In paintComponent panel: " + this);

        // Set the color for the grid lines
        g.setColor(Color.BLACK);

        // Get the width and height of the panel
        int width = this.numCols * this.tileWidth;
        int height = this.numRows * this.tileHeight;

        // Draw vertical lines
        for (int x = 0; x <= width; x += this.tileWidth) {
            g.drawLine(x, 0, x, height);
        }

        // Draw horizontal lines
        for (int y = 0; y <= height; y += this.tileHeight) {
            g.drawLine(0, y, width, y);
        }

        for (int i = 0; i < this.levelArr.size(); i++) {
            for (int j = 0; j < this.levelArr.get(i).size(); j++) {
                int tileID = this.levelArr.get(i).get(j);
                if (tileID != -1) {
                    BufferedImage image = levelEditor.getImageFromTileID(tileID);
                    int y = i * this.tileHeight;
                    int x = j * this.tileWidth;
                    g.drawImage(image, x, y, this.tileWidth, this.tileHeight, this);
                }
                
            }
        }

        for (PlacedBeastieTile t : this.placedBeastieTileArr) {
            int imgWidth = t.image.getWidth(this);
            int imgHeight = t.image.getHeight(this);
        
            // Defensive check — sometimes width/height can return -1 if not yet loaded
            if (imgWidth > 0 && imgHeight > 0) {
                g.drawImage(t.image, t.x, t.y, imgWidth, imgHeight, this);
            }
        }

        for (PlacedWaterSpoutTile t : this.placedWaterSpoutTileArr) {
            // System.out.println("t.image width=" + t.image.getWidth(this) + " height=" + t.image.getHeight(this));
            int imgWidth = t.image.getWidth(this);
            int imgHeight = t.image.getHeight(this);
            // Defensive check — sometimes width/height can return -1 if not yet loaded
            if (imgWidth > 0 && imgHeight > 0) {
                ArrayList<PlacedWaterCurrentTile> placedWaterCurrentTileArr = t.getPlacedWaterCurrentTileArr();
                for (PlacedWaterCurrentTile wct : placedWaterCurrentTileArr) {
                    int wctImgWidth = wct.image.getWidth(this);
                    int wctImgHeight = wct.image.getHeight(this);
                    if (wctImgWidth > 0 && wctImgHeight > 0) {
                        g.drawImage(wct.image, wct.x, wct.y, wctImgWidth, wctImgHeight, this);
                    }
                }
                g.drawImage(t.image, t.x, t.y, imgWidth, imgHeight, this);
            }
        }

        for (PlacedMovingPlatform p : this.placedMovingPlatformArr) {
            ArrayList<PlacedMovingPlatformTile> placedMovingPlatformTileArr = p.getPlacedMovingPlatformTileArr();
            for (PlacedMovingPlatformTile t : placedMovingPlatformTileArr) {
                // System.out.println("t.image width=" + t.image.getWidth(this) + " height=" + t.image.getHeight(this));
                int imgWidth = t.image.getWidth(this);
                int imgHeight = t.image.getHeight(this);
                // Defensive check — sometimes width/height can return -1 if not yet loaded
                if (imgWidth > 0 && imgHeight > 0) {
                    g.drawImage(t.image, t.x, t.y, imgWidth, imgHeight, this);
                }
            }
            ArrayList<PlacedMovingPlatformBorderBoxTile> placedMovingPlatformBorderBoxTileArr = p.getPlacedMovingPlatformBorderBoxTileArr();
            for (PlacedMovingPlatformBorderBoxTile bbt : placedMovingPlatformBorderBoxTileArr) {
                // System.out.println("bbt.image width=" + bbt.image.getWidth(this) + " height=" + bbt.image.getHeight(this));
                int bbtImgWidth = bbt.image.getWidth(this);
                int bbtImgHeight = bbt.image.getHeight(this);
                if (bbtImgWidth > 0 && bbtImgHeight > 0) {
                    g.drawImage(bbt.image, bbt.x, bbt.y, bbtImgWidth, bbtImgHeight, this);
                }
            }
        }

        for (PlacedSandTile t : this.placedSandTilesArr) {
            int imgWidth = t.image.getWidth(this);
            int imgHeight = t.image.getHeight(this);
        
            // Defensive check — sometimes width/height can return -1 if not yet loaded
            if (imgWidth > 0 && imgHeight > 0) {
                g.drawImage(t.image, t.x, t.y, imgWidth, imgHeight, this);
            }
        }
    }

    public int getTileWidth() {
        return this.tileWidth;
    }

    public int getTileHeight() {
        return this.tileHeight;
    }
}