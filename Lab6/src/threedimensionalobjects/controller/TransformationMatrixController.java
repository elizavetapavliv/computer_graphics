package threedimensionalobjects.controller;

import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import static threedimensionalobjects.MatrixOperations.multiplyMatrixes;

public class TransformationMatrixController implements Initializable {

   @FXML
   private AnchorPane anchorPane; 
    
   @FXML
   private TextArea textArea;
   
   private ArrayList<Double> matrix;
   
   private double[][] result;
   
   private float[] points, initialPoints;
   
   private MeshView letter;
   
   public void loadMatrix(){
       Stage stage = (Stage) anchorPane.getScene().getWindow();

       letter = (MeshView) stage.getProperties().get("letter");
       
       matrix = (ArrayList<Double>) stage.getProperties().get("matrix");
       points = (float[]) stage.getProperties().get("points");
       initialPoints = (float[]) stage.getProperties().get("initialPoints");
       
       calculateResultMatrix();
   }
   
    private void calculateResultMatrix() {
        double[][] translate = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {matrix.get(6), matrix.get(7), matrix.get(8), 1}
        };
        
        double a = toRadians(matrix.get(3));
        double[][] rotateX = {
            {1, 0, 0, 0},
            {0, cos(a), sin(a), 0},
            {0, -sin(a), cos(a), 0},
            {0, 0, 0, 1}
        };
        a = toRadians(matrix.get(4));
        double[][] rotateY = {
            {cos(a), 0, -sin(a), 0},
            {0, 1, 0, 0},
            {sin(a), 0, cos(a), 0},
            {0, 0, 0, 1}
        };
        a = toRadians(matrix.get(5));
        double[][] rotateZ = {
            {cos(a), sin(a), 0, 0},
            {-sin(a), cos(a), 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
        };

        double[][] scale = {
            {matrix.get(0), 0, 0, 0},
            {0, matrix.get(1), 0, 0},
            {0, 0, matrix.get(2), 0},
            {0, 0, 0, 1}
        };
        double[][] r1 = multiplyMatrixes(translate, rotateX);
        double[][] r2 = multiplyMatrixes(r1, rotateY);
        double[][] r3 = multiplyMatrixes(r2, rotateZ);
        result = multiplyMatrixes(r3, scale);      
        
        recalculatePoints();
        printMatrix();
    }

    private void recalculatePoints(){
        int rows = points.length / 3;
        double[][] twoDimensionalPoints = new double[rows][4];
        for (int i = 0; i < rows; i++) {
            twoDimensionalPoints[i][3] = 1;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 3; j++) {
                twoDimensionalPoints[i][j] = initialPoints[3 * i + j];
            }
        }

        double[][] twoDimensionalPointsResult = multiplyMatrixes(twoDimensionalPoints, result);

        int k = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 4; j++) {
                if (j != 3) {
                    points[k++] = (float) twoDimensionalPointsResult[i][j];
                }
            }
        }
        TriangleMesh mesh = (TriangleMesh) letter.getMesh();
        mesh.getPoints().setAll(points);     
    }
    
    private void printMatrix(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String format = "%.2f ";
                if( result[i][j] == floor(result[i][j])){
                   format = "%.0f ";
                }
                else if(String.format(format, result[i][j]).endsWith("0 ")){
                   format = "%.1f "; 
                }
                textArea.appendText(String.format(format, result[i][j]));
            }
            textArea.appendText("\n");
        }
    }
    
   @Override
   public void initialize(URL url, ResourceBundle rb) { }
}