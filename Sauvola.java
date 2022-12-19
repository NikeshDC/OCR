        public class Sauvola extends Binarization
        {
            int w;
            float k; //k ranges from 0 to 1
            
            public Sauvola(float _k, int _windowSize)
            { k = _k;     w = _windowSize; }
            
            public void setParam(float _k, int _w)
            {  k = _k;  w = _w; }
            
            
            public void binarize(ImageWindow imageWindow)
            {
                if(sourceImage == null)
                {
                    System.out.println("No source image set");
                    return;
                }
                else if(binarizedImage == null)
                {
                    System.out.println("Binarized image set for Image for binarization");
                    binarizedImage = new Image(sourceImage.getWidth(), sourceImage.getHeight());
                    binarizedImage.setType(Image.TYPE.BIN);
                }
                
                if(imageWindow.getImage() != sourceImage)
                {
                    System.out.println("Image window has different source image");
                    return;
                }
                
                int threshold;
                int mean;   //mean centered around a window of size w
                double sd;    //standard deviation centered around a window of size w
                int R = 128;  //dynamic range of standartd deviation
                
                imageWindow.useSqrIntegralImage();
                
                //for every pixel in image calculate threshold value and compare to assign binarization
                for (int i=0; i < sourceImage.getWidth(); i++)
                {
                    for(int j=0; j < sourceImage.getHeight(); j++)
                    {
                        mean = imageWindow.mean(i, j);
                        sd =  Math.sqrt(imageWindow.variance(i, j, mean));
                        threshold = (int) (mean * (1 + k *(sd/R - 1)));
                        //adaptive methods---
                        //threshold = (int) (mean * (1 + k * (1 - sd/mean) *(sd/R - 1)));
                        //---
                        if (sourceImage.pixel[i][j] < threshold)
                            {binarizedImage.pixel[i][j] = 1;}
                        else
                            {binarizedImage.pixel[i][j] = 0;}
                    }
                }
            }
            
            public void binarize()
            {
                ImageWindow imageWindow = new ImageWindow(sourceImage, w);
                binarize(imageWindow);
            }
            
            public void binarize(int secondaryMean, float weight)
            {//weight ranges from 0 to 1f
                binarizedImage = new Image(sourceImage.getWidth(), sourceImage.getHeight());
                int threshold;
                int mean;   //mean centered around a window of size w
                double sd;    //standard deviation centered around a window of size w
                int R = 128;  //dynamic range of standartd deviation
                ImageWindow imageWindow = new ImageWindow(sourceImage, w);
                
                //for every pixel in image calculate threshold value and compare to assign binarization
                for (int i=0; i < sourceImage.getWidth(); i++)
                {
                    for(int j=0; j < sourceImage.getHeight(); j++)
                    {
                        mean = imageWindow.mean(i, j);
                        mean = (int)(mean * (1 - weight) + secondaryMean * weight);
                        sd =  Math.sqrt(imageWindow.variance(i, j, mean));
                        threshold = (int) (mean * (1 + k *(sd/R - 1)));
                        //threshold = (int)(threshold *  + );
                        if (sourceImage.pixel[i][j] < threshold)
                            {binarizedImage.pixel[i][j] = 1;}
                        else
                            {binarizedImage.pixel[i][j] = 0;}
                    }
                }
            }
        }
        