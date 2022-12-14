
    public class ImageWindow
    {
        Image image;
        int windowSideX;
        int windowSideY;
       
        //integral image use will allow for calculating sum of the window efficiently irrespective of window size
        private IntegralImage integralImage;  
        //sqaure integral image use will allow for calculating variance (along with integralImage) of the window efficiently irrespective of window size                                              
        private SqrIntegralImage sqrIntegralImage;
        
        //actual bounds of the window (may vary based on center pixel)
        private int noOfPixels;  //these are used for integral image
        private int wxs, wxs_clamped, wxe, wys, wys_clamped, wye; 
        private int wl, wt, wc;
        
        private int sum;
        private int sqrSum;
        
        public int getWindowSizeX()
        {return windowSideX * 2 + 1 ;}
         public int getWindowSizeY()
        {return windowSideY * 2 + 1 ;}
        public void setImage(Image _image)
        {image = _image;}
        public Image getImage()
        {return image;}
        
        private void initialize(Image _image, int _windowSizeX, int _windowSizeY)
        {
            image = _image;
            windowSideX = Math.max(1, (_windowSizeX - 1) / 2);   //window must atleast be 3 pixel
            windowSideY = Math.max(1, (_windowSizeY - 1) / 2);   //window side is the number of pixels on the side of center pixel
            createIntegralImage();
        }
        
        public ImageWindow(Image _image, int _windowSize)
        {   initialize(_image, _windowSize, _windowSize);}
        public ImageWindow(Image _image, int _windowSizeX, int _windowSizeY)
        {   initialize(_image, _windowSizeX, _windowSizeY);   }
        
        private void createIntegralImage()
        { integralImage = new IntegralImage(image); }
        private void createSqrIntegralImage()
        { sqrIntegralImage = new SqrIntegralImage(image); }
        
        public void useSquareIntegralImage()
        {   createSqrIntegralImage();   }
        
        /*public void setCenter(int x, int y)
        {   centerX = x;    centerY = y;  }*/
        
        void calculateBounds(int x, int y)
        {   wxs = x-windowSideX - 1;
            wxs_clamped = Math.max(wxs, -1);  //starting point for actual window
            wxe = Math.min(x+windowSideX, image.sizeX - 1);
            wys = y-windowSideY - 1;
            wys_clamped = Math.max(wys, -1);
            wye = Math.min(y+windowSideY, image.sizeY - 1);
            noOfPixels = (wxe - wxs_clamped) * (wye - wys_clamped); //no of pixels inside the actual window
        }
        
        public int getMaxForBinarized(int x, int y)
        {
            if(image.getType() != Image.TYPE.BIN)
            {   
                System.out.println("Cannot find max for un-binarized image");
                return -1;
            }
            //maximum for binarized image
            findSum(x, y);
            if(sum > 0) //if any element is greater than zero max is 1
            { return 1;}
            else
            { return 0;}
        }
        
        public int getMinForBinarized(int x, int y)
        {
            if(image.getType() != Image.TYPE.BIN)
            {   
                System.out.println("Cannot find max for un-binarized image");
                return -1;
            }
            //minimum for binarized image
            findSum(x, y);
            if( sum < noOfPixels) //if any element is less than 1 min is 0
            { return 0; }
            else
            { return 1; }
        }
        
        public int getSum() {return sum;}
        public int getSqrSum() {return sqrSum;}
        
        public void findSum(int x, int y)
        {//return sum of pixels centered around (x,y) in the image using integralImage
            //actual window size may be smaller than the specified window size at the image borders as there may not be enough pixels for the window
            //int wxs, wxs_clamped, wxe, wys, wys_clamped, wye; //starting and ending xy coordinate positions for the actual window; wxs = window start postion for x axis
            calculateBounds(x, y);

            //int wl; //sum of pixels to left of the window to subtract to obtain the sum of window using integral image
            if (wxs < 0)
                {wl = 0;}
            else
                {wl = integralImage.pixel[wxs][wye];}
            //int wt; //sum of pixels to top of the window
            if (wys < 0)
                {wt = 0;}
            else
                {wt = integralImage.pixel[wxe][wys];}
            //int wc; //sum up to starting corner (intersection of left and top parts)
            if (wxs < 0 || wys < 0)
                {wc = 0;}
            else
                {wc = integralImage.pixel[wxs][wys];}
            
            sum = integralImage.pixel[wxe][wye] - wl -wt +wc;
        }
        
        private void findSqrSum(int x, int y)
        {//return sum of pixels centered around (x,y) in the image using sqrIntegralImage
            //this should be called after corresponding sum() call so that which wxs, wxe... shouldnot be computed again
            //i.e. this is only for finding variance
            calculateBounds(x, y);

            //int wl; //sum of pixels to left of the window to subtract to obtain the sum of window using integral image
            if (wxs < 0)
                {wl = 0;}
            else
                {wl = sqrIntegralImage.pixel[wxs][wye];}
            //int wt; //sum of pixels to top of the window
            if (wys < 0)
                {wt = 0;}
            else
                {wt = sqrIntegralImage.pixel[wxe][wys];}
            //int wc; //sum up to starting corner (intersection of left and top parts)
            if (wxs < 0 || wys < 0)
                {wc = 0;}
            else
                {wc = sqrIntegralImage.pixel[wxs][wys];}
               
            sqrSum = sqrIntegralImage.pixel[wxe][wye] - wl -wt +wc;
        }
        
        public void findSumAndSqrSum(int x, int y)
        {
            calculateBounds(x, y);
            //sum---------------------------------------------------------------------------------------------------------
            //int wl; //sum of pixels to left of the window to subtract to obtain the sum of window using integral image
            if (wxs < 0)
                {wl = 0;}
            else
                {wl = integralImage.pixel[wxs][wye];}
            //int wt; //sum of pixels to top of the window
            if (wys < 0)
                {wt = 0;}
            else
                {wt = integralImage.pixel[wxe][wys];}
            //int wc; //sum up to starting corner (intersection of left and top parts)
            if (wxs < 0 || wys < 0)
                {wc = 0;}
            else
                {wc = integralImage.pixel[wxs][wys];}
            sum = integralImage.pixel[wxe][wye] - wl -wt +wc;
            
            //squaresum-------------------------------------------------------------------------------------------------------------
            if (wxs < 0)
                {wl = 0;}
            else
                {wl = sqrIntegralImage.pixel[wxs][wye];}
            //int wt; //sum of pixels to top of the window
            if (wys < 0)
                {wt = 0;}
            else
                {wt = sqrIntegralImage.pixel[wxe][wys];}
            //int wc; //sum up to starting corner (intersection of left and top parts)
            if (wxs < 0 || wys < 0)
                {wc = 0;}
            else
                {wc = sqrIntegralImage.pixel[wxs][wys];}
               
            sqrSum = sqrIntegralImage.pixel[wxe][wye] - wl -wt +wc;
        }
        
        public int mean(int x, int y)
        {//return mean of pixels centered around (x,y) in the image using integralImage
            //int wxs, wxe, wys, wye; //starting and ending xy coordinate positions; wxs = window start postion for x axis
            findSum(x, y);
            return sum / noOfPixels;
        }
        
        public int getImageMean()
        {//return the mean of whole image
            return (integralImage.pixel[image.sizeX - 1][image.sizeY - 1] / (image.sizeX * image.sizeY));
        }
        
        public int variance(int x, int y, int _mean)
        {//return variance of pixels centered around (x,y) in the image using integralImage and sqrIntegralImage
            //int wxs, wxe, wys, wye; //starting and ending xy coordinate positions; wxs = window start postion for x axis
            //int _mean = mean(x,y); mean has been already supplied in parameter itself 
            findSqrSum(x,y);
            return (sqrSum/noOfPixels - _mean * _mean);
        }
        
        public int getImageVariance(int _mean)
        {
            return (sqrIntegralImage.pixel[image.sizeX - 1][image.sizeY - 1]/(image.sizeX * image.sizeY) - _mean * _mean);
        }
        
        public int variance(int x, int y)
        {//return variance of pixels centered around (x,y) in the image using integralImage and sqrIntegralImage
            //int wxs, wxe, wys, wye; //starting and ending xy coordinate positions; wxs = window start postion for x axis
            findSumAndSqrSum(x, y);
            int _mean = sum / noOfPixels;
            return (sqrSum/noOfPixels - _mean * _mean);
        }
    }
