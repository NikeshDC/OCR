    public abstract class Binarization
    {
        Image sourceImage;
        Image binarizedImage;
        
       public abstract void binarize();
       
       public void setImage(Image _srcImage)
       {
           sourceImage = _srcImage;
           //convert rgb image to grayscale
           if(sourceImage.getType() == Image.TYPE.RGB)
           {   
               System.out.println("Converted RGB image to gary for binarization");
               ImageUtility.convertRGB2gray(_srcImage);
           }
           else if(sourceImage.getType() == Image.TYPE.BIN)
           {   
               System.out.println("Source image for binarization in binary format!!");
           }
           
           binarizedImage = new Image(sourceImage.getWidth(), sourceImage.getHeight());
           binarizedImage.setType(Image.TYPE.BIN);
       }
       
       public Image getBinarizedImage()
       {return binarizedImage;}
       
       public static Image simpleThreshold(Image _image, int threshold)
        {
            Image binarizedImage = new Image(_image.getWidth(), _image.getHeight());
            binarizedImage.setType(Image.TYPE.BIN);
            for (int i=0; i < _image.getWidth(); i++)
            {
                for(int j=0; j < _image.getHeight(); j++)
                {
                    if (_image.pixel[i][j] < threshold)
                        {binarizedImage.pixel[i][j] = 1;}
                    else
                        {binarizedImage.pixel[i][j] = 0;}
                }
            }
            return binarizedImage;
        }
    }