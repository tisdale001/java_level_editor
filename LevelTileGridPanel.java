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




public class LevelTileGridPanel extends JPanel {
    private int tileWidth;
    private int tileHeight;
    private int numRows;
    private int numCols;
    private LevelEditor levelEditor;
    private ArrayList<ArrayList<Integer>> levelArr = new ArrayList<>();
    private boolean fastEntryMode = false;
    private boolean fastEraseMode = false;
    

    public LevelTileGridPanel(LevelEditor levelEditor, int numRows, int numCols, int tileWidth, int tileHeight) {
        this.levelEditor = levelEditor;
        this.numRows = numRows;
        this.numCols = numCols;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        createLevelArr();

        setPreferredSize(new Dimension(this.numCols * this.tileWidth, this.numRows * this.tileHeight));

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
                    LevelTileGridPanel.this.placeTileInLevel(x, y, -1);
                }

                // Optionally, you can trigger a repaint or other actions based on the click
                repaint();
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
                } else if (fastEraseMode) {
                    LevelTileGridPanel.this.placeTileInLevel(x, y, -1);
                    repaint();
                }
            }
        });
    }

    private void placeTileInLevel(int x, int y, int tileID) {
        int col = x / this.tileWidth;
        int row = y / this.tileHeight;
        if (row >= 0 && row < this.levelArr.size() && col >= 0 && col < this.levelArr.get(row).size()) {
            this.levelArr.get(row).set(col, tileID);
        }
    }

    private void createLevelArr() {
        for (int i = 0; i < this.numRows; i++) {
            ArrayList<Integer> innerList = new ArrayList<>();
            for (int j = 0; j < this.numCols; j++) {
                innerList.add(-1);
            }
            this.levelArr.add(innerList);
        }
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
    }

    public void saveGridAsLevel(String filePath, String fileName, String tileSetFolderName) {
        LevelFileWriter lfw = new LevelFileWriter(this.levelArr, this.levelArr.size(), this.levelArr.get(0).size());
        lfw.saveLevelToFile(filePath, fileName, tileSetFolderName);
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
    }
}