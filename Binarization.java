    public abstract class Binarization
    {
        Image sourceImage;
        Image binarizedImage;
        
       public abstract void binarize();
       
       private static void rectifyImage(Image image)
       {
           if(image.getType() == Image.TYPE.RGB)
           {   
               //System.out.println("Converted RGB image to gary for binarization");
               ImageUtility.convertRGB2gray(image);
           }
           else if(image.getType() == Image.TYPE.BIN)
           {   
               System.out.println("Source image for binarization in binary format!!");
           }
       }
       
       public void setImage(Image _srcImage)
       {
           sourceImage = _srcImage;
           //convert rgb image to grayscale if necessary
           rectifyImage(_srcImage);
           
           binarizedImage = new Image(sourceImage.getWidth(), sourceImage.getHeight());
           binarizedImage.setType(Image.TYPE.BIN);
       }
       
       public Image getBinarizedImage()
       {return binarizedImage;}
       
       public static Image simpleThreshold(Image image, int threshold)
        {
            //convert rgb image to grayscale if necessary
            rectifyImage(image);
           
            Image binarizedImage = new Image(image.getWidth(), image.getHeight());
            binarizedImage.setType(Image.TYPE.BIN);
            for (int i=0; i < image.getWidth(); i++)
            {
                for(int j=0; j < image.getHeight(); j++)
                {
                    if (image.pixel[i][j] < threshold)
                        {binarizedImage.pixel[i][j] = 1;}
                    else
                        {binarizedImage.pixel[i][j] = 0;}
                }
            }
            return binarizedImage;
        }
    }