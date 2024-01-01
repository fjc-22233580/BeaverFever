import java.awt.Point;

import greenfoot.Color;
import greenfoot.World;

public class PlayerStats {

    private final int MAX_LIVES = 3;

    private boolean hasKey = false;
    
    private int woodCount = 0;    
    private int livesCount = 1;  
    private StatusBar statusBar = new StatusBar();
    private Label statusBarLabel = new Label("", 12); 
    
    private Point statusBarLocation = new Point(40, 10);
    
    
    public PlayerStats() {
        
        statusBarLabel.setFillColor(Color.BLACK);
        statusBarLabel.setLineColor(Label.transparent);
        
        setFormattedLabelInfo();
    }
    
    public void addStatusBarToWorld(World world) {
        world.addObject(statusBar, statusBarLocation.x, statusBarLocation.y);
        world.addObject(statusBarLabel, statusBarLocation.x, statusBarLocation.y);
    }
    
    private void setFormattedLabelInfo() {
        
        // Set both wood and health on the same line,
        // but add a space between them for the heart icon.
        String output = woodCount + "         " + livesCount;
        statusBarLabel.setValue(output);
    }
    
    public int getMAX_LIVES() {
        return MAX_LIVES;
    }
    
    public void addWood() {
        woodCount++;
        setFormattedLabelInfo();
        System.out.println("Wood count: " + woodCount); 
    }
    
    public boolean canAddLife() {        
        
        if (livesCount < MAX_LIVES) {
            livesCount++;
            setFormattedLabelInfo();
            return true;
        }
        
        return false;
    }
    
    public boolean decreaseLife() {
        livesCount--;
        
        if (livesCount <= 0) {
            livesCount = 0;
            return true;
        }
        
        return false;
    }
    
    public int getWoodCount() {
        return woodCount;
    }
    
    public int getLivesCount() {
        return livesCount;
    }
    
    public void collectKey() {
        hasKey = true;
    }
    
    public boolean isHasKey() {
        return hasKey;
    }
}
