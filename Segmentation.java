import javax.swing.JFrame;
import java.awt.image.BufferedImage;

public class Segmentation {

    private int MAX_COMP = 50000;
    private int IMG_X = 10;
    private int IMG_Y = 10;

    private Image image; // component labeled image
    private Image binaryImage; // input binary image
    private Component listedComp[];
    private Component components[];

    // values in components is correct
    private boolean COMPONENTS_SET;
    private boolean COMPONENT_IMAGE_SET;
    // Count of components after merging siblings
    private int componentsCount;

    // component index i.e. label count
    private int componentTotalCount;

    private int componentSequence[];
    private int componentRoot[];
    private int componentTrailer[];

    /**
     * Constructor for objects of class Segmentation
     */
    public Segmentation(Image binImage) {
        if (binImage.getType() != Image.TYPE.BIN) {
            System.out.println("Cannot apply segmentation for unbinarized image.");
            binImage = null;
            return;
        }
        binaryImage = binImage;
        image = new Image(binImage);
        // copying the input binary image to new image for component labeling
        /*
         * image = new Image(binaryImage.getWidth(), binaryImage.getHeight(),
         * Image.TYPE.BIN);
         * for (int i = 0; i < IMG_X; i++)
         * for (int j = 0; j < IMG_Y; j++)
         * image.pixel[i][j] = binaryImage.pixel[i][j];
         */

        // binarizedImage = binImage;
        IMG_X = binImage.getWidth();
        IMG_Y = binImage.getHeight();

        MAX_COMP = IMG_X * IMG_Y / 2;
        listedComp = new Component[MAX_COMP];
        componentSequence = new int[MAX_COMP];
        componentRoot = new int[MAX_COMP];
        componentTrailer = new int[MAX_COMP];
        COMPONENTS_SET = false;
        COMPONENT_IMAGE_SET = false;
    }

    public void printImg() {
        for (int j = 0; j < IMG_Y; j++) {
            for (int i = 0; i < IMG_X; i++) {
                System.out.print(image.pixel[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void labelComponents() {
        int componentIndex = 2; // initial component index (0 and 1 already used for labeling binarized image)
        image.setType(Image.TYPE.COMP);
        // boolean neighbourFound;
        for (int j = 0; j < IMG_Y; j++) {
            for (int i = 0; i < IMG_X; i++) {
                if (image.pixel[i][j] == 1) {
                    // Checking upper neighbour
                    if (((j > 0 && image.pixel[i][j - 1] != 0))) {
                        image.pixel[i][j] = image.pixel[i][j - 1];
                        // Checking left neighbour for SIBLINGS
                        if (((i > 0 && image.pixel[i - 1][j] != 0)) && image.pixel[i][j] != image.pixel[i - 1][j]) {

                            int rootX = componentRoot[image.pixel[i][j]];
                            int rootY = componentRoot[image.pixel[i - 1][j]];
                            if (rootX == rootY) {
                                continue;
                            }
                            componentSequence[rootY] = componentTrailer[rootX];
                            componentTrailer[rootX] = componentTrailer[rootY];

                            int temp1 = componentTrailer[rootY];

                            while (temp1 != componentSequence[rootY]) {
                                // System.out.println("Root changed.");
                                componentRoot[temp1] = rootX;
                                temp1 = componentSequence[temp1];
                            }
                        }
                    }
                    // Checking left neighbour
                    else if (((i > 0 && image.pixel[i - 1][j] != 0))) {
                        image.pixel[i][j] = image.pixel[i - 1][j];
                        // neighbourFound = true;
                    }

                    // Labelling as new component
                    else {
                        image.pixel[i][j] = componentIndex;
                        componentIndex++;
                        componentSequence[image.pixel[i][j]] = image.pixel[i][j];

                        componentRoot[image.pixel[i][j]] = image.pixel[i][j];
                        componentTrailer[image.pixel[i][j]] = image.pixel[i][j];
                    }
                }
            }
        }
        componentTotalCount = componentIndex;
    }

    public void prepareComponentList() {
        for (int i = 0; i < IMG_X; i++) {
            for (int j = 0; j < IMG_Y; j++) {
                // For every black pixel i.e part of some component
                if (image.pixel[i][j] != 0) {
                    // Initially all indices are null

                    // listedComp array has objects defined at indices corresponding to component
                    // labels other indices have no object efined i.e. are null
                    if (listedComp[image.pixel[i][j]] == null) {

                        // Defining an oject at index of component label for every new component label
                        // encountered during the pass
                        listedComp[image.pixel[i][j]] = new Component();

                        // setValues compares and sets diagonal co-ordinate values for component
                        // rectangle
                        listedComp[image.pixel[i][j]].setValues(i, j);
                        listedComp[image.pixel[i][j]].increaseCountOfBlackPixels();
                    }
                    // For parts of components already initiallized, setValues is only called
                    else {

                        listedComp[image.pixel[i][j]].setValues(i, j);
                        listedComp[image.pixel[i][j]].increaseCountOfBlackPixels();
                    }

                }
            }
        }
    }

    public void mergeSiblings() {
        componentsCount = componentTotalCount - 2;
        for (int i = 2; i < componentTotalCount; i++) {
            if (componentRoot[i] != i) {
                listedComp[componentRoot[i]].mergeComp(listedComp[i]);
                setPixelVal(image, listedComp[i], componentRoot[i]);
                listedComp[i] = null;

                componentsCount--;
            }

        }
    }

    private void setPixelVal(Image img, Component c, int val) {
        for (int i = c.getMinX(); i < c.getMaxX() + 1; i++) {
            for (int j = c.getMinY(); j < c.getMaxY() + 1; j++) {
                if (img.pixel[i][j] != 0) {
                    img.pixel[i][j] = val;
                }
            }
        }
    }

    public Component[] getComponents() {
        if (COMPONENTS_SET) {
            return components;
        } else {
            storeComponents();
        }
        return components;
    }

    public void storeComponents() {
        components = new Component[componentsCount];

        for (int i = 0, j = 0; i < componentTotalCount; i++) {
            if (listedComp[i] != null) {
                components[j] = listedComp[i];
                j++;
            }
        }
        COMPONENTS_SET = true;
        // return components;
    }

    public void setComponentsImage() {
        for (int i = 0; i < componentTotalCount; i++) {
            if (listedComp[i] != null) {
                listedComp[i].setImage(binaryImage);
            }
        }
        COMPONENT_IMAGE_SET = true;
    }

    public void segment() {
        labelComponents(); // label the components initially and set the equivalence relation between these
                           // components using componentSequence, componentRoot

        prepareComponentList(); // make component objects for each component labeled above and set their
                                // rectangle bounds

        mergeSiblings(); // merge the equivalent component objects

        setComponentsImage();
        
        // drawRectangles(listedComp); // show rectangle for each components in a new
        // JFrame
        // window

    }

    public void getRectangles() {
        int count = 0;
        for (int i = 0; i < components.length - 1; i++) {
            if (components[i] != null) {
                components[i].showValues(i);
                count++;
            }
        }
        System.out.println("components = " + count);
    }

    public void drawRectangles(Component[] comp) {
        // System.out.println("Comp" + componentsCount);
        ImagePanel imgP = new ImagePanel();
        JFrame window = new JFrame();
        // printImg();
        imgP.setBuffBoundingOnBinarized(image, comp);

        window.getContentPane().add(imgP);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(IMG_X + 50, IMG_Y + 50);
        window.setVisible(true);

    }

    public void saveComponentImage(Component[] comp, String savepath) {
        ImagePanel imgP = new ImagePanel();
        BufferedImage bimg = imgP.getbBufferedImageWithComponents(image, comp);
        ImageUtility.writeImage(bimg, savepath);

    }

    public Image getSegmentedImage() {
        return image;
    }

    public void setComponentsArray(Component[] comps) {
        components = comps;
        int count = 0;
        for (int i = 0; i < componentsCount; i++) {
            if (comps[i] != null) {
                count++;
            }
        }
        componentsCount = count;
        // componentsCount = comps.length;
    }

    public int getComponentsCount() {
        return componentsCount;
    }
}
