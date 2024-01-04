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

import javax.lang.model.util.ElementScanner14;

import greenfoot.World;

public class MapParser {

    // 
    private final int ROWS = 3;
    private final int COLS = 3;
    
    /**
     * A dictionary of all tile images. Key is the tile ID, value is the path to the image.
     */
    private HashMap<Integer, String> tileImagesList = new HashMap<>();

    private List<String> mapTilesList = new ArrayList<>();

    private boolean isImportComplete;

    private World[][] worlds;

    private List<Integer> woodTileIDs = new ArrayList<>();
    private List<Integer> berryTileIDs = new ArrayList<>();
    private List<Integer> waterTileIDs = new ArrayList<>();

    public MapParser(World[][] worlds) {

        if (HelperMethods.isSquare(worlds)) {
            this.worlds = worlds;
        } else {
            System.out.println("Grid is not square!");
            throw new IllegalArgumentException();
        }
    }

    public void prepareAllMaps() {
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

        if (isImportComplete) {
            populateAllWorlds();
        }
    }

    // Iterates 9 times
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

    // Iterates 225 times
    private void populateMap(World currentWorld, TileInfo[][] currentMapTiles ){

        final int TILE_COLS = 15;
        final int TILE_ROWS = 15;

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

    private TileInfo[][] parseMapData(String mapPath) {

        final int TILE_COLS = 15;
        final int TILE_ROWS = 15;

        TileInfo[][] grid = new TileInfo[TILE_COLS][TILE_ROWS];

        int xOffset = 16;
        int yOffset = 16;

        int x = 8;
        int y = 8;

        try (BufferedReader br = new BufferedReader(new FileReader(mapPath))) {

            String line;
            int count = 0;

            // Rows
            while ((line = br.readLine()) != null) {

                // 1 row
                // Example: -1,0,5,7,-1,-1
                String[] rowValues = line.split(",");

                // Columns
                for (int i = 0; i < rowValues.length; i++) {

                    int currentTileID = Integer.parseInt(rowValues[i]);

                    if (currentTileID > -1) {

                        String tilePath = tileImagesList.get(currentTileID);

                        ActorType actorType = getActorType(currentTileID);
                        TileInfo currentTile = new TileInfo(currentTileID, x, y, tilePath, actorType);
                        grid[count][i] = currentTile;
                    }

                    if (currentTileID < -1 || currentTileID > tileImagesList.size()) {
                       
                        System.out.println("Illegal index range: " + currentTileID);
                        throw new IllegalArgumentException();
                    }

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
                                
                            } else{

                                System.err.println("Invalid Column Header!" + headerName);
                            }
                        }
                    }
                }
            }
        }

        if (woodTileIDs.size() > 0 || berryTileIDs.size() > 0 || waterTileIDs.size() > 0) {

            System.out.println("Imported IDs wood: " + woodTileIDs.size() + " | ");
            System.out.println("Imported IDs berry: " + berryTileIDs.size() + " | ");
            System.out.println("Imported IDs water: " + waterTileIDs.size() + " | ");
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

                        // TODO - Reduce complexity of this method
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
