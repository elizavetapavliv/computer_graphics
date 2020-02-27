package colormodels;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ColorModelsController implements Initializable {
    
    private ColorConverter converter;
    
    @FXML
    private ColorPicker colorPicker;
    
    @FXML
    private TextField rText, gText, bText;
    
    @FXML
    private Slider rSlider, gSlider, bSlider;

    @FXML
    private TextField xText, yText1, zText;
    
    @FXML
    private Slider xSlider, ySlider1, zSlider;

    @FXML
    private TextField cText, mText, yText2, kText;
    
    @FXML
    private Slider cSlider, mSlider, ySlider2, kSlider;
    
    @FXML
    private Label notificationText;

    @FXML
    private void handleRGBAction(ActionEvent event) {
        recalculateFromRGBFields();
    }

    @FXML
    private void handleXYZAction(ActionEvent event) {
        recalculateFromXYZFields();
    }

    @FXML
    private void handleCMYKAction(ActionEvent event) {
        recalculateFromCMYKFields();
    }
    
    @FXML
    private void handleRGBSliderEvent(MouseEvent event) {
        recalculateFromRGBSliders();
    }

    @FXML
    private void handleXYZSliderEvent(MouseEvent event) {
        recalculateFromXYZSliders();
    }

    @FXML
    private void handleCMYKSliderEvent(MouseEvent event) {
        recalculateFromCMYKSliders();
    }
    
     @FXML
    private void handlePickerAction(ActionEvent event) {
        recalculateFromPicker();
    }
    
    private void recalculateFromPicker(){
        notificationText.setText("");
        Color color = colorPicker.getValue();
        double r = 255*color.getRed();
        double g = 255*color.getGreen();
        double b = 255*color.getBlue();
        
        double[] rgb = new double[] {r, g, b};
        setRGBFieldValues(rgb, true);
        double[] xyz = converter.RGBtoXYZ(rgb);
        setXYZFieldValues(xyz, true);
        double[] cmyk = converter.RGBtoCMYK(rgb);
        setCMYKFieldValues(cmyk, true);
        
        setRGBSliderValues(rgb);
        setXYZSliderValues(xyz);
        setCMYKSliderValues(cmyk);
    } 
    
    private void recalculateFromRGBSliders(){
        notificationText.setText("");
        double[] rgb = new double[]{rSlider.getValue(), gSlider.getValue(), bSlider.getValue()};
        setRGBFieldInFormat(rgb);
        double[] xyz = converter.RGBtoXYZ(rgb);
        setXYZFieldValues(xyz, true);
        double[] cmyk = converter.RGBtoCMYK(rgb);
        setCMYKFieldValues(cmyk, true); 
        
        setColorPickerValue(rgb);
        
        setXYZSliderValues(xyz);
        setCMYKSliderValues(cmyk);
    }
    
    private void recalculateFromXYZSliders(){
        notificationText.setText("");
        double[] xyz = new double[]{xSlider.getValue(), ySlider1.getValue(), zSlider.getValue()};
        setXYZFieldInFormat(xyz);
        double[] rgb = converter.XYZtoRGB(xyz);
        setRGBFieldValues(rgb, true);
        double[] cmyk = converter.XYZtoCMYK(xyz);
        setCMYKFieldValues(cmyk, true);
        
        setColorPickerValue(rgb);  
        
        setRGBSliderValues(rgb);
        setCMYKSliderValues(cmyk);     
    }
    
    private void recalculateFromCMYKSliders(){  
        notificationText.setText("");
        double[] cmyk = new double[]{cSlider.getValue() / 100, mSlider.getValue() / 100, 
            ySlider2.getValue() / 100, kSlider.getValue() / 100};
        setCMYKFieldInFormat(cmyk);
        double[] xyz = converter.CMYKtoXYZ(cmyk);
         setXYZFieldValues(xyz, true);
        double[] rgb = converter.CMYKtoRGB(cmyk);
        setRGBFieldValues(rgb, true);
        
        setColorPickerValue(rgb);
        
        setRGBSliderValues(rgb);
        setXYZSliderValues(xyz);     
    }
    
    private void setRGBSliderValues(double[] rgb){
        rSlider.setValue(rgb[0]);
        gSlider.setValue(rgb[1]);
        bSlider.setValue(rgb[2]);
    }
    
    private void setXYZSliderValues(double[] xyz){
        xSlider.setValue(xyz[0]);
        ySlider1.setValue(xyz[1]);
        zSlider.setValue(xyz[2]);
    }
    
    private void setCMYKSliderValues(double[] cmyk){
        cSlider.setValue(cmyk[0] * 100);
        mSlider.setValue(cmyk[1] * 100);
        ySlider2.setValue(cmyk[2] * 100);
        kSlider.setValue(cmyk[3] * 100);
    }
    
    private void recalculateFromRGBFields(){
        notificationText.setText("");
        double r = 0;
        double g = 0;
        double b = 0;
        try{
            r = Double.parseDouble(rText.getText());
        }
        catch(NumberFormatException e){
            notificationText.setText("R value must be a number. Changed to 0");
            rText.setText("0");
        }
        try{
            g = Double.parseDouble(gText.getText());
        }
        catch(NumberFormatException e){
            notificationText.setText("G value must be a number. Changed to 0");
            gText.setText("0");
        }
        try{
            b = Double.parseDouble(bText.getText());
        }
        catch(NumberFormatException e){
            notificationText.setText("B value must be a number. Changed to 0");
            bText.setText("0");
        } 
         
        double[] rgb = new double[]{r, g, b};
        setRGBFieldValues(rgb, false);
        setColorPickerValue(rgb);
        double[] xyz = converter.RGBtoXYZ(rgb);      
        setXYZFieldValues(xyz, true);
        double[] cmyk = converter.RGBtoCMYK(rgb);       
        setCMYKFieldValues(cmyk, true);
        
        setRGBSliderValues(rgb);
        setXYZSliderValues(xyz);
        setCMYKSliderValues(cmyk);
    }
   
    private void recalculateFromXYZFields(){  
        notificationText.setText("");      
        double x = 0;
        double y = 0;
        double z = 0;
        try{
            x = Double.parseDouble(xText.getText());
        }
        catch(NumberFormatException e){
            notificationText.setText("X value must be a number. Changed to 0");
            xText.setText("0");
        }
        try{
            y = Double.parseDouble(yText1.getText());
        }
        catch(NumberFormatException e){
            notificationText.setText("Y value must be a number. Changed to 0");
            yText1.setText("0");
        }
        try{
            z = Double.parseDouble(zText.getText());
        }
        catch(NumberFormatException e){
            notificationText.setText("Z value must be a number. Changed to 0");
            zText.setText("0");
        } 
        
        double[] xyz = new double[]{x, y, z};
        setXYZFieldValues(xyz, false);
        double[] rgb = converter.XYZtoRGB(xyz);
        setRGBFieldValues(rgb, true);
        setColorPickerValue(rgb);
        double[] cmyk = converter.XYZtoCMYK(xyz);
        setCMYKFieldValues(cmyk, true);
        
        setRGBSliderValues(rgb);
        setXYZSliderValues(xyz);
        setCMYKSliderValues(cmyk);
    }
    
    private void recalculateFromCMYKFields(){  
        notificationText.setText("");
        double c = 0;
        double m = 0;
        double y = 0;
        double k = 0;
        try{
            c = Double.parseDouble(cText.getText()) / 100;
        }
        catch(NumberFormatException e){
            notificationText.setText("C value must be a number. Changed to 0");
            cText.setText("0");
        }
        try{
            m = Double.parseDouble(mText.getText()) / 100;
        }
        catch(NumberFormatException e){
            notificationText.setText("M value must be a number. Changed to 0");
            mText.setText("0");
        }
        try{
            y = Double.parseDouble(yText2.getText()) / 100;
        }
        catch(NumberFormatException e){
            notificationText.setText("Y value must be a number. Changed to 0");
            yText2.setText("0");
        } 
        try{
            k = Double.parseDouble(kText.getText()) / 100;
        }
        catch(NumberFormatException e){
            notificationText.setText("K value must be a number. Changed to 0");
            kText.setText("0");
        } 
        double[] cmyk = new double[]{c, m, y, k};
        setCMYKFieldValues(cmyk, false);
        double[] rgb = converter.CMYKtoRGB(cmyk);
        setRGBFieldValues(rgb, true);
        setColorPickerValue(rgb);
        double[] xyz = converter.CMYKtoXYZ(cmyk);    
        setXYZFieldValues(xyz, true);
        
        setRGBSliderValues(rgb);
        setXYZSliderValues(xyz);
        setCMYKSliderValues(cmyk);
    }
    
    private void setRGBFieldValues( double[] rgb, boolean needToChange){   
        String message = converter.checkRGBvalues(rgb);
        if (!message.equals("")) {
            notificationText.setText(message);
            needToChange = true;
        }
        if (needToChange) {
           setRGBFieldInFormat(rgb);
        }
    }
    
    private void setXYZFieldValues(double[] xyz, boolean needToChange){
        String message = converter.checkXYZvalues(xyz);
        if (!message.equals("")) {
            notificationText.setText(message);
            needToChange = true;
        }
        if (needToChange) {
           setXYZFieldInFormat(xyz);
        }
    }
    
    private void setCMYKFieldValues( double[] cmyk, boolean needToChange){
        String message = converter.checkCMYKvalues(cmyk);
        if (!message.equals("")) {
            notificationText.setText(message);
            needToChange = true;
        }
        if (needToChange) {
            setCMYKFieldInFormat(cmyk);
        }
    }
    
    private void setRGBFieldInFormat(double[] rgb) {
        rText.setText(String.format("%.0f", rgb[0]));
        gText.setText(String.format("%.0f", rgb[1]));
        bText.setText(String.format("%.0f", rgb[2]));
    }
    
    private void setXYZFieldInFormat(double[] xyz) {
        xText.setText(String.format("%.0f", xyz[0]));
        yText1.setText(String.format("%.0f", xyz[1]));
        zText.setText(String.format("%.0f", xyz[2]));
    }
    
    private void setCMYKFieldInFormat(double[]cmyk){
        cText.setText(String.format("%.0f", cmyk[0] * 100));
        mText.setText(String.format("%.0f", cmyk[1] * 100));
        yText2.setText(String.format("%.0f", cmyk[2] * 100));
        kText.setText(String.format("%.0f", cmyk[3] * 100));
    } 
    
    private void setColorPickerValue(double[] rgb){
        colorPicker.setValue(Color.rgb((int)rgb[0], (int)rgb[1], (int)rgb[2]));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        converter = new ColorConverter();
        recalculateFromPicker();
        
        ChangeListener<Boolean> rgbListener = (ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) -> {
            if(!newVal)
                recalculateFromRGBFields();
        };
        rText.focusedProperty().addListener(rgbListener);
        gText.focusedProperty().addListener(rgbListener);
        bText.focusedProperty().addListener(rgbListener);
       
        ChangeListener<Boolean> xyzListener = (ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) -> {
            if(!newVal)
                 recalculateFromXYZFields();
        };
        xText.focusedProperty().addListener(xyzListener);
        yText1.focusedProperty().addListener(xyzListener);
        zText.focusedProperty().addListener(xyzListener);
        
        ChangeListener<Boolean> cmykListener = (ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) -> {
            if(!newVal)
                 recalculateFromCMYKFields();
        };
        cText.focusedProperty().addListener(cmykListener);
        mText.focusedProperty().addListener(cmykListener);
        yText2.focusedProperty().addListener(cmykListener);
        kText.focusedProperty().addListener(cmykListener);     
    }
}