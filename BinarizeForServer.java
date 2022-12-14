public class BinarizeForServer
{
    /*public static void main(String args[])
    {
        binarizeAndSave(args[0], args[1]);
    }*/
    
    public static void test()
    {
        binarizeAndSave("a.jpg", "tempa.jpg");
    }
        
    public static void binarizeAndSave(String imagepath, String savepath)
    {
        System.out.println("Reading image");
        Image srcimg = ImageUtility.readImage(imagepath);
        
        //## image setup
        float k = 0.25f;
        int w = 70;
        Binarization sauv = new Sauvola(k, w);
        System.out.println("Setting image in sauvola");
        sauv.setImage(srcimg);
        System.out.println("Binarizing image in sauvola");
        sauv.binarize();
        
        System.out.println("Dilating image in sauvola");
        Image dilatedImage = ImageUtility.dilate(sauv.getBinarizedImage(), 3);
        
        System.out.println("creating segmentation object");
        Segmentation segmentation = new Segmentation(dilatedImage);
        System.out.println("Segmenting image");
        //segmentation.segment();
        
        //save binarized image
        System.out.println("Writing binarized image");
        ImageUtility.writeImage(sauv.getBinarizedImage(), savepath);
    }
}
