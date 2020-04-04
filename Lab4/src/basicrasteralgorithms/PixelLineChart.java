package basicrasteralgorithms;

import java.util.ArrayList;
import javafx.beans.NamedArg;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PixelLineChart extends LineChart {

    private ArrayList<Rectangle> rectangles;  
    private Circle circle;

    public PixelLineChart(@NamedArg("xAxis") Axis xAxis, @NamedArg("yAxis") Axis yAxis) {
        super(xAxis, yAxis);
    }

    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        if (rectangles != null) {
            getPlotChildren().removeIf( o -> o instanceof Rectangle || o instanceof Circle);            
            for (Rectangle rectangle : rectangles) {
                rectangle.toBack();
                rectangle.setFill(Color.rgb(255, 0, 0, .2));
            }
            getPlotChildren().addAll(rectangles);
            rectangles = null;
        }
        
        if(circle != null){                 
            circle.toFront();
            circle.setStrokeWidth(2.0);
            circle.setStroke(Color.RED); 
            circle.setFill(Color.rgb(0, 255, 0, 0));
            getPlotChildren().add(circle);
            circle = null;
        }
    }
    
    public void setRectangles(ArrayList<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }
    
    public void setCircle(Circle circle) {
        this.circle = circle;
    }
}
