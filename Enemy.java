import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Point;
import java.awt.Rectangle;
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
    
    /**
     * The delay between attacks.
     * Set as 120 frames.
     */
    private final int ATTACK_DELAY = 120; 

    //#region Radii for detection, chasing and attacking
    private final int DETECTION_RADIUS = 60;
    private final int CHASING_RADIUS = 50;
    private final int ATTACKING_RADIUS = 30;
    //#endregion

    /**
     * The current state of the enemy.
     * Can be one of the following:
     * 1. PATROLLING
     * 2. CHASING
     * 3. ATTACKING
     * 4. RETURNING
     */ 
    private EnemyState state;
    
    /**
     * The list of points that the enemy will patrol.
     */
    private List<Point> pathPoints;
    
    /**
     * Refernces to the player that the enemy will chase and attack.
     */
    private Beaver player;
    
    /**
     * The index of the current location in the pathPoints list.
     */
    private int locationIndex = 0;    
    
    /**
     * The destination point that the enemy is moving towards.
     */
    private Point destinationPoint = new Point();

    /**
     * The initial location of the enemy, when it is added to the world.
     */
    private Point initialLocation;
    
    /**
     * A flag to indicate whether the enemy can chase and attack.
     * This is used to prevent the enemy from attacking too frequently,
     * and is reset using the resetAttacklessPeriod method based on the ATTACK_DELAY.
     */
    private boolean canAttack = true;


    public Enemy(List<Point> pathPoints, Point initialLocatiPoint) {
        
        if (pathPoints.size() <= 0) {
            System.err.println("Error: pathPoints must contain at least one point");
            throw new IllegalArgumentException();
        }
        
        this.pathPoints = pathPoints;
        this.initialLocation = initialLocatiPoint;

        // Set the wolverine image. 
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

        resetAttacklessPeriod();
    }

    private int attackCounter = 0;
    
    private void resetAttacklessPeriod() {

        if (!canAttack) {

            attackCounter++;
            if (attackCounter > ATTACK_DELAY) {
                attackCounter = 0;
                canAttack = true;
            }
        }
    }

    private void returning() {

        Point currentLocation = new Point(getX(), getY());

        destinationPoint = EuclideanFunctions.getNearestPoint(pathPoints, currentLocation.x, currentLocation.y);
        
        // Set the location index to the nearest point, so patrolling can continue from there
        locationIndex = pathPoints.indexOf(destinationPoint);
        
        state = EnemyState.PATROLLING;
        Point nextPoint = EuclideanFunctions.getNextPoint(currentLocation, destinationPoint, VELOCITY);
        collisionSetLocation(nextPoint.x, nextPoint.y);

    }

    // TODO - To be removed
    private int delayCounter = 0;


    private void attacking() {

        player.setBeingAttacked(true);

        // TODO - Add attack animation here

        if (delayCounter > 60) {
            player.setBeingAttacked(false);
            state = EnemyState.RETURNING;
            canAttack = false;
            delayCounter = 0;
        }

        delayCounter++;
    }
    
    private void chasing() {

        double chasingDistance = EuclideanFunctions.getHypotenuse(getX(), getY(), player.getX(), player.getY());
        if (chasingDistance < CHASING_RADIUS && canAttack) {
            
            Point currentLocation = new Point(getX(), getY());            
            Point wombatLocation = new Point(player.getX(), player.getY());

            Point nextPoint = EuclideanFunctions.getNextPoint(currentLocation, wombatLocation, VELOCITY);
            collisionSetLocation(nextPoint.x, nextPoint.y);

            double attackingDistance = EuclideanFunctions.getHypotenuse(getX(), getY(), player.getX(), player.getY());
            if (attackingDistance < ATTACKING_RADIUS) {
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

                locationIndex = (locationIndex + 1) % pathPoints.size();

                destinationPoint = pathPoints.get(locationIndex);
            }
        }

        destinationPoint = pathPoints.get(locationIndex);
        Point nextPoint = EuclideanFunctions.getNextPoint(currentPoint, destinationPoint, VELOCITY);

        collisionSetLocation(nextPoint.x, nextPoint.y);

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
    

    public boolean collisionSetLocation(final int x, final int y) {
        final int oldX = getX();
        final int oldY = getY();
        setLocation(x, y);

        if (isTouching(ObjectTile.class) || isTouching(WoodTile.class) || isTouching(WaterTile.class) || isTouching(Beaver.class)) {

            // Collided with an object, so revert to previous position.
            setLocation(oldX, oldY);
            return false; // move was not allowed!
        }

        return true; // move was allowed!
    }

    public void reset() {

        locationIndex = 0;
        destinationPoint = pathPoints.get(locationIndex);
        setLocation(initialLocation.x, initialLocation.y);
        state = EnemyState.PATROLLING;
    }
}
