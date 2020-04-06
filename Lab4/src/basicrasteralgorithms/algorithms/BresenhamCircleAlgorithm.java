package basicrasteralgorithms.algorithms;

import java.util.ArrayList;
import javafx.geometry.Point2D;

public class BresenhamCircleAlgorithm {

    public ArrayList<Point2D> rasterize(int x0, int y0, int r) {
        int x = 0;
        int y = r;
        ArrayList<Point2D> points = new ArrayList<>();
        
        points.add(new Point2D(x0, y + y0));
        if (r > 0) {
            points.add(new Point2D(-y + x0, y0));
            points.add(new Point2D(y + x0, y0));
            points.add(new Point2D(x0, -y + y0));
        }
        int E = 3 - 2 * r;
        while (x < y) {
            if (E >= 0) {
                E += 4 * (x - y) + 10;
                y--;
            } 
            else {
                E += 4 * x + 6;
            }
            x++;
            if (x > y) {
                break;
            }
            points.add(new Point2D(x + x0, y + y0));
            points.add(new Point2D(-x + x0, y + y0));
            points.add(new Point2D(x + x0, -y + y0));
            points.add(new Point2D(-x + x0, -y + y0));
            if (x != y) {
                points.add(new Point2D(y + x0, x + y0));
                points.add(new Point2D(-y + x0, x + y0));
                points.add(new Point2D(y + x0, -x + y0));
                points.add(new Point2D(-y + x0, -x + y0));
            }
        }
        return points;
    }
}
