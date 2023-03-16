import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Files;

public class PostNoiseReducerForServer {
    public static void main(String args[]) {
        postReduceNoiseAndSave(args[0], args[1], args[2]);
        // postReduceNoiseAndSave("1.jpg", "1.txt", "denoised_temp_test_1.png");
    }

    public static void postReduceNoiseAndSave(String imagepath, String readingOrderTextFile, String savepath) {
        try {
            Image srcimg = ImageUtility.readImageInGrayScale(imagepath);
            List<String> allLines = Files.readAllLines(Paths.get(readingOrderTextFile));
            // convert to binarized form
            Image binarizedImage = Binarization.simpleThreshold(srcimg, 128);
            Image combinedImage = new Image(binarizedImage.getWidth(), binarizedImage.getHeight(), Image.TYPE.BIN);

            allLines.remove(0);
            allLines.remove(0);
            for (String line : allLines) {
                String[] bounds = line.split(",");
                int minX = Integer.parseInt(bounds[0]);
                int maxX = Integer.parseInt(bounds[1]);
                int minY = Integer.parseInt(bounds[2]);
                int maxY = Integer.parseInt(bounds[3]);

                Image img = binarizedImage.getCroppedImage(minX, maxX, minY, maxY);
                NoiseReducer noiseReducer = new NoiseReducer();
                int[] widthHeight = noiseReducer.getAverageDimensions(img);
                int width = widthHeight[0];
                int height = widthHeight[1];

                int heightScoreThreshold = height * 10;
                int widthScoreThreshold = width * 10;

                Segmentation segmentation = new Segmentation(img);
                segmentation.segment();

                Component[] heightFilteredcomponents = noiseReducer.checkHeightScorePX(segmentation.getComponents(),
                        segmentation.getComponentsCount(),
                        heightScoreThreshold);
                segmentation.setComponentsArray(heightFilteredcomponents);
                Component[] widthFilteredcomponents = noiseReducer.checkHeightScorePX(segmentation.getComponents(),
                        segmentation.getComponentsCount(),
                        widthScoreThreshold);
                segmentation.setComponentsArray(widthFilteredcomponents);
                img = ImageUtility.addComponentsOnImage(segmentation.getComponents(), segmentation.getComponentsCount(),
                        img.sizeX, img.sizeY);
                for (int i = 0; i < img.sizeX; i++) {
                    for (int j = 0; j < img.sizeY; j++) {
                        combinedImage.pixel[i + minX][j + minY] = img.pixel[i][j];
                    }
                }

            }
            // save binarized image
            ImageUtility.writeImage(combinedImage, savepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
