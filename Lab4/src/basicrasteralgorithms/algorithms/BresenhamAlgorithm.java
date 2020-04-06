package basicrasteralgorithms.algorithms;

import static basicrasteralgorithms.Utils.swap;
import static java.lang.Math.abs;
import java.util.ArrayList;
import javafx.geometry.Point2D;

public class BresenhamAlgorithm {

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
        
        int dx = x1 - x0;
        int dy = abs(y1 - y0);
        int error = dx/2;
        int yInc = y0 < y1 ? 1: -1;
        int y = y0;
        ArrayList<Point2D> points = new ArrayList<>();
        
        for (int x = x0; x <= x1; x++){
            points.add(new Point2D(steep ? y : x, steep ? x: y));
            error -= dy;
            if(error < 0){
                y += yInc;
                error += dx;
            }
        }
        return points;
    }
}