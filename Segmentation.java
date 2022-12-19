import javax.swing.JFrame;

public class Segmentation {

    private int MAX_COMP = 50000;
    private int IMG_X = 10;
    private int IMG_Y = 10;

    private Image image;        //component labeled image
    private Image binaryImage;  //input binary image
    private Component listedComp[];
    private int componentsCount;
    private int componentIndex;

    private int componentSequence[];
    private int componentRoot[];
    private int componentTrailer[];

    /**
     * Constructor for objects of class Segmentation
     */
    public Segmentation(Image binImage) {
        if(binImage.getType() != Image.TYPE.BIN)
        {
            System.out.println("Cannot apply segmentation for unbinarized image.");
            binImage = null;
            return;
        }
        binaryImage = binImage;
        image = new Image(binImage);
        //copying the input binary image to new image for component labeling
        /*image = new Image(binaryImage.getWidth(), binaryImage.getHeight(), Image.TYPE.BIN);
        for (int i = 0; i < IMG_X; i++) 
            for (int j = 0; j < IMG_Y; j++)
                image.pixel[i][j] = binaryImage.pixel[i][j];*/
        
        //binarizedImage = binImage;
        IMG_X = binImage.getWidth();
        IMG_Y = binImage.getHeight();

        MAX_COMP = IMG_X * IMG_Y / 2;
        listedComp = new Component[MAX_COMP];
        componentSequence = new int[MAX_COMP];
        componentRoot = new int[MAX_COMP];
        componentTrailer = new int[MAX_COMP];
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
        int componentIndex = 2;  //initial component index (0 and 1 already used for labeling binarized image)
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
                    }
                    // For parts of components already initiallized, setValues is only called
                    else {

                        listedComp[image.pixel[i][j]].setValues(i, j);
                    }

                }
            }
        }
    }

    public void mergeSiblings() {
        componentsCount = 0;
        for (int i = 2; i < componentIndex; i++) {
            componentsCount++;
            listedComp[componentRoot[i]].mergeComp(listedComp[i]);
            if (componentRoot[i] != i) {
                listedComp[i] = null;
                componentsCount--;
            }
        }
    }
    
    public void segment(){
        labelComponents();   //label the components initially and set the equivalence relation between these components using componentSequence, componentRoot
        prepareComponentList();  //make component objects for each component labeled above and set their rectangle bounds
        mergeSiblings();  //merge the equivalent component objects
        drawRectangles(); //show rectangle for each components in a new JFrame window
    }

    public void getRectangles() {
        int count = 0;
        for (int i = 0; i < listedComp.length - 1; i++) {
            if (listedComp[i] != null) {
                listedComp[i].showValues(i);
                count++;
            }
        }
        System.out.println("components = " + count);
    }
    
    public int getComponentLength()
    {return componentsCount; }
    
    public Component[] getComponents()
    {
        Component[] _components = new Component[componentsCount];
        for (int i = 0, j = 0; i < componentIndex; i++) {
            if(listedComp[i] != null)
            {
                    _components[j] = listedComp[i];
                    j++;
            }
        }
        
        return _components;
    }

    public void drawRectangles() {
        ImagePanel imgP = new ImagePanel();
        Image img = new Image(IMG_X, IMG_Y);
        JFrame window = new JFrame();
        // printImg();
        imgP.setBuffBoundingOnBinarized(image, listedComp);

        window.getContentPane().add(imgP);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(IMG_X + 50, IMG_Y + 50);
        window.setVisible(true);

    }
}