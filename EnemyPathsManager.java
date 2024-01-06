import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EnemyPathsManager {

    private HashMap<Integer, List<Point>> allEnemyPaths = new HashMap<Integer, List<Point>>();    

    public EnemyPathsManager() {

        try {
            loadPaths("enemypaths");
            System.out.println("Enemy paths loaded succesfully!");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public List<Point> getEnemyPath(int worldId) {
        return allEnemyPaths.get(worldId);
    }

    public List<Integer> getAllWorldIds() {
        
        Set<Integer> keysSet = (Set<Integer>)allEnemyPaths.keySet();
        List<Integer> keysList = new ArrayList<>(keysSet);
        return keysList;
    }   

    public void savePaths(int worldId, List<Point> pathPoints) {

        allEnemyPaths.put(worldId, pathPoints);

        String filePath = "enemypaths/" + worldId + ".csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Point point : pathPoints) {

                // Join array elements with a comma and write to the file
                String rowString = point.x + "," + point.y + ",";
                writer.write(String.join(",", rowString));
                writer.newLine();
            }

            System.out.println("CSV for world: " + worldId + " updated succesfully!");
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    private void loadPaths(String folderPath) throws IOException {

        // Get all files from this folder.
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folderPath))) {

            // Iterate through files
            for (Path path : stream) {
                
                // Check if we have files but ignoring directoes
                if (Files.isDirectory(path) == false) {

                    // Check file is a .csv
                    String fileName = path.toString();
                    if (HelperMethods.getFileExtension(fileName).equals(".csv")) {

                        // Open and read the file contents
                        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {                              
                            
                            List<Point> values = new ArrayList<Point>();

                            // Go through all rows
                            String line;
                            while ((line = br.readLine()) != null) {                               

                                Point currentPoint = new Point();

                                String[] rowValues = line.split(",");

                                Integer currentValueX = HelperMethods.tryParseInt(rowValues[0]);
                                Integer currentValueY = HelperMethods.tryParseInt(rowValues[1]);

                                if (currentValueX != null && currentValueY != null) {
                                    currentPoint = new Point(currentValueX, currentValueY);
                                    values.add(currentPoint);
                                }
                            }

                            File file = new File(fileName); 

                            // Get the world id from the file name
                            String worldId = HelperMethods.getFileName(file);

                            // Add the world id and the list of points to our hashmap
                            allEnemyPaths.put(Integer.parseInt(worldId), values);

                        } 
                    } else {
                        System.err.println("Invalid File Type!");
                        throw new IllegalArgumentException();
                    }
                }
            }
        }

    }
}
