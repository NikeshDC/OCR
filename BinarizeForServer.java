public class BinarizeForServer
{
    public static void main(String args[])
    {
        binarizeAndSave(args[0], args[1]);
    }
    
    public static Image getBinarizedUsingPredictor(String imagepath)
    {
        Image srcimg = ImageUtility.readImageInGrayScale(imagepath);
       
        KPrediction predictor = new KPrediction(srcimg);
        float k = (float)predictor.getK();
        System.out.println("Predicted K: "+ k);
        int w = (int)(Math.min(srcimg.getWidth(), srcimg.getHeight()) * 0.1f);
        Binarization sauv = new Sauvola(k, w);
        sauv.setImage(srcimg);
        sauv.binarize();
        return sauv.getBinarizedImage();
    }
        
    public static void binarizeAndSave(String imagepath, String savepath)
    {
        Image srcimg = ImageUtility.readImageInGrayScale(imagepath);
        
        //## image setup
        KPrediction predictor = new KPrediction(srcimg);
        float k = (float)predictor.getK();
        System.out.println("Predicted K: "+ k);
        int w = (int)(Math.min(srcimg.getWidth(), srcimg.getHeight()) * 0.1f);
        Binarization sauv = new Sauvola(k, w);
        sauv.setImage(srcimg);
        sauv.binarize();
        
        //Image dilatedImage = ImageUtility.dilate(sauv.getBinarizedImage(), 5);
        
        //Segmentation segmentation = new Segmentation(dilatedImage);
        //segmentation.segment();
        
        //Binarization otsuBound = new OtsuBounds(srcimg, segmentation.getComponents());
        //otsuBound.binarize();
        
        //save binarized image
        ImageUtility.writeImage(sauv.getBinarizedImage(), savepath);
    }
}
