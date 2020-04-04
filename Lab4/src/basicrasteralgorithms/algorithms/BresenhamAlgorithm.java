package basicrasteralgorithms.algorithms;

import java.util.ArrayList;
import javafx.geometry.Point2D;

public class BresenhamAlgorithm {

    public ArrayList<Point2D> rasterize(int x0, int y0, int x1, int y1) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double d = dy - dx / 2;
        int x = x0;
        int y = y0;
        ArrayList<Point2D> points = new ArrayList<>();
        points.add(new Point2D(x, y));
        while (x < x1){
            x++;
            if(d < 0){
                d += dy;
            }
            else{
                d += dy - dx;
                y++;
            }
            points.add(new Point2D(x, y));
        }
        return points;
    }
}