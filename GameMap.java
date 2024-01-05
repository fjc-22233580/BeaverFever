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

    private List<Point> pathPoints = new ArrayList<Point>();

    private boolean devMode;

    private EnemyPathsManager enemyPathsManager = WorldManager.getInstance().getEnemyPathsManager();

    private boolean hasBridge;

    
    private Point constructionLocation;   
    
    /**
     * Constructor for objects of class GameMap.
     * 
     */
    public GameMap(int mapWidth, int mapHeight, boolean isBounded, GreenfootImage backGround, boolean devMod, int worldId, boolean hasBridge)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(mapWidth, mapHeight, 1, isBounded);
        this.devMode = devMod;
        this.worldId = worldId;
        this.hasBridge = hasBridge;
        
        setBackground(backGround); 
        setPaintOrder(Beaver.class);
    }
    
    /**
     * Constructor for objects of class GameMap.
     * 
     */
    public GameMap(int mapWidth, int mapHeight, boolean isBounded, GreenfootImage backGround, boolean devMod, int worldId, boolean hasBridge, Point constructionLocation)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(mapWidth, mapHeight, 1, isBounded);
        this.devMode = devMod;
        this.worldId = worldId;
        this.hasBridge = hasBridge;
        this.constructionLocation = constructionLocation; 
        
        setBackground(backGround);
    }
    
    // TODO - dev only method. Remove when done.
    public void setHasBridge(boolean hasBridge) {
        this.hasBridge = hasBridge;
    }
    
    public boolean hasBridge() {
        return hasBridge;
    }
    
    public Point getConstructionLocation() {
        return constructionLocation;
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
