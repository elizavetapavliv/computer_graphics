package graphicfiles;

import com.drew.imaging.bmp.BmpMetadataReader;
import com.drew.imaging.gif.GifMetadataReader;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.pcx.PcxMetadataReader;
import com.drew.imaging.png.PngMetadataReader;
import com.drew.imaging.png.PngProcessingException;
import com.drew.imaging.tiff.TiffMetadataReader;
import com.drew.imaging.tiff.TiffProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.bmp.BmpHeaderDescriptor;
import com.drew.metadata.bmp.BmpHeaderDirectory;
import com.drew.metadata.exif.ExifIFD0Descriptor;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.gif.GifHeaderDirectory;
import com.drew.metadata.jfif.JfifDescriptor;
import com.drew.metadata.jfif.JfifDirectory;
import com.drew.metadata.jpeg.JpegDescriptor;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.pcx.PcxDirectory;
import com.drew.metadata.png.PngDescriptor;
import com.drew.metadata.png.PngDirectory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageMetadata {

    private String fileName;
    private String size;
    private String resolution;
    private int bitDepth;
    private String compressionType;
    
    public boolean initMetadataByFileType(File file) throws IOException, JpegProcessingException, PngProcessingException, TiffProcessingException {
        Path filePath = file.toPath();
        String mime;
        try {
            mime = Files.probeContentType(filePath);
        } 
        catch (IOException ex) {
            return false;
        }
        if (mime == null) {
            String filePathLowerCase = filePath.toString().toLowerCase();
            if (filePathLowerCase.endsWith(".pcx") || filePathLowerCase.endsWith(".pcc")) {
                initPcxMetadata(file);
            } 
            else {
                return false;
            }
        } 
        else {
            switch (mime) {
                case "image/jpeg":
                    initJpegMetadata(file);
                    break;
                case "image/png":
                    initPngMetadata(file);
                    break;
                case "image/gif":
                    initGifMetadata(file);
                    break;
                case "image/bmp":
                    initBmpMetadata(file);
                    break;
                case "image/tiff":
                    initTiffMetadata(file);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private void initJpegMetadata(File jpegFile) throws JpegProcessingException, IOException{
        fileName = jpegFile.getName();
        Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);      
        JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
        try {
            size = jpegDirectory.getInt(JpegDirectory.TAG_IMAGE_WIDTH) + " x " + jpegDirectory.getInt(JpegDirectory.TAG_IMAGE_HEIGHT);
            bitDepth = jpegDirectory.getInt(JpegDirectory.TAG_DATA_PRECISION) * jpegDirectory.getInt(JpegDirectory.TAG_NUMBER_OF_COMPONENTS);
            getJpegResolution(metadata);
        } catch (MetadataException ex) {
            Logger.getLogger(ImageMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
        String compression = new JpegDescriptor(jpegDirectory).getDescription(JpegDirectory.TAG_COMPRESSION_TYPE);
        compressionType = compression.equals("Baseline") ? "JPEG" : compression;
    }

    private void getJpegResolution(Metadata metadata) {
        JfifDirectory jfifDirectory = metadata.getFirstDirectoryOfType(JfifDirectory.class);
        if (jfifDirectory != null) {
            String description = (new JfifDescriptor(jfifDirectory)).getDescription(JfifDirectory.TAG_UNITS);
            try{
                if (description != null && description.equals("centimetre")) {
                    getDpiResolutionFromMeter(jfifDirectory.getInt(JfifDirectory.TAG_RESX),
                            jfifDirectory.getInt(JfifDirectory.TAG_RESY));
                } 
                else {
                    resolution = "horizontal: " + jfifDirectory.getInt(JfifDirectory.TAG_RESX)
                            + "\nvertical: " + jfifDirectory.getInt(JfifDirectory.TAG_RESY);
                }
            }
            catch(MetadataException ex){}
        }
        else{
            getExifResolution(metadata);
        }
    }
        
    private void getExifResolution(Metadata metadata){
        ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (exifDirectory != null) {
            String description = (new ExifIFD0Descriptor(exifDirectory)).getDescription(ExifIFD0Directory.TAG_RESOLUTION_UNIT);
            try {
                if (description != null && description.equals("cm")) {
                    getDpiResolutionFromMeter(exifDirectory.getInt(ExifIFD0Directory.TAG_X_RESOLUTION),
                            exifDirectory.getInt(ExifIFD0Directory.TAG_Y_RESOLUTION));
                } 
                else {
                    resolution = "horizontal: " + exifDirectory.getInt(ExifIFD0Directory.TAG_X_RESOLUTION)
                            + "\nvertical: " + exifDirectory.getInt(ExifIFD0Directory.TAG_Y_RESOLUTION);
                }
            } 
            catch (MetadataException ex) { }
        }
    }
  
    private void getDpiResolutionFromMeter(int x, int y){
       resolution = "horizontal: " + String.format("%.0f", x * 0.0254)
                    + "\nvertical: " + String.format("%.0f", y * 0.0254);
    }

    private void initTiffMetadata(File tiffFile) throws IOException, TiffProcessingException {
        fileName = tiffFile.getName();     
        Metadata metadata = TiffMetadataReader.readMetadata(tiffFile);
        ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        try {
            size = exifDirectory.getInt(ExifIFD0Directory.TAG_IMAGE_WIDTH) + " x " + exifDirectory.getInt(ExifIFD0Directory.TAG_IMAGE_HEIGHT);
            getExifResolution(metadata);
            int[] bitsPerSample = exifDirectory.getIntArray(ExifIFD0Directory.TAG_BITS_PER_SAMPLE);
            for(int bit: bitsPerSample){
                bitDepth += bit;
            }
        } 
        catch (MetadataException ex) {
            Logger.getLogger(ImageMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
        compressionType = new ExifIFD0Descriptor(exifDirectory).getDescription(ExifIFD0Directory.TAG_COMPRESSION);
    }

    private void initBmpMetadata(File bmpFile) throws IOException {  
        fileName = bmpFile.getName();
        Metadata metadata = BmpMetadataReader.readMetadata(bmpFile);
        BmpHeaderDirectory bmpDirectory = metadata.getFirstDirectoryOfType(BmpHeaderDirectory.class);
        try {
            size = bmpDirectory.getInt(BmpHeaderDirectory.TAG_IMAGE_WIDTH) + " x " + bmpDirectory.getInt(BmpHeaderDirectory.TAG_IMAGE_HEIGHT);
            bitDepth = bmpDirectory.getInt(BmpHeaderDirectory.TAG_BITS_PER_PIXEL);
            getDpiResolutionFromCentimeter( bmpDirectory.getInt(BmpHeaderDirectory.TAG_X_PIXELS_PER_METER), 
                    bmpDirectory.getInt(BmpHeaderDirectory.TAG_Y_PIXELS_PER_METER));
        } 
        catch (MetadataException ex) {
            Logger.getLogger(ImageMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
        compressionType = new BmpHeaderDescriptor(bmpDirectory).getDescription(BmpHeaderDirectory.TAG_COMPRESSION);
    }
    
    private void getDpiResolutionFromCentimeter(int x, int y) {
        resolution = "horizontal: " + String.format("%.0f", x * 2.54)
                + "\nvertical: " + String.format("%.0f", y * 2.54);
    }
    
    private void initGifMetadata(File gifFile) throws IOException {
        fileName = gifFile.getName();
        Metadata metadata = GifMetadataReader.readMetadata(gifFile);
        GifHeaderDirectory gifDirectory = metadata.getFirstDirectoryOfType(GifHeaderDirectory.class);
        try {
            size = gifDirectory.getInt(GifHeaderDirectory.TAG_IMAGE_WIDTH) + " x " + gifDirectory.getInt(GifHeaderDirectory.TAG_IMAGE_HEIGHT);
            bitDepth = gifDirectory.getInt(GifHeaderDirectory.TAG_BITS_PER_PIXEL);         
        } 
        catch (MetadataException ex) {
            Logger.getLogger(ImageMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }  
        compressionType = "LZW";
        resolution = "-";
    }
     
    private void initPngMetadata(File pngFile) throws PngProcessingException, IOException {
        fileName = pngFile.getName();     
        Metadata metadata = PngMetadataReader.readMetadata(pngFile);
        PngDirectory pngDirectory = metadata.getFirstDirectoryOfType(PngDirectory.class);
        try {
            size = pngDirectory.getInt(PngDirectory.TAG_IMAGE_WIDTH) + " x " + pngDirectory.getInt(PngDirectory.TAG_IMAGE_HEIGHT);
            bitDepth = pngDirectory.getInt(PngDirectory.TAG_BITS_PER_SAMPLE);
            getPngResolution(metadata);       
        } 
        catch (MetadataException ex) {
            Logger.getLogger(ImageMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }  
        compressionType = "Deflate";
    } 
    
    private void getPngResolution(Metadata metadata) {
        Iterator<PngDirectory> iterator = metadata.getDirectoriesOfType(PngDirectory.class).iterator();
        PngDirectory pngDirectory = null;
        while (iterator.hasNext()) {
            pngDirectory = iterator.next();
        }
        if (pngDirectory != null) {
            try {
                String description = new PngDescriptor(pngDirectory).getDescription(PngDirectory.TAG_UNIT_SPECIFIER);
                if (description != null && description.equals("Metres")) {
                    getDpiResolutionFromMeter(pngDirectory.getInt(PngDirectory.TAG_PIXELS_PER_UNIT_X),
                            pngDirectory.getInt(PngDirectory.TAG_PIXELS_PER_UNIT_Y));
                } 
                else {
                    resolution = "horizontal: " + pngDirectory.getInt(PngDirectory.TAG_PIXELS_PER_UNIT_X)
                            + "\nvertical: " + pngDirectory.getInt(PngDirectory.TAG_PIXELS_PER_UNIT_Y);
                }
            } 
            catch (MetadataException ex) {}
        }
    }
     
    private void initPcxMetadata(File pcxFile) throws IOException {
        fileName = pcxFile.getName();
        Metadata metadata =PcxMetadataReader.readMetadata(pcxFile);
        PcxDirectory pcxDirectory = metadata.getFirstDirectoryOfType(PcxDirectory.class);
        try {
            size = Integer.toString(pcxDirectory.getInt(PcxDirectory.TAG_XMAX) - pcxDirectory.getInt(PcxDirectory.TAG_XMIN) + 1) + " x "
                    + Integer.toString(pcxDirectory.getInt(PcxDirectory.TAG_YMAX) - pcxDirectory.getInt(PcxDirectory.TAG_YMIN) + 1);
            bitDepth = pcxDirectory.getInt(PcxDirectory.TAG_BITS_PER_PIXEL);
            resolution = "horizontal: " + pcxDirectory.getInt(PcxDirectory.TAG_HORIZONTAL_DPI)
                    + "\nvertical: " + pcxDirectory.getInt(PcxDirectory.TAG_VERTICAL_DPI);        
        }
        catch (MetadataException ex) {
            Logger.getLogger(ImageMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
        compressionType = "RLE"; 
    } 

    public String getFileName() {
        return fileName;
    }

    public String getSize() {
        return size;
    }

    public String getResolution() {
        return resolution;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public String getCompressionType() {
        return compressionType;
    }
}