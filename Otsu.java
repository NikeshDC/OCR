    public class Otsu extends Binarization
    {
        int L;  //assuming bit depth of image is 8-bits; max level for histogram is 255
        public int threshold;
        
        public void binarize()
        {
            ImageHistogram histogram = new ImageHistogram(sourceImage);
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
    }
