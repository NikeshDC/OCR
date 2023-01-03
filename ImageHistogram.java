/**
 * histogram is distribution the number of pixels at various levels(0 to 255 for 8-bit image depth)
 * it may be represented as array of integers with a value for each level
 *
 * @author (Nikesh DC)
 * @version (1.2)
 */
public class ImageHistogram
{
    // instance variables - replace the example below with your own
    public int[] level;
    public double[] level_normalized;
    private Image image;
    int L;
    
    private void initialize(Image _image)
    {
        image = _image;
        
        if(image.getType() == Image.TYPE.BIN)
            L = 2;  //1 bit depth for binary image
        else if(image.getType() == Image.TYPE.GRAY)
            L = 256;  //8 bit depth for grayscale image
        else if(image.getType() == Image.TYPE.RGB)
            L = 256 * 256 * 256;  //24 bit depth for RGB image
         
        level = new int[L];  //default initialized value for each element is 0
        level_normalized = new double[L];  //lvel is normalized to [0-1]
    }
    
    private void constructHistogram( int xs, int xe, int ys, int ye)
    {//construct histogram for pixels inside given bounds of the image
        
        //check if any bound is greater than image size//includes both starting and ending bound
        int imgXE = image.getMaxX();
        int imgYE = image.getMaxY();
        if (xs > imgXE || xe > imgXE || ys > imgYE || ye > imgYE)
        {
            System.out.println("Given bound exceeds Image bound (in histogram)");
            return;
        }
        if (xs >= xe || ys >= ye || xs < 0 || xe < 0 || ys <0 || ye < 0)
        {
            System.out.println("Given bounds not appropriate in histogram");
            return;
        }
        
        //constructing histogram
        for (int i=xs; i <= xe; i++)
          for(int j=ys; j <= ye; j++)
                   level[image.pixel[i][j]] ++;  //increment value for each level 

        //normalizing level values
        int N = (xe - xs + 1) * (ye - ys + 1); //number of pixels in image
        
        for(int i=0; i<L; i++)
            level_normalized[i] = (double)level[i]/N; 
    }

    public ImageHistogram(Image _image)
    {
        initialize(_image);
        int imgXE = image.getMaxX();
        int imgYE = image.getMaxY();
        constructHistogram(0,imgXE,0,imgYE);
    }
    
    public ImageHistogram(Image _image, int xs, int xe, int ys, int ye)
    {
        initialize(_image);
        constructHistogram(xs,xe,ys,ye);
    }
    
    public int getMode()
    {
        int mode = 0;
        for(int i=1; i < L; i++)
        {
            if(level[i] > mode)
                mode = i;
        }
        return mode;
    }
    
    public double getEntropy()
    {
        double entropy = 0.0;
        double log2base = Math.log(2);
        for(int i=0; i < L; i++)
        {
            if (level_normalized[i] !=0)
                entropy += -level_normalized[i] * Math.log(level_normalized[i])/log2base;
        }        
        return entropy;
    }
    
    int getLevel(){return L;}
}
