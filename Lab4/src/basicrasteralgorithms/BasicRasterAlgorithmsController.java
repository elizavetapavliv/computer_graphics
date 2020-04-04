package basicrasteralgorithms;

import basicrasteralgorithms.algorithms.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class BasicRasterAlgorithmsController implements Initializable {
    
    @FXML
    private PixelLineChart lineChart;
    
    @FXML
    private ComboBox algorithmComboBox;
    
    @FXML
    private Label timeLabel, scaleLabel, x0Label, y0Label, x1RLabel, y1Label;
    
    @FXML
    private TextField x0Text, y0Text, x1RText, y1Text;
    
    @FXML
    private Button button;
    
    private boolean firstChoice;
    
    private final ObservableList<String> algorithms = FXCollections.observableArrayList(
            "Step by step", "DDA", "Bresenham", "Bresenham (circle)");
    
    private int x0, y0, x1, y1, r; 
    private final int defaultX0 = 2, defaultY0 = 2, defaultX1 = 8, defaultY1 = 5, defaultR = 4;   
    private int scale, size;
    
    private ArrayList<Point2D> pixels;
    private NumberAxis xAxis, yAxis;
    
    private DigitalDifferentialAnalyzerAlgorithm ddaAlgorithm;
    private StepByStepAlgorithm stepAlgorithm;
    private BresenhamAlgorithm bresenhamAlgorithm;    
    private BresenhamCircleAlgorithm bresenhamCircleAlgorithm;
    
    @FXML
    private void handleComboBoxAction(ActionEvent event) {
       if(firstChoice){
            toggleControls(true);
            firstChoice = false;
       }    
       lineChart.getData().clear();  
       lineChart.setRectangles(new ArrayList<>());
    
        String algorithm = (String) algorithmComboBox.getSelectionModel().getSelectedItem();
        if (algorithm.equals("Bresenham (circle)")) {
            toggleLineCircle(false);
        } 
        else {
            toggleLineCircle(true);
        }
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        rasterize();
    }
    
    private void rasterize() {
        String algorithm = (String) algorithmComboBox.getSelectionModel().getSelectedItem();
        long startTime = 0L, endTime = 0L;
        getCoordinates();
        switch (algorithm) {
            case "Step by step":          
                startTime = System.nanoTime();
                pixels = stepAlgorithm.rasterize(x0, y0, x1, y1);
                endTime = System.nanoTime();
                drawLine();
                break;
            case "DDA":
                startTime = System.nanoTime();
                pixels = ddaAlgorithm.rasterize(x0, y0, x1, y1);
                endTime = System.nanoTime();
                drawLine();
                break;
            case "Bresenham":
                startTime = System.nanoTime();
                pixels = bresenhamAlgorithm.rasterize(x0, y0, x1, y1);
                endTime = System.nanoTime();
                drawLine();
                break;
            case "Bresenham (circle)":
                startTime = System.nanoTime();
                pixels = bresenhamCircleAlgorithm.rasterize(x0, y0, r);
                endTime = System.nanoTime();
                drawCircle();
                break;
        }
        timeLabel.setText(String.format("Time: %d μs.", (endTime - startTime) / 1000));
    }
    
    private void getCoordinates(){
        x0 = validate(x0Text, defaultX0, -9);
        y0 = validate(y0Text, defaultY0, -9);       
        
        if(y1Label.isVisible()){
            x1 = validate(x1RText, defaultX1, -9);
            y1 = validate(y1Text, defaultY1, -9);       
        }
        else{
            r = validate(x1RText, defaultR, 0);
        }
    }
    
    private int validate(TextField text, int defaultValue, int lowBorder){
        int result;
        try{
            result = Integer.parseInt(text.getText());
        }
        catch(NumberFormatException e){
            text.setText(Integer.toString(defaultValue));
            return defaultValue;
        }
        if(result < lowBorder || result > 9){
            text.setText(Integer.toString(defaultValue));
            return defaultValue;
        }
        return result;
    }
    
    private void drawPixels() {   
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        
        for(int i = 0; i < pixels.size(); i++){
            rectangles.add(new Rectangle(xAxis.getDisplayPosition(pixels.get(i).getX()),
                    yAxis.getDisplayPosition(pixels.get(i).getY() + 1), scale, scale));
        }
        lineChart.setRectangles(rectangles);
    }
    
    private void drawLine()
    {       
        lineChart.getData().clear();
        ObservableList<Data<Double, Double>> data = FXCollections.observableArrayList();       
        data.add(new Data<>(x0 + 0.5, y0 + 0.5));
        data.add(new Data<>(x1 + 0.5, y1 + 0.5));
        lineChart.getData().add(new Series(data));
        drawPixels();
    }
    
    private void drawCircle(){
        ObservableList<Data<Double, Double>> data = FXCollections.observableArrayList();       
        data.add(new Data<>(x0 + 0.5, y0 + 0.5));
        lineChart.getData().clear();
        lineChart.getData().add(new Series(data));
        Circle circle = new Circle(xAxis.getDisplayPosition(x0 + 0.5), yAxis.getDisplayPosition(y0 + 0.5), r * scale);
        lineChart.setCircle(circle);
        drawPixels();
    }
    
    private void toggleControls(boolean isOn){
        button.setVisible(isOn);
        x0Label.setVisible(isOn);
        y0Label.setVisible(isOn);
        x1RLabel.setVisible(isOn);
        y1Label.setVisible(isOn);
        x0Text.setVisible(isOn);
        y0Text.setVisible(isOn);
        x1RText.setVisible(isOn);
        y1Text.setVisible(isOn);       
    }
    
    
    private void toggleLineCircle(boolean isLine){
        if(isLine){
            x1RLabel.setText("x1");
            x1RText.setText(Integer.toString(defaultX1));
            y1Label.setVisible(true);
            y1Text.setVisible(true);
        }
        else{
            x1RLabel.setText("r");
            x1RText.setText(Integer.toString(defaultR));
            y1Label.setVisible(false);
            y1Text.setVisible(false);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        algorithmComboBox.setItems(algorithms);
        xAxis = (NumberAxis)lineChart.getXAxis();
        yAxis = (NumberAxis)lineChart.getYAxis();
        size = (int)xAxis.getPrefWidth();
        scale = size / (int)(xAxis.getUpperBound() - xAxis.getLowerBound());
        scaleLabel.setText(String.format("Pixels scale %d:1.", scale));
        
        ddaAlgorithm = new DigitalDifferentialAnalyzerAlgorithm();
        stepAlgorithm = new StepByStepAlgorithm();
        bresenhamAlgorithm = new BresenhamAlgorithm();
        bresenhamCircleAlgorithm = new BresenhamCircleAlgorithm();
        
        toggleControls(false);
        firstChoice = true;       
    }       
}