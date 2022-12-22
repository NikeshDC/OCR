import java.io.File;

public class BinarizationAccuracy
{
    static char fileExtensionSeparator = '.';
    final static int SimpleThreshold = 128;
    
    public float compareBinarizedImages(String sourceImageFileName, String groundTruthImageFileName)
    {
        return compareBinarizedImages(new File(sourceImageFileName), new File(groundTruthImageFileName));
    }
    public float compareBinarizedImages(File sourceImageFile, File groundTruthImageFile)
    {//compares binarized image to its ground truth and prints f1 score
        AccuracyMeasure accuracyMeasure = new AccuracyMeasure();
        Image srcImage = Binarization.simpleThreshold(ImageUtility.readImage(sourceImageFile), SimpleThreshold);
        Image gtImage = Binarization.simpleThreshold(ImageUtility.readImage(groundTruthImageFile), SimpleThreshold);
        accuracyMeasure.compareImages(srcImage, gtImage);
        return accuracyMeasure.getF1score();
        
    }
    
    private float[] compareImagesUsingSauvolaAtRange(File sourceImageFile, File groundTruthImageFile, 
                                                    float[] k_range, int[] w_range, boolean printAllValues)
    {//returns {maxf1, maxk, maxw}
        //the range should be listed in order: {start, step, end}
        AccuracyMeasure accuracy = new AccuracyMeasure();
        
        Image groundTruthImage = Binarization.simpleThreshold(ImageUtility.readImage(groundTruthImageFile), SimpleThreshold);
        Sauvola sauvola = new Sauvola(0.2f, 50);
        sauvola.setImage(ImageUtility.readImage(sourceImageFile));
        
        //w-range
        int min_w = w_range[0];
        int step_w = w_range[1];
        int max_w = w_range[2];
        //k-range
        float min_k = k_range[0];
        float step_k = k_range[1];
        float max_k = k_range[2];
        
        //best result values
        float max_f1 = 0f;     //max f1-score value
        float max_f1_k = 0f;   //k-value where max f1 occurs
        float max_f1_w = 0f;   //w-value where max f1 occurs
        float f1;
        
        for(float k=min_k; k <= max_k; k+=step_k)
        {
            for(int w=min_w; w<= max_w; w+= step_w)
            {
                sauvola.setParameters(k, w);
                sauvola.binarize();
                accuracy.compareImages(sauvola.getBinarizedImage(), groundTruthImage);
                f1 = accuracy.getF1score();
                if (f1 > max_f1)
                {
                    max_f1 = f1;
                    max_f1_k = k;
                    max_f1_w = w;
                }
                if(printAllValues)
                    System.out.println(k+","+w+","+f1);   //in csv format
            }
        }
        if(printAllValues)
            System.out.println("max f1: "+max_f1+ ", k: "+max_f1_k+ ", w: "+max_f1_w);
        float[] maxValues = {max_f1, max_f1_k, max_f1_w};
        return maxValues;
    }
    
    public float[] compareImagesUsingSauvolaAtRangeAndPrintAll(File sourceImageFile, File groundTruthImageFile, float[] k_range, int[] w_range)
    {
        return compareImagesUsingSauvolaAtRange(sourceImageFile, groundTruthImageFile, k_range, w_range, true);
    }
    public float[] compareImagesUsingSauvolaAtRangeAndPrintAll(String sourceImageFileName, String groundTruthImageFileName, float[] k_range, int[] w_range)
    {
        return compareImagesUsingSauvolaAtRange(new File(sourceImageFileName), new File(groundTruthImageFileName), k_range, w_range, true);
    }
    
    public float[] compareImagesUsingSauvolaAtRange(File sourceImageFile, File groundTruthImageFile, float[] k_range, int[] w_range)
    {
        return compareImagesUsingSauvolaAtRange(sourceImageFile, groundTruthImageFile, k_range, w_range, false);
    }
    public float[] compareImagesUsingSauvolaAtRange(String sourceImageFile, String groundTruthImageFile, float[] k_range, int[] w_range)
    {
        return compareImagesUsingSauvolaAtRange(new File(sourceImageFile), new File(groundTruthImageFile), k_range, w_range, false);
    }
    
    public float compareImagesUsingSauvolaAtParam(File sourceImageFile, File groundTruthImageFile, float k, int w)
    {
        AccuracyMeasure accuracy = new AccuracyMeasure();
        Image groundTruthImage = Binarization.simpleThreshold(ImageUtility.readImage(groundTruthImageFile), SimpleThreshold);
        Sauvola sauvola = new Sauvola(k, w);
        sauvola.setImage(ImageUtility.readImage(sourceImageFile));
        sauvola.binarize();
        accuracy.compareImages(sauvola.getBinarizedImage(), groundTruthImage);
        return accuracy.getF1score();
    }
    public float compareImagesUsingSauvolaAtParam(String sourceImageFile, String groundTruthImageFile, float k, int w)
    {
        return compareImagesUsingSauvolaAtParam(new File(sourceImageFile), new File(groundTruthImageFile), k, w);
    }
    
    public void compareImagesInDirectory(File sourceDirectory, float[] k_range, int[] w_range)
    {
        for(File srcfile : sourceDirectory.listFiles())
        {
            if(srcfile.isDirectory())
            {
                compareImagesInDirectory(srcfile, k_range, w_range);
                continue;
            }
            
            String srcfilename = srcfile.getPath();
            int filenamelen = srcfilename.length();
            int fileExtensionIndex = srcfilename.lastIndexOf(fileExtensionSeparator);
            int fileExtensionLength = filenamelen - 1 - fileExtensionIndex;
            char[] temp = srcfilename.toCharArray();
            //if gt image skip the file
            //System.out.println(srcfilename.substring(filenamelen - 7, filenamelen - 3));
            if(srcfilename.substring(fileExtensionIndex - 3, fileExtensionIndex).equals("_gt"))
                continue;
            //make 01_in.png as 01_gt.png
            temp[fileExtensionIndex - 1] = 't';
            temp[fileExtensionIndex - 2] = 'g';
            String gtfilename = String.copyValueOf(temp);
            
            System.out.println("Image name:- gt: "+gtfilename + ", src: "+ srcfilename);
            float[] bestValues = compareImagesUsingSauvolaAtRange(srcfile, new File(gtfilename), k_range, w_range);
            System.out.println("max f1: "+ bestValues[0] + ", k: "+ bestValues[1] + ", w: "+ bestValues[2]);
        }
    }
    public void compareImagesInDirectory(String sourceDirectory, float[] k_range, int[] w_range)
    {
        compareImagesInDirectory(new File(sourceDirectory), k_range, w_range);
    }
}
