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

        Image openedImage = ImageUtility.open(sauv.getBinarizedImage(), 3);
        Image closedImage = ImageUtility.close(openedImage, 3);
        
        ImageUtility.writeImage(closedImage, savepath);
    }
}
