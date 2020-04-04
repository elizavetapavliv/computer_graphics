package basicrasteralgorithms.algorithms;

import java.util.ArrayList;
import javafx.geometry.Point2D;

public class BresenhamCircleAlgorithm {
    
      public ArrayList<Point2D> rasterize(int x0, int y0, int r) {        
        int x = r;
        int y = 0;     
        ArrayList<Point2D> points = new ArrayList<>();
        points.add(new Point2D(x + x0, y0));
        
        if(r > 0){
            points.add(new Point2D(-x + x0, y0));
            points.add(new Point2D(x0, x + y0));
            points.add(new Point2D(x0, -x + y0));           
        }
        int P = 1 - r;
        while(x > y){
            y++;
            if(P <= 0){
                P += 2 * y + 1;
            }
            else{
                x--;
                P += 2 * y - 2 * x + 1;
            }
            if(x < y){
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
