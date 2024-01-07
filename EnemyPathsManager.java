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

/**
 * The EnemyPathsManager class manages the paths for all enemies in the game.
 * It provides methods to load, save, and retrieve enemy paths based on world IDs.
 */
public class EnemyPathsManager {

    /**
     * A HashMap that stores the paths for all enemies.
     * The keys are the world IDs, and the values are lists of points representing the paths.
     */
    private HashMap<Integer, List<Point>> allEnemyPaths = new HashMap<Integer, List<Point>>();    

    public EnemyPathsManager() {

        try {
            loadPaths("enemypaths");
            System.out.println("Enemy paths loaded succesfully!");

        } catch (IOException e) {
            System.out.println("Error loading enemy paths: " + e.getMessage());
            throw new UnknownError();
        }        
    }

    /**
     * Retrieves the enemy path for the specified world ID.
     *
     * @param worldId the ID of the world
     * @return the enemy path as a list of points
     */
    public List<Point> getEnemyPath(int worldId) {
        return allEnemyPaths.get(worldId);
    }

    /**
     * Returns a list of all world IDs.
     */
    public List<Integer> getAllWorldIds() {
        
        Set<Integer> keysSet = (Set<Integer>)allEnemyPaths.keySet();
        List<Integer> keysList = new ArrayList<>(keysSet);
        return keysList;
    }   

    /**
     * Saves the enemy paths for a specific world to a CSV file.
     *
     * @param worldId    the ID of the world
     * @param pathPoints the list of path points for the enemy
     */
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
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    /**
     * Loads enemy paths from a specified folder.
     * 
     * @param folderPath the path to the folder containing the enemy path files
     * @throws IOException if an I/O error occurs while reading the files
     */
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

                                // Split the row into values
                                String[] rowValues = line.split(",");

                                // Try to onvert the values to integers
                                Integer currentValueX = HelperMethods.tryParseInt(rowValues[0]);
                                Integer currentValueY = HelperMethods.tryParseInt(rowValues[1]);

                                // If we have valid values, create a point and add it to the list
                                if (currentValueX != null && currentValueY != null) {
                                    Point currentPoint = new Point(currentValueX, currentValueY);
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
