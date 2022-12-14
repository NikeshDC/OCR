
    public class SqrIntegralImage
    {
        protected Image image;  //the image of which to create the integral image for
        public int[][] pixel;  //size must be big enough to hold the sum of square of all pixels of the image
        public SqrIntegralImage(Image _image)
        {
            image = _image;
            constructIntegralImage();
        }
        
        protected void constructIntegralImage()
        {
            pixel = new int[image.getWidth()][image.getHeight()];
            pixel[0][0] = image.pixel[0][0] * image.pixel[0][0]; //first value of integral image is the initial pixel value squared 
            
            //summing pixel-squares along the first row
            for (int i=1; i <image.getWidth(); i++)
                pixel[i][0] = image.pixel[i][0] * image.pixel[i][0] + pixel[i-1][0];
            //summing pixel-squares along the first column
            for (int i=1; i <image.getHeight(); i++)
                pixel[0][i] = image.pixel[0][i] * image.pixel[0][i] + pixel[0][i-1];
            //summing for the remaining positions
            for (int i=1; i <image.getWidth(); i++)
                for(int j=1; j < image.getHeight(); j++)
                    pixel[i][j] = pixel[i-1][j] + pixel[i][j-1] + image.pixel[i][j] * image.pixel[i][j] - pixel[i-1][j-1];
        }
    }
