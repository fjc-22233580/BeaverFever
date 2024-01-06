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

    private final int ATTACK_TIME = 60;
    
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
    private EnemyState state = null;
    
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
    
    
    /**
     * A counter to keep track of the number of frames since the last attack,
     * whilst this is counting up, the enemy cannot attack.
     */
    private int attacklessCounter = 0;

    /**
     * The counter for tracking the time since an attack started. 
     */
    private int attackTimeCounter = 0;

    /**
     * Reference the class provides Euclidean functions, 
     * we also need a list of points (patrol path) to calculate the nearest point.
     */
    private EuclideanFunctions euclideanFunctions;

    private GreenfootImage defaultImage = new GreenfootImage("wolverine.png");

    private GifImage attackingGif = new GifImage("wattack.gif");

     /**
     * Constructs an enemy with the given path points and initial location (this is used to reset the enemy position).    
     * @param pathPoints the list of points representing the path that the enemy will follow
     * @param initialLocationPoint the initial location of the enemy
     * @throws IllegalArgumentException if the pathPoints list is empty or contains less than two points
     */
    public Enemy(List<Point> pathPoints, Point initialLocationPoint) {

        if (pathPoints.size() <= 0) {
            System.err.println("Error: pathPoints must contain two points or more.");
            throw new IllegalArgumentException();
        }

        this.pathPoints = pathPoints;
        this.initialLocation = initialLocationPoint;

        euclideanFunctions = new EuclideanFunctions(pathPoints);

        // Set the wolverine image.        
        setImage(defaultImage);        
    }

    /**
     * Called when the enemy is added to the world.
     * Sets the initial state to PATROLLING.
     * Needed to be done here, as the world is not set when the constructor is called.
     */
    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);

        // Set the initial state
        state = EnemyState.PATROLLING;
        resetEnemyPosition();
    }
    
    /**
     * Act method - called by Greenfoot every 16ms.
     * Used to control which state the enemy should go into.
     */
    public void act() {

        // Check for null state, as the state is set in the addedToWorld method, which is called after the constructor.
        if(state != null) {

            switch (state) {
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

        resetAttacklessPeriod();
    }
    
    /**
     * Resets the attackless period for the enemy.
     * The enemy cannot attack whilst the attackless period is counting up.
     */
    private void resetAttacklessPeriod() {

        if (!canAttack) {

            attacklessCounter++;
            if (attacklessCounter > ATTACK_DELAY) {
                attacklessCounter = 0;
                canAttack = true;
                System.out.println("Attack period reset");
            }
        }
    }

    /**
     * Moves the enemy back to the nearest point in the patrol path and resumes patrolling.
     */
    private void returning() {

        // Get the nearest point in the path to us.
        Point currentLocation = new Point(getX(), getY());
        destinationPoint = euclideanFunctions.getNearestPoint(currentLocation);
        
        // Set the location index to the nearest point, so patrolling can continue from there
        locationIndex = pathPoints.indexOf(destinationPoint);        
        Point nextPoint = euclideanFunctions.getNextPoint(currentLocation, destinationPoint, VELOCITY);

        collisionSetLocation(nextPoint);
        System.out.println("returning: " + nextPoint.x + ", " + nextPoint.y);
        state = EnemyState.PATROLLING;
    }

    /**
     * The enemy attacking method, which is called when the enemy is in the ATTACKING state.
     * Sets the player to being attacked, and then returns to the RETURNING state after a delay.
     */
    private void attacking() {

        player.setBeingAttacked(true);

        setImage(attackingGif.getCurrentImage());

        if (attackTimeCounter > ATTACK_TIME) {
            player.setBeingAttacked(false);
            state = EnemyState.RETURNING;
            canAttack = false;
            attackTimeCounter = 0;
            setImage(defaultImage);
        }

        attackTimeCounter++;
    }
    
    /**
     * Performs the chasing behavior of the enemy.
     * The enemy moves towards the player if within a certain radius and attacks if within attacking radius.
     * If the player is outside the chasing radius, the enemy returns to its original position, and resumes patrolling.
     */
    private void chasing() {

        Point currentLocation = new Point(getX(), getY());            
        Point playerLocation = new Point(player.getX(), player.getY());

        double chasingDistance = euclideanFunctions.getDistance(currentLocation, playerLocation);
        if (chasingDistance < CHASING_RADIUS && canAttack) {            

            Point nextPoint = euclideanFunctions.getNextPoint(currentLocation, playerLocation, VELOCITY);
            collisionSetLocation(nextPoint);

            double attackingDistance = euclideanFunctions.getDistance(currentLocation, playerLocation);
            if (attackingDistance < ATTACKING_RADIUS) {
                state = EnemyState.ATTACKING;                
            }

        } else {
            state = EnemyState.RETURNING;
        }
    }

  
    /**
     * Moves the enemy character along a predefined path, patrolling the area.
     * If the enemy reaches its destination point, it updates the destination point
     * to the next point in the path.
     * If the enemy detects the player, it changes its state to chasing.
     */
    private void patrol() {

        int currentIndex = 0;
        Point currentPoint = new Point(getX(), getY());

        // Check if we have reached the destination point.
        // A bounding box was used to check if the current point is within the destination point, 
        // otherwise if the velocity is too high, the enemy will skip over the destination point.
        Rectangle destinationBoundBox = new Rectangle(destinationPoint.x - 2, destinationPoint.y - 2, 4, 4);
        if (destinationBoundBox.contains(currentPoint)) {

            currentIndex = pathPoints.indexOf(destinationPoint);

            // Only update to the next point if we're on the current point in the path.
            if (locationIndex == currentIndex) {

                // If we have reached the end of the path, then go back to the start.
                locationIndex = (locationIndex + 1) % pathPoints.size();
                destinationPoint = pathPoints.get(locationIndex);
            }
        }

        // Get the next point based on the VELOCITY, torwards the destination point.
        destinationPoint = pathPoints.get(locationIndex);
        Point nextPoint = euclideanFunctions.getNextPoint(currentPoint, destinationPoint, VELOCITY);

        // Move the enemy to the next point, and check for collisions.
        collisionSetLocation(nextPoint);
        System.out.println("patrol: " + nextPoint.x + ", " + nextPoint.y);

        // Check if the player is within the detection radius.
        if (detectPlayer()) {
            state = EnemyState.CHASING;
        }
    }

    /**
     * Detects the player if its within a certain radius.
     * If we find the player, then cache the reference to the player.
     * @return true if the player is detected, false otherwise
     */
    private boolean detectPlayer(){

        List<Beaver> wombats = getObjectsInRange(DETECTION_RADIUS, Beaver.class); 
        if (wombats.size() > 0) {

            player = wombats.get(0);

            return true;
        }
        return false;
    }    

    /**
     * Sets the location of the enemy, and checks for collisions with objects.
     * If a collision is detected, then the enemy is reverted to its previous position.
     * @param location the location to set the enemy to.
     */
    private void collisionSetLocation(final Point location) {
        final int oldX = getX();
        final int oldY = getY();
        setLocation(location.x, location.y);

        // Check for collisions with objects.
        if (isTouching(ObjectTile.class) || isTouching(WoodTile.class) || isTouching(WaterTile.class) || isTouching(Beaver.class)) {

            // Collided with an object, so revert to previous position.
            setLocation(oldX, oldY);
        }
    }

    /**
     * Resets the enemy's state and location to its initial point,
     * called externally when the player switches worlds.
     */
    public void resetEnemyPosition() {

        locationIndex = 0;
        destinationPoint = pathPoints.get(locationIndex);
        setLocation(initialLocation.x, initialLocation.y);
        state = EnemyState.PATROLLING;
    }
}
