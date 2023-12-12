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

    private final int ROWS = 15;
    private final int COLS = 15;

    private HashMap<Integer, String> tileImagesList;
    private List<String> mapCSVList;
    
    private boolean isImportComplete;

    private TileInfo[][] grid = new TileInfo[ROWS][COLS];
    private World[][] worlds;

    public MapParser(World[][] worlds){

        this.worlds = worlds;
        try {

            mapCSVList = getMapFiles("griddata");
            tileImagesList = getFiles("images");
            isImportComplete = true;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<String> getMapFiles(String mapFilesPath) throws IOException{
        
        List<String> csvPaths = new ArrayList<String>();
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(mapFilesPath))) {

            for (Path path : stream) {

                if (Files.isDirectory(path) == false ) {

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

    private HashMap<Integer, String> getFiles(String imageTilesPath) throws IOException {

        HashMap<Integer, String> imgPaths = new HashMap<Integer, String>();

        int count = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(imageTilesPath))) {

            for (Path path : stream) {

                if (Files.isDirectory(path) == false ) {

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
