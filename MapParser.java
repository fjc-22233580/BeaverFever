import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapParser {

    public MapParser(){

        try {

            getMapFiles("griddata");
            getFiles("images");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getMapFiles(String mapFilesPath) throws IOException{
        
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
