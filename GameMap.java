import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GameMap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameMap extends World
{

    /**
     * Constructor for objects of class GameMap.
     * 
     */
    public GameMap(int mapWidth, int mapHeight, boolean isBounded, GreenfootImage backGround)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(mapWidth, mapHeight, 1, isBounded); 

        setBackground(backGround);
    }
}
