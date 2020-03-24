package digitalimageprocessing;

import digitalimageprocessing.models.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class DigitalImageProcessingController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private ImageView originalImageView, filteredImageView;
    
    @FXML
    private Label originalLabel, filteredLabel;
    
    @FXML
    private ComboBox filterComboBox;
        
    @FXML
    private ToggleGroup filterTypeGroup;
    
    @FXML
    private RadioButton highPassRadioButton, thresholdRadioButton;
    
    private FileChooser fileChooser;
    
    private BufferedImage originalImage;
    
    private HighPassFiltering highPassFiltering;
    
    private AdaptiveThresholding adaptiveThresholding;
    
    private LocalThresholding localThresholding;
    
    private final ObservableList<String> highPassFilters = FXCollections.observableArrayList(
            "Laplasian 1", "Laplasian 2", "Laplasian 3", "LoG");
    
    private final ObservableList<String> thresholdFilters = FXCollections.observableArrayList("Local 1", "Local 2", "Adaptive");
    
    private boolean isThresholding;
    
    @FXML
    private void handleFileButtonAction(ActionEvent event) {   
        try {
            processFile();
        } 
        catch (IOException ex) {
            Logger.getLogger(DigitalImageProcessingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleComboBoxAction(ActionEvent event) {   
        if(originalImage != null){ 
            filterImage();
        }
    }
    
    private void processFile() throws IOException{
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            originalImage = ImageIO.read(file);
            originalImageView.setImage(new Image(file.toURI().toString()));
            originalLabel.setVisible(true);
            
            filterImage();
        }
    }
    
    private void filterImage(){
        int selectedIndex = filterComboBox.getSelectionModel().getSelectedIndex();
        if(selectedIndex != -1){
            BufferedImage filteredImage = null;
            if(isThresholding){
                if(selectedIndex == 2){
                    filteredImage = adaptiveThresholding.filterImage(originalImage); 
                }
                else{
                    filteredImage = localThresholding.filterImage(selectedIndex, originalImage); 
                }
            }
            else{                 
                filteredImage = highPassFiltering.filterImage(selectedIndex, originalImage);        
            }
            filteredImageView.setImage(SwingFXUtils.toFXImage(filteredImage, null));
            filteredLabel.setVisible(true);
        }
    }
    
    private void changeFiltersList(Toggle toggle){    
        String filterType = ((RadioButton)toggle).getText();
        if(filterType.equals("Threshold")){
            filterComboBox.setItems(thresholdFilters);
            isThresholding = true;
        }
        else{
            filterComboBox.setItems(highPassFilters);
            isThresholding = false;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        
        highPassFiltering = new HighPassFiltering();
        adaptiveThresholding = new AdaptiveThresholding();
        localThresholding = new LocalThresholding();
        isThresholding = false;
        
        filterComboBox.setItems(highPassFilters);
        filterTypeGroup.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            changeFiltersList(filterTypeGroup.getSelectedToggle());
        });
        highPassRadioButton.setToggleGroup(filterTypeGroup);
        thresholdRadioButton.setToggleGroup(filterTypeGroup);
    }
}