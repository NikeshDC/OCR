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
    }