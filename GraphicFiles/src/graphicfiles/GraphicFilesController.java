package graphicfiles;

import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.png.PngProcessingException;
import com.drew.imaging.tiff.TiffProcessingException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class GraphicFilesController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private TableView<ImageMetadata> tableView;
    
    @FXML
    private Label timeLabel;
    
    private DirectoryChooser directoryChooser;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {   
        processFiles();
    }

    public void processFiles() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File directory = directoryChooser.showDialog(stage);
        ObservableList<ImageMetadata> data = FXCollections.observableArrayList();
        File[] graphicFiles = directory.listFiles();

        long startTime = System.nanoTime();
        for (File file : graphicFiles) {
            ImageMetadata imageMetadata = new ImageMetadata();
            try {
                if (imageMetadata.initMetadataByFileType(file)) {
                    data.add(imageMetadata);
                }
            } 
            catch (IOException | JpegProcessingException | PngProcessingException | TiffProcessingException ex) {
                Logger.getLogger(GraphicFilesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        long endTime = System.nanoTime();
        timeLabel.setText(String.format("File processing time: %d ms\n", (endTime - startTime) / 1000000));
        tableView.setItems(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a directory");
    }      
}
