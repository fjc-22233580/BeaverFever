import java.awt.Point;
import greenfoot.Color;
import greenfoot.World;

/**
 * The PlayerStats class represents the statistics and status of a player in the game.
 * It keeps track of the player's wood count, lives count, key status, and provides methods
 * to modify and retrieve these values. The class also manages a status bar and label to
 * display the player's stats in the game world.
 */
public class PlayerStats {

    // #region Default values

    private final int MAX_LIVES = 3;
    private final int INITIAL_HEALTH = 2;
    private final int INITIAL_WOOD = 0;

    // #endregion

    /**
     * The location of the status bar on the screen.
     */
    private final Point STATUS_BAR_LOCATION = new Point(40, 10);

    /**
     * Represents whether the player has a key or not.
     */
    private boolean hasKey = false;
    
    /**
     * The number of wood collected by the player.
     */
    private int woodCount = INITIAL_WOOD;    

    /**
     * The number of lives the player currently has.
     */
    private int livesCount = INITIAL_HEALTH;  

    /**
     * Holds a reference to the the status bar - displayed in the top left corner of the screen.
     */
    private StatusBar statusBar = new StatusBar();

    /**
     * The text label for the status bar.
     */
    private Label statusBarLabel = new Label("", 12);     
    
    
    public PlayerStats() {
        
        statusBarLabel.setFillColor(Color.BLACK);
        statusBarLabel.setLineColor(Label.transparent);
        
        setFormattedLabelInfo();
    }
    
    /**
     * Adds the status bar and status bar label to the given world at the specified location.
     * 
     * @param world The world to add the status bar and status bar label to.
     */
    public void addStatusBarToWorld(World world) {
        world.addObject(statusBar, STATUS_BAR_LOCATION.x, STATUS_BAR_LOCATION.y);
        world.addObject(statusBarLabel, STATUS_BAR_LOCATION.x, STATUS_BAR_LOCATION.y);
    }
    
    /**
     * Sets the formatted label information for the player stats.
     * The wood count and lives count are concatenated with a space between them for the heart icon.
     * The resulting string is set as the value of the status bar label.
     */
    private void setFormattedLabelInfo() {
        
        // Set both wood and health on the same line,
        // but add a space between them for the heart icon.
        String output = woodCount + "       " + livesCount;
        statusBarLabel.setValue(output);
    }
    
    /**
     * Returns the maximum number of lives.
     */
    public int getMAX_LIVES() {
        return MAX_LIVES;
    }
    
    /**
     * Increases the wood count by one and updates the formatted label information.
     */
    public void addWood() {
        woodCount++;
        setFormattedLabelInfo();
    }
    
    /**
     * Checks if a life can be added to the player's stats.
     * If the player's lives count is less than the maximum number of lives, then the lives count is increased by one.
     */
    public boolean canAddLife() {        
        
        if (livesCount < MAX_LIVES) {
            livesCount++;
            setFormattedLabelInfo();
            return true;
        }
        
        return false;
    }
    
    /**
     * Decreases the life count of the player.
     * Updates the label displaying the life count.
     * 
     * @return true if the life count reaches 0, false otherwise.
     */
    public boolean decreaseLife() {
        
        // Decrease the life count and update the label.
        livesCount--;
        setFormattedLabelInfo();

        // If the lives count is 0, return true.
        if (livesCount <= 0) {
            livesCount = 0;
            return true;
        }
        
        return false;
    }

    /**
     * Removes the specified amount of wood from the player's wood count.
     * 
     * @param count the amount of wood to remove
     */
    public void removeWood(int count){
        woodCount -= count;
        setFormattedLabelInfo();
    }
    
    /**
     * Returns the count of wood.
     *
     * @return the count of wood
     */
    public int getWoodCount() {
        return woodCount;
    }
    
    /**
     * Returns the number of lives the player currently has.
     *
     * @return the number of lives
     */
    public int getLivesCount() {
        return livesCount;
    }
    
    /**
     * Sets the hasKey flag to true and updates the status bar to indicate that the key has been collected.
     */
    public void collectKey() {
        hasKey = true;
        statusBar.setKeyCollected();
    }
    
    /**
     * Returns whether the player has a key or not.
     *
     * @return true if the player has a key, false otherwise
     */
    public boolean hasKey() {
        return hasKey;
    }

    /**
     * Resets the player's statistics to their initial values.
     * This includes resetting the wood count, lives count, key status,
     * and updating the status bar and label information.
     */
    public void resetStats() {
        woodCount = INITIAL_WOOD;
        livesCount = INITIAL_HEALTH;
        hasKey = false;
        statusBar.setDefaultStatusBar();
        setFormattedLabelInfo();
    }
}
