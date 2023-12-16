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
import greenfoot.World;

public class MapParser {

    private final int ROWS = 3;
    private final int COLS = 3;
    
    private HashMap<Integer, String> tileImagesList;
    private List<String> mapCSVList;

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
            mapCSVList = getMapFiles("griddata");
            tileImagesList = getTileImagePaths("images\\tiles");
            isImportComplete = true;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

                String currentMap = mapCSVList.get(index_1d);
                TileInfo[][] currentMapTiles = parseMapData(currentMap);

                populateMap(world, currentMapTiles);                
            }
        }
    }

    // Iterates 225 times
    private void populateMap(World currentWorld, TileInfo[][] currentMapTiles ){

        final int TILE_COLS = 15;
        final int TILE_ROWS = 15;

        for (int x = 0; x < TILE_COLS; x++) {
            for (int y = 0; y < TILE_ROWS; y++) {

                TileInfo currenTile = currentMapTiles[x][y];
                
                if (currenTile != null) {  

                    ActorType type = currenTile.getActorType();
                    switch (type) {
                        case BERRY:
                            currentWorld.addObject(new BerryTile(currenTile.getTilePath()), currenTile.getX(), currenTile.getY());
                            break;

                        case WOOD:
                            currentWorld.addObject(new WoodTile(currenTile.getTilePath()), currenTile.getX(), currenTile.getY());
                            break;

                        case WATER:
                            currentWorld.addObject(new WaterTile(currenTile.getTilePath()), currenTile.getX(), currenTile.getY());
                            break;

                        case OTHER:
                            currentWorld.addObject(new ObjectTile(currenTile.getTilePath(), currenTile.getActorType()), currenTile.getX(), currenTile.getY());
                            break;
                    }
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
                    } else {
                        System.err.println("Invalid File Type!");
                        throw new IllegalArgumentException();
                    }
                }
            }
        }

        System.out.println("Imported IDs wood: " + woodTileIDs.size() + " | ");
        System.out.println("Imported IDs berry: " + berryTileIDs.size() + " | ");
        System.out.println("Imported IDs water: " + waterTileIDs.size() + " | ");
    }

    private List<String> getMapFiles(String mapFilesPath) throws IOException {

        List<String> csvPaths = new ArrayList<String>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(mapFilesPath))) {

            for (Path path : stream) {

                if (Files.isDirectory(path) == false) {

                    String fileName = path.toString();

                    if (HelperMethods.getFileExtension(fileName).equals(".csv")) {
                        csvPaths.add(fileName);
                    }
                }
            }
        }

        System.out.println("Imported CSVs: " + csvPaths.size());

        return csvPaths;

    }

    /** Gets all tile images (.png) from images\tiles and puts them into a Dictionary for quick retrieval.
     * @param imageTilesPath images\tiles
     * @return HashMap of all tiles
     * @throws IOException throws for generic IO Exception
     */
    private HashMap<Integer, String> getTileImagePaths(String imageTilesPath) throws IOException {

        HashMap<Integer, String> imgPaths = new HashMap<Integer, String>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(imageTilesPath))) {

            for (Path path : stream) {

                if (Files.isDirectory(path) == false) {

                    String filePath = path.getFileName().toString();

                    if (HelperMethods.getFileExtension(filePath).equalsIgnoreCase(".png")) {

                        File file = new File(filePath);

                        String fileName = HelperMethods.getFileName(file);

                        int fileNumber = Integer.parseInt(fileName);

                        // Remove "images\\" because for reasons unkown greenfoot hardcoded that folder when creating GreenfootImage
                        String tilePathString = path.toString().replace("images\\", "");

                        imgPaths.put(fileNumber, tilePathString);
                    }
                }
            }
        }

        System.out.println("Imported tiles: " + imgPaths.size());

        return imgPaths;
    }

}
