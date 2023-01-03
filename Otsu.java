    public class Otsu extends Binarization
    {
        int L;  //assuming bit depth of image is 8-bits; max level for histogram is 255
        private int threshold;
        ImageHistogram histogram;
        
        public double sb;
        public double sw;
        public double u0;
        public double u1;
        public double s0;
        public double s1;
        public double w0;
        public double w1;
        
        public void setImage(Image image)
        {
            if(image != sourceImage)
            {
                histogram = new ImageHistogram(image);
                super.setImage(image);
            }
        }
        public void forceSetImage(Image image)
        {
            histogram = new ImageHistogram(image);
            super.forceSetImage(image);
        }
        
        public ImageHistogram getHistogram()
        {
            return histogram;
        }
        
        public int getThreshold()
        {return threshold;}
        
        public void calculateThreshold()
        {
            calculateThreshold(false, 0, 0, 0, 0);  //calculate globally
        }
        public void calculateThresholdLocally(int xs, int xe, int ys, int ye)
        {
            calculateThreshold(true, xs, xe, ys, ye);
        }
        private void calculateThreshold(boolean local, int xs, int xe, int ys, int ye)
        {
            ImageHistogram histogram;
            if(local)
                histogram = new ImageHistogram(sourceImage, xs, xe, ys, ye);
            else
                histogram = this.histogram;
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
            
            //calculate all measures with the threshold obtained
            uk = 0.0;
            wk = 0.0;
            for(int i=0; i <= threshold; i++)
                uk += i * histogram.level_normalized[i]; 
            
            for(int i=0; i <= threshold; i++)
                wk += histogram.level_normalized[i]; 
                
            this.w0 = wk;
            this.w1 = 1.0 - this.w0;
            this.u0 = uk / wk;
            this.u1 = (uT - uk) / (1 - wk);
            this.sb = Math.pow((uT*wk - uk), 2) / (wk * (1-wk));
            
            this.s0 = 0.0;
            for(int i=0; i <= threshold; i++)
                this.s0 += Math.pow((i - this.u0), 2) * histogram.level_normalized[i]; 
            this.s0 = this.s0 / this.w0;
            
            this.s1 = 0.0;
            for(int i=threshold+1; i < L; i++)
                this.s1 += Math.pow((i - this.u1), 2) * histogram.level_normalized[i]; 
            this.s1 = this.s1 / this.w1;
            
            this.sw = this.w0 * this.s0 + this.w1 * this.s1;
        }
        
        public void binarize()
        {
            calculateThreshold();
            for (int i=0; i < sourceImage.getWidth(); i++)
            {
                for(int j=0; j < sourceImage.getHeight(); j++)
                {
                    if (sourceImage.pixel[i][j] < threshold)
                            {binarizedImage.pixel[i][j] = 1;}
                        else
                            {binarizedImage.pixel[i][j] = 0;}
                }
            }
        }
    }
