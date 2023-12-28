import java.awt.Point;
import java.util.List;

public class EuclideanFunctions {

    @Deprecated
    public static Point getNextPoint(Point currentPoint, Point destinatioPoint, int STEP_SIZE) {

        double xStart = currentPoint.x;
        double yStart = currentPoint.y;
        double xNext = destinatioPoint.x;
        double yNext = destinatioPoint.y;

        double dy = yNext - yStart;
        double dx = xNext - xStart;

        int nextX = (int) xStart;
        int nextY = (int) yStart;

        // Check for vertical slope        
        if (Math.abs(dx) < 1e-6) {        
            nextY += Math.signum(dy) * STEP_SIZE;
        } else{

            int direction = (int) Math.signum(dx);
            nextX += STEP_SIZE * direction;
            nextY += (dy / dx) * STEP_SIZE * direction;            
        }

        return new Point(nextX, nextY);
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
