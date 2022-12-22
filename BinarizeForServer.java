public class BinarizeForServer
{
    public static void main(String args[])
    {
        binarizeAndSave(args[0], args[1]);
    }
    
    public static void test()
    {
        binarizeAndSave("05_in.png", "temp05_in.jpg");
    }
        
    public static void binarizeAndSave(String imagepath, String savepath)
    {
        Image srcimg = ImageUtility.readImage(imagepath);
        
        //## image setup
        float k = 0.059f;
        int w = 30;
        Binarization sauv = new Sauvola(k, w);
        sauv.setImage(srcimg);
        sauv.binarize();
        
        Image dilatedImage = ImageUtility.dilate(sauv.getBinarizedImage(), 5);
        
        Segmentation segmentation = new Segmentation(dilatedImage);
        segmentation.segment();
        
        Binarization otsuBound = new OtsuBounds(srcimg, segmentation.getComponents());
        otsuBound.binarize();
        
        //save binarized image
        ImageUtility.writeImage(otsuBound.getBinarizedImage(), savepath);
    }
}
