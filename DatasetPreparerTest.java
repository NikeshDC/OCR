
public class DatasetPreparerTest
{
    DatasetPreparer datasetPreparer;
    float[] k_range = {0f, 0.1f, 1.0f};
    int[] w_range = {10,10,100};
    
    public DatasetPreparerTest()
    {
        datasetPreparer = new DatasetPreparer();
    }
    
    public void createBinarizedImagesForParameterRange()
    {
        String inputdir = "DIBCO";
        String outputdir = "DIBCO_OUT";
        
        datasetPreparer.createBinarizedImagesForParameterRange(inputdir, outputdir, k_range, w_range);
    }

}
