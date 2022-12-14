public class OCR
{
    //Image image;
    
    public static Image simpleThreshold(Image _image, int threshold)
    {
             Image binarizedImage = new Image(_image.getWidth(), _image.getHeight());
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
    
    /*public void setImage(Image _image)
    {
        image = _image;
    }*/
}