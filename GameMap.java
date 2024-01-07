import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
/**
 * The GameMap class represents the game map in the Beaver Fever game.
 * Excluding dev mode it only sets the background and acting order of the world.
 */
public class GameMap extends World
{
    
    /**
     * Constructor for objects of class GameMap.   
     */
    public GameMap(int mapWidth, int mapHeight, boolean isBounded, boolean devMod, int worldId)
    {            
        super(mapWidth, mapHeight, 1, isBounded);
        this.devMode = devMod;
        this.worldId = worldId;        
        
        // Set the background image, and paint order so key classes are above everything else. 
        setBackground(new GreenfootImage("grass.png")); 
        setPaintOrder(Label.class, StatusBar.class,  Beaver.class, Enemy.class);
    }

    // #region DEV ONLY - Used for creating enemy patrol paths

    /**
     * The ID of the world associated with the game map.
     * This is used to save the enemy paths for the world.
     */
    private int worldId;

    // Create a list of points to store the path points
    private List<Point> pathPoints = new ArrayList<Point>();

    // Create a flag to indicate whether we are in dev mode
    private boolean devMode;

    // Create a reference to the enemy paths manager
    private EnemyPathsManager enemyPathsManager = WorldManager.getInstance().getEnemyPathsManager();  

    @Override
    public void act() {
        super.act();
        
        if(devMode) {
            
            if (Greenfoot.mouseClicked(null)) {
                System.out.println("Mouse clicked");
    
                MouseInfo mouse = Greenfoot.getMouseInfo();
                Point mousePoint = new Point(mouse.getX(), mouse.getY());
                pathPoints.add(mousePoint);    
    
                addObject(new Locator(),mousePoint.x,mousePoint.y);   
                enemyPathsManager.savePaths(worldId, pathPoints);             
            }
        }
    }
    // #endregion
}
