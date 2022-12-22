public class AccuracyMeasure
{//accuracy measure for binary classification
    private int tp; //true positives
    private int tn; //true negative
    private int fp;
    private int fn;
    
    void compareImages(Image source, Image groundTruth)
    {
        if(source == null)
            System.out.println("Can't compare images for accuracy. Source image null!");
        if(groundTruth == null)
            System.out.println("Can't compare images for accuracy. Ground-truth image null!");
        if(source.getWidth() != groundTruth.getWidth() || 
                source.getHeight() != groundTruth.getHeight())
            System.out.println("Cannot compare images of different sizes for accuracy");

        if (source.getType() != Image.TYPE.BIN || groundTruth.getType() != Image.TYPE.BIN)
            System.out.println("Can only compare binarized images for accuracy");
        
        tp = tn = fp = fn = 0;
        for(int i=0; i< source.getWidth(); i++)
        {
            for(int j=0; j< source.getHeight(); j++)
            {
                //1(text) is positive class and 0(background) is negative
                if(source.pixel[i][j] == 1 && groundTruth.pixel[i][j] == 1) 
                    tp++;
                else if(source.pixel[i][j] == 1 && groundTruth.pixel[i][j] == 0) //positive identification is false
                    fp++;
                else if(source.pixel[i][j] == 0 && groundTruth.pixel[i][j] == 0)
                    tn++;
                else //(source.pixel[i][j] == 0 && groundTruth.pixel[i][j] == 1)
                    fn++;
            }
        }
    }
    
    float getAccuracy()
    {
        float accuracy =((float)tp + tn) / (tp + tn + fp + fn);
        return accuracy;
    }
    
    float getF1score()
    {
        //f1 = 2*precision*recall/(precision + recall)
        return (2.0f * tp)/ (2*tp+ fp + fn );
    }
}
