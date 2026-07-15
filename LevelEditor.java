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
    private static final String EDITOR_LEVELS_ROOT = "Assets/Levels/";
    private static final String EDITOR_MOVING_COLUMN_LEVELS_ROOT = "Assets/MovingColumnLevels/";
    private static final String EDITOR_BEASTIE_LEVEL_DATA_ROOT = "Assets/Beasties/BeastieLevelData/";

    private static final String LUCIAN_CAT_GAME_ASSETS_PATH = "/home/luciantisdale/AndroidStudioProjects/cat_game/Proto/app/src/main/assets/";
    private static final String WALLY_CAT_GAME_ASSETS_PATH = "C:\\cat_game\\Proto\\app\\src\\main\\assets";

    // Change this to WALLY_CAT_GAME_ASSETS_PATH when Wally uses the editor.
    private static final String SECOND_COPY_CAT_GAME_ASSETS_PATH = LUCIAN_CAT_GAME_ASSETS_PATH;

    private final int scaledTileWidth = 32;
    private final int scaledTileHeight = 32;
    private ArrayList<TileSet> fgTileSetArr = new ArrayList<>();
    private ArrayList<TileSet> bgTileSetArr = new ArrayList<>();
    private ArrayList<MovingColumnTileSet> mcTileSetArr = new ArrayList<>();
    private ArrayList<BeastieTileSet> beastieTileSetArr = new ArrayList<>();
    private ArrayList<Tile> fgTileArr = new ArrayList<>();
    private ArrayList<Tile> bgTileArr = new ArrayList<>();
    private ArrayList<MovingColumnTile> mcTileArr = new ArrayList<>();
    private ArrayList<BeastieTile> beastieTileArr = new ArrayList<>();
    private int fgNumCols = 10;
    private int bgNumCols = 10;
    private int mcNumCols = 10;
    private int beastieNumCols = 10;
    private int levelNumRows = 20;
    private int levelNumCols = 50;
    private int curTileID = -1;
    private int curMovingColumnTileID = -1;
    private JTextField rowTextField;
    private JTextField colTextField;
    private LevelTileGridPanel levelTileGridPanel;
    public BeastieGridPanel beastieGridPanel;
    private String tileSetName = "None";
    public JComboBox<String> levelSelector;
    private String curSelectedLevel;
    // Beastie variables
    private TilemapOverview overview = TilemapOverview.getInstance();
    private DragOverlayPane dragOverlay;
    private Image curBeastieImage = null;
    private int curBeastieTileId = -1;
    // Water Spout variables and Water Current variables
    private int curWaterSpout = -1;
    private PlacedWaterSpoutTile curPlacedWaterSpoutTile = null;
    // private ArrayList<PlacedWaterSpoutTile> curPlacedWaterSpoutArr = new ArrayList<>();
    // Moving Platform variables and platform border box variables
    private int curMovingPlatform = -1;
    private PlacedMovingPlatform curPlacedMovingPlatform = null;
    private int curMovingPlatformBorderBox = -1;
    // private PlacedMovingPlatformBorderBoxTile curPlacedMovingPlatformBorderBoxTile = null;
    // Moving Column variables
    // private int curMovingColumn = -1;
    private PlacedMovingColumn curPlacedMovingColumn = null;
    private int curMovingColumnBorderBox = -1;
    private int curBat = -1;
    private PlacedBat curPlacedBat = null;
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
    public static final int SWITCH_ON = 11;
    public static final int SWITCH_OFF = 12;
    public static final int TOGGLE_DOOR_OPEN = 13;
    public static final int TOGGLE_DOOR_CLOSED = 14;
    public static final int SPIDER_FLOOR_RIGHT = 15;
    public static final int SPIDER_FLOOR_LEFT = 16;
    public static final int SPIDER_CEILING_RIGHT = 17;
    public static final int SPIDER_CEILING_LEFT = 18;
    public static final int SPIDER_LEFT_WALL_UP = 19;
    public static final int SPIDER_LEFT_WALL_DOWN = 20;
    public static final int SPIDER_RIGHT_WALL_UP = 21;
    public static final int SPIDER_RIGHT_WALL_DOWN = 22;
    public static final int SPIDER_BORDER_BOX = 23;
    public static final int RAT_RIGHT = 24;
    public static final int RAT_LEFT = 25;
    public static final int RAT_BORDER_BOX_RIGHT = 26;
    public static final int RAT_BORDER_BOX_LEFT = 27;
    public static final int WATER_SPOUT_RIGHT = 28;
    public static final int WATER_SPOUT_LEFT = 29;
    public static final int WATER_SPOUT_UP = 30;
    public static final int WATER_SPOUT_DOWN = 31;
    public static final int WATER_CURRENT_RIGHT = 32;
    public static final int WATER_CURRENT_LEFT = 33;
    public static final int WATER_CURRENT_UP = 34;
    public static final int WATER_CURRENT_DOWN = 35;
    public static final int BEE_POT = 36;
    public static final int SPIKES_UP = 37;
    public static final int SPIKES_DOWN = 38;
    public static final int SAND_LEFT_BORDER = 39;
    public static final int SAND_LEFT = 40;
    public static final int SAND_RIGHT = 41;
    public static final int SAND_RIGHT_BORDER = 42;
    public static final int MOVING_PLATFORM_TILE = 43;
    public static final int MOVING_PLATFORM_BORDER_BOX_TILE = 44;
    public static final int DOG_RIGHT = 45;
    public static final int DOG_LEFT = 46;
    public static final int DOG_BORDER_BOX_LEFT = 47;
    public static final int DOG_BORDER_BOX_RIGHT = 48;
    public static final int MOVING_COLUMN_LEFT_BORDER = 49;
    public static final int MOVING_COLUMN_RIGHT_BORDER = 50;
    public static final int MOVING_COLUMN_UP_BORDER = 51;
    public static final int MOVING_COLUMN_DOWN_BORDER = 52;
    public static final int DEPTH_BORDER_BOX = 53;
    public static final int SCORPION_FLOOR_RIGHT = 54;
    public static final int SCORPION_FLOOR_LEFT = 55;
    public static final int SCORPION_CEILING_RIGHT = 56;
    public static final int SCORPION_CEILING_LEFT = 57;
    public static final int SCORPION_LEFT_WALL_UP = 58;
    public static final int SCORPION_LEFT_WALL_DOWN = 59;
    public static final int SCORPION_RIGHT_WALL_UP = 60;
    public static final int SCORPION_RIGHT_WALL_DOWN = 61;
    public static final int SCORPION_BORDER_BOX = 62;
    public static final int FLIGHT_PATH = 63;
    public static final int BAT_LEFT = 64;
    public static final int BAT_RIGHT = 65;
    public static final int BIRD_LEFT = 66;
    public static final int BIRD_RIGHT = 67;
    public static final int WATERFALL = 68;
    public static final int FALLING_ROCK = 69;
    public static final ArrayList<Integer> snapIntoPlaceBeasties = new ArrayList<>(Arrays.asList(ANEMONE_FLOOR, ANEMONE_LEFT_WALL, ANEMONE_CEILING, ANEMONE_RIGHT_WALL,
        SPIDER_FLOOR_RIGHT, SPIDER_FLOOR_LEFT, SPIDER_CEILING_RIGHT, SPIDER_CEILING_LEFT, SPIDER_LEFT_WALL_UP, SPIDER_LEFT_WALL_DOWN, SPIDER_RIGHT_WALL_UP,
        SPIDER_RIGHT_WALL_DOWN, SPIDER_BORDER_BOX, RAT_RIGHT, RAT_LEFT, RAT_BORDER_BOX_RIGHT, RAT_BORDER_BOX_LEFT, SPIKES_UP, SPIKES_DOWN, DOG_RIGHT, DOG_LEFT,
        DOG_BORDER_BOX_LEFT, DOG_BORDER_BOX_RIGHT, DEPTH_BORDER_BOX, SCORPION_FLOOR_RIGHT, SCORPION_FLOOR_LEFT, SCORPION_CEILING_RIGHT, SCORPION_CEILING_LEFT,
        SCORPION_LEFT_WALL_UP, SCORPION_LEFT_WALL_DOWN, SCORPION_RIGHT_WALL_UP, SCORPION_RIGHT_WALL_DOWN, SCORPION_BORDER_BOX, BIRD_LEFT, BIRD_RIGHT,
        FALLING_ROCK));
    public static final ArrayList<Integer> enlargeToFourByFourBeasties = new ArrayList<>(Arrays.asList(PUFFERFISH));
    public static final ArrayList<Integer> enlargeByOnePointTwentyFiveBeasties = new ArrayList<>(Arrays.asList(SWITCH_ON, SWITCH_OFF));
    public static final ArrayList<Integer> enlargeToOneByFourVerticallyBeasties = new ArrayList<>(Arrays.asList(TOGGLE_DOOR_OPEN, TOGGLE_DOOR_CLOSED));
    public static final ArrayList<Integer> enlargeToTwoByFiveVerticallyBeasties = new ArrayList<>(Arrays.asList(WATERFALL));
    public static final ArrayList<Integer> waterSpoutBeasties = new ArrayList<>(Arrays.asList(WATER_SPOUT_RIGHT, WATER_SPOUT_LEFT, WATER_SPOUT_UP, WATER_SPOUT_DOWN));
    public static final ArrayList<Integer> waterCurrentBeasties = new ArrayList<>(Arrays.asList(WATER_CURRENT_RIGHT, WATER_CURRENT_LEFT, WATER_CURRENT_UP, WATER_CURRENT_DOWN));
    public static final ArrayList<Integer> enlargeToFourByOneHorizontallyBeasties = new ArrayList<>(Arrays.asList(BEE_POT));
    public static final ArrayList<Integer> sandBeasties = new ArrayList<>(Arrays.asList(SAND_LEFT_BORDER, SAND_LEFT, SAND_RIGHT, SAND_RIGHT_BORDER));
    public static final ArrayList<Integer> movingPlatformBeasties = new ArrayList<>(Arrays.asList(MOVING_PLATFORM_TILE));
    public static final ArrayList<Integer> movingPlatformBorderBoxBeasties = new ArrayList<>(Arrays.asList(MOVING_PLATFORM_BORDER_BOX_TILE));
    public static final ArrayList<Integer> movingColumnBorderBoxBeasties = new ArrayList<>(Arrays.asList(MOVING_COLUMN_LEFT_BORDER, MOVING_COLUMN_RIGHT_BORDER,
        MOVING_COLUMN_UP_BORDER, MOVING_COLUMN_DOWN_BORDER));
    public static final ArrayList<Integer> flightPathBeasties = new ArrayList<>(Arrays.asList(FLIGHT_PATH));
    public static final ArrayList<Integer> batBeasties = new ArrayList<>(Arrays.asList(BAT_LEFT, BAT_RIGHT));
    public LevelEditor() {
        createTileSetArrays();
        createMovingColumnTileSetArrays();
        createBeastieTileSetArrays();
        createForeGroundSet();
        createBackGroundSet();
        createMovingColumnSet();
        createBeastieSet();
    }

    private static String withTrailingSeparator(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        return path.endsWith(File.separator) || path.endsWith("/") ? path : path + File.separator;
    }

    private String getEditorLevelPath() {
        return EDITOR_LEVELS_ROOT + this.tileSetName + "/";
    }

    private String getEditorMovingColumnLevelPath() {
        return EDITOR_MOVING_COLUMN_LEVELS_ROOT + this.tileSetName + "/";
    }

    private String getCatGameAssetsRoot() {
        return withTrailingSeparator(SECOND_COPY_CAT_GAME_ASSETS_PATH);
    }

    private String getCatGameLevelPath() {
        return getCatGameAssetsRoot();
    }

    private String getCatGameMovingColumnLevelPath() {
        return getCatGameAssetsRoot() + "MovingColumnLevels/" + this.tileSetName + "/";
    }

    private String getCatGameBeastiePath() {
        return getCatGameAssetsRoot() + "Beasties/";
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
        // Switches
        BeastieTileSet beastieTileSet12 = createBeastieTileSet("Assets/Beasties/SpriteSheets/switch_on_labeled.png", 1, 1, 36, 36, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet12);
        BeastieTileSet beastieTileSet13 = createBeastieTileSet("Assets/Beasties/SpriteSheets/switch_off_labeled.png", 1, 1, 36, 36, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet13);
        // Toggle Doors
        BeastieTileSet beastieTileSet14 = createBeastieTileSet("Assets/Beasties/SpriteSheets/vintage_door_open_labeled_tile.png", 1, 1, 90, 149, scaledTileWidth, scaledTileHeight, 23, 0);
        beastieTileSetArr.add(beastieTileSet14);
        BeastieTileSet beastieTileSet15 = createBeastieTileSet("Assets/Beasties/SpriteSheets/vintage_door_closed_labeled_tile.png", 1, 1, 90, 149, scaledTileWidth, scaledTileHeight, 23, 0);
        beastieTileSetArr.add(beastieTileSet15);
        // Spiders
        BeastieTileSet beastieTileSet16 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spider_floor_right_tile.png", 1, 1, 100, 100, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet16);
        BeastieTileSet beastieTileSet17 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spider_floor_left_tile.png", 1, 1, 100, 100, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet17);
        BeastieTileSet beastieTileSet18 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spider_ceiling_right_tile.png", 1, 1, 100, 100, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet18);
        BeastieTileSet beastieTileSet19 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spider_ceiling_left_tile.png", 1, 1, 100, 100, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet19);
        BeastieTileSet beastieTileSet20 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spider_left_wall_up_tile.png", 1, 1, 100, 100, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet20);
        BeastieTileSet beastieTileSet21 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spider_left_wall_down_tile.png", 1, 1, 100, 100, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet21);
        BeastieTileSet beastieTileSet22 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spider_right_wall_up_tile.png", 1, 1, 100, 100, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet22);
        BeastieTileSet beastieTileSet23 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spider_right_wall_down_tile.png", 1, 1, 100, 100, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet23);
        // Spider border box
        BeastieTileSet beastieTileSet24 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spider_border_box_labeled_tile.png", 1, 1, 360, 360, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet24);
        // Rats
        BeastieTileSet beastieTileSet25 = createBeastieTileSet("Assets/Beasties/SpriteSheets/rat_right_tile.png", 1, 1, 126, 87, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet25);
        BeastieTileSet beastieTileSet26 = createBeastieTileSet("Assets/Beasties/SpriteSheets/rat_left_tile.png", 1, 1, 126, 87, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet26);
        // Rat border boxes
        BeastieTileSet beastieTileSet27 = createBeastieTileSet("Assets/Beasties/SpriteSheets/rat_border_box_right_tile.png", 1, 1, 360, 360, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet27);
        BeastieTileSet beastieTileSet28 = createBeastieTileSet("Assets/Beasties/SpriteSheets/rat_border_box_left_tile.png", 1, 1, 360, 360, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet28);
        // Water spouts
        BeastieTileSet beastieTileSet29 = createBeastieTileSet("Assets/Beasties/SpriteSheets/water_spout_right_tile.png", 1, 1, 50, 50, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet29);
        BeastieTileSet beastieTileSet30 = createBeastieTileSet("Assets/Beasties/SpriteSheets/water_spout_left_tile.png", 1, 1, 50, 50, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet30);
        BeastieTileSet beastieTileSet31 = createBeastieTileSet("Assets/Beasties/SpriteSheets/water_spout_up_tile.png", 1, 1, 50, 50, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet31);
        BeastieTileSet beastieTileSet32 = createBeastieTileSet("Assets/Beasties/SpriteSheets/water_spout_down_tile.png", 1, 1, 50, 50, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet32);
        // Water Current arrows
        BeastieTileSet beastieTileSet33 = createBeastieTileSet("Assets/Beasties/SpriteSheets/green_right_arrow_tile.png", 1, 1, 450, 450, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet33);
        BeastieTileSet beastieTileSet34 = createBeastieTileSet("Assets/Beasties/SpriteSheets/green_left_arrow_tile.png", 1, 1, 450, 450, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet34);
        BeastieTileSet beastieTileSet35 = createBeastieTileSet("Assets/Beasties/SpriteSheets/green_up_arrow_tile.png", 1, 1, 450, 450, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet35);
        BeastieTileSet beastieTileSet36 = createBeastieTileSet("Assets/Beasties/SpriteSheets/green_down_arrow_tile.png", 1, 1, 450, 450, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet36);
        // Bee pots
        BeastieTileSet beastieTileSet37 = createBeastieTileSet("Assets/Beasties/SpriteSheets/metroid_tiles_cropped.png", 1, 1, 32, 7, scaledTileWidth, scaledTileHeight, 83, 80);
        beastieTileSetArr.add(beastieTileSet37);
        // Spikes
        BeastieTileSet beastieTileSet38 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spikes_upward_cropped_tile.png", 1, 1, 28, 19, scaledTileWidth, scaledTileHeight, 0, 2);
        beastieTileSetArr.add(beastieTileSet38);
        BeastieTileSet beastieTileSet39 = createBeastieTileSet("Assets/Beasties/SpriteSheets/spikes_downward_cropped_tile.png", 1, 1, 28, 19, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet39);
        // SandTiles
        BeastieTileSet beastieTileSet40 = createBeastieTileSet("Assets/Beasties/SpriteSheets/sand_L_tile.png", 1, 1, 148, 148, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet40);
        BeastieTileSet beastieTileSet41 = createBeastieTileSet("Assets/Beasties/SpriteSheets/sand_left_arrow_tile.png", 1, 1, 148, 148, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet41);
        BeastieTileSet beastieTileSet42 = createBeastieTileSet("Assets/Beasties/SpriteSheets/sand_right_arrow_tile.png", 1, 1, 148, 148, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet42);
        BeastieTileSet beastieTileSet43 = createBeastieTileSet("Assets/Beasties/SpriteSheets/sand_R_tile.png", 1, 1, 148, 148, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet43);
        // Moving Platforms
        BeastieTileSet beastieTileSet44 = createBeastieTileSet("Assets/Beasties/SpriteSheets/moving_platform_tile.png", 1, 1, 230, 230, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet44);
        // Moving platform border boxes
        BeastieTileSet beastieTileSet45 = createBeastieTileSet("Assets/Beasties/SpriteSheets/platform_border_box.png", 1, 1, 360, 360, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet45);
        // Dogs
        BeastieTileSet beastieTileSet46 = createBeastieTileSet("Assets/Beasties/SpriteSheets/dog_right_tile.png", 1, 1, 99, 99, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet46);
        BeastieTileSet beastieTileSet47 = createBeastieTileSet("Assets/Beasties/SpriteSheets/dog_left_tile.png", 1, 1, 99, 99, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet47);
        // Dog border boxes
        BeastieTileSet beastieTileSet48 = createBeastieTileSet("Assets/Beasties/SpriteSheets/dog_border_left_tile.png", 1, 1, 288, 288, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet48);
        BeastieTileSet beastieTileSet49 = createBeastieTileSet("Assets/Beasties/SpriteSheets/dog_border_right_tile.png", 1, 1, 288, 288, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet49);
        // Moving column border boxes
        BeastieTileSet beastieTileSet50 = createBeastieTileSet("Assets/Beasties/SpriteSheets/moving_column_left_border_tile.png", 1, 1, 450, 450, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet50);
        BeastieTileSet beastieTileSet51 = createBeastieTileSet("Assets/Beasties/SpriteSheets/moving_column_right_border_tile.png", 1, 1, 450, 450, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet51);
        BeastieTileSet beastieTileSet52 = createBeastieTileSet("Assets/Beasties/SpriteSheets/moving_column_up_border_tile.png", 1, 1, 450, 450, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet52);
        BeastieTileSet beastieTileSet53 = createBeastieTileSet("Assets/Beasties/SpriteSheets/moving_column_down_border_tile.png", 1, 1, 450, 450, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet53);
        // Depth border boxes
        BeastieTileSet beastieTileSet54 = createBeastieTileSet("Assets/Beasties/SpriteSheets/black_arrow_down_tile.png", 1, 1, 450, 450, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet54);
        // Scorpions
        BeastieTileSet beastieTileSet55 = createBeastieTileSet("Assets/Beasties/SpriteSheets/scorpion_right_tile.png", 1, 1, 90, 90, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet55);
        BeastieTileSet beastieTileSet56 = createBeastieTileSet("Assets/Beasties/SpriteSheets/scorpion_left_tile.png", 1, 1, 90, 90, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet56);
        BeastieTileSet beastieTileSet57 = createBeastieTileSet("Assets/Beasties/SpriteSheets/scorpion_ceiling_right_tile.png", 1, 1, 90, 90, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet57);
        BeastieTileSet beastieTileSet58 = createBeastieTileSet("Assets/Beasties/SpriteSheets/scorpion_ceiling_left_tile.png", 1, 1, 90, 90, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet58);
        BeastieTileSet beastieTileSet59 = createBeastieTileSet("Assets/Beasties/SpriteSheets/scorpion_left_wall_up_tile.png", 1, 1, 90, 90, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet59);
        BeastieTileSet beastieTileSet60 = createBeastieTileSet("Assets/Beasties/SpriteSheets/scorpion_left_wall_down_tile.png", 1, 1, 90, 90, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet60);
        BeastieTileSet beastieTileSet61 = createBeastieTileSet("Assets/Beasties/SpriteSheets/scorpion_right_wall_up_tile.png", 1, 1, 90, 90, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet61);
        BeastieTileSet beastieTileSet62 = createBeastieTileSet("Assets/Beasties/SpriteSheets/scorpion_right_wall_down_tile.png", 1, 1, 90, 90, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet62);
        // Scorpion border box
        BeastieTileSet beastieTileSet63 = createBeastieTileSet("Assets/Beasties/SpriteSheets/scorpion_border_tile.png", 1, 1, 360, 360, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet63);
        // Flight paths
        BeastieTileSet beastieTileSet64 = createBeastieTileSet("Assets/Beasties/SpriteSheets/bezier_upside_down_tile.png", 1, 1, 800, 800, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet64);
        // Bats
        BeastieTileSet beastieTileSet65 = createBeastieTileSet("Assets/Beasties/SpriteSheets/bat_left_tile.png", 1, 1, 210, 210, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet65);
        BeastieTileSet beastieTileSet66 = createBeastieTileSet("Assets/Beasties/SpriteSheets/bat_right_tile.png", 1, 1, 210, 210, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet66);
        // Birds
        BeastieTileSet beastieTileSet67 = createBeastieTileSet("Assets/Beasties/SpriteSheets/bird_resting_left.png", 1, 1, 740, 593, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet67);
        BeastieTileSet beastieTileSet68 = createBeastieTileSet("Assets/Beasties/SpriteSheets/bird_resting_right.png", 1, 1, 740, 593, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet68);
        // Waterfalls
        BeastieTileSet beastieTileSet69 = createBeastieTileSet("Assets/Beasties/SpriteSheets/waterfall_tile.png", 1, 1, 200, 175, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet69);
        // Falling rocks
        BeastieTileSet beastieTileSet70 = createBeastieTileSet("Assets/Beasties/SpriteSheets/rock_tile.png", 1, 1, 59, 59, scaledTileWidth, scaledTileHeight, 0, 0);
        beastieTileSetArr.add(beastieTileSet70);
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

        TileSet fgTileSet15 = createTileSet("Assets/tilesheets/sand_ani_1.png", 1, 1, 148, 148, scaledTileWidth, scaledTileHeight, 0, 0);
        fgTileSetArr.add(fgTileSet15);

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

    private void createMovingColumnTileSetArrays() {
        // tileSetName is set in createTileSetArrays()
        // this.tileSetName = "Sewer";

        MovingColumnTileSet mcTileSet1 = createMovingColumnTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 54, this.scaledTileWidth, this.scaledTileHeight, 0, 20);
        mcTileSetArr.add(mcTileSet1);

        MovingColumnTileSet mcTileSet2 = createMovingColumnTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 58, this.scaledTileWidth, this.scaledTileHeight, 0, 20 + 5 * 54 + 27);
        mcTileSetArr.add(mcTileSet2);

        MovingColumnTileSet mcTileSet3 = createMovingColumnTileSet("Assets/tilesheets/sewer_bricks.png", 2, 19, 52, 52, this.scaledTileWidth, this.scaledTileHeight, 0, 20 + 16 * 54 + 18);
        mcTileSetArr.add(mcTileSet3);

        MovingColumnTileSet mcTileSet4 = createMovingColumnTileSet("Assets/tilesheets/light_wooden_beams.png", 1, 5, 91, 81, this.scaledTileWidth, this.scaledTileHeight, 5, 934);
        mcTileSetArr.add(mcTileSet4);

        MovingColumnTileSet mcTileSet5 = createMovingColumnTileSet("Assets/tilesheets/light_wooden_beams_rotated.png", 5, 1, 81, 91, scaledTileWidth, scaledTileHeight, 6, 4);
        mcTileSetArr.add(mcTileSet5);

        // sewer pipes
        MovingColumnTileSet mcTileSet6 = createMovingColumnTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 105, 120, scaledTileWidth, scaledTileHeight, 52, 870);
        mcTileSetArr.add(mcTileSet6);

        MovingColumnTileSet mcTileSet7 = createMovingColumnTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 54, 364);
        mcTileSetArr.add(mcTileSet7);

        MovingColumnTileSet mcTileSet8 = createMovingColumnTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 54, 438);
        mcTileSetArr.add(mcTileSet8);

        MovingColumnTileSet mcTileSet9 = createMovingColumnTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 54, 490);
        mcTileSetArr.add(mcTileSet9);

        MovingColumnTileSet mcTileSet10 = createMovingColumnTileSet("Assets/tilesheets/sewer_pipes_sheet_horizontal_flip.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 756, 65);
        mcTileSetArr.add(mcTileSet10);

        MovingColumnTileSet mcTileSet11 = createMovingColumnTileSet("Assets/tilesheets/sewer_pipes_sheet.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 157, 65);
        mcTileSetArr.add(mcTileSet11);

        MovingColumnTileSet mcTileSet12 = createMovingColumnTileSet("Assets/tilesheets/sewer_pipes_sheet_horizontal_flip.png", 1, 1, 110, 110, scaledTileWidth, scaledTileHeight, 586, 65);
        mcTileSetArr.add(mcTileSet12);

        MovingColumnTileSet mcTileSet13 = createMovingColumnTileSet("Assets/tilesheets/coral_ground_tilesheet.png", 1, 9, 96, 96, scaledTileWidth, scaledTileHeight, 0, 0);
        mcTileSetArr.add(mcTileSet13);

        MovingColumnTileSet mcTileSet14 = createMovingColumnTileSet("Assets/tilesheets/mvrk_coralgroundm_2x.png", 1, 1, 48, 48, scaledTileWidth, scaledTileHeight, 0, 0);
        mcTileSetArr.add(mcTileSet14);

        MovingColumnTileSet mcTileSet15 = createMovingColumnTileSet("Assets/tilesheets/sand_ani_1.png", 1, 1, 148, 148, scaledTileWidth, scaledTileHeight, 0, 0);
        mcTileSetArr.add(mcTileSet15);


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

    private void createMovingColumnSet() {
        int counter = 100;
        for (MovingColumnTileSet mcTileSet : mcTileSetArr) {
            for (MovingColumnTile tile : mcTileSet.getMovingColumnTileArr()) {
                tile.setID(counter);
                mcTileArr.add(tile);
                counter++;
            }
        }
    }

    private void createBeastieSet() {
        int counter = 0;
        for (BeastieTileSet tileSet : beastieTileSetArr) {
            for (BeastieTile tile : tileSet.getBeastieTileArr()) {
                tile.setID(counter);
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
        this.levelTileGridPanel.refreshBeastiesRowsCols(topBottomSelection, leftRightSelection, newNumRows, newNumCols, this.levelNumRows, this.levelNumCols);
        this.levelNumRows = newNumRows;
        this.levelNumCols = newNumCols;
    }

    private void loadContent() {
        System.out.println("Content loaded!");
        String filePath = getEditorLevelPath();
        String mcFilePath = getEditorMovingColumnLevelPath();
        String fileName = this.curSelectedLevel;
        if (fileName == null || fileName == "") {
            return;
        }
        // get data
        LevelData levelData = this.levelTileGridPanel.loadLevelFromFile(filePath, mcFilePath, fileName);
        if (levelData != null) {
            this.levelNumRows = levelData.numRows;
            this.levelNumCols = levelData.numCols;
            this.tileSetName = levelData.tileSetFolderName;
            // set data in textFields
            this.rowTextField.setText(Integer.toString(this.levelNumRows));
            this.colTextField.setText(Integer.toString(this.levelNumCols));
        }
        String beastieFilepath = EDITOR_BEASTIE_LEVEL_DATA_ROOT;
        this.levelTileGridPanel.loadBeastiesFromFiles(beastieFilepath, fileName);
        this.curPlacedWaterSpoutTile = null;
        this.curPlacedMovingPlatform = null;
        this.curPlacedMovingColumn = null;
        this.curPlacedBat = null;
        this.levelTileGridPanel.loadWaterSpoutsFromFile(beastieFilepath, fileName);
        this.levelTileGridPanel.loadWaterfallsFromFile(beastieFilepath, fileName);
        this.levelTileGridPanel.loadSandTilesFromFile(beastieFilepath, fileName);
        this.levelTileGridPanel.loadMovingPlatformsFromFile(beastieFilepath, fileName);
        this.levelTileGridPanel.loadMovingColumnsFromFile(beastieFilepath, fileName);
        this.levelTileGridPanel.loadBatsFromFile(beastieFilepath, fileName);
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
        File directory = new File(getEditorLevelPath());
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
                String filePath = LevelEditor.this.getEditorLevelPath();
                String mcFilePath = LevelEditor.this.getEditorMovingColumnLevelPath();
                LevelEditor.this.checkLevelNameForSaving(filePath, mcFilePath, comboBoxInput, LevelEditor.this.tileSetName);

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

    private void checkLevelNameForSaving(String filePath, String mcFilePath, String fileName, String tileSetFolderName) {
        System.out.println("checkLevelNameForSaving()");
        File file = new File(filePath + fileName);
        if (file.exists()) {
            createOverridePopup(filePath, mcFilePath, fileName, tileSetFolderName);
        } else {
            saveContent(filePath, mcFilePath, fileName, tileSetFolderName);
        }
    }

    private void createOverridePopup(String filePath, String mcFilePath, String fileName, String tileSetFolderName) {
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

                LevelEditor.this.saveContent(filePath, mcFilePath, fileName, tileSetFolderName);

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

    private void saveContent(String filePath, String mcFilePath, String fileName, String tileSetFolderName) {
        System.out.println("=== saveContent() called ===");
        System.out.println("filePath: " + filePath);
        System.out.println("mcFilePath: " + mcFilePath);
        System.out.println("fileName: " + fileName);
        System.out.println("tileSetFolderName: " + tileSetFolderName);
        System.out.println("============================");
        saveContentToDestination(filePath, mcFilePath, EDITOR_BEASTIE_LEVEL_DATA_ROOT, fileName, tileSetFolderName);

        String catGameAssetsRoot = getCatGameAssetsRoot();
        if (!catGameAssetsRoot.isEmpty()) {
            saveContentToDestination(getCatGameLevelPath(), getCatGameMovingColumnLevelPath(), getCatGameBeastiePath(), fileName, tileSetFolderName);
        }

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

    private void saveContentToDestination(String filePath, String mcFilePath, String beastieFilePath, String fileName, String tileSetFolderName) {
        this.levelTileGridPanel.saveGridAsLevel(filePath, fileName, tileSetFolderName);
        this.levelTileGridPanel.saveMovingColumnTilesToLevel(mcFilePath, fileName, tileSetFolderName);
        this.levelTileGridPanel.saveBeastiesToLevel(fileName, beastieFilePath);
        this.levelTileGridPanel.saveWaterSpoutsToLevel(fileName, beastieFilePath);
        this.levelTileGridPanel.saveWaterfallsToLevel(fileName, beastieFilePath);
        this.levelTileGridPanel.saveSandTilesToLevel(fileName, beastieFilePath);
        this.levelTileGridPanel.saveMovingPlatformsToLevel(fileName, beastieFilePath);
        this.levelTileGridPanel.saveMovingColumnsToLevel(fileName, beastieFilePath);
        this.levelTileGridPanel.saveBatsToLevel(fileName, beastieFilePath);
    }

    private JPanel createLabeledPanel(String title, JScrollPane scrollPane, int x, int y, int w, int h) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
    
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
    
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        panel.setBounds(x, y, w, h);
    
        return panel;
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

        JPanel fgPanel = createLabeledPanel("Foreground Tiles", fgTileScrollPane, 100, 50, 350, 150);
        canvasPanel.add(fgPanel);

        TileGridPanel bgTilePanel = new TileGridPanel(this, this.bgTileArr, this.bgNumCols, this.scaledTileWidth, this.scaledTileHeight);
        JScrollPane bgTileScrollPane = new JScrollPane(bgTilePanel);
        bgTileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bgTileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JPanel bgPanel = createLabeledPanel("Background Tiles", bgTileScrollPane, 450, 50, 350, 150);
        canvasPanel.add(bgPanel);

        MovingColumnTileGridPanel mcTilePanel = new MovingColumnTileGridPanel(this, this.mcTileArr, this.mcNumCols, this.scaledTileWidth, this.scaledTileHeight);
        JScrollPane mcTileScrollPane = new JScrollPane(mcTilePanel);
        mcTileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mcTileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JPanel mcPanel = createLabeledPanel("Moving Columns", mcTileScrollPane, 800, 50, 350, 150);
        canvasPanel.add(mcPanel);

        this.levelTileGridPanel = new LevelTileGridPanel(this, this.levelNumRows, this.levelNumCols, this.scaledTileWidth, this.scaledTileHeight);
        JScrollPane gameLevelScrollPane = new JScrollPane(levelTileGridPanel);
        gameLevelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gameLevelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        gameLevelScrollPane.setBounds(100, 200, 1400, 500);

        // This must be initialized last because levelTileGridPanel must be passed into beastieGridPanel
        this.beastieGridPanel = new BeastieGridPanel(this, this.levelTileGridPanel, this.beastieTileArr, this.beastieNumCols, this.scaledTileWidth, this.scaledTileHeight);
        JScrollPane beastieScrollPane = new JScrollPane(beastieGridPanel);
        beastieScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        beastieScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel beastiePanel = createLabeledPanel("Game Objects / Beasties", beastieScrollPane, 1150, 50, 350, 150);
        canvasPanel.add(beastiePanel);

        canvasPanel.add(gameLevelScrollPane);

        this.levelTileGridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int tileId = getCurBeastieTileId();
                    Image tileImage = LevelEditor.this.getBeastieImageFromTileID(tileId);
        
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
        // Add listener for when a user selects something
        levelSelector.addActionListener(e -> {
            String selectedLevel = (String) levelSelector.getSelectedItem();
            if (selectedLevel == null || selectedLevel.equals("Select Level to Load")) {
                System.out.println("No level selected yet.");
                return;
            }

            System.out.println("Loading level: " + selectedLevel);
            curSelectedLevel = selectedLevel;  // ← Call your level-loading function here
        });
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

    private MovingColumnTileSet createMovingColumnTileSet(String filePath, int tileSetRows, int tileSetCols, int width, int height, int scaledWidth, 
    int scaledHeight, int offSetX, int offSetY) {
        MovingColumnTileSet tileSet = new MovingColumnTileSet();
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
                    MovingColumnTile tile = new MovingColumnTile(scaledImage);
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
        this.curMovingColumnTileID = -1;
    }

    public void setCurMovingColumnTile(int tileID) {
        this.curMovingColumnTileID = tileID;
        this.curTileID = -1;
    }

    public int getCurTile() {
        return this.curTileID;
    }

    public int getCurMovingColumnTile() {
        return this.curMovingColumnTileID;
    }

    public BufferedImage getBeastieImageFromTileID(int tileID) {
        // System.out.println("getBeastieImageFromTileID, tileID = " + tileID);
        if (tileID >= 0) {
            return this.beastieTileArr.get(tileID).getImage();
        }
        return null;
    }

    public BufferedImage getImageFromMovingColumnTileID(int mcTileID) {
        if (mcTileID >= 0) {
            int idx = mcTileID - 100;
            return this.mcTileArr.get(idx).getImage();
        }
        return null;
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

    public void startDragging(Image img, Point start, int tileId) {
        this.curTileID = -1;
        this.curMovingColumnTileID = -1;
        this.levelTileGridPanel.cancelFastEntryMode();
        this.levelTileGridPanel.cancelFastEraseMode();
        curBeastieImage = img;
        curBeastieTileId = tileId;
        int actualId = tileId;
        if (actualId == WATER_SPOUT_RIGHT || actualId == WATER_SPOUT_LEFT || actualId == WATER_SPOUT_UP || actualId == WATER_SPOUT_DOWN) {
            // set curPlacedWaterSpoutTile in stopDragging()
            this.curWaterSpout = tileId; // is this right?
        } else if (actualId == MOVING_PLATFORM_TILE) {
            this.curMovingPlatform = tileId;
        } else if (actualId == MOVING_PLATFORM_BORDER_BOX_TILE) {
            this.curMovingPlatformBorderBox = tileId;
        } else if (actualId == MOVING_COLUMN_LEFT_BORDER || actualId == MOVING_COLUMN_RIGHT_BORDER || actualId == MOVING_COLUMN_UP_BORDER || actualId == MOVING_COLUMN_DOWN_BORDER) {
            this.curMovingColumnBorderBox = tileId;
        } else if (batBeasties.contains(actualId)) {
            this.curBat = tileId;
        }
        dragOverlay.setDraggedImage(img);
        dragOverlay.setMousePoint(start);
        dragOverlay.repaint();
    }
    
    public void updateDragLocation(Component source, Point p) {
        dragOverlay.setMousePoint(SwingUtilities.convertPoint(source, p, dragOverlay));
        dragOverlay.repaint();
    }

    public void cancelDragging() {
        curBeastieTileId = -1;
        curBeastieImage = null;
        dragOverlay.setDraggedImage(null);
        dragOverlay.repaint();
    }
    
    
    public void stopDragging(Point releasePoint) {
        // Only place if there’s a valid beastie image being dragged
        if (curBeastieImage != null && curBeastieTileId != -1) {
            int gridX = -1;
            int gridY = -1;
            int beastieConstantId = curBeastieTileId;
            if (snapIntoPlaceBeasties.contains(beastieConstantId)) {
                // Convert releasePoint to grid coordinates
                gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
            } else if (enlargeToFourByFourBeasties.contains(curBeastieTileId)) {
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
            } else if (enlargeByOnePointTwentyFiveBeasties.contains(beastieConstantId)) {
                int imageWidth = curBeastieImage.getWidth(null);
                int imageHeight = curBeastieImage.getHeight(null);
                gridX = releasePoint.x - (imageWidth/2);
                gridY = releasePoint.y - (imageHeight/2);
                // Compute scaled dimensions
                int scaledWidth = (int)(this.scaledTileWidth * 1.25);
                int scaledHeight = (int)(this.scaledTileHeight * 1.25);
                // Create a new BufferedImage to hold the scaled image
                BufferedImage scaledBuffered = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = scaledBuffered.createGraphics();

                // For pixel art, use NEAREST_NEIGHBOR; for smoother scaling, use BILINEAR
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2.drawImage(curBeastieImage, 0, 0, scaledWidth, scaledHeight, null);
                g2.dispose();

                // Replace the current image with the scaled one
                curBeastieImage = scaledBuffered;
            } else if (enlargeToOneByFourVerticallyBeasties.contains(beastieConstantId)) {
                gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                // Compute scaled dimensions
                int scaledWidth = this.scaledTileWidth;
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
            } else if (enlargeToTwoByFiveVerticallyBeasties.contains(beastieConstantId)) {
                gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                int scaledWidth = this.scaledTileWidth * 2;
                int scaledHeight = this.scaledTileHeight * 5;

                BufferedImage scaledBuffered = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = scaledBuffered.createGraphics();

                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2.drawImage(curBeastieImage, 0, 0, scaledWidth, scaledHeight, null);
                g2.dispose();

                curBeastieImage = scaledBuffered;
            } else if (enlargeToFourByOneHorizontallyBeasties.contains(beastieConstantId)) {
                gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                // Compute scaled dimensions
                int scaledWidth = this.scaledTileWidth * 4;
                int scaledHeight = this.scaledTileHeight;

                // Create a new BufferedImage to hold the scaled image
                BufferedImage scaledBuffered = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = scaledBuffered.createGraphics();

                // For pixel art, use NEAREST_NEIGHBOR; for smoother scaling, use BILINEAR
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2.drawImage(curBeastieImage, 0, 0, scaledWidth, scaledHeight, null);
                g2.dispose();

                // Replace the current image with the scaled one
                curBeastieImage = scaledBuffered;
            } else if (waterSpoutBeasties.contains(beastieConstantId)) {
                if (this.curWaterSpout != -1) {
                    // Convert releasePoint to grid coordinates
                    gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                    gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                    PlacedWaterSpoutTile pwsTile = new PlacedWaterSpoutTile(gridX, gridY, curBeastieTileId, curBeastieImage);
                    this.curPlacedWaterSpoutTile = pwsTile;
                    levelTileGridPanel.placeWaterSpoutTile(pwsTile);
                }
                cancelDragging();
                return;
            } else if (waterCurrentBeasties.contains(beastieConstantId)) {
                if (this.curWaterSpout != -1) {
                    // Convert releasePoint to grid coordinates
                    gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                    gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                    PlacedWaterCurrentTile pwcTile = new PlacedWaterCurrentTile(gridX, gridY, curBeastieTileId, curBeastieImage);
                    this.curPlacedWaterSpoutTile.addPlacedWaterCurrentTile(pwcTile);
                }
                cancelDragging();
                return;
            } else if (movingPlatformBeasties.contains(beastieConstantId)) {
                if (this.curMovingPlatform != -1) {
                    // Convert releasePoint to grid coordinates
                    gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                    gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                    if (this.curPlacedMovingPlatform == null) {
                        PlacedMovingPlatformTile pmpTile = new PlacedMovingPlatformTile(gridX, gridY, curBeastieTileId, curBeastieImage);
                        PlacedMovingPlatform pmp = new PlacedMovingPlatform();
                        pmp.addPlacedMovingPlatformTile(pmpTile);
                        this.curPlacedMovingPlatform = pmp;
                        levelTileGridPanel.placeMovingPlatform(pmp);
                    } else {
                        PlacedMovingPlatformTile pmpTile = new PlacedMovingPlatformTile(gridX, gridY, curBeastieTileId, curBeastieImage);
                        this.curPlacedMovingPlatform.addPlacedMovingPlatformTile(pmpTile);
                    }
                }
                cancelDragging();
                return;
            } else if (movingPlatformBorderBoxBeasties.contains(beastieConstantId)) {
                if (this.curMovingPlatformBorderBox != -1) {
                    // Convert releasePoint to grid coordinates
                    gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                    gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                    if (this.curPlacedMovingPlatform == null) {
                        PlacedMovingPlatformBorderBoxTile pmpbbTile = new PlacedMovingPlatformBorderBoxTile(gridX, gridY, curBeastieTileId, curBeastieImage);
                        PlacedMovingPlatform pmp = new PlacedMovingPlatform();
                        pmp.addPlacedMovingPlatformBorderBoxTile(pmpbbTile);
                        this.curPlacedMovingPlatform = pmp;
                        levelTileGridPanel.placeMovingPlatform(pmp);
                    } else {
                        PlacedMovingPlatformBorderBoxTile pmpbbTile = new PlacedMovingPlatformBorderBoxTile(gridX, gridY, curBeastieTileId, curBeastieImage);
                        this.curPlacedMovingPlatform.addPlacedMovingPlatformBorderBoxTile(pmpbbTile);
                    }
                }
                cancelDragging();
                return;
            } else if (sandBeasties.contains(beastieConstantId)) {
                // Convert releasePoint to grid coordinates
                gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                PlacedSandTile pst = new PlacedSandTile(gridX, gridY, curBeastieTileId, curBeastieImage);
                this.levelTileGridPanel.placeSandTile(pst);
                cancelDragging();
                return;
            } else if (movingColumnBorderBoxBeasties.contains(beastieConstantId)) {
                if (this.curMovingColumnBorderBox != -1) {
                    // Convert releasePoint to grid coordinates
                    gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                    gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                    if (this.curPlacedMovingColumn == null) {
                        PlacedMovingColumnBorderBoxTile pmcbbt = new PlacedMovingColumnBorderBoxTile(gridX, gridY, curBeastieTileId, curBeastieImage);
                        PlacedMovingColumn pmc = new PlacedMovingColumn();
                        pmc.addPlacedMovingColumnBorderBoxTile(pmcbbt);
                        this.curPlacedMovingColumn = pmc;
                        levelTileGridPanel.placeMovingColumn(pmc);
                    } else {
                        PlacedMovingColumnBorderBoxTile pmcbbt = new PlacedMovingColumnBorderBoxTile(gridX, gridY, curBeastieTileId, curBeastieImage);
                        this.curPlacedMovingColumn.addPlacedMovingColumnBorderBoxTile(pmcbbt);
                    }
                }
                cancelDragging();
                return;
            } else if (batBeasties.contains(beastieConstantId)) {
                if (this.curBat != -1) {
                    gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                    gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                    PlacedBat placedBat = new PlacedBat(gridX, gridY, curBeastieTileId, curBeastieImage);
                    this.curPlacedBat = placedBat;
                    this.levelTileGridPanel.placeBat(placedBat);
                }
                cancelDragging();
                return;
            } else if (flightPathBeasties.contains(beastieConstantId)) {
                gridX = (releasePoint.x / levelTileGridPanel.getTileWidth()) * levelTileGridPanel.getTileWidth();
                gridY = (releasePoint.y / levelTileGridPanel.getTileHeight()) * levelTileGridPanel.getTileHeight();
                if (this.curPlacedBat != null) {
                    this.levelTileGridPanel.attachFlightPathToBat(this.curPlacedBat, gridX, gridY, curBeastieTileId, curBeastieImage);
                } else {
                    this.levelTileGridPanel.placeFlightPath(gridX, gridY, curBeastieTileId, curBeastieImage);
                }
                cancelDragging();
                return;
            } else {
                int imageWidth = curBeastieImage.getWidth(null);
                int imageHeight = curBeastieImage.getHeight(null);
                gridX = releasePoint.x - (imageWidth/2);
                gridY = releasePoint.y - (imageHeight/2);
            }

            // Place the image on the levelTileGridPanel
            levelTileGridPanel.placeBeastieTile(gridX, gridY, curBeastieTileId, curBeastieImage);
        }
        cancelDragging();
    }

    public boolean hasClickedOnAWaterSpoutElement(int x, int y) {
        // System.out.println("Panel identity: " + levelTileGridPanel);
        for (int i = 0; i < levelTileGridPanel.placedWaterSpoutTileArr.size(); ++i) {
            PlacedWaterSpoutTile pwsTile = levelTileGridPanel.placedWaterSpoutTileArr.get(i);
            if (pwsTile.isClicked(x, y) || pwsTile.hasClickedPlacedWaterCurrentTile(x, y)) {
                levelTileGridPanel.placedWaterSpoutTileArr.remove(i);
                return true;
            }
        }
        // TODO: I don't think this code can ever be called!!!!!!!
        // now check the current placedWaterSpout
        if (this.curPlacedWaterSpoutTile != null) {
            if (this.curPlacedWaterSpoutTile.isClicked(x, y) || this.curPlacedWaterSpoutTile.hasClickedPlacedWaterCurrentTile(x, y)) {
                this.curWaterSpout = -1;
                this.curPlacedWaterSpoutTile = null;
                return true;
            }
        }
        
        return false;
    }

    public boolean hasClickedOnAMovingPlatformElement(int x, int y) {
        for (int i = 0; i < levelTileGridPanel.placedMovingPlatformArr.size(); ++i) {
            PlacedMovingPlatform pmp = levelTileGridPanel.placedMovingPlatformArr.get(i);
            if (pmp.hasClickedPlacedMovingPlatformTile(x, y) || pmp.hasClickedPlacedMovingPlatformBorderBoxTile(x, y)) {
                levelTileGridPanel.placedMovingPlatformArr.remove(i);
                return true;
            }
        }
        
        return false;
    }

    public boolean hasClickedOnAMovingColumnElement(int x, int y) {
        for (int i = 0; i < levelTileGridPanel.placedMovingColumnArr.size(); ++i) {
            PlacedMovingColumn pmc = levelTileGridPanel.placedMovingColumnArr.get(i);
            if (pmc.hasClickedPlacedMovingColumnBorderBoxTile(x, y)) {
                levelTileGridPanel.placedMovingColumnArr.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean hasEndedWaterSpoutSetup(int x, int y) {
        if (this.curWaterSpout != -1) {
            this.curWaterSpout = -1;
            this.curPlacedWaterSpoutTile = null;

            return true;
        }
        return false;
    }

    public boolean hasEndedMovingPlatformSetup(int x, int y) {
        if (this.curMovingPlatform != -1 && this.curMovingPlatformBorderBox != -1) {
            this.curMovingPlatform = -1;
            this.curMovingPlatformBorderBox = -1;
            this.curPlacedMovingPlatform = null;

            return true;
        }
        return false;
    }

    public boolean hasEndedMovingColumnSetup(int x, int y) {
        if (this.curMovingColumnBorderBox != -1) {
            this.curMovingColumnBorderBox = -1;
            this.curPlacedMovingColumn = null;

            return true;
        }
        return false;
    }

    public boolean hasEndedBatSetup(int x, int y) {
        if (this.curBat != -1) {
            this.curBat = -1;
            this.curPlacedBat = null;

            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            

            LevelEditor editor = new LevelEditor();
            editor.createEditor();
        });
    }

}
