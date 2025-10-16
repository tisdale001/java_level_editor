import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.text.AbstractDocument;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

// compile: javac *.java
// OR
// compile: javac LevelEditor.java LevelFileWriter.java TilemapOverview.java


public class LevelEditor {
    private final int scaledTileWidth = 32;
    private final int scaledTileHeight = 32;
    private ArrayList<TileSet> fgTileSetArr = new ArrayList<>();
    private ArrayList<TileSet> bgTileSetArr = new ArrayList<>();
    private ArrayList<BeastieTileSet> beastieTileSetArr = new ArrayList<>();
    private ArrayList<Tile> fgTileArr = new ArrayList<>();
    private ArrayList<Tile> bgTileArr = new ArrayList<>();
    private ArrayList<BeastieTile> beastieTileArr = new ArrayList<>();
    private int fgNumCols = 21;
    private int bgNumCols = 10;
    private int beastieNumCols = 10;
    private int levelNumRows = 20;
    private int levelNumCols = 50;
    private int curTileID = -1;
    private JTextField rowTextField;
    private JTextField colTextField;
    private LevelTileGridPanel levelTileGridPanel;
    public BeastieGridPanel beastieGridPanel;
    private String tileSetName = "None";
    private JComboBox<String> levelSelector;
    // Beastie variables
    private TilemapOverview overview = TilemapOverview.getInstance();
    public static final int BEASTIE_PREFIX = 10000; // access tish LevelEditor.BEASTIE_PREFIX
    private DragOverlayPane dragOverlay;
    private Image curBeastieImage = null;
    private int curBeastieTileId = -1;
    // Beastie constants
    public static final int ANEMONE_FLOOR = 0;
    public static final int ANEMONE_LEFT_WALL = 1;
    public static final int ANEMONE_CEILING = 2;
    public static final int ANEMONE_RIGHT_WALL = 3;
    public static final int PIRANHA_RIGHT = 4;
    public static final int PIRANHA_LEFT = 5;
    public static final int ALLIGATOR_RIGHT = 6;
    public static final int ALLIGATOR_LEFT = 7;
    public static final int MAGIC_FISH_RIGHT = 8;
    public static final int MAGIC_FISH_LEFT = 9;
    public static final int PUFFERFISH = 10;
    private ArrayList<Integer> snapIntoPlaceBeasties = new ArrayList<>(Arrays.asList(ANEMONE_FLOOR, ANEMONE_LEFT_WALL, ANEMONE_CEILING, ANEMONE_RIGHT_WALL));
    private ArrayList<Integer> enlargeToFourByFourBeasties = new ArrayList<>(Arrays.asList(PUFFERFISH));

    public LevelEditor() {
        createTileSetArrays();
        createBeastieTileSetArrays();
        createForeGroundSet();
        createBackGroundSet();
        createBeastieSet();
    }

    private void createBeastieTileSetArrays() {
        // private BeastieTileSet createBeastieTileSet(String filePath, int tileSetRows, int tileSetCols, int width, int height, int scaledWidth, 
        //     int scaledHeight, int offSetX, int offSetY)

        // Anemones
        BeastieTileSet beastieTileSet1 = createBeastieTileSet("Assets/Beasties/SpriteSheets/anemone_floor_labeled.png", 1, 1, 850, 625, scaledTileWidth, scaledTileHeight, 70, 163);
        beastieTileSetArr.add(beastieTileSet1);
        BeastieTileSet beastieTileSet2 = createBeastieTileSet("Assets/Beasties/SpriteSheets/anemone_left_wall_labeled.png", 1, 1, 625, 850, scaledTileWidth, scaledTileHeight, 163, 70);
        beastieTileSetArr.add(beastieTileSet2);
        BeastieTileSet beastieTileSet3 = createBeastieTileSet("Assets/Beasties/SpriteSheets/anemone_ceiling_labeled.png", 1, 1, 850, 625, scaledTileWidth, scaledTileHeight, 70, 213);
        beastieTileSetArr.add(beastieTileSet3);
        BeastieTileSet beastieTileSet4 = createBeastieTileSet("Assets/Beasties/SpriteSheets/anemone_right_wall_labeled.png", 1, 1, 625, 850, scaledTileWidth, scaledTileHeight, 163, 70);
        beastieTileSetArr.add(beastieTileSet4);
        // Piranha
        BeastieTileSet beastieTileSet5 = createBeastieTileSet("Assets/Beasties/SpriteSheets/piranha_left_snapshot.png", 1, 1, 130, 125, scaledTileWidth, scaledTileHeight, 3, 3);
        beastieTileSetArr.add(beastieTileSet5);
        BeastieTileSet beastieTileSet6 = createBeastieTileSet("Assets/Beasties/SpriteSheets/piranha_right_snapshot.png", 1, 1, 130, 125, scaledTileWidth, scaledTileHeight, 3, 3);
        beastieTileSetArr.add(beastieTileSet6);
        // Alligators
        BeastieTileSet beastieTileSet7 = createBeastieTileSet("Assets/Beasties/SpriteSheets/alligator_left_tile.png", 1, 1, 59, 59, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet7);
        BeastieTileSet beastieTileSet8 = createBeastieTileSet("Assets/Beasties/SpriteSheets/alligator_right_tile.png", 1, 1, 59, 59, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet8);
        // Magic Fish
        BeastieTileSet beastieTileSet9 = createBeastieTileSet("Assets/Beasties/SpriteSheets/magic_fish_left_tile.png", 1, 1, 110, 78, scaledTileWidth, scaledTileHeight, 4, 0);
        beastieTileSetArr.add(beastieTileSet9);
        BeastieTileSet beastieTileSet10 = createBeastieTileSet("Assets/Beasties/SpriteSheets/magic_fish_right_tile.png", 1, 1, 113, 84, scaledTileWidth, scaledTileHeight, 0, 2);
        beastieTileSetArr.add(beastieTileSet10);
        // Pufferfish
        BeastieTileSet beastieTileSet11 = createBeastieTileSet("Assets/Beasties/SpriteSheets/pufferfish_tile.png", 1, 1, 194, 180, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet11);
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
        //createTileSet(String filePath, int tileSetRows, int tileSetCols, int width, int height, int scaledWidth, 
            //int scaledHeight, int offSetX, int offSetY)

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

        // sewer pipes
        TileSet fgTileSet6 = createTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 105, 120, scaledTileWidth, scaledTileHeight, 52, 870);
        fgTileSetArr.add(fgTileSet6);

        TileSet fgTileSet7 = createTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 54, 364);
        fgTileSetArr.add(fgTileSet7);

        TileSet fgTileSet8 = createTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 54, 438);
        fgTileSetArr.add(fgTileSet8);

        TileSet fgTileSet9 = createTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 54, 490);
        fgTileSetArr.add(fgTileSet9);

        TileSet fgTileSet10 = createTileSet("Assets/tilesheets/sewer_pipes_sheet_horizontal_flip.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 756, 65);
        fgTileSetArr.add(fgTileSet10);

        TileSet fgTileSet11 = createTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 157, 65);
        fgTileSetArr.add(fgTileSet11);

        TileSet fgTileSet12 = createTileSet("Assets/tilesheets/sewer_pipes_sheet_horizontal_flip.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 586, 65);
        fgTileSetArr.add(fgTileSet12);

        TileSet fgTileSet13 = createTileSet("Assets/tilesheets/coral_ground_tilesheet.png", 1, 9, 96, 96, scaledTileWidth, scaledTileHeight, 0, 0);
        fgTileSetArr.add(fgTileSet13);

        TileSet fgTileSet14 = createTileSet("Assets/tilesheets/mvrk_coralgroundm_2x.png", 1, 1, 48, 48, scaledTileWidth, scaledTileHeight, 0, 0);
        fgTileSetArr.add(fgTileSet14);

        // background tilesets
        TileSet bgTileSet1 = createTileSet("Assets/tilesheets/sewer_bricks.png", 3, 6, 52, 54, this.scaledTileWidth,
        this.scaledTileHeight, 11 * 52, 2 * 54);
        bgTileSetArr.add(bgTileSet1);

        TileSet bgTileSet2 = createTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 56, this.scaledTileWidth, this.scaledTileHeight, 0, 20 + 6 * 54 + 27);
        bgTileSetArr.add(bgTileSet2);


        // TileSet bgTileSet3 = createTileSet("Assets/tilesheets/black_tiles.png", 1, 1, 140, 120, scaledTileWidth, scaledTileHeight, 370, 10);
        // bgTileSetArr.add(bgTileSet3);

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

    private void createBeastieSet() {
        int counter = 100;
        for (BeastieTileSet tileSet : beastieTileSetArr) {
            for (BeastieTile tile : tileSet.getBeastieTileArr()) {
                tile.setID(BEASTIE_PREFIX + counter);
                beastieTileArr.add(tile);
                counter++;
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
        String beastieFilepath = "Assets/Beasties/BeastieLevelData/";
        this.levelTileGridPanel.loadBeastiesFromFiles(beastieFilepath, fileName);
        this.refreshContent("Bottom", "Right", this.rowTextField.getText(), this.colTextField.getText());
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
        this.levelTileGridPanel.saveBeastiesToLevel(fileName);
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

        dragOverlay = new DragOverlayPane();
        frame.setGlassPane(dragOverlay);
        dragOverlay.setVisible(true);

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
        bgTileScrollPane.setBounds(800, 50, 350, 150);
        canvasPanel.add(bgTileScrollPane);

        this.levelTileGridPanel = new LevelTileGridPanel(this, this.levelNumRows, this.levelNumCols, this.scaledTileWidth, this.scaledTileHeight);
        JScrollPane gameLevelScrollPane = new JScrollPane(levelTileGridPanel);
        gameLevelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gameLevelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        gameLevelScrollPane.setBounds(100, 200, 1400, 500);

        this.beastieGridPanel = new BeastieGridPanel(this, this.levelTileGridPanel, this.beastieTileArr, this.beastieNumCols, this.scaledTileWidth, this.scaledTileHeight);
        JScrollPane beastieScrollPane = new JScrollPane(beastieGridPanel);
        beastieScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        beastieScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        beastieScrollPane.setBounds(1150, 50, 350, 150);

        canvasPanel.add(beastieScrollPane);
        canvasPanel.add(gameLevelScrollPane);

        this.levelTileGridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int tileId = getCurBeastieTileId();
                    Image tileImage = LevelEditor.this.getImageFromTileID(tileId);
        
                    if (tileImage != null) {
                        int gridX = e.getX();
                        int gridY = e.getY();
        
                        LevelEditor.this.levelTileGridPanel.placeBeastieTile(gridX, gridY, tileId, tileImage);
                        System.out.printf("Placed BeastieTile at grid (%d, %d)%n", gridX / scaledTileWidth, gridY / scaledTileHeight);
                    }
                }
            }
        });
        
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Padding around components
        buttonPanel.setBounds(550, 650, 500, 200);

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

        JButton overviewButton = new JButton("Overview");
        // GridBagConstraints for overviewButton (Larger, 2-row tall)
        gbc.gridx = 5;  // Column position
        gbc.gridy = 0;  // Start at row 0
        gbc.gridheight = 2;  // Span 2 vertical grid positions
        gbc.fill = GridBagConstraints.BOTH;  // Allow resizing in both directions
        gbc.weighty = 0;  // Allow vertical expansion only for this button
        gbc.weightx = 0;  // Do not affect horizontal distribution
        gbc.ipady = 30;  // Increase internal padding for height
        buttonPanel.add(overviewButton, gbc);
        // Add an ActionListener to the button
        overviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the method when the button is clicked
                overview.openNewWindow();
                overview.refresh();
            }
        });

        // Reset constraints for other buttons
        gbc.gridheight = 1;  // Ensure other buttons stay in 1 row
        gbc.weighty = 0;  // Prevent affecting their height
        gbc.ipady = 0;  // Remove extra padding

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

    private int getCurBeastieTileId() {
        return this.curBeastieTileId;
    }

    private BeastieTileSet createBeastieTileSet(String filePath, int tileSetRows, int tileSetCols, int width, int height, int scaledWidth, 
    int scaledHeight, int offSetX, int offSetY) {
        BeastieTileSet tileSet = new BeastieTileSet();
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
                    BeastieTile tile = new BeastieTile(scaledImage);
                    tileSet.addBeastieTile(tile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tileSet;
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
        } else if (tileID >= BEASTIE_PREFIX) {
            int idx = tileID - BEASTIE_PREFIX - 100;
            return this.beastieTileArr.get(idx).getImage();
        } else if (tileID >= 0) {
            int idx = tileID - 100;
            return this.fgTileArr.get(idx).getImage();
        }
        return null;
    }

    public void startDragging(Image img, Point start, int tileId) {
        curBeastieImage = img;
        curBeastieTileId = tileId;
        dragOverlay.setDraggedImage(img);
        dragOverlay.setMousePoint(start);
        dragOverlay.repaint();
    }
    
    public void updateDragLocation(Component source, Point p) {
        dragOverlay.setMousePoint(SwingUtilities.convertPoint(source, p, dragOverlay));
        dragOverlay.repaint();
    }
    
    public void stopDragging(Point releasePoint) {
        // Only place if there’s a valid beastie image being dragged
        if (curBeastieImage != null && curBeastieTileId != -1) {
            int gridX = -1;
            int gridY = -1;
            if (snapIntoPlaceBeasties.contains(curBeastieTileId - BEASTIE_PREFIX - 100)) {
                // Convert releasePoint to grid coordinates
                gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
            } else if (enlargeToFourByFourBeasties.contains(curBeastieTileId - BEASTIE_PREFIX - 100)) {
                gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                // Compute scaled dimensions
                int scaledWidth = this.scaledTileWidth * 4;
                int scaledHeight = this.scaledTileHeight * 4;

                // Create a new BufferedImage to hold the scaled image
                BufferedImage scaledBuffered = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = scaledBuffered.createGraphics();

                // For pixel art, use NEAREST_NEIGHBOR; for smoother scaling, use BILINEAR
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2.drawImage(curBeastieImage, 0, 0, scaledWidth, scaledHeight, null);
                g2.dispose();

                // Replace the current image with the scaled one
                curBeastieImage = scaledBuffered;
            } else {
                int imageWidth = curBeastieImage.getWidth(null);
                int imageHeight = curBeastieImage.getHeight(null);
                gridX = releasePoint.x - (imageWidth/2);
                gridY = releasePoint.y - (imageHeight/2);
            }

            // Place the image on the levelTileGridPanel
            levelTileGridPanel.placeBeastieTile(gridX, gridY, curBeastieTileId, curBeastieImage);
        }
        curBeastieTileId = -1;
        curBeastieImage = null;
        dragOverlay.setDraggedImage(null);
        dragOverlay.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            

            LevelEditor editor = new LevelEditor();
            editor.createEditor();
        });
    }

}
