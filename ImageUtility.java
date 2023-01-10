import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageUtility
{
    public static int COLOR_WHITE = 0xffffffff;
    public static int COLOR_BLACK = 0xff000000;
     
    public static Image readImage(File file)
    {
        //read from image file into a buffered-image
        BufferedImage bimage;
        //bimage.setType();
        try{
            bimage = ImageIO.read(file);
        }
        catch(IOException e)
        {
            System.out.println("Image file not found or not readable");
            return null;
        }
        
        //wrap the buffered-image by a new Image object to store the read image 
        Image image = getImage(bimage);
        return image;
    }
    public static Image readImage(String filepath)
    {
        File file = new File(filepath);
        return readImage(file);
    }
    public static Image readImageInGrayScale(File file)
    {
        Image image = readImage(file);
        convertRGB2gray(image);
        return image;
    }
    public static Image readImageInGrayScale(String filepath)
    {
        File file = new File(filepath);
        return readImageInGrayScale(file);
    }
    
    public static void writeImage(BufferedImage bimage, File imagefile, String format)
    {
        try{
            ImageIO.write(bimage, format, imagefile);
        }
        catch(IOException e){
            System.out.println("Couldnot save image to the file");
        }
    }
    
    public static void writeImage(BufferedImage bimage, String filename)
    {
        
        String format = filename.substring(filename.length() - 3);
        if(!format.equals("png") && !format.equals("jpg"))
        {
            System.out.println("Unsupported format for saving image");
            return;
        }
        
        File out = new File(filename);
        writeImage(bimage, out, format);
    }
    
    public static void writeImage(Image image, File imagefile, String format)
    {
        BufferedImage bimage = getBufferedImage(image);
        writeImage(bimage, imagefile, format);
    }
    
    public static void writeImage(Image image, String filename)
    {
        BufferedImage bimage = getBufferedImage(image);
        writeImage(bimage, filename);
    }
    
    /*public static void writeBinarizedImage(Image image, int textColor, int bgColor, String filename)
    {
        
    }
    
    public static void writeBinarizedImage(Image image, String filename)
    {
        writeBinarizedImage(image, COLOR_BLACK, COLOR_WHITE, filename);
    }*/
    
    
    public static Image getImage(BufferedImage bimage)
    {
        //wrap the buffered-image by a new Image object to store the read image 
        //the type of image is gray if buffered image is gary and rgb if buffered image is rgb else null is returned
        if(bimage == null)
        {
            System.out.println("Cannot create Image object. Buffered image null.");
            return null;
        }
        Image image = new Image(bimage.getWidth(), bimage.getHeight());
        
        
        if(bimage.getType() == BufferedImage.TYPE_BYTE_GRAY)
        {
            image.setType(Image.TYPE.GRAY);
            //assuming buffered image is of type RGB else sepearte method is needed
            for(int i=0; i<image.getWidth(); i++)
                for(int j=0; j<image.getHeight(); j++)
                    image.pixel[i][j] = bimage.getRGB(i, j) & 0x000000ff; //only take the last byte value
        }
        else //(bimage.getType() == BufferedImage.TYPE_INT_RGB || bimage.getType() == BufferedImage.TYPE_INT_ARGB)
        {
            image.setType(Image.TYPE.RGB);
            //assuming buffered image is of type RGB else sepearte method is needed
            for(int i=0; i<image.getWidth(); i++)
                for(int j=0; j<image.getHeight(); j++)
                    image.pixel[i][j] = bimage.getRGB(i, j);
        }
        /*else
        {
            System.out.println("Cannot create Image object. Buffered image type not supported.");
            return null;
        }*/
        return image;
    }
    
    public static BufferedImage getBufferedImage(Image image)
    {
        if(image == null)
        {
            System.out.println("Cannot create BufferedImage object.Image object null.");
            return null;
        }
        
        BufferedImage bimage = null;
        
        if(image.getType() == Image.TYPE.RGB)
        {
            bimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
           
            for(int i=0; i<image.getWidth(); i++)
                for(int j=0; j<image.getHeight(); j++)
                    bimage.setRGB(i, j, image.pixel[i][j] );
        }
        else if (image.getType() == Image.TYPE.GRAY)
        {
            bimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
           
            for(int i=0; i<image.getWidth(); i++)
                for(int j=0; j<image.getHeight(); j++)
                {
                    int pix_byte = image.pixel[i][j] | 0x000000ff;  //only the last byte of gray image is needed
                    int pix = (pix_byte<<16) | (pix_byte<<8) | pix_byte | 0xff000000;
                    bimage.setRGB(i, j, pix);
                }
        }
        else if (image.getType() == Image.TYPE.BIN)
        {
            bimage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
           
            for(int i=0; i<image.getWidth(); i++)
                for(int j=0; j<image.getHeight(); j++)
                {
                    int pix;
                    if (image.pixel[i][j] == 0)
                        pix = COLOR_WHITE;
                    else
                        pix = COLOR_BLACK;   //1 in binarized image represents text
                    bimage.setRGB(i, j, pix);
                }
        }
                
        return bimage;
    }
    
    public static void convertRGB2gray(Image image)
    {
        if(image.getType() == Image.TYPE.GRAY)
            return;  //image already in grayscale type
        /*else if (image.getType() == Image.TYPE.BIN)
        {
            image.setType(Image.TYPE.GRAY);
            for(int i=0; i<image.getWidth(); i++)
                for(int j=0; j<image.getHeight(); j++)
                {
                    if(image.pixel[i][j] == 0)
                        image.pixel[i][j] = 0xff;  //background pixel
                    else
                        image.pixel[i][j] = 0;  //text pixel
                }
        }*/
        
        image.setType(Image.TYPE.GRAY);
        for(int i=0; i<image.getWidth(); i++)
            for(int j=0; j<image.getHeight(); j++)
            {
                int pix;
                int r,g,b, gray;
                pix = image.pixel[i][j];
                r = ((pix>>16) & 0x000000ff);
                g = ((pix>>8) & 0x000000ff);
                b = ((pix) & 0x000000ff);
                gray = (int)(0.33 * r + 0.33 * g + 0.33 * b);    //grayscale conversion
                image.pixel[i][j] = gray;
            }
    }
    
    public static ArrayList<File> getAllFilesInDirectory(String directoryName)
    {
        ArrayList<File> files = new ArrayList<File>();
        getAllFilesInDirectory(files,new File(directoryName));
        return files;
    }
    private static void getAllFilesInDirectory(ArrayList<File> files, File sourceDirectory)
    {
        for(File srcfile : sourceDirectory.listFiles())
        {
            if(srcfile.isDirectory())
            {
                getAllFilesInDirectory(files, srcfile);
                continue;
            }
            files.add(srcfile);
        }
    }
    public static void filterGroundTruthImages(ArrayList<File> files)
    {
        ArrayList<File> gtFiles = new ArrayList<File>();
        for(File srcfile : files)
        {
            String srcfilename = srcfile.getPath();
            int fileExtensionIndex = srcfilename.lastIndexOf('.');
            
            if(srcfilename.substring(fileExtensionIndex - 3, fileExtensionIndex).equals("_gt"))
            {
                gtFiles.add(srcfile);
                continue;
            }
        }
        files.removeAll(gtFiles);
    }
    public static ArrayList<File> getGroundTruthFiles(ArrayList<File> inputFiles)
    {
        ArrayList<File> gtFiles = new ArrayList<File>();
        for(File srcfile : inputFiles)
        {
            String srcfilename = srcfile.getPath();
            int fileExtensionIndex = srcfilename.lastIndexOf('.');
            char[] temp = srcfilename.toCharArray();
            //if gt image skip the file
            //System.out.println(srcfilename.substring(filenamelen - 7, filenamelen - 3));
            if(srcfilename.substring(fileExtensionIndex - 3, fileExtensionIndex).equals("_gt"))
            {
                System.out.println("GT file found in input image files");
            }
            //make 01_in.png as 01_gt.png
            temp[fileExtensionIndex - 1] = 't';
            temp[fileExtensionIndex - 2] = 'g';
            String gtfilename = String.copyValueOf(temp);
            gtFiles.add(new File(gtfilename));
        }
        return gtFiles;
    }
    public static File getGroundTruthFile(File inputFile)
    {
        String srcfilename = inputFile.getPath();
        int fileExtensionIndex = srcfilename.lastIndexOf('.');
        char[] temp = srcfilename.toCharArray();
       
        if(srcfilename.substring(fileExtensionIndex - 3, fileExtensionIndex).equals("_gt"))
        {
            System.out.println("GT file provided");
        }
        //make 01_in.png as 01_gt.png
        temp[fileExtensionIndex - 1] = 't';
        temp[fileExtensionIndex - 2] = 'g';
        String gtfilename = String.copyValueOf(temp);
        return new File(gtfilename);
    }
    
    
    //methods below are appropriate for grayscale images
    public static int findMaxPixelValue(Image img, int xs, int xe, int ys, int ye)
    {//most appropriate for grayscale image
        //xs = starting bound for x coord, xe = ending bound for x coord(inclusive), similar for ys and ye
        int max = 0;   
        for(int i=xs; i<=xe; i++)
            for(int j=ys; j<=ye; j++)
            {
                if(max < img.pixel[i][j])
                    max = img.pixel[i][j];
            }
        return max;
    }
    
    public static int findMaxPixelValue(Image img)
    {
        int xs = 0, ys = 0;
        int xe = img.getMaxX();
        int ye = img.getMaxY();
        return findMaxPixelValue(img, xs, xe, ys, ye);
    }
    
    public static int findMinPixelValue(Image img, int xs, int xe, int ys, int ye)
    {
        int min = Integer.MAX_VALUE;  
        for(int i=0; i<img.getWidth(); i++)
            for(int j=0; j<img.getHeight(); j++)
            {
                if(min >  img.pixel[i][j])
                    min = img.pixel[i][j];
            }
        return min;
    }
    
    public static int findMinPixelValue(Image img)
    {
        int xs = 0, ys = 0;
        int xe = img.getMaxX();
        int ye = img.getMaxY();
        return findMinPixelValue(img, xs, xe, ys, ye);
    }
    
    
    //image processing techniques below
    public static Image enhanceText(Image img, int imageMean, int imageSD)
    {//perform enhancement using pixel range segmentation approach for garyscale image
        //sd- standard deviation of the whole image
        Image enhancedImg = new Image(img.getWidth(), img.getHeight());
        
        int Na = 9;
        int Imax = findMaxPixelValue(img);  //maximum intensity of pixel
        int Imin = findMinPixelValue(img);  //minimum intensity of pixel
        
        int Fseg = Imin + (Imax - Imin)/Na;   //first segment (text segment) largest value
        int Lseg = Imin + (Imax - Imin)/Na;   //last segment (background) smallest value
        
        for(int i=0; i< img.getWidth(); i++)
        {
            for(int j=0; j<img.getHeight(); j++)
            {
                //segmentation into first or last segments
                if(img.pixel[i][j] < Fseg)
                    enhancedImg.pixel[i][j] = 0;   //intensity value for text pixels
                else if(img.pixel[i][j] > Lseg)
                    enhancedImg.pixel[i][j] = imageMean;
                else
                    enhancedImg.pixel[i][j] = img.pixel[i][j];
                    
                //uniform illumination
                if(enhancedImg.pixel[i][j] >= (imageMean - imageSD/2) && enhancedImg.pixel[i][j] <= (imageMean + imageSD/2))
                    enhancedImg.pixel[i][j] = imageMean;
            }
        }
        return enhancedImg;
    }
    
    
    //morphological operations
    public static Image dilate(Image image, ImageWindow imageWindow)
    {
        if(image.getType() != Image.TYPE.BIN)
        {System.out.println("Doesn't support dilation for non-binary image"); return null;}
        
        Image dilatedImg = new Image(image.getWidth(), image.getHeight(), Image.TYPE.BIN);
        //perform dilation
        for(int i=0; i<image.getWidth(); i++)
            for(int j=0; j<image.getHeight(); j++)
                dilatedImg.pixel[i][j] = imageWindow.getMaxForBinarized(i, j);
                
        return dilatedImg;
    }
    public static Image dilate(Image image, int windowSizeX, int windowSizeY)
    {
        ImageWindow imageWindow = new ImageWindow(image, windowSizeX, windowSizeY);
        Image dilatedImg = dilate(image, imageWindow);
        return dilatedImg;
    }
    public static Image dilate(Image img, int windowSize)
    {
       Image dilatedImg = dilate(img, windowSize, windowSize);
       return dilatedImg;
    }
    
    public static Image erode(Image image, ImageWindow imageWindow)
    {
        if(image.getType() != Image.TYPE.BIN)
        {System.out.println("Doesn't support erosion for non-binary image"); return null;}
        
        Image erodedImg = new Image(image.getWidth(), image.getHeight(), Image.TYPE.BIN);
        //perform erosion
        for(int i=0; i<image.getWidth(); i++)
            for(int j=0; j<image.getHeight(); j++)
                erodedImg.pixel[i][j] = imageWindow.getMinForBinarized(i, j);
        return erodedImg;
    }
    public static Image erode(Image image, int windowSizeX, int windowSizeY)
    {
        ImageWindow imageWindow = new ImageWindow(image, windowSizeX, windowSizeY);
        Image erodedImg = erode(image, imageWindow);
        return erodedImg;
    }
    public static Image erode(Image img, int windowSize)
    {
       Image erodedImg = erode(img, windowSize, windowSize);
       return erodedImg;
    }
    
    public static Image open(Image img, int windowSizeX, int windowSizeY)
    {
        Image erodedImage = erode(img, windowSizeX, windowSizeY);
        Image openedImage = dilate(erodedImage, windowSizeX, windowSizeY);
        return openedImage;
    }
    public static Image open(Image img, int windowSize)
    {
        return open(img, windowSize, windowSize);
    }
    
    public static Image close(Image img, int windowSizeX, int windowSizeY)
    {
        Image dilatedImage = dilate(img, windowSizeX, windowSizeY);
        Image closedImage = erode(dilatedImage, windowSizeX, windowSizeY);
        return closedImage;
    }
    public static Image close(Image img, int windowSize)
    {
        return close(img, windowSize, windowSize);
    }
}
