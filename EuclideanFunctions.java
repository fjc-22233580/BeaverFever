import java.awt.Point;
import java.util.List;

/**
 * This class provides helper methods for 2d space Euclidean calculations.
 */
public class EuclideanFunctions {

    /**
     * Represents a list of points that define a path.
     */
    private List<Point> pathPoints;

    public EuclideanFunctions(List<Point> pathPoints) {
        this.pathPoints = pathPoints;
    }

    /** Gets the next point between a current point and a destination point, based on a step size.
     * @param currentPoint The current point, normally where the enemy is when the method is called.
     * @param destinationPoint The destination point, normally the next point in the path.
     * @param STEP_SIZE The step size, normally the velocity of the enemy.
     * @return The calculated next point.
     */
    public Point getNextPoint(Point currentPoint, Point destinationPoint, int STEP_SIZE) {      

        Point nextPoint = null;

        // If the current point is the destination point, then use the current point as the next point
        if (currentPoint.equals(destinationPoint)) {
            nextPoint = currentPoint;
        } else {

            double x1 = currentPoint.x;
            double y1 = currentPoint.y;
            double x2 = destinationPoint.x;
            double y2 = destinationPoint.y;
    
            // Calculate vector from current to destination
            double vx = x2 - x1;
            double vy = y2 - y1;
    
            // Calculate the magnitude of the vector
            double magnitude = Math.sqrt(vx * vx + vy * vy);
    
            // Normalize the vector, other we can generate a point that is too far away
            double ux = vx / magnitude;
            double uy = vy / magnitude;
    
            // Calculate the next point, based on the step size (in effect this is the velocity)
            double nextX = x1 + STEP_SIZE * ux;
            double nextY = y1 + STEP_SIZE * uy;

            nextPoint = new Point((int)nextX, (int)nextY);
        }

        return nextPoint;
    } 

    /** Gets the nearest point from a list of points, based on a current point.
     * @param currentPoint The current point, normally where the enemy is when the method is called.
     * @return The nearest point in the patrol path that enemy is nearest to.
     */
    public Point getNearestPoint(Point currentPoint){       

        // Set initial minimum value
        Point nearestPoint = pathPoints.get(0);

        // Get the distance between the current point and the first point in the list
        double dMin = getDistance(currentPoint, nearestPoint);

        // Iterate through the list of points, and see if any other points are closer. 
        for (Point point : pathPoints) {

            double d = getDistance(currentPoint, point);
            if (d < dMin) {
                dMin = d;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }


    /** Gets the Euclidean distance between two points.
     * @param pointA The first point.
     * @param pointB The second point.
     * @return The distance between the two points.
     */
    public double getDistance(Point pointA, Point pointB)
    {
        double a = pointB.x - pointA.x;
        double b = pointB.y - pointA.y;
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;
    }
}
