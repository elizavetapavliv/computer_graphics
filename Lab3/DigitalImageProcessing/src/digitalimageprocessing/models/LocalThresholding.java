package digitalimageprocessing.models;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class LocalThresholding {

    private final int neighbourWindowBound = 5;
    
    public BufferedImage filterImage(int type, BufferedImage originalImage) {
        if (type == 0) {
            return niblackFilter(originalImage);
        }
        return minMaxFilter(originalImage);
    }

    private BufferedImage minMaxFilter(BufferedImage originalImage) {
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        for (int i = neighbourWindowBound; i < width - neighbourWindowBound; i++) {
            for (int j = neighbourWindowBound; j < height - neighbourWindowBound; j++) {
                double min = 255;
                double max = 0;
                for (int a = -neighbourWindowBound; a <= neighbourWindowBound; a++) {
                    for (int b = -neighbourWindowBound; b <= neighbourWindowBound; b++) {
                        double luminance = getRGBLuminance(originalImage.getRGB(i + a, j + b));
                        min = min(min, luminance);
                        max = max(max, luminance);
                    }
                }
                double threshold = (min + max) / 2;
                if (getRGBLuminance(originalImage.getRGB(i, j))>= threshold) {
                    resultImage.setRGB(i, j, Color.WHITE.getRGB());
                } 
                else {
                    resultImage.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return resultImage;
    }

    private BufferedImage niblackFilter(BufferedImage originalImage){
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        int width  = originalImage.getWidth();
        int height = originalImage.getHeight();
        int neighbourWindowSize = 2 * neighbourWindowBound + 1;
        double k = -0.2;
         
        for (int i = neighbourWindowBound; i < width - neighbourWindowBound; i++) {
            for (int j = neighbourWindowBound; j < height - neighbourWindowBound; j++) {
                double mean  = 0;
                double[][] neighbourWindowLuminances = new double[neighbourWindowSize][neighbourWindowSize];
                for (int a = -neighbourWindowBound; a <= neighbourWindowBound; a++) {
                    for (int b = -neighbourWindowBound; b <= neighbourWindowBound; b++) {
                        double luminance = getRGBLuminance(originalImage.getRGB(i + a, j + b));
                        mean += luminance;
                        neighbourWindowLuminances[a + neighbourWindowBound][b + neighbourWindowBound] = luminance;
                    }
                }
                mean /= pow(neighbourWindowSize, 2);
                double standardDeviation = 0;
                for (int a = 0; a < neighbourWindowSize; a++) {
                    for (int b = 0; b < neighbourWindowSize; b++) {
                        standardDeviation += pow(neighbourWindowLuminances[a][b] - mean, 2);
                    }
                }
                standardDeviation = sqrt(standardDeviation / (neighbourWindowSize * neighbourWindowSize));
               
                if(neighbourWindowLuminances[neighbourWindowBound][neighbourWindowBound] > mean + k * standardDeviation){
                    resultImage.setRGB(i, j, Color.WHITE.getRGB());
                }
                else{
                    resultImage.setRGB(i, j, Color.BLACK.getRGB());
                }
            }           
        }
        return resultImage;
    }
    
    private double getRGBLuminance(int rgb) {
        Color color = new Color(rgb);
        return (color.getRed() * 0.2126 + color.getGreen() * 0.7152 + color.getBlue() * 0.0722);
    }
}