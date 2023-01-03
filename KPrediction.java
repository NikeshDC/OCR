public class KPrediction
{
    // instance variables - replace the example below with your own
    private double predicted_k;

    public KPrediction(Image image)
    {
        if(image.getType() != Image.TYPE.GRAY)
            System.out.println("Image not in grayscale. <KPrediction>");
        //best predictors are s0/u1sqrRoot from otsu and entropy
        Otsu otsu = new Otsu();
        otsu.setImage(image);
        otsu.calculateThreshold();
        double s1 = otsu.s1;
        double u0 = otsu.u0;
        double s1byu0SqrRoot = Math.pow((s1/u0), 0.5);
        
        ImageHistogram hist = otsu.getHistogram();
        double entropy = hist.getEntropy();
        //System.out.println("s1: "+s1+", u0: "+u0+", s1byu0SqrRoot: "+s1byu0SqrRoot+", entropy: "+entropy);
        
        //linear regression formula obtained by experimenting with dataset
        predicted_k = 0.149662 * s1byu0SqrRoot + 0.025375 * entropy - 0.11769;
        //System.out.println("un-adjusted: "+predicted_k);
        
        //if less than t make it larger and if greater make smaller
        // double t = 0.3;
       
        // if(predicted_k < t)
        // {
            // double lower_max = 1.3;
            // double lower_min = 1;
            // double multiplier_lower = (lower_min - lower_max)/t + lower_max;
            
            // predicted_k *= multiplier_lower;
        // }
        // else
        // {
            // double upper_max = 0.64;
            // double upper_min = 1;
            // double multiplier_upper = (upper_max - upper_min)/(1 - t) + upper_min;
            // predicted_k *= multiplier_upper;
        // }
    }
    
    public KPrediction(Otsu otsu, int[] bounds)
    {
        initialize(otsu, bounds[0], bounds[1], bounds[2], bounds[3]);
    }
    public KPrediction(Otsu otsu, int xs, int xe, int ys, int ye)
    {
        initialize(otsu, xs, xe, ys, ye);
    }
    public void initialize(Otsu otsu, int xs, int xe, int ys, int ye)
    {
        //local k prediction within bounds (xs,ys) & (xe, ye) for the image
        //best predictors are s0/u1sqrRoot from otsu and entropy
        //Otsu otsu = new Otsu();
        //otsu.setImage(image);
        otsu.calculateThresholdLocally(xs, xe, ys, ye);
        double s1 = otsu.s1;
        double u0 = otsu.u0;
        double s1byu0SqrRoot = Math.pow((s1/u0), 0.5);
        
        ImageHistogram hist = otsu.getHistogram();
        double entropy = hist.getEntropy();
        //System.out.println("s1: "+s1+", u0: "+u0+", s1byu0SqrRoot: "+s1byu0SqrRoot+", entropy: "+entropy);
        
        //linear regression formula obtained by experimenting with dataset
        predicted_k = 0.149662 * s1byu0SqrRoot + 0.025375 * entropy - 0.11769;
    }
    
    public double getK()
    {   return predicted_k;}

}
