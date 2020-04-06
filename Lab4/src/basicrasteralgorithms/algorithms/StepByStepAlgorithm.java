package basicrasteralgorithms.algorithms;

import static basicrasteralgorithms.Utils.swap;
import static java.lang.Math.abs;
import static java.lang.Math.round;
import java.util.ArrayList;
import javafx.geometry.Point2D;

public class StepByStepAlgorithm {
      public ArrayList<Point2D> rasterize(int x0, int y0, int x1, int y1) {        
        boolean steep = abs(y1 - y0) > abs(x1 - x0);    
        if(steep){
            y0 = swap(x0, x0 = y0);
            y1 = swap(x1, x1 = y1);
        }        
        if(x0 > x1){
           x1 = swap(x0, x0 = x1);
           y1 = swap(y0, y0 = y1); 
        }
        
        double dx = x1 - x0;
        double dy = y1 - y0;
        double k = dy / dx;
        double y;
        
        ArrayList<Point2D> points = new ArrayList<>();      
        for (double x = x0; x <= x1; x++) {
              y = y0 + k * (x - x0);
              points.add(new Point2D(round(steep ? y : x ), round(steep ? x: y)));
        }
        return points;
    }
}