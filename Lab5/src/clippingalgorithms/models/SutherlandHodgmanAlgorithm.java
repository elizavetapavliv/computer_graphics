package clippingalgorithms.models;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

public class SutherlandHodgmanAlgorithm {

    private final Polygon clippingRectangle;

    public SutherlandHodgmanAlgorithm(Polygon clippingRectangle) {
        this.clippingRectangle = clippingRectangle;
    }

    private Point2D getLinesIntersect(double x1, double y1, double x2, double y2,
            double x3, double y3, double x4, double y4){
        double xNumerator = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4);
        double yNumerator = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4);
        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        return new Point2D(xNumerator / denominator, yNumerator / denominator);
    }

    private void clipWithEdge(Polygon polygon, double x1, double y1, double x2, double y2) {
        Polygon newPolygon = new Polygon();
        ObservableList<Double> points = polygon.getPoints();
        int size = points.size();

        for (int i = 0; i < size; i += 2) {
            double currentX = points.get(i);
            double currentY = points.get(i + 1); 
            
            int nextPointIndex = (i + 2) % size;
            double nextX = points.get(nextPointIndex);
            double nextY = points.get(nextPointIndex + 1);
            
            double currentPosition = (x2 - x1) * (currentY - y1) - (y2 - y1) * (currentX - x1);
            double nextPosition = (x2 - x1) * (nextY - y1) - (y2 - y1) * (nextX - x1);

            if (currentPosition < 0 && nextPosition < 0) {
                newPolygon.getPoints().addAll(nextX, nextY);
            } 
            else if (currentPosition >= 0 && nextPosition < 0) {
                Point2D point = getLinesIntersect(x1, y1, x2, y2, currentX, currentY, nextX, nextY);
                newPolygon.getPoints().addAll(point.getX(), point.getY());
                newPolygon.getPoints().addAll(nextX, nextY);
            }
            else if (currentPosition < 0 && nextPosition >= 0) {
                Point2D point = getLinesIntersect(x1, y1, x2, y2, currentX, currentY, nextX, nextY);
                newPolygon.getPoints().addAll(point.getX(), point.getY());
            } 
        }
        polygon.getPoints().clear();
        polygon.getPoints().addAll(newPolygon.getPoints());
    }

    public void clip(Polygon polygon) {
        ObservableList<Double> clipperPoints = clippingRectangle.getPoints();
        int clipperSize = clipperPoints.size();
        
        for (int i = 0; i < clipperSize; i += 2) {
            int nextPointIndex = (i + 2) % clipperSize;          
            clipWithEdge(polygon, clipperPoints.get(i), clipperPoints.get(i + 1),
                    clipperPoints.get(nextPointIndex), clipperPoints.get(nextPointIndex + 1));
        }
    }
}