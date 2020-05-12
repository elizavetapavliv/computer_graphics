package threedimensionalobjects.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import threedimensionalobjects.ThreeDimensionalObjects;
import threedimensionalobjects.TransformationMatrix;

public class TransformationInputController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private TextField tX, tY, tZ, rX, rY, rZ, sX, sY, sZ;
    
    private Rotate xAxis, yAxis, zAxis;   
    
    private MeshView letter;
    
    private ArrayList<Double> matrix;
    
    private float[] points, initialPoints;
    
    private double validate(TextField text, int defaultValue, int min, int max){
        double result;
        try{
            result = Double.parseDouble(text.getText());
        }
        catch(NumberFormatException e){
            text.setText(Double.toString(defaultValue));
            return defaultValue;
        }
        if(result < min || result > max){
            text.setText(Double.toString(defaultValue));
            return defaultValue;
        }
        return result;
    }

    @FXML
    private void handleTransformateButtonAction(ActionEvent event){
        transformate();
        loadMatrix();
    }
    
    private void transformate() {  
        double x = validate(sX, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        double y = validate(sY, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        double z = validate(sZ, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        addRowToMatrix(x, y , z);
        
        x = validate(rX, 0, -360, 360);
        y = validate(rY, 0, -360, 360);
        z = validate(rZ, 0, -360, 360);
        addRowToMatrix(x, y , z);
        
        x = validate(tX, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        y = validate(tY, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        z = validate(tZ, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        addRowToMatrix(x, y , z);
    }
    
    private void addRowToMatrix(double x, double y, double z){
        matrix.add(x);
        matrix.add(y);
        matrix.add(z);
    }
    
    private void loadMatrix(){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();

        TransformationMatrix transformationMatrix = new TransformationMatrix();
        Stage childStage = new Stage();
        childStage.initOwner(stage);
        childStage.getProperties().put("matrix", matrix);
        childStage.getProperties().put("points", points);
        childStage.getProperties().put("initialPoints", initialPoints);
        childStage.getProperties().put("letter", letter);
        try {
            transformationMatrix.start(childStage);
        } 
        catch (Exception ex) {
            Logger.getLogger(ThreeDimensionalObjects.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadLetter() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        
        points = (float[]) stage.getProperties().get("points");
        initialPoints = (float[]) stage.getProperties().get("initialPoints");
        
        letter = (MeshView) stage.getProperties().get("letter"); 
        letter.getTransforms().removeIf(o -> o instanceof Rotate);
        
        xAxis = new Rotate(0, Rotate.X_AXIS);
        yAxis = new Rotate(0, Rotate.Y_AXIS);
        zAxis = new Rotate(0, Rotate.Z_AXIS);
        letter.getTransforms().addAll(xAxis, yAxis, zAxis);
        matrix = new ArrayList<>();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { }
}