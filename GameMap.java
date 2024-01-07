import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Write a description of class GameMap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameMap extends World
{
    private int worldId;

    public int getWorldId() {
        return worldId;
    }

    private List<Point> pathPoints = new ArrayList<Point>();

    private boolean devMode;

    private EnemyPathsManager enemyPathsManager = WorldManager.getInstance().getEnemyPathsManager();    
    
    // Greenfootsound
    private GreenfootSound backgroundMusic = new GreenfootSound("HoliznaCC0 - 2nd Dimension.mp3");

    /**
     * Constructor for objects of class GameMap.
     * 
     */
    public GameMap(int mapWidth, int mapHeight, boolean isBounded, boolean devMod, int worldId)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(mapWidth, mapHeight, 1, isBounded);
        this.devMode = devMod;
        this.worldId = worldId;
        
        GreenfootImage backGround = new GreenfootImage("grass.png");
        setBackground(backGround); 
        setPaintOrder(Label.class, StatusBar.class,  Beaver.class, Enemy.class);

        // Play the background music
        backgroundMusic.playLoop(); 
    }

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
}
