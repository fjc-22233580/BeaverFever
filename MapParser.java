import java.io.BufferedReader;
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

    public MapParser(World[][] worlds) {

        if (HelperMethods.isSquare(worlds)) {
            this.worlds = worlds;
        } else {
            System.out.println("Grid is not square!");
        }

        try {

            mapCSVList = getMapFiles("griddata");
            tileImagesList = getTileImagePaths("images");
            isImportComplete = true;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (isImportComplete) {
            populateWorlds();
        }
    }

    // Iterates 9 times
    private void populateWorlds() {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                int index_1d = i * 3 + j;

                World world = worlds[i][j];

                String currentMap = mapCSVList.get(index_1d);
                TileInfo[][] currentMapTiles = parseMapData(currentMap);

                populateWorld(world, currentMapTiles);                
            }
        }
    }

    // Iterates 225 times
    private void populateWorld(World currentWorld, TileInfo[][] currentMapTiles ){

        final int TILE_COLS = 15;
        final int TILE_ROWS = 15;

        for (int i = 0; i < TILE_COLS; i++) {
            for (int j = 0; j < TILE_ROWS; j++) {

                TileInfo currenTile = currentMapTiles[i][j];

                if (currenTile != null) {
                    
                    // TODO - create base actor tile
                    //currentWorld.addObject(new ObjectTile(currenTile.getTilePath()), currenTile.getX(), currenTile.getY());
                }
            }
        }


    }

    private TileInfo[][] parseMapData(String mapPath) {

        TileInfo[][] grid = new TileInfo[ROWS][COLS];

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

                    int currentItem = Integer.parseInt(rowValues[i]);

                    if (currentItem != -1) {

                        String tilePath = tileImagesList.get(currentItem);
                        TileInfo currentTile = new TileInfo(x, y, tilePath);
                        grid[count][i] = currentTile;

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

    private List<String> getMapFiles(String mapFilesPath) throws IOException {

        List<String> csvPaths = new ArrayList<String>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(mapFilesPath))) {

            for (Path path : stream) {

                if (Files.isDirectory(path) == false) {

                    String fileName = path.getFileName().toString();

                    if (HelperMethods.getFileExtension(fileName).equals(".csv")) {
                        csvPaths.add(fileName);
                    }
                }
            }
        }

        System.out.println("Imported CSVs: " + csvPaths.size());
        System.out.println("Imported CSV: " + csvPaths.get(0));

        return csvPaths;

    }

    private HashMap<Integer, String> getTileImagePaths(String imageTilesPath) throws IOException {

        HashMap<Integer, String> imgPaths = new HashMap<Integer, String>();

        int count = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(imageTilesPath))) {

            for (Path path : stream) {

                if (Files.isDirectory(path) == false) {

                    String fileName = path.getFileName().toString();

                    if (HelperMethods.getFileExtension(fileName).equals(".png")) {
                        imgPaths.put(count, fileName);
                    }
                }
                count++;
            }
        }

        return imgPaths;
    }

}
