import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.awt.image.BufferedImage;

public class TopDownSegmenterForServer
{
    public static void main(String args[])
    {
        String imageName = args[0];
        String outputfileName = args[1];
        String outputImageName;
        if(args.length > 2)
            outputImageName = args[2];
        else
            outputImageName = "segmented_image.jpg";
        segmentAndSave(imageName, outputfileName, outputImageName);
    }
    
    public static void segmentAndSave(String imageName, String outputFileName, String outImageName)
    {//saves rectangular bounds of segments in order in a text file
        Image srcImage = ImageUtility.readImageInGrayScale(imageName);
        Image imageBin = Binarization.simpleThreshold(srcImage, 128);
        //Image testImageBin = BinarizeForServer.getBinarizedUsingPredictor(testImagePath);
        
        TopDownSegmenter segmenter = new TopDownSegmenter(imageBin);
        segmenter.segment();
        ArrayList<RectangularBound<Integer>> segments = segmenter.getSegments();
        
        //save the segmented image layout also
        BufferedImage outBufImage = ImageUtility.getBufferedImage(imageBin);
        //adding the boundry boxes to image
        segmenter.addSegmentsToBufferedImage(outBufImage);
        ImageUtility.writeImage(outBufImage, outImageName);
        
        //write the segments to the file in order
        File outfile = new File(outputFileName);
        outfile.delete();  //if old file of same name exists remove it
        try{
            outfile.createNewFile();
            FileWriter writer = new FileWriter(outfile);
             
            String content = "minX,maxX,minY,maxY" + System.lineSeparator();   //first line is order of coordinates for bound
            content += segments.size();  //second line is the number of bounds
            //then comes all the bounds
            for(RectangularBound<Integer> segment : segments)
            {
                String bounds = segment.minX+ "," + segment.maxX+ "," + segment.minY+ "," +segment.maxY;
                content += System.lineSeparator() + bounds;
            }
            writer.write(content);
            writer.close();
        }
        catch(IOException exception)
        {
            System.out.println("Cannot create output file!");
        }
    }
}
