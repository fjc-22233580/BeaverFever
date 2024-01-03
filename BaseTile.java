import greenfoot.Actor;
import greenfoot.GreenfootImage;

public abstract class BaseTile extends Actor{    

    private ActorType type;

    public BaseTile(String tilePath, ActorType type) {

        this.type = type;
        GreenfootImage tile = new GreenfootImage(tilePath);
        setImage(tile);
    }

    public abstract void act();
}
