import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MapParser {

    public MapParser(){

        try {
            getMapFiles("griddata");
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
    
}
