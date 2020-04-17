package clippingalgorithms;

import clippingalgorithms.models.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ClippingAlgorithmsController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private PolygonLineChart lineChart;
    
    @FXML
    private ComboBox algorithmComboBox;
    
    @FXML
    private Button fileButton, clipButton;
    
    private final ObservableList<String> algorithms = FXCollections.observableArrayList(
            "Line clipping", "Polygon clipping");
    
    private final String lightColor = "#C097F5";
    private final String darkColor = "#5D1CB3";
   
    private ArrayList<Line> lines;
    private Polygon polygon;
    private Polygon clippingPolygon;
    private NumberAxis xAxis, yAxis;
    
    private CohenSutherlandAlgorithm lineAlgorithm;
    private SutherlandHodgmanAlgorithm polygonAlgorithm;
    
    private FileChooser fileChooser;
    
    @FXML
    private void handleComboBoxAction(ActionEvent event) {
        fileButton.setVisible(true);
    }
    
    @FXML
    private void handleFileButtonAction(ActionEvent event) {
        int algorithmIndex = algorithmComboBox.getSelectionModel().getSelectedIndex();        
        try {
            if (algorithmIndex == 0) {
                drawLinesFromFile();
            } 
            else {
                drawPolygonFromFile();
            }
            clipButton.setVisible(true);
        } 
        catch (IOException ex) {
            Logger.getLogger(ClippingAlgorithmsController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
      
    @FXML
    private void handleClipButtonAction(ActionEvent event) {
        clip();
    }
    
    private void clip() {
        int algorithmIndex = algorithmComboBox.getSelectionModel().getSelectedIndex();
        if(algorithmIndex ==  0){
            for(Line line: lines){
                line = lineAlgorithm.clip(line);
                if(line != null){
                    drawLine(line, darkColor);
                }
            }
        }
        else {   
            polygonAlgorithm.clip(polygon);
            lineChart.setColor(darkColor);
            drawPolygon(false);
        }
    }

    private void drawLine(Line line, String color)
    {
        ObservableList<Data<Double, Double>> data = FXCollections.observableArrayList();
        data.add(new Data<>(line.getStartX(), line.getStartY()));
        data.add(new Data<>(line.getEndX(), line.getEndY()));
        Series<Double, Double> series = new Series<>(data);
        
        lineChart.getData().add(series);
        series.getNode().setStyle("-fx-stroke: " + color + ";");
        for (int i = 0; i < series.getData().size(); i++) {
            Data dataPoint = series.getData().get(i);
            dataPoint.getNode().setStyle("-fx-background-color: " + color + "; -fx-padding: 0px;");
        }
    }
    
    private void drawClippingRectangle(){
        ObservableList<Double> points = clippingPolygon.getPoints();
        lineChart.setClippingPolygon(new Polygon(
                xAxis.getDisplayPosition(points.get(0)), yAxis.getDisplayPosition(points.get(1)),
                xAxis.getDisplayPosition(points.get(0)), yAxis.getDisplayPosition(points.get(3)),
                xAxis.getDisplayPosition(points.get(2)), yAxis.getDisplayPosition(points.get(3)),
                xAxis.getDisplayPosition(points.get(2)), yAxis.getDisplayPosition(points.get(1))
        ));
    }
    
    private void drawPolygon(boolean isClipping){
        ObservableList<Data<Double, Double>> data = FXCollections.observableArrayList();
        data.add(new Data<>(20.0, 20.0));
        lineChart.getData().add(new Series(data));
        
        ObservableList<Double> points = isClipping ? clippingPolygon.getPoints() : polygon.getPoints();
        double[] chartPoints = new double[points.size()];
        
        for(int i = 0; i < points.size(); i += 2){
            chartPoints[i] = xAxis.getDisplayPosition(points.get(i));
            chartPoints[i + 1] = yAxis.getDisplayPosition(points.get(i + 1));
        }       
        Polygon current = new Polygon(chartPoints);
        if (isClipping) {
            lineChart.setClippingPolygon(current);
        } 
        else {
            lineChart.setPolygon(current);
        }
    }
    
    private void drawLinesFromFile() throws IOException {
        lineChart.getData().clear();  
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Scanner scanner = new Scanner(file.toPath());

            int n = scanner.nextInt();
            lines.clear();
            for (int i = 0; i < n; i++) {
                lines.add(new Line(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
            }
            clippingPolygon = new Polygon(scanner.nextInt(), scanner.nextInt(),
                    scanner.nextInt(), scanner.nextInt());
            lineAlgorithm = new CohenSutherlandAlgorithm(clippingPolygon);
            lines.forEach(line -> drawLine(line, lightColor));
            drawClippingRectangle();
        }
    }
    
     private void drawPolygonFromFile() throws IOException {
        lineChart.getData().clear();  
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        Scanner scanner = new Scanner(file.toPath());
        
        int n = scanner.nextInt();
        polygon = new Polygon();
        for(int i = 0; i < n; i++){
            polygon.getPoints().addAll(scanner.nextDouble(), scanner.nextDouble());
        }
        n = scanner.nextInt();
        clippingPolygon = new Polygon();
        for(int i = 0; i < n; i++){
            clippingPolygon.getPoints().addAll(scanner.nextDouble(), scanner.nextDouble());
        }
        polygonAlgorithm = new SutherlandHodgmanAlgorithm(clippingPolygon);
        lineChart.setColor(lightColor);
        drawPolygon(false);
        drawPolygon(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        algorithmComboBox.setItems(algorithms);
        fileButton.setVisible(false);
        clipButton.setVisible(false);
        
        fileChooser = new FileChooser();
        lines = new ArrayList<>();
        
        xAxis = (NumberAxis)lineChart.getXAxis();
        yAxis = (NumberAxis)lineChart.getYAxis();
    }       
}