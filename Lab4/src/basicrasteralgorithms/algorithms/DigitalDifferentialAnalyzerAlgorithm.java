package basicrasteralgorithms.algorithms;

import static java.lang.Double.max;
import static java.lang.Math.abs;
import static java.lang.Math.round;
import java.util.ArrayList;
import javafx.geometry.Point2D;

public class DigitalDifferentialAnalyzerAlgorithm {

    public ArrayList<Point2D> rasterize(int x0, int y0, int x1, int y1) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double L = max(abs(dx), abs(dy));
        ArrayList<Point2D> points = new ArrayList<>();
       
        dx /= L;
        dy /= L;
        double x = x0;
        double y = y0;   
        for (int i = 0; i <= L; i++) {    
            points.add(new Point2D(round(x), round(y)));
            x += dx;           
            y += dy;      
           
        }
        return points;
    }
}
