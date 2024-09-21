import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;



public class TileGridPanel extends JPanel {
    private int tileWidth;
    private int tileHeight;
    private int numRows;
    private int numCols;
    private ArrayList<Tile> tileArr;
    private LevelEditor levelEditor;

    public TileGridPanel(LevelEditor levelEditor, ArrayList<Tile> tileArr, int numCols, int tileWidth, int tileHeight) {
        this.levelEditor = levelEditor;
        this.tileArr = tileArr;
        this.numCols = numCols;
        this.numRows = calculateNumRows();
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        setPreferredSize(new Dimension(this.numCols * this.tileWidth, this.numRows * this.tileHeight));

        // Add a mouse listener to handle click events
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Get the x and y coordinates of the click
                int x = e.getX();
                int y = e.getY();

                // Display the coordinates
                if (SwingUtilities.isLeftMouseButton(e)) {
                    System.out.println("Left clicked at: (" + x + ", " + y + ")");
                    int tileID = getTileID(x, y);
                    TileGridPanel.this.levelEditor.setCurTile(tileID);
                    System.out.println(String.format("current tile = %d", tileID));
                }

                // Optionally, you can trigger a repaint or other actions based on the click
                // repaint();
            }
        });
    }

    private int getTileID(int x, int y) {
        int col = x / this.tileWidth;
        int row = y / this.tileHeight;
        if (col >= 0 && col < this.numCols && row >= 0 && row < this.numRows) {
            int idx = row * this.numCols + col;
            if (idx < this.tileArr.size()) {
                return this.tileArr.get(idx).getID();
            }
        }
        return -1;
    }

    private int calculateNumRows() {
        int rows = (int) Math.ceil((double) this.tileArr.size() / this.numCols);
        return rows;
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

        // Render a tile image in each grid cell
        int counter = 0;
        for (int y = 0; y < height; y += this.tileHeight) {
            for (int x = 0; x < width; x += this.tileWidth) {
                if (counter < this.tileArr.size()) {
                    BufferedImage tileImage = this.tileArr.get(counter).getImage();
                    if (tileImage != null) {
                        g.drawImage(tileImage, x, y, this.tileWidth, this.tileHeight, this);
                    }
                    counter++;
                }
            }
        }

    }
}