public class OtsuBounds extends Binarization
{
    Component[] components;
    int L;
    private int threshold;
    
    private void initialize(Image srcImage, Component[] _components)
    {
        components = _components;
        setImage(srcImage);
    }
    
    public OtsuBounds(Image srcImage, Component[] _components)
    {
        initialize(srcImage, _components);
    }
    
    public OtsuBounds(Image srcImage, Segmentation segmentation)
    {
        initialize(srcImage, segmentation.getComponents());
    }
    
    public void binarize()
    {
        setBinImageForBounds();    //initially setting all image pixel to background (i.e 0)

        //for every components binarize seperately
        for(int i=0; i < components.length ;i++)
        {
            int xs = components[i].getMinX();
            int xe = components[i].getMaxX();
            int ys = components[i].getMinY();
            int ye = components[i].getMaxY();        
            binarizeWithinBounds(xs, xe, ys, ye);
        }
    }
    
    private void setBinImageForBounds()
    {
        //binarizedImage = new Image(sourceImage.getWidth(), sourceImage.getHeight()); //already set in Binarization.setImage()
        for(int i=0; i< sourceImage.getWidth(); i++)
        {
            for(int j=0; j<sourceImage.getHeight(); j++)
            {
                binarizedImage.pixel[i][j] = 0;     
            }
        }
    }
    
    public void calculateThresholdWithinBounds(int xs, int xe, int ys, int ye)
    {
        ImageHistogram histogram = new ImageHistogram(sourceImage, xs, xe, ys, ye);
        L = histogram.getLevel();
        
        double uT = 0.0;
        for(int i=0; i < L; i++)
            uT += i * histogram.level_normalized[i]; 
        
        double uk, wk;  //expected level(mean) and probability of occurance for probable text pixels seperated by threshold 'k'
        double sb;  //between class variance that is a measure of goodness of threshold seperating the background and text
        double max_sb = -1.0;  //sb must be positive
        for(int k=0; k<(L-1); k++)
        {
            uk = 0.0;
            wk = 0.0;
            for(int i=0; i <= k; i++)
                uk += i * histogram.level_normalized[i]; 
            
            for(int i=0; i <= k; i++)
                wk += histogram.level_normalized[i]; 
                
            sb = Math.pow((uT*wk - uk), 2) / (wk * (1-wk));
            
            if (max_sb < sb)
            {
                max_sb = sb;
                threshold = k;  //all pixels from 0 up to and including k are 'text'
            }
        }
    }
    
    private void binarizeWithinBounds(int xs, int xe, int ys, int ye)
    {
        //apply otsu binarization only within given bounds
        //must set binarized image once before using within bounds
        
        for(int i= xs; i<= xe; i++)
        {
            for(int j= ys; j<= ye; j++)
            {
                if(sourceImage.pixel[i][j] <= threshold)
                {binarizedImage.pixel[i][j] = 1;} //all pixels from 0 up to and including threshold are 'text'
                else
                {binarizedImage.pixel[i][j] = 0;} 
                
                //binarizedImage.pixel[i][j] = (image.pixel[i][j] <= threshold) ? (short)1 : (short)0;
            }
        }
    }
}
