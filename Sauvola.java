        public class Sauvola extends Binarization
        {
            int w;
            float k; //k ranges from 0 to 1
            
            public Sauvola(float _k, int _windowSize)
            { k = _k;     w = _windowSize; }
            
            public void setParameters(float _k, int _w)
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
                //System.out.println(imageWindow.getWindowSizeX());
                
                //applying local prediction of k
                //Otsu otsu = new Otsu();
                //otsu.setImage(this.sourceImage);
                
                //for every pixel in image calculate threshold value and compare to assign binarization
                for (int i=0; i < sourceImage.getWidth(); i++)
                {
                    for(int j=0; j < sourceImage.getHeight(); j++)
                    {
                        mean = imageWindow.mean(i, j);
                        sd =  Math.sqrt(imageWindow.variance(i, j, mean));
                        
                        //local k prediction
                        //KPrediction kpred = new KPrediction(otsu, imageWindow.getBounds());
                        //k = (float)kpred.getK();
                        
                        //System.out.println(imageWindow.variance(i, j, mean));
                        threshold = (int) (mean * (1 - k *(1- sd/R)));
                        //adaptive methods---
                        //float localAdaptiveFactor1 = sd / mean;
                        
                        //int maxPix = imageWindow.getMax(i, j);
                        //int minPix = imageWindow.getMin(i, j);
                        //float localAdaptiveFactor2 = ((float)maxPix - minPix) / (maxPix);
                        //threshold = (int) (mean * (1 + k * localAdaptiveFactor2 *(sd/R - 1)));
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
        