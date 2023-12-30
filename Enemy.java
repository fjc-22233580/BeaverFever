import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Actor
{
    
    /**
     * The velocity of the enemy.
     * Set as 2 pixels per frame.
     * A frame is the cycle of each call of the act method, which is usually set to 60Hz.
     */
    private final int VELOCITY = 2; 

    private final int DETECTION_RADIUS = 40;
    
    private EnemyState state;
    
    private List<Point> pathPoints;
    
    private Beaver player;
    
    private int locationIndex = 0;    
    
    private Point destinationPoint = new Point();

    public Enemy(List<Point> pathPoints) {
        
        if (pathPoints.size() <= 0) {
            System.err.println("Error: pathPoints must contain at least one point");
            throw new IllegalArgumentException();
        }

        this.pathPoints = pathPoints;

        GreenfootImage image = new GreenfootImage("wolverine.png");
        setImage(image);

        // Set the initial state
        state = EnemyState.PATROLLING;
    }

    /**
     * Act - do whatever the Enemy wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        updateState();
    }

    private void updateState(){
        switch (state){
            case PATROLLING:
                patrol();
                break;
            case CHASING:
                chasing();
                break;
            case ATTACKING:
                attacking();
                break;
            case RETURNING:
                returning();
                break;
        }
    }
    
    private void returning() {

        Point currentLocation = new Point(getX(), getY());

        destinationPoint = EuclideanFunctions.getNearestPoint(pathPoints, currentLocation.x, currentLocation.y);

        // Set the location index to the nearest point, so patrolling can continue from there
        locationIndex = pathPoints.indexOf(destinationPoint);
       
        state = EnemyState.PATROLLING;

        Point nextPoint = EuclideanFunctions.getNextPoint(currentLocation, destinationPoint, VELOCITY);
        setLocation(nextPoint.x, nextPoint.y);

    }

    // TODO - To be removed
    private int delayCounter = 0;
    private void attacking() {

        // TODO - Add logic to attack the beaver
        if (delayCounter > 60) {
            state = EnemyState.RETURNING;
            delayCounter = 0;
        }

        delayCounter++;
    }
    
    private void chasing() {


        double chasingDistance = EuclideanFunctions.getHypotenuse(getX(), getY(), player.getX(), player.getY());
        if (chasingDistance < 30) {
            
            Point currentLocation = new Point(getX(), getY());
            
            Point wombatLocation = new Point(player.getX(), player.getY());

            Point nextPoint = EuclideanFunctions.getNextPoint(currentLocation, wombatLocation, VELOCITY);
            setLocation(nextPoint.x, nextPoint.y);

            double attackingDistance = EuclideanFunctions.getHypotenuse(getX(), getY(), player.getX(), player.getY());
            if (attackingDistance < 10) {
                state = EnemyState.ATTACKING;
            }

        } else {
            state = EnemyState.RETURNING;
        }

    }


  
    private void patrol() {

        int currentIndex = 0;

        Point currentPoint = new Point(getX(), getY());

        Rectangle destinationBoundBox = new Rectangle(destinationPoint.x - 2, destinationPoint.y - 2, 4, 4);
        if (destinationBoundBox.contains(currentPoint)) {

            currentIndex = pathPoints.indexOf(destinationPoint);

            if (locationIndex == currentIndex) {

                if (currentIndex == pathPoints.size() - 1) {

                    locationIndex = 0;
                } else {
                    locationIndex++;
                }

                destinationPoint = pathPoints.get(locationIndex);
            }
        }

        destinationPoint = pathPoints.get(locationIndex);
        Point nextPoint = EuclideanFunctions.getNextPoint(currentPoint, destinationPoint, VELOCITY);

        setLocation(nextPoint.x, nextPoint.y);

        if (detectPlayer()) {
            state = EnemyState.CHASING;
        }

    }

    private boolean detectPlayer(){

        List<Beaver> wombats = getObjectsInRange(DETECTION_RADIUS, Beaver.class); 
        if (wombats.size() > 0) {

            player = wombats.get(0);

            return true;
        }
        return false;
    }
}
