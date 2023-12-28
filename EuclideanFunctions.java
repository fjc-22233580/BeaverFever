import java.awt.Point;
import java.util.List;

public class EuclideanFunctions {

    public static Point getNextPoint(Point currentPoint, Point destinatioPoint, int STEP_SIZE) {

        if (currentPoint == destinatioPoint) {
            return currentPoint;
        }

        double x1 = currentPoint.x;
        double y1 = currentPoint.y;
        double x2 = destinatioPoint.x;
        double y2 = destinatioPoint.y;

        // Calculate vector from current to destination
        double vx = x2 - x1;
        double vy = y2 - y1;

        // Calculate the magnitude of the vector
        double magnitude = Math.sqrt(vx * vx + vy * vy);

        // Normalize the vector
        double ux = vx / magnitude;
        double uy = vy / magnitude;

        // Calculate the next point
        double nextX = x1 + STEP_SIZE * ux;
        double nextY = y1 + STEP_SIZE * uy;

        return new Point((int)nextX, (int)nextY);
    } 

    public static Point getNearestPoint(List<Point> pathPoints, double x, double y){       

        // Set initial minimum value
        Point pointMin = pathPoints.get(0);
        double dMin = getHypotenuse(x, y, pointMin.x, pointMin.y);

        for (Point point : pathPoints) {

            double d = getHypotenuse(x, y, point.x, point.y);
            if (d < dMin) {
                dMin = d;
                pointMin = point;
            }
        }
        return pointMin;
    }


    public static double getHypotenuse(double x1, double y1, double x2, double y2)
    {
        double a = x2 - x1;
        double b = y2 - y1;
        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        return c;
    }
}
