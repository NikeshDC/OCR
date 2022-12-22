import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatasetPreparer
{
    String sauvolaFileNameAdditive = "sauv";  //appends this name to dataset images created using sauvola's method
    String otsuFileNameAdditive = "otsu";  //appends this name to dataset images created using otsu's method
    char infoSeperator = '_';  //string to use to seperate distinct elements of information in the names of dataset images created
    char fileExtensionSeperator = '.';
    
    boolean wAdditiveFirst = true;   //should w parameter be appended first in the image name
    
    private String[] getFileNameAndExtension(String path)
    {//returns the filename and extension given a path for a file
        int fileNameStartIndex = path.lastIndexOf(File.separator) + 1; //if no directory seperator is present lastindex returns -1 added to 1 makes 0
        int fileExtensionStartIndex = path.lastIndexOf(fileExtensionSeperator) + 1;
        if (fileExtensionStartIndex == 0) //no extension
            System.out.println("Image file has no extension!<From DatasetPreparer>");
            
        String fileName = path.substring(fileNameStartIndex, fileExtensionStartIndex - 1);
        String fileExtension = path.substring(fileExtensionStartIndex - 1);
        String[] fileNameExt = {fileName, fileExtension};
        return (fileNameExt);
    }
    
    private String getParameterAdditive(float k, int w)
    {
        if(wAdditiveFirst)
            return "w" + w + infoSeperator + "k" + String.format("%.02f", k);
        else
            return "k" + String.format("%.02f", k)+ "w" + w + infoSeperator;
    }
    
    public void createBinarizedImagesForParameterRange(String inputDirectory, String outputDirectoryPath,
                        float[] k_range, int[] w_range )
    {
        createBinarizedImagesForParameterRange(new File(inputDirectory), outputDirectoryPath, k_range, w_range);
    }
    
    public void createBinarizedImagesForParameterRange(File inputDirectory, String outputDirectoryPath,
                        float[] k_range, int[] w_range )
    {//recursively moves inside input-root directory and creates its corresponding images using sauvola binarization at same level in output-root directory
        //k-range must contain {start, step, end} in order; end and start is inclusive, similar is for w-range
        if(!inputDirectory.isDirectory())
        {
            System.out.println("Directory doesn't exist. Input directory must be present for preparing dataset images");
            return;
        }
        
        //if output directory doesn't exist create it first
        try{
        Files.createDirectories(Paths.get(outputDirectoryPath));}
        catch(IOException ie)
        {
            System.out.println("Couldn't create output directory!<From DatasetPreparer>");
        }
       
        //w-range
        int min_w = w_range[0];
        int step_w = w_range[1];
        int max_w = w_range[2];
        //k-range
        float min_k = k_range[0];
        float step_k = k_range[1];
        float max_k = k_range[2];
        
        //check the file entries 
        for(File fileEntry : inputDirectory.listFiles())
        {
            if(fileEntry.isDirectory())
            {//recursively move down
                String nextDirectory = outputDirectoryPath + File.separator + fileEntry.getName();
                createBinarizedImagesForParameterRange(fileEntry, nextDirectory, k_range, w_range);
                continue;
            }
            String fileNameExtension[] = getFileNameAndExtension(fileEntry.getName());
            String fileName = fileNameExtension[0];
            String fileExtension = fileNameExtension[1];
            
            Image srcImg = ImageUtility.readImage(fileEntry);
            Sauvola sauvola = new Sauvola(0.25f, 70);
            sauvola.setImage(srcImg);

            for(float k=min_k; k <= max_k; k+=step_k)
            {
                for(int w=min_w; w<= max_w; w+= step_w)
                {
                    sauvola.setParameters(k, w);
                    sauvola.binarize();
                    String outFileName = outputDirectoryPath + File.separator + 
                                    fileName + infoSeperator + sauvolaFileNameAdditive + infoSeperator + 
                                    getParameterAdditive(k, w) + fileExtensionSeperator + fileExtension;
                                    
                    ImageUtility.writeImage(sauvola.getBinarizedImage(), outFileName);
                }
            }
        }
    }
}
