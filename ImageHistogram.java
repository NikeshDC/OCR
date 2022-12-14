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
    int L;

    public ImageHistogram(Image image)
    {
        if(image.getType() == Image.TYPE.BIN)
            L = 2;  //1 bit depth for binary image
        else if(image.getType() == Image.TYPE.GRAY)
            L = 256;  //8 bit depth for grayscale image
        else if(image.getType() == Image.TYPE.RGB)
            L = 256 * 256 * 256;  //24 bit depth for RGB image
         
        level = new int[L];  //default initialized value for each element is 0
        level_normalized = new double[L];  //lvel is normalized to [0-1]
        
        //constructing histogram
        for (int i=0; i < image.getWidth(); i++)
        {
           for(int j=0; j < image.getHeight(); j++)
           {
               level[image.pixel[i][j]] ++;  //increment value for each level
           }
        }
        
        //normalizing level values
        int N = image.getWidth() * image.getHeight(); //number of pixels in image
        
        for(int i=0; i<L; i++)
        {
            level_normalized[i] = (double)level[i]/N;
        }
    }
    
    public ImageHistogram(Image image, int xs, int xe, int ys, int ye)
    {
        //includes both starting and ending bound
        
        //check if any bound is greater than image size
        int imgXE = image.getWidth();
        int imgYE = image.getHeight();
        if (xs > imgXE || xe > imgXE || ys > imgYE || ye > imgYE)
            System.out.println("Given bound exceeds Image bound (in histogram)");
        if (xs >= xe || ys >= ye || xs < 0 || xe < 0 || ys <0 || ye < 0)
        {
            System.out.println("Given bounds not appropriate in histogram");
        }
        
        
        level = new int[L];  //default initialized value for each element is 0
        level_normalized = new double[L];
        
        //constructing histogram
        for (int i=xs; i <= xe; i++)
        {
           for(int j=ys; j <= ye; j++)
           {
               level[image.pixel[i][j]] ++;  //increment value for each level
           }
        }
        
        //normalizing level values
        int N = (xe - xs + 1) * (ye - ys + 1); //number of pixels in image
        
        for(int i=0; i<L; i++)
        {
            level_normalized[i] = (double)level[i]/N;
        }
    }
    
    int getLevel(){return L;}
}
