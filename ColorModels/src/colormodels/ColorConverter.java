package colormodels;

public class ColorConverter {
    
    public double[] RGBtoXYZ(double[] rgb){
        double rN = RGBtoXYZfunction(rgb[0] / 255) * 100;
        double gN = RGBtoXYZfunction(rgb[1] / 255) * 100;
        double bN = RGBtoXYZfunction(rgb[2] / 255) * 100;
        double x = 0.412453 * rN + 0.357580 * gN + 0.180423 * bN;
        double y = 0.212671 * rN + 0.715160 * gN + 0.072169 * bN;
        double z = 0.019334 * rN + 0.119193 * gN + 0.950227 * bN;
        return new double[]{x, y, z};
    }
    
    private double RGBtoXYZfunction(double x){
        if(x >= 0.04045)
            return Math.pow((x + 0.055)/1.055, 2.4);
        return x / 12.92;
    }
    
    public double[] XYZtoRGB(double[] xyz){
        double x = xyz[0] / 100;
        double y = xyz[1] / 100;
        double z =  xyz[2] / 100;
            
        double rN = 3.2406 * x - 1.5372 * y - 0.4986 * z;
        double gN = -0.9689 * x + 1.8758 * y + 0.0415 * z;
        double bN = 0.0557 * x - 0.2040 * y + 1.0570 * z;

        double r = XYZtoRGBfunction(rN) * 255;
        double g = XYZtoRGBfunction(gN) * 255;
        double b = XYZtoRGBfunction(bN) * 255; 
        return new double[]{r, g, b};
    }
    
    private double XYZtoRGBfunction(double x){
        if(x >= 0.0031308)
            return 1.055 * Math.pow(x, 1/2.4) - 0.055;
        return 12.92 * x;
    }
    
    public double[] RGBtoCMYK(double[] rgb){
        double rN = 1 - rgb[0] / 255;
        double gN = 1 - rgb[1] / 255;
        double bN = 1 - rgb[2] / 255;     
        double k = Math.min(Math.min(rN, gN), bN);
        if(k == 1.0){
            return new double[]{0.0, 0.0, 0.0, k};
        }
        double c = (rN - k) / (1 - k);
        double m = (gN - k) / (1 - k);
        double y = (bN - k) / (1 - k);
        return new double[]{c, m, y, k};
    }
    
    public double[] CMYKtoRGB(double[] cmyk){
        double r = 255 * (1 - cmyk[0]) * (1 - cmyk[3]);
        double g = 255 * (1 - cmyk[1]) * (1 - cmyk[3]);
        double b = 255 * (1 - cmyk[2]) * (1 - cmyk[3]);
        return new double[]{r, g, b};
    }
    
    public double[] XYZtoCMYK(double[] xyz){
        double[] rgb = XYZtoRGB(xyz);
        checkRGBvalues(rgb);
        return RGBtoCMYK(rgb);
    }
    
    public double[] CMYKtoXYZ(double[] cmyk){
        double[] rgb = CMYKtoRGB(cmyk);
        checkRGBvalues(rgb);
        return RGBtoXYZ(rgb);
    }
    
    public String checkRGBvalues(double[] rgb){
         String message = "";
        
        if (rgb[0] < 0) {
            rgb[0] = 0;
            message = "R was less than 0 and clipped to 0";        
        }
        else if (rgb[0] > 255) {
            rgb[0] = 255;
            message = "R was greater than 255 and clipped to 255";
        }

        if (rgb[1] < 0) {
            rgb[1] = 0;
            message = "G was less than 0 and clipped to 0";
        }
        else if (rgb[1] > 255) {
            rgb[1] = 255;
            message = "G was greater than 255 and clipped to 255";
        }

        if (rgb[2] < 0) {
            rgb[2] = 0;
            message = "B was less than 0 and clipped to 0";
        }
        else if (rgb[2] > 255) {
            rgb[2] = 255;
            message = "B was greater than 255 and clipped to 255";
        }
        return message;
    }
    
    public String checkXYZvalues(double[] xyz){
         String message = "";
        
        if (xyz[0] < 0) {
            xyz[0] = 0;
            message = "X was less than 0 and clipped to 0";        
        }
        else if (xyz[0] >  95.047) {
            xyz[0] =  95.047;
            message = "X was greater than 95.047 and clipped to  95.047";
        }

        if (xyz[1] < 0) {
            xyz[1] = 0;
            message = "Y was less than 0 and clipped to 0";
        }
        else if (xyz[1] > 100) {
            xyz[1] = 100;
            message = "Y was greater than 100 and clipped to 100";
        }

        if (xyz[2] < 0) {
            xyz[2] = 0;
            message = "Z was less than 0 and clipped to 0";
        }
        else if (xyz[2] > 108.883) {
            xyz[2] = 108.883;
            message = "Z was greater than 108.883 and clipped to 108.883";
        }
        return message;
    }
    
    public String checkCMYKvalues(double[] cmyk){
         String message = "";
        
        if (cmyk[0] < 0) {
            cmyk[0] = 0;
            message = "C was less than 0 and clipped to 0";        
        }
        else if (cmyk[0] > 1) {
            cmyk[0] = 1;
            message = "C was greater than 100 and clipped to 100";
        }

        if (cmyk[1] < 0) {
            cmyk[1] = 0;
            message = "M was less than 0 and clipped to 0";
        }
        else if (cmyk[1] > 1) {
            cmyk[1] = 1;
            message = "M was greater than 100 and clipped to 100";
        }

        if (cmyk[2] < 0) {
            cmyk[2] = 0;
            message = "Y was less than 0 and clipped to 0";
        }
        else if (cmyk[2] > 1) {
            cmyk[2] = 1;
            message = "Y was greater than 100 and clipped to 100";
        }
        
        if (cmyk[3] < 0) {
            cmyk[3] = 0;
            message = "K was less than 0 and clipped to 0";
        }
        else if (cmyk[3] > 1) {
            cmyk[3] = 1;
            message = "K was greater than 100 and clipped to 100";
        }
        return message;
    }
}
