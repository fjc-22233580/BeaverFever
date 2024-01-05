import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StatusBar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StatusBar extends Actor
{

    public StatusBar() {

        GreenfootImage toolBarNoKey = new GreenfootImage("Toolbar.png");
        setImage(toolBarNoKey);
    }

    public void setKeyCollected() {
        GreenfootImage toolBarKey = new GreenfootImage("Toolbar_Key.png");
        setImage(toolBarKey);
    }
}
