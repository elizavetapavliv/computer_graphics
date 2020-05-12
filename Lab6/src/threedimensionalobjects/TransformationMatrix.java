package threedimensionalobjects;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import threedimensionalobjects.controller.TransformationMatrixController;

public class TransformationMatrix extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/TransformationMatrix.fxml"));   
        Parent root = loader.load();      
      
        Scene scene = new Scene(root, 400, 400, true);    
        stage.setScene(scene);
        stage.setTitle("Transformation matrix");
        
        TransformationMatrixController controller = loader.getController();
        controller.loadMatrix();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}