import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.text.AbstractDocument;

// compile: javac LevelEditor.java LevelFileWriter.java



public class LevelEditor {
    private final int scaledTileWidth = 32;
    private final int scaledTileHeight = 32;
    private ArrayList<TileSet> fgTileSetArr = new ArrayList<>();
    private ArrayList<TileSet> bgTileSetArr = new ArrayList<>();
    private ArrayList<Tile> fgTileArr = new ArrayList<>();
    private ArrayList<Tile> bgTileArr = new ArrayList<>();
    private int fgNumCols = 21;
    private int bgNumCols = 21;
    private int levelNumRows = 20;
    private int levelNumCols = 50;
    private int curTileID = -1;
    private JTextField rowTextField;
    private JTextField colTextField;
    private LevelTileGridPanel levelTileGridPanel;
    private String tileSetName = "None";
    private JComboBox<String> levelSelector;

    public LevelEditor() {
        createTileSetArrays();
        createForeGroundSet();
        createBackGroundSet();
    }

    private void createTileSetArrays() {
        // create a name for this setting: this will be the folder name in which the levels are contained

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // this.tileSetName = "First_Try";

        // TileSet fgTileSet1 = createTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 54, this.scaledTileWidth, this.scaledTileHeight, 0, 20);
        // fgTileSetArr.add(fgTileSet1);

        // TileSet fgTileSet2 = createTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 58, this.scaledTileWidth, this.scaledTileHeight, 0, 20 + 5 * 54 + 27);
        // fgTileSetArr.add(fgTileSet2);

        // TileSet bgTileSet1 = createTileSet("Assets/tilesheets/sewer_bricks.png", 3, 6, 52, 54, this.scaledTileWidth,
        // this.scaledTileHeight, 11 * 52, 2 * 54);
        // bgTileSetArr.add(bgTileSet1);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        this.tileSetName = "Sewer";

        TileSet fgTileSet1 = createTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 54, this.scaledTileWidth, this.scaledTileHeight, 0, 20);
        fgTileSetArr.add(fgTileSet1);

        TileSet fgTileSet2 = createTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 58, this.scaledTileWidth, this.scaledTileHeight, 0, 20 + 5 * 54 + 27);
        fgTileSetArr.add(fgTileSet2);

        TileSet fgTileSet3 = createTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 52, this.scaledTileWidth, this.scaledTileHeight, 0, 20 + 16 * 54 + 18);
        fgTileSetArr.add(fgTileSet3);

        TileSet fgTileSet4 = createTileSet("Assets/tilesheets/light_wooden_beams.png", 1, 5, 91, 81, this.scaledTileWidth, this.scaledTileHeight, 5, 934);
        fgTileSetArr.add(fgTileSet4);

        TileSet fgTileSet5 = createTileSet("Assets/tilesheets/light_wooden_beams_rotated.png", 5, 1, 81, 91, scaledTileWidth, scaledTileHeight, 6, 4);
        fgTileSetArr.add(fgTileSet5);

        TileSet bgTileSet1 = createTileSet("Assets/tilesheets/sewer_bricks.png", 3, 6, 52, 54, this.scaledTileWidth,
        this.scaledTileHeight, 11 * 52, 2 * 54);
        bgTileSetArr.add(bgTileSet1);

        TileSet bgTileSet2 = createTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 56, this.scaledTileWidth, this.scaledTileHeight, 0, 20 + 6 * 54 + 27);
        bgTileSetArr.add(bgTileSet2);

        TileSet bgTileSet3 = createTileSet("Assets/tilesheets/black_tiles.png", 1, 1, 140, 120, scaledTileWidth, scaledTileHeight, 370, 10);
        bgTileSetArr.add(bgTileSet3);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // // Metroid Level
        // this.tileSetName = "Brinstar";

        // TileSet fgTileSet1 = createTileSet("Assets/tilesheets/metroid_tileset.png", 2, 5, 18, 18, this.scaledTileWidth, this.scaledTileHeight, 11, 35);
        // fgTileSetArr.add(fgTileSet1);


    }

    private void createForeGroundSet() {
        int counter = 100;
        for (TileSet tileSet : fgTileSetArr) {
            for (Tile tile : tileSet.getTileArr()) {
                tile.setID(counter);
                fgTileArr.add(tile);
                counter++;
            }
        }
    }

    private void createBackGroundSet() {
        int counter = -100;
        for (TileSet tileSet : bgTileSetArr) {
            for (Tile tile : tileSet.getTileArr()) {
                tile.setID(counter);
                bgTileArr.add(tile);
                counter--;
            }
        }
    }

    // Method to be called when the Refresh button is clicked
    private void chooseHowToRefresh() {
        JFrame popupFrame = new JFrame("Refresh");
        popupFrame.setSize(300, 200);
        popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close the pop-up window
        JPanel popupPanel = new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS)); // Vertical layout
        // Create radio buttons for "Top"/"Bottom"
        JRadioButton topButton = new JRadioButton("Top");
        JRadioButton bottomButton = new JRadioButton("Bottom");

        // Group them so only one can be selected at a time
        ButtonGroup topBottomGroup = new ButtonGroup();
        topBottomGroup.add(topButton);
        topBottomGroup.add(bottomButton);

        // Create radio buttons for "Left"/"Right"
        JRadioButton leftButton = new JRadioButton("Left");
        JRadioButton rightButton = new JRadioButton("Right");

        // Group them so only one can be selected at a time
        ButtonGroup leftRightGroup = new ButtonGroup();
        leftRightGroup.add(leftButton);
        leftRightGroup.add(rightButton);

        // Set initial selections
        bottomButton.setSelected(true);  // "Bottom" selected by default
        rightButton.setSelected(true); // "Right" selected by default

        // Set alignment to center for all components
        topButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton popupButton = new JButton("Refresh");
        popupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        popupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // You can access the selected radio buttons here if needed
                String topBottomSelection = topButton.isSelected() ? "Top" : "Bottom";
                String leftRightSelection = leftButton.isSelected() ? "Left" : "Right";
                // Get Rows and Cols from text input
                String rows = LevelEditor.this.rowTextField.getText();
                String cols = LevelEditor.this.colTextField.getText();

                LevelEditor.this.refreshContent(topBottomSelection, leftRightSelection, rows, cols);

                // Close the pop-up window
                popupFrame.dispose();
            }
        });
        popupPanel.add(topButton);
        popupPanel.add(bottomButton);
        popupPanel.add(leftButton);
        popupPanel.add(rightButton);
        popupPanel.add(popupButton);
        popupFrame.add(popupPanel);
        popupFrame.setVisible(true);
    }

    private void refreshContent(String topBottomSelection, String leftRightSelection, String rows, String cols) {
        System.out.println("Content refreshed!");
        int newNumRows = this.levelNumRows;
        int newNumCols = this.levelNumCols;
        try {
            int r = Integer.parseInt(rows);
            newNumRows = r;
        } catch (NumberFormatException e) {
            System.out.println("Number incorrectly formatted");
        }
        try {
            int c = Integer.parseInt(cols);
            newNumCols = c;
        } catch (NumberFormatException e) {
            System.out.println("Number incorrectly formatted");
        }
        if (newNumRows == 0 || newNumCols == 0) {
            System.out.println("Level cannot have zero rows or zero columns.");
            return;
        }
        int deltaRows = newNumRows - this.levelNumRows;
        int deltaCols = newNumCols - this.levelNumCols;
        this.levelTileGridPanel.refreshRowsCols(topBottomSelection, leftRightSelection, deltaRows, deltaCols);
        this.levelNumRows = newNumRows;
        this.levelNumCols = newNumCols;
    }

    private void loadContent() {
        System.out.println("Content loaded!");
        String filePath = "Assets/Levels/" + this.tileSetName + "/";
        String fileName = (String) this.levelSelector.getSelectedItem();
        if (fileName == null || fileName == "") {
            return;
        }
        // get data
        LevelData levelData = this.levelTileGridPanel.loadLevelFromFile(filePath, fileName);
        if (levelData != null) {
            this.levelNumRows = levelData.numRows;
            this.levelNumCols = levelData.numCols;
            this.tileSetName = levelData.tileSetFolderName;
            // set data in textFields
            this.rowTextField.setText(Integer.toString(this.levelNumRows));
            this.colTextField.setText(Integer.toString(this.levelNumCols));
        }
        
    }

    private void chooseHowToSaveContent() {
        System.out.println("Content saved!");
        JFrame popupFrame = new JFrame("Save");
        popupFrame.setSize(400, 300);
        popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close the pop-up window
        JPanel popupPanel = new JPanel();
        // popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS)); // Vertical
        popupPanel.setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel popupLabel = new JLabel("Select or enter a level:");

        // Create an editable JComboBox
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setEditable(true);

        // Populate the JComboBox with file names from "Assets/Levels/tilsSetName"
        File directory = new File("Assets/Levels/" + this.tileSetName);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".lvl")); // Only add .lvl files
            if (files != null) {
                for (File file : files) {
                    comboBox.addItem(file.getName());
                }
            }
        }
        // Limit the size of the comboBox
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, comboBox.getPreferredSize().height));
        // Set alignment to center for all components
        popupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton popupButton = new JButton("Save");
        popupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        popupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comboBoxInput = (String) comboBox.getSelectedItem();
                if (comboBoxInput == "" || comboBoxInput == null) {
                    System.out.println("Please enter a name to save the level.");
                    return;
                }
                if (!comboBoxInput.endsWith(".lvl")) {
                    comboBoxInput = comboBoxInput + ".lvl";
                }
                String filePath = "Assets/Levels/" + LevelEditor.this.tileSetName + "/";
                LevelEditor.this.checkLevelNameForSaving(filePath, comboBoxInput, LevelEditor.this.tileSetName);

                // Close the pop-up window
                popupFrame.dispose();
            }
        });
        // add elements here
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(popupLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(comboBox);
        popupPanel.add(centerPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(popupButton);
        popupPanel.add(buttonPanel, BorderLayout.SOUTH);
        popupFrame.add(popupPanel);
        popupFrame.setVisible(true);
    }

    private void checkLevelNameForSaving(String filePath, String fileName, String tileSetFolderName) {
        System.out.println("checkLevelNameForSaving()");
        File file = new File(filePath + fileName);
        if (file.exists()) {
            createOverridePopup(filePath, fileName, tileSetFolderName);
        } else {
            saveContent(filePath, fileName, tileSetFolderName);
        }
    }

    private void createOverridePopup(String filePath, String fileName, String tileSetFolderName) {
        JFrame popupFrame = new JFrame("Override");
        popupFrame.setSize(400, 300);
        popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close the pop-up window
        JPanel popupPanel = new JPanel();
        popupPanel.setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel popupLabel = new JLabel("This level name alread exists: OVERRIDE?");

        popupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton popupButton = new JButton("Save");
        popupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        popupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LevelEditor.this.saveContent(filePath, fileName, tileSetFolderName);

                // Close the pop-up window
                popupFrame.dispose();
            }
        });

        JButton popupButton2 = new JButton("Cancel");
        popupButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        popupButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Close the pop-up window
                popupFrame.dispose();
            }
        });
        // add elements here
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(popupLabel);
        
        popupPanel.add(centerPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(popupButton);
        buttonPanel.add(popupButton2);
        popupPanel.add(buttonPanel, BorderLayout.SOUTH);
        popupFrame.add(popupPanel);
        popupFrame.setVisible(true);
    }

    private void saveContent(String filePath, String fileName, String tileSetFolderName) {
        System.out.println("saveContent()");
        this.levelTileGridPanel.saveGridAsLevel(filePath, fileName, tileSetFolderName);
        // Refresh levelSelector so it has new saved level
        // Directory where .lvl files are stored

        // Get list of .lvl files in the directory
        File directory = new File(filePath);
        String[] levels = {};
        if (directory.exists() && directory.isDirectory()) {
            levels = directory.list((dir, name) -> name.endsWith(".lvl"));
        }
        if (levels != null && levels.length > 0) {
            // Clear the existing items in the levelSelector
            levelSelector.removeAllItems();
            // Add the prompt again
            levelSelector.addItem("Select Level to Load");
            // Add all the levels to the levelSelector
            for (String level : levels) {
                levelSelector.addItem(level);
            }
        }
    }

    private void createEditor() {
        JFrame frame = new JFrame("Level Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 850);

        // Create a JPanel to act as a canvas for drawing and adding buttons
        JPanel canvasPanel = new JPanel();
        canvasPanel.setLayout(null);  // Using null layout for absolute positioning


        // JPanel fgTilePanel = new JPanel();
        // fgTilePanel.setPreferredSize(new Dimension(800, 600));
        // TileGridPanel(int tileArrSize, int numCols, int tileWidth, int tileHeight)
        TileGridPanel fgTilePanel = new TileGridPanel(this, this.fgTileArr, this.fgNumCols, this.scaledTileWidth, this.scaledTileHeight);
        JScrollPane fgTileScrollPane = new JScrollPane(fgTilePanel);
        fgTileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        fgTileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        fgTileScrollPane.setBounds(100, 50, 700, 150);
        canvasPanel.add(fgTileScrollPane);

        TileGridPanel bgTilePanel = new TileGridPanel(this, this.bgTileArr, this.bgNumCols, this.scaledTileWidth, this.scaledTileHeight);
        // bgTilePanel.setPreferredSize(new Dimension(800, 600));
        JScrollPane bgTileScrollPane = new JScrollPane(bgTilePanel);
        bgTileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bgTileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        bgTileScrollPane.setBounds(800, 50, 700, 150);
        canvasPanel.add(bgTileScrollPane);

        this.levelTileGridPanel = new LevelTileGridPanel(this, this.levelNumRows, this.levelNumCols, this.scaledTileWidth, this.scaledTileHeight);
        JScrollPane gameLevelScrollPane = new JScrollPane(levelTileGridPanel);
        gameLevelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gameLevelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        gameLevelScrollPane.setBounds(100, 200, 1400, 500);
        canvasPanel.add(gameLevelScrollPane);

        
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Padding around components
        buttonPanel.setBounds(600, 650, 400, 200);

        // Add JLabel
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.anchor = GridBagConstraints.WEST;
        buttonPanel.add(new JLabel("Rows:"), gbc);

        // Add JTextField
        gbc.gridx = 1; // Column 1
        gbc.gridy = 0; // Row 0
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Fill horizontally
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 1.0; // Allow JTextField to expand
        this.rowTextField = new JTextField(String.valueOf(this.levelNumRows), 15);
        // Apply the DocumentFilter to restrict input to numbers only
        ((AbstractDocument) rowTextField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        buttonPanel.add(rowTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.EAST;
        gbc.anchor = GridBagConstraints.EAST;
        buttonPanel.add(new JLabel("Cols:"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // Fill horizontally
        gbc.weightx = 1.0;
        this.colTextField = new JTextField(String.valueOf(this.levelNumCols), 15);
        // Apply the DocumentFilter to restrict input to numbers only
        ((AbstractDocument) colTextField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        buttonPanel.add(colTextField, gbc);

        JButton refreshButton = new JButton("Refresh");
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        buttonPanel.add(refreshButton, gbc);
        // Add an ActionListener to the button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method when the button is clicked
                chooseHowToRefresh();
            }
        });

        // Row 1 - Add JComboBox (Dropdown Menu)
        gbc.gridx = 0; // Start from the first column
        gbc.gridy = 1; // Move to row 1
        gbc.gridwidth = 3; // Let the dropdown span across three columns
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        // Directory where .lvl files are stored
        String filePath = "Assets/Levels/" + this.tileSetName + "/";

        // Get list of .lvl files in the directory
        File directory = new File(filePath);
        String[] levels = {};
        if (directory.exists() && directory.isDirectory()) {
            levels = directory.list((dir, name) -> name.endsWith(".lvl"));
        }
        this.levelSelector = new JComboBox<>(levels);
        // Set the initial prompt
        levelSelector.insertItemAt("Select Level to Load", 0);
        levelSelector.setSelectedIndex(0);
        buttonPanel.add(levelSelector, gbc);

        JButton loadButton = new JButton("Load");
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        buttonPanel.add(loadButton, gbc);
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method when the button is clicked
                loadContent();
            }
        });

        JButton saveButton = new JButton("Save");
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        buttonPanel.add(saveButton, gbc);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method when the button is clicked
                chooseHowToSaveContent();
            }
        });

        canvasPanel.add(buttonPanel);
        // Add the scroll pane to the frame
        frame.add(canvasPanel);
        // Show the window
        frame.setVisible(true);
    }

    private TileSet createTileSet(String filePath, int tileSetRows, int tileSetCols, int width, int height, int scaledWidth, 
    int scaledHeight, int offSetX, int offSetY) {
        TileSet tileSet = new TileSet();
        try {
            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                System.out.println("File does not exist!");
                return tileSet;
            }
            BufferedImage tileSheet = ImageIO.read(imageFile);
            for (int j = 0; j < tileSetRows; j++) {
                for (int i = 0; i < tileSetCols; i++) {
                    BufferedImage scaledImage = getScaledImage(tileSheet.getSubimage(offSetX + (i * width),
                            offSetY + (j * height), width, height), scaledWidth, scaledHeight);
                    Tile tile = new Tile(scaledImage);
                    tileSet.addTile(tile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tileSet;
    }

    private BufferedImage getScaledImage(BufferedImage originalImage, int scaledWidth, int scaledHeight) {
        Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        BufferedImage scaledBufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledBufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        return scaledBufferedImage;
    }

    public void setCurTile(int tileID) {
        this.curTileID = tileID;
    }

    public int getCurTile() {
        return this.curTileID;
    }

    public BufferedImage getImageFromTileID(int tileID) {
        if (tileID < -1) {
            // back ground tile
            int idx = (tileID + 100) * (-1);
            return this.bgTileArr.get(idx).getImage();
        } else if (tileID >= 0) {
            int idx = tileID - 100;
            return this.fgTileArr.get(idx).getImage();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            

            LevelEditor editor = new LevelEditor();
            editor.createEditor();
        });
    }

}
