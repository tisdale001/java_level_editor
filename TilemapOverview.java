import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;


public class TilemapOverview {
    private static TilemapOverview instance = null; // Singleton instance
    private JFrame newFrame;
    private JPanel overviewMapPanel;
    private LevelEditor levelEditor;
    private ArrayList<ArrayList<Integer>> levelArr;
    private ArrayList<ArrayList<Integer>> mcLevelArr;
    private int numRows;
    private int numCols;
    private int tileWidth;
    private int tileHeight;
    private int scaleFactor = 4;
    private int scaledWidth;
    private int scaledHeight;

    private TilemapOverview() {

        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.8);
        int height = (int) (screenSize.height * 0.8);

        // Create new frame
        newFrame = new JFrame("Tilemap Overview");
        newFrame.setSize(width, height);
        newFrame.setLocationRelativeTo(null); // Center on screen
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel canvasPanel = new JPanel();
        canvasPanel.setLayout(null);  // Using null layout for absolute positioning

        // Set up canvas panel for custom drawing
        overviewMapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTiles(g);
                drawMovingColumnTiles(g);
            }
        };

        JScrollPane overviewScrollPane = new JScrollPane(overviewMapPanel);
        overviewScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        overviewScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        overviewScrollPane.setBounds(20, 20, width - 40, height - 80);
        canvasPanel.add(overviewScrollPane);

        // Add the scroll pane to the frame
        newFrame.add(canvasPanel);
    }

    private void drawTiles(Graphics g) {
        // Draw tiles (scaled down for the overview window)
        for (int i = 0; i < levelArr.size(); i++) {
            for (int j = 0; j < levelArr.get(i).size(); j++) {
                int tileID = levelArr.get(i).get(j);
                if (tileID != -1) {
                    BufferedImage image = levelEditor.getImageFromTileID(tileID);
                    int yPos = i * scaledHeight;
                    int xPos = j * scaledWidth;

                    g.drawImage(image, xPos, yPos, scaledWidth, scaledHeight, overviewMapPanel);
                }
            }
        }
    }

    private void drawMovingColumnTiles(Graphics g) {
        // Draw tiles (scaled down for the overview window)
        for (int i = 0; i < mcLevelArr.size(); i++) {
            for (int j = 0; j < mcLevelArr.get(i).size(); j++) {
                int mcTileID = mcLevelArr.get(i).get(j);
                if (mcTileID != -1) {
                    BufferedImage image = levelEditor.getImageFromMovingColumnTileID(mcTileID);
                    int yPos = i * scaledHeight;
                    int xPos = j * scaledWidth;

                    g.drawImage(image, xPos, yPos, scaledWidth, scaledHeight, overviewMapPanel);
                }
            }
        }
    }

    public void initiateOverviewMap(LevelEditor levelEditor, ArrayList<ArrayList<Integer>> levelArr, ArrayList<ArrayList<Integer>> mcLevelArr, int numRows, int numCols,
            int tileWidth, int tileHeight) {
        this.levelEditor = levelEditor;
        this.levelArr = levelArr;
        this.mcLevelArr = mcLevelArr;
        this.numRows = numRows;
        this.numCols = numCols;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        // Scale down the tile image for the overview window
        this.scaledWidth = tileWidth / scaleFactor;
        this.scaledHeight = tileHeight / scaleFactor;
    }

    public void placeTileInLevel(int x, int y, int tileID) {
        int col = x / this.tileWidth;
        int row = y / this.tileHeight;
        if (row >= 0 && row < this.levelArr.size() && col >= 0 && col < this.levelArr.get(row).size()) {
            this.levelArr.get(row).set(col, tileID);
        }
    }

    public void placeMovingColumnTileInLevel(int x, int y, int mcTileID) {
        int col = x / this.tileWidth;
        int row = y / this.tileHeight;
        if (row >= 0 && row < this.mcLevelArr.size() && col >= 0 && col < this.mcLevelArr.get(row).size()) {
            this.mcLevelArr.get(row).set(col, mcTileID);
        }
    }

    public void setPreferredSize() {
        this.overviewMapPanel.setPreferredSize(new Dimension(this.numCols * this.scaledWidth, this.numRows * this.scaledHeight));
    }

    public void revalidate() {
        this.overviewMapPanel.revalidate();
    }

    public void repaint() {
        this.overviewMapPanel.repaint();
    }

    // Static method to get instance or create a new one
    public static TilemapOverview getInstance() {
        if (instance == null || !instance.newFrame.isDisplayable()) {
            instance = new TilemapOverview();
        }
        return instance;
    }

    public void openNewWindow() {
        if (instance == null || !instance.newFrame.isDisplayable()) {
            instance.newFrame.setVisible(true);
        } else {
            // Just bring the window to the front
            instance.newFrame.toFront();
        }
    }

    public void refresh() {
        // This can be used to update or refresh the window (if needed)
        SwingUtilities.invokeLater(() -> {
            if (instance != null && instance.newFrame.isVisible()) {
                // Perform any refresh or UI update logic here
                instance.newFrame.repaint();
                instance.newFrame.revalidate();
            }
        });
    }
}