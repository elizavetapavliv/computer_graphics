package digitalimageprocessing.models;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;

public class AdaptiveThresholding {

    private final int neighbourWindowBound = 9;
    private final int neighbourWindowSize = 2 * neighbourWindowBound + 1;
    private final double alpha = 1.0 / 3;
    private BufferedImage originalImage;
    private double[][] localMeans, luminances;
    private int width, height;
    
    private double calculateLocalMean(int i, int j){
        double z = 0;
        double count = pow(neighbourWindowSize, 2);
        for (int a = -neighbourWindowBound; a <= neighbourWindowBound; a++) {
            for (int b = -neighbourWindowBound; b <= neighbourWindowBound; b++) {
                if(a + i >= 0 && a + i < width){
                    if(b + j >= 0 && b + j < height){
                       double luminance = getRGBLuminance(originalImage.getRGB(i + a, j + b));
                       z += luminance;
                       luminances[i + a][j + b] = luminance;
                    }
                    else{
                        count--;
                    }
                }
                else{
                    count--;
                }
            }
        }
        z /= count;
        return z;
    }
       
    private void calculateAllLocalMeans(){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                localMeans[i][j] = calculateLocalMean(i, j);
            }
        }
    }

    public BufferedImage filterImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        width = originalImage.getWidth();
        height = originalImage.getHeight();
        localMeans = new double[width][height];
        luminances = new double[width][height];
        calculateAllLocalMeans();
        for (int i = neighbourWindowBound; i < width - neighbourWindowBound; i++) {
            for (int j = neighbourWindowBound; j < height - neighbourWindowBound; j++) {
                double min = 255;
                double max = 0;
                for (int a = -neighbourWindowBound; a <= neighbourWindowBound; a++) {
                    for (int b = -neighbourWindowBound; b <= neighbourWindowBound; b++) {
                        double luminance = luminances[i + a][j + b];
                        min = min(min, luminance);
                        max = max(max, luminance);
                    }
                }
                double z = localMeans[i][j];
                double dMin = abs(min - z);
                double dMax = abs(max - z);
                double threshold;
                if (dMax > dMin) {
                    threshold = alpha * (2.0 * min + 1.0 * z) / 3;
                } 
                else {
                    threshold = alpha * (1.0 * min + 2.0 * z) / 3;
                }
                boolean isBlack = true;
                for (int a = -neighbourWindowBound; a <= neighbourWindowBound; a++) {
                    for (int b = -neighbourWindowBound; b <= neighbourWindowBound; b++) {
                        if (!(a == 0 && b == 0)) {
                            if (abs(localMeans[i + a][j + b] - luminances[i][j]) <= threshold) {
                                isBlack = false;
                                break;
                            }
                        }
                    }
                }
                if (isBlack) {
                    resultImage.setRGB(i, j, Color.BLACK.getRGB());
                } 
                else {
                    resultImage.setRGB(i, j, Color.WHITE.getRGB());
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