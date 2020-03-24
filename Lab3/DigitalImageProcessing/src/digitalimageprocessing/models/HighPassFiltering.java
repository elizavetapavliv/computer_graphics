package digitalimageprocessing.models;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import static java.awt.image.ConvolveOp.EDGE_NO_OP;
import java.awt.image.Kernel;

public class HighPassFiltering {
    
    private final float[] filter1 = {-1, -1, -1, -1, 9, -1, -1, -1, -1};
    
    private final float[] filter2 = {1, -2, 1, -2, 5, -2, 1, -2, 1};
    
    private final float[] filter3 = {0, -1, 0, -1, 5, -1, 0, -1, 0};
    
    private final float[] filter4 = {0, 0, -1, 0, 0, 0, -1, -2, -1, 0, -1, -2, 17, -2, -1, 0, -1, -2, -1, 0, 0, 0, -1, 0, 0};
      
    private float[] currentFilter;
    
    public BufferedImage filterImage(int filterNum, BufferedImage originalImage){
        BufferedImage resultImage = null;
        switch(filterNum){
            case 0:
                currentFilter = filter1;
                resultImage = filterImage(originalImage);
                break;
            case 1:
                currentFilter = filter2;
                resultImage = filterImage(originalImage);
                break;  
            case 2:
                currentFilter = filter3;
                resultImage = filterImage(originalImage);
                break;  
            case 3:
                currentFilter = filter4;
                resultImage = filterImage(originalImage); 
                break;      
        }
        return resultImage;
    }
    
    private BufferedImage filterImage(BufferedImage original){
        int length = (int)Math.sqrt(currentFilter.length);
        BufferedImageOp conOp = new ConvolveOp(new Kernel(length, length, currentFilter), EDGE_NO_OP, null);
        return conOp.filter(original, null);
    }
}