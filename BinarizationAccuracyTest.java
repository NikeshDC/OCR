import java.io.File;

public class BinarizationAccuracyTest
{
    BinarizationAccuracy binarizationAccuracy;
    float[] k_range = {0f, 0.1f, 0.9f};
    int[] w_range = {40,10,70};
    
    public BinarizationAccuracyTest()
    {
        binarizationAccuracy = new BinarizationAccuracy(); 
    }
    
    public void compareImagesInDirectory()
    {
        String srcdirname = "DIBCO";
        binarizationAccuracy.compareImagesInDirectory(new File(srcdirname), k_range, w_range);
    }
    
    public void compareBinarizedImage()
    {
        String infile = "05_gt.png";
        String gtfile = "05_gt.png";
        float f1 = binarizationAccuracy.compareBinarizedImages(infile, gtfile);
        System.out.println(f1);
    }
    
    public void compareImagesUsingSauvolaAtParam()
    {
        String infile = "05_in.png";
        String gtfile = "05_gt.png";
        float k = 0.2f;
        int w = 60;
        float f1 = binarizationAccuracy.compareImagesUsingSauvolaAtParam(infile, gtfile, k, w);
        System.out.println(f1);
    }
    
    public void compareImagesUsingSauvolaAtRangeAndPrintAll()
    {
        String infile = "05_in.png";
        String gtfile = "05_gt.png";
        float[] bestValues = binarizationAccuracy.compareImagesUsingSauvolaAtRangeAndPrintAll(infile, gtfile, k_range, w_range);
        //System.out.println("max f1: "+ bestValues[0] + ", k: "+ bestValues[1] + ", w: "+ bestValues[2]);
    }
}
