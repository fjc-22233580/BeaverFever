import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The StatusBar class represents the status bar in the game.
 * It is displayed in the top left corner of the screen.
 */
public class StatusBar extends Actor{

    public StatusBar() {
        setDefaultStatusBar();
    }

    public void setDefaultStatusBar() {
        GreenfootImage toolBarNoKey = new GreenfootImage("Toolbar.png");
        setImage(toolBarNoKey);
    }

    public void setKeyCollected() {
        GreenfootImage toolBarKey = new GreenfootImage("Toolbar_Key.png");
        setImage(toolBarKey);
    }
}
