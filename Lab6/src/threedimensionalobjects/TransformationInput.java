package threedimensionalobjects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import threedimensionalobjects.controller.TransformationInputController;

public class TransformationInput extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/TransformationInput.fxml"));      
        Parent root = loader.load();     
        Scene scene = new Scene(root, 550, 300);
    
        stage.setScene(scene);
        stage.setTitle("Transformate letter");
        TransformationInputController controller = loader.getController();
        controller.loadLetter();
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}