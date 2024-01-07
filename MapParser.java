import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import greenfoot.Actor;
import greenfoot.World;

/**
 * The MapParser class is responsible for parsing map data and populating the worlds with tiles.
 * It reads tile IDs, map files, and tile images to create the maps.
 * The class also provides methods to retrieve the type of actor associated with a tile ID.
 */
public class MapParser {

    // Rows and Cols are hardcoded because we know the map is 3x3
    private final int ROWS = 3;
    private final int COLS = 3;
    
    /**
     * A dictionary of all tile images. Key is the tile ID, value is the path to the image.
     */
    private HashMap<Integer, String> tileImagesList = new HashMap<>();

    /**
     * List of map tiles.
     */
    private List<String> mapTilesList = new ArrayList<>();

    /**
     * Indicates whether the import of map data is complete.
     */
    private boolean isImportComplete;

    /**
     * Represents a two-dimensional array of World objects.
     */
    private World[][] worlds;

    // #region Lists of different types of tile IDs

    private List<Integer> woodTileIDs = new ArrayList<>();
    private List<Integer> berryTileIDs = new ArrayList<>();
    private List<Integer> waterTileIDs = new ArrayList<>();
    private List<Integer> walkWayTiles = new ArrayList<>();
    private List<Integer> fenceTiles = new ArrayList<>();

    // #endregion

    public MapParser(World[][] worlds) {
        this.worlds = worlds;
    }

    /**
     * Prepares all the maps by parsing the necessary key information required to create the maps.
     * This includes retrieving tile IDs, map files, and tile images.
     * If the import process is not complete, it attempts to retrieve the information and sets the import status to complete.
     * If the import process is complete, it populates all the worlds with the parsed information.
     */
    public void prepareAllMaps() {

        // Try to parse all the key information we need to create the maps:
        // 1. Tile IDs
        // 2. Map files
        // 3. Tile images
        if(isImportComplete == false) {
            try {
    
                getTileIDs("actortypes");
                getMapFiles("griddata");
                getTileImagePaths("images\\tiles");
                isImportComplete = true;
    
            } catch (Exception e) {            
                System.err.println("Error!");
                System.err.println(e.getMessage());
                throw new UnknownError();
            }
        }

        if (isImportComplete) {
            populateAllWorlds();
        }
    }
    
    /**
     * Populates all the worlds in the game.
     * This method iterates through each cell in the worlds array and populates it with map data.
     * It retrieves the current map data from the mapTilesList and parses it into a 2D array of TileInfo objects.
     * Then, it calls the populateMap method to populate the current world with specific actors (tiles).
     * Will iterate 9 times. (3x3)
     */
    private void populateAllWorlds() {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                int index_1d = i * 3 + j;

                World world = worlds[i][j];

                String currentMap = mapTilesList.get(index_1d);
                TileInfo[][] currentMapTiles = parseMapData(currentMap);

                populateMap(world, currentMapTiles);                
            }
        }
    }
    
    /**
     * Populates the map with tiles based on the given currentMapTiles array.
     * 
     * @param currentWorld the world in which the tiles will be added
     * @param currentMapTiles the 2D array of TileInfo objects representing the map tiles
     */
    private void populateMap(World currentWorld, TileInfo[][] currentMapTiles ){

        final int TILE_COLS = 15;
        final int TILE_ROWS = 15;

        // Remove any previous tiles
        List<Actor> tiles = currentWorld.getObjects(null);
        if (tiles.size() > 0) {
            currentWorld.removeObjects(tiles);
        }

        // Create a tile factory to create the tiles
        TileFactory tileFactory = new TileFactory();

        for (int x = 0; x < TILE_COLS; x++) {
            for (int y = 0; y < TILE_ROWS; y++) {

                // Check if we have a tile, null means no tile at this location,
                // so we can skip it. (This means that -1 in the CSV)
                TileInfo currenTile = currentMapTiles[x][y];                
                if (currenTile != null) {  

                    // Get the actor type and create the tile
                    ActorType type = currenTile.getActorType();
                    BaseTile tile = tileFactory.createTile(currenTile.getTilePath(), type);

                    // Add the appropriate tile to the world
                    currentWorld.addObject(tile, currenTile.getX(), currenTile.getY());                   
                }
            }
        }
    }

    /**
     * Parses the map data from the given file path and returns a 2D array of TileInfo objects representing the map grid.
     *
     * @param mapPath the file path of the map data file
     * @return a 2D array of TileInfo objects representing the map grid
     */
    private TileInfo[][] parseMapData(String mapPath) {

        // Fixed values for the map grid
        final int TILE_COLS = 15;
        final int TILE_ROWS = 15;

        TileInfo[][] grid = new TileInfo[TILE_COLS][TILE_ROWS];

        // Offset values for the tiles, based on the tile size
        int xOffset = 16;
        int yOffset = 16;

        // Starting coordinates for the tiles, offset based on half the tile size
        int x = 8;
        int y = 8;

        try (BufferedReader br = new BufferedReader(new FileReader(mapPath))) {

            String line;
            int count = 0;

            // Rows
            while ((line = br.readLine()) != null) {

                // Example: -1,0,5,7,-1,-1
                String[] rowValues = line.split(",");

                // Columns
                for (int i = 0; i < rowValues.length; i++) {

                    int currentTileID = Integer.parseInt(rowValues[i]);

                    // Check we have a tile, be default -1 means no tile at this location
                    if (currentTileID > -1) {

                        String tilePath = tileImagesList.get(currentTileID);

                        // Get the actor type based on the tile ID
                        ActorType actorType = getActorType(currentTileID);

                        // Create a new TileInfo object and add it to the grid
                        TileInfo currentTile = new TileInfo(currentTileID, x, y, tilePath, actorType);
                        grid[count][i] = currentTile;
                    }

                    // Handle invalid IDs - throw at this stage to alert user. 
                    if (currentTileID < -1 || currentTileID > tileImagesList.size()) {
                       
                        System.out.println("Illegal index range: " + currentTileID);
                        throw new IllegalArgumentException();
                    }

                    // Offset the x coordinate
                    x += xOffset;
                }

                // Reset for new row
                x = 8;
                y += yOffset;
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return grid;
    }

    /**
     * Returns the type of actor based on the given tile ID.
     * Checks from any of te list of tile IDs.
     * @param tileID the ID of the tile
     * @return the type of actor associated with the tile ID
     */
    private ActorType getActorType(int tileID){

        if (berryTileIDs.contains(tileID)) {
            return ActorType.BERRY;
        } else if (woodTileIDs.contains(tileID)) {
            return ActorType.WOOD;
        } else if (waterTileIDs.contains(tileID)) {
            return ActorType.WATER;
        } else if (walkWayTiles.contains(tileID)) {
            return ActorType.WALKWAY;
        } else if (fenceTiles.contains(tileID)) {
            return ActorType.FENCE;
        } 

        return ActorType.OTHER;
    }

    /**
     * Gets tile IDs from the relevent CSV in the actor types folder.
     * The tile IDs are stored in separate lists based on the column header in the CSV file.
     * If no tile IDs are found, an IllegalArgumentException is thrown.
     *
     * @param actorTypesFolderPath the path to the actor types folder
     * @throws FileNotFoundException if the specified folder or file is not found
     * @throws IOException if an I/O error occurs while reading the files
     */
    private void getTileIDs(String actorTypesFolderPath) throws FileNotFoundException, IOException {
        
        // Get all files from this folder.
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(actorTypesFolderPath))) {

            // Iterate through files
            for (Path path : stream) {
                
                // Check if we have files but ignoring directoes
                if (Files.isDirectory(path) == false) {

                    // Check file is a .csv
                    String fileName = path.toString();
                    if (HelperMethods.getFileExtension(fileName).equals(".csv")) {

                        // Open and read the file contents
                        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

                            List<Integer> values = new ArrayList<Integer>();
                            
                            // Get column header
                            String headerName = br.readLine();

                            // Go through all rows
                            String rowString;
                            while ((rowString = br.readLine()) != null) {

                                String rowValue = rowString.replace(",", "");

                                // Check we can parse the values into ints
                                Integer currentValue = HelperMethods.tryParseInt(rowValue);
                                if (currentValue != null) {
                                    values.add(currentValue);
                                }
                            }

                            if (headerName.contains("wood")) {
                                
                                woodTileIDs = new ArrayList<>(values);

                            } else if (headerName.contains("berry")) {
                                
                                berryTileIDs = new ArrayList<>(values);

                            } else if (headerName.contains("water")) {

                                waterTileIDs = new ArrayList<>(values);

                            } else if (headerName.contains("walkway")) {

                                walkWayTiles = new ArrayList<>(values);
                                
                            }else if (headerName.contains("fences")) {

                                fenceTiles = new ArrayList<>(values); 
                            } else {

                                System.err.println("Invalid Column Header!" + headerName);
                            }
                        }
                    }
                }
            }
        }

        if (woodTileIDs.size() > 0 || berryTileIDs.size() > 0 || waterTileIDs.size() > 0 || walkWayTiles.size() > 0) {

            System.out.println("Imported IDs wood: " + woodTileIDs.size());
            System.out.println("Imported IDs berry: " + berryTileIDs.size());
            System.out.println("Imported IDs water: " + waterTileIDs.size());
            System.out.println("Imported IDs walkway: " + walkWayTiles.size());
        }else{
            System.err.println("No tile IDs found!");
            throw new IllegalArgumentException();  
        }
    }

    /**
     * Retrieves the list of map csv files from the "griddata" folder.
     * If the number of map files is not equal to 9, an exception is thrown.
     * If no map files are found, an exception is thrown.
     *
     * @param mapFilesPath the path to the directory containing the map files
     * @throws IOException if an I/O error occurs while accessing the directory
     * @throws FileNotFoundException if the specified folder or file is not found
     * @throws IllegalArgumentException if the number of map files is incorrect or no map files are found
     */
    private void getMapFiles(String mapFilesPath) throws FileNotFoundException, IOException {
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(mapFilesPath))) {

            for (Path path : stream) {

                // Check we have a file
                if (Files.isDirectory(path) == false) {

                    // Check file is a .csv
                    String fileName = path.toString();
                    if (HelperMethods.getFileExtension(fileName).equals(".csv")) {
                        mapTilesList.add(fileName);
                    }
                }
            }
        }

        // Check we have the correct number of maps
        if (mapTilesList.size() > 0) {
            
            if (mapTilesList.size() != 9) {
                System.err.println("Incorrect number of maps!");
                throw new IllegalArgumentException();                
            }
            System.out.println("Imported CSVs: " + mapTilesList.size());
        } else {
            System.err.println("No CSVs found!");
            throw new IllegalArgumentException();
        }
    }

    /** Gets all tile images (.png) from images\tiles and puts them into a Dictionary for quick retrieval.
     * Also removes "images\\" from the path as GreenfootImage will add this automatically(!);
     * @param imageTilesPath images\tiles
     * @throws IOException throws for generic IO Exception
     * @throws FileNotFoundException if the specified folder or file is not found
     */
    private void getTileImagePaths(String imageTilesPath) throws FileNotFoundException, IOException {        

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(imageTilesPath))) {

            for (Path path : stream) {

                // Check we have a file
                if (Files.isDirectory(path) == false) {

                    // Check file is a .png
                    String filePath = path.getFileName().toString();
                    if (HelperMethods.getFileExtension(filePath).equalsIgnoreCase(".png")) {

                        File file = new File(filePath);
                        String fileName = HelperMethods.getFileName(file);
                        int fileNumber = Integer.parseInt(fileName);

                        // Remove "images\\" because for reasons unkown greenfoot hardcoded that folder when creating GreenfootImage
                        String tilePathString = path.toString().replace("images\\", "");

                        tileImagesList.put(fileNumber, tilePathString);
                    } 
                }
            }
        }

        if (tileImagesList.size() > 0) {
            System.out.println("Imported tiles: " + tileImagesList.size());
        }else{
            System.err.println("No tile images found!");
            throw new IllegalArgumentException();            
        }
    }

}
