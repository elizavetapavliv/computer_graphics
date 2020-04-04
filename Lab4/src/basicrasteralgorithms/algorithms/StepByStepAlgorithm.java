package basicrasteralgorithms.algorithms;

import static java.lang.Math.round;
import java.util.ArrayList;
import javafx.geometry.Point2D;

public class StepByStepAlgorithm {
      public ArrayList<Point2D> rasterize(int x0, int y0, int x1, int y1) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double y;
        ArrayList<Point2D> points = new ArrayList<>();
        for (double x = x0; x <= x1; x++) {
            if (dx != 0) {
                y = y0 + dy * (x - x0) / dx;
                points.add(new Point2D(round(x), round(y)));
            }
        }
        return points;
    }
}