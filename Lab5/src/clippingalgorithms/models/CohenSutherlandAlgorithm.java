package clippingalgorithms.models;

import javafx.collections.ObservableList;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class CohenSutherlandAlgorithm {
    
    private final int INSIDE = 0;
    private final int LEFT = 1;
    private final int RIGHT = 2;
    private final int BOTTOM = 4;
    private final int TOP = 8;
    
    private final double xMin, yMin, xMax, yMax;

    public CohenSutherlandAlgorithm(Polygon clippingRectangle) {
        ObservableList<Double> points = clippingRectangle.getPoints();
        this.xMin = points.get(0);
        this.yMin = points.get(1);
        this.xMax = points.get(2);
        this.yMax = points.get(3);
    }
    
    private int getPointCode(double x, double y){
        int code = INSIDE;
        
        if(x < xMin){
            code |= LEFT;
        }
        else if(x > xMax){
            code |= RIGHT;
        }
        if (y < yMin) {
            code |= BOTTOM;
        } 
        else if (y > yMax) {
            code |= TOP;
        }
        
        return code;
    }
    
    public Line clip(Line line){
        double x1 = line.getStartX();
        double y1 = line.getStartY();
        double x2 = line.getEndX();
        double y2 = line.getEndY();

        double k = (y2 - y1) / (x2 - x1);
        int code1 = getPointCode(x1, y1);
        int code2 = getPointCode(x2, y2);
        boolean accept = false;

        while (true) {
            if (code1 == INSIDE && code2 == INSIDE) {
                accept = true;
                break;
            } 
            else if ((code1 & code2) != INSIDE) {
                break;
            } 
            else {
                int code;

                if (code1 != INSIDE) {
                    code = code1;
                } 
                else {
                    code = code2;
                }

                double x = 0, y = 0;

                if ((code & TOP) != INSIDE) {
                    x = x1 + (1 / k) * (yMax - y1);
                    y = yMax;
                } 
                else if ((code & BOTTOM) != INSIDE) {
                    x = x1 + (1 / k) * (yMin - y1);
                    y = yMin;
                } 
                else if ((code & RIGHT) != INSIDE) {
                    y = y1 + k * (xMax - x1);
                    x = xMax;
                } 
                else if ((code & LEFT) != INSIDE) {
                    y = y1 + k * (xMin - x1);
                    x = xMin;
                }

                if (code == code1) {
                    x1 = x;
                    y1 = y;
                    code1 = getPointCode(x1, y1);
                } 
                else {
                    x2 = x;
                    y2 = y;
                    code2 = getPointCode(x2, y2);
                }
            }
        }
        if(accept){
            return new Line(x1, y1, x2, y2);
        }
        return null;
    }
}
