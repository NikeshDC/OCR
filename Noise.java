import java.util.HashMap;
// import Image.TYPE;
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Noise {
    Image image;
    int k;
    int quanta;
    String filename;

    public Noise(Image _img) {
        image = new Image(_img);
        k = 0;
    }

    public Noise(Image _img, int _k) {
        image = new Image(_img);
        k = _k;
        quanta = 0;
    }

    public Noise(Image _img, int _quanta, String _filename) {
        image = new Image(_img);
        quanta = _quanta;
        filename = _filename;
        k = 0;
    }

    public Image reducedNoiseImage() {
        kFillReduce();
        return image;
    }

    public void kFillReduce() {
        boolean onFillOccured = false;
        boolean offFillOccured = false;
        // do {
        for (int i = 0; i < (image.getWidth() - k); i = i + k) {
            for (int j = 0; j < (image.getHeight() - k); j = j + k) {
                Image temp = getWindow(image, i, j);

                if ((getTotalOnPixels(temp) - getCornerOnPixels(temp)) >= (((k - 2) * (k - 2)) / 2)) {
                    if (offFill(temp)) {
                        offFillOccured = true;
                        setWindow(temp, i, j, false);
                    } else {
                        offFillOccured = false;
                        setWindow(temp, i, j, true);
                    }
                } else {
                    if (onFill(temp)) {
                        onFillOccured = true;
                        setWindow(temp, i, j, true);
                    } else {
                        onFillOccured = false;
                        setWindow(temp, i, j, false);
                    }
                }
            }
            // System.out.println(i);
        }
        // } while (onFillOccured || offFillOccured);
    }

    public boolean onFill(Image img) {

        int n = getTotalOnPixels(img);
        int r = getCornerOnPixels(img);
        int c = getConnectedComponents(img);

        if (c == 1 && (n > ((3 * k) - 4) || ((n == ((3 * k) - 4) && (r == 2))))) {
            return true;
        }
        return false;
    }

    public boolean offFill(Image img) {
        int n = (k * k) - getTotalOnPixels(img);
        int r = (4 * (k - 1)) - getCornerOnPixels(img);
        int c = getConnectedComponents(invertImage(img));

        if (c == 1 && (n > ((3 * k) - 4) || ((n == ((3 * k) - 4) && (r == 2))))) {
            return true;
        }
        return false;
    }

    // area analysis

    public void getTinyComponents(Component[] comps, int componentsCount) {

        int[] countOfBlackPixels = new int[componentsCount];
        HashMap<Integer, Integer> areaCount = new HashMap<Integer, Integer>();

        for (int i = 0; i < componentsCount; i++) {
            // countOfBlackPixels[i] = (((int) (comps[i].getCountOfBlackPixels() / quanta))
            // * quanta) + 1;

            countOfBlackPixels[i] = comps[i].getCountOfBlackPixels();

            if (areaCount.get(countOfBlackPixels[i]) == null) {
                areaCount.put(countOfBlackPixels[i], 1);
            }
            // else if(areaCount.get(countOfBlackPixels[i] > )){

            // }
            else {
                areaCount.put(countOfBlackPixels[i], areaCount.get(countOfBlackPixels[i]) + 1);
            }
        }
        writeToFile(areaCount, filename);
        // areaCount.forEach((key, value) -> {
        // System.out.println(key + " count " + value);
        // });

        // for (int i = 0; i < componentsCount; i++) {
        // System.out.println(comps[i].getCountOfBlackPixels() + " " +
        // countOfBlackPixels[i]);
        // }

    }

    public void removeTinyComponents(Component[] comps, int componentsCount, Segmentation segment,
            int lowerAreaThreshold, int upperAreaThreshold) {
        // (((int) (comps[i].getCountOfBlackPixels() / quanta)) * quanta)

        Component[] newComps = new Component[componentsCount];
        int count = 0;

        for (int i = 0; i < componentsCount; i++) {
            // if ((((int) (comps[i].getCountOfBlackPixels() / quanta)) * quanta) >
            // areaThreshold)
            if (comps[i].getCountOfBlackPixels() > lowerAreaThreshold
                    && comps[i].getCountOfBlackPixels() < upperAreaThreshold) {
                newComps[count] = comps[i];
                count++;
            }
        }
        segment.setComponentsArray(newComps);
    }

    //////////////////// Utility
    private Image getWindow(Image img, int i, int j) {
        Image temp = new Image(k, k, Image.TYPE.BIN);
        for (int x = 0; x < k; x++) {
            for (int y = 0; y < k; y++) {
                temp.pixel[x][y] = img.pixel[i + x][j + y];
            }
        }
        return temp;
    }

    // Modify the main image
    private void setWindow(Image img, int i, int j, boolean onFill) {
        // Image temp = new Image(img.getWidth(), img.getHeight(), Image.TYPE.BIN);
        // temp = invertImage(img);

        // Image temp = invertImage(img);
        for (int x = 1; x < k - 1; x++) {
            for (int y = 1; y < k - 1; y++) {
                // if (img.pixel[i][j] == 0) {
                // image.pixel[i + x][j + y] = 1;
                // } else {
                // image.pixel[i + x][j + y] = 0;
                // }
                if (onFill) {
                    image.pixel[x + i][y + j] = 1;
                } else {
                    image.pixel[x + i][y + j] = 0;
                }
            }
        }
    }

    private int getCornerOnPixels(Image img) {
        int count = 0;
        for (int x = 0; x < k; x++) {
            for (int y = 0; y < k; y++) {
                if (x == 0 || x == k - 1 || y == 0 || y == k - 1) {
                    if (img.pixel[x][y] == 1) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int getTotalOnPixels(Image img) {
        int count = 0;
        for (int x = 0; x < k; x++) {
            for (int y = 0; y < k; y++) {
                if (img.pixel[x][y] == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private int getConnectedComponents(Image img) {
        Segmentation segmentation = new Segmentation(img);
        segmentation.segment();
        return segmentation.getComponentsCount();
    }

    private Image invertImage(Image _img) {
        Image img = new Image(_img.getWidth(), _img.getHeight(), Image.TYPE.BIN);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                if (img.pixel[i][j] == 0) {
                    img.pixel[i][j] = 1;
                } else {
                    img.pixel[i][j] = 0;
                }
            }
        }
        return img;
    }

    // Area analysis
    private void writeToFile(HashMap<Integer, Integer> areaCount, String filename) {
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                // System.out.println("File created: " + myObj.getName());
            } else {
                // System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(myWriter);
            bw.write(Integer.toString(areaCount.size()));
            // bw.newLine();
            bw.write(",");
            areaCount.forEach((key, value) -> {
                try {

                    bw.write(key + " " + value);
                    // bw.newLine();
                    bw.write(",");

                    // System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            });
            bw.close();
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}