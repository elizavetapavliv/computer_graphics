package clippingalgorithms.models;

import javafx.beans.NamedArg;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PolygonLineChart extends LineChart {

    private Polygon clippingPolygon;
    
    private Polygon polygon;
    
    private String color;
    
    public PolygonLineChart(@NamedArg("xAxis") Axis xAxis, @NamedArg("yAxis") Axis yAxis) {
        super(xAxis, yAxis);
    }
    
    private boolean isPolygon = false;

    @Override
    protected void layoutPlotChildren() {
        super.layoutPlotChildren();
        if (polygon != null) {    
            if(!isPolygon){
                getPlotChildren().removeIf(o -> o instanceof Polygon);      
                isPolygon = true;
            }
            else{
                isPolygon  = false;
            }           
            polygon.toBack();
            polygon.setStrokeWidth(3.0);
            polygon.setStroke(Color.web(color));
            polygon.setFill(Color.TRANSPARENT);
            getPlotChildren().add(polygon);
            polygon = null;
        }
        if (clippingPolygon != null) {
            if(!isPolygon){
                 getPlotChildren().removeIf(o -> o instanceof Polygon);                
            }
            clippingPolygon.toBack();
            clippingPolygon.setStrokeWidth(3.0);
            clippingPolygon.setStroke(Color.web("#9A1059"));
            clippingPolygon.setFill(Color.TRANSPARENT);
            getPlotChildren().add(clippingPolygon);
            clippingPolygon = null;
        }     
    }

    public void setClippingPolygon(Polygon clippingPolygon) {
        this.clippingPolygon = clippingPolygon;
    }
    
    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
    
    public void setColor(String color){
        this.color = color;
    }
}