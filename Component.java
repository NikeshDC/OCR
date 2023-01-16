public class Component {
    private int MAX_COMP = 99999;
    // instance variables - replace the example below with your own
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int height;
    private int width;
    private int countOfBlackPixels;
    private boolean componentSet;
    private Image selfImage;

    public Component() {
        minX = MAX_COMP;
        maxX = 0;
        minY = MAX_COMP;
        maxY = 0;
        countOfBlackPixels = 0;
        componentSet = false;
    }

    public void setRect() {
        width = maxX - minX + 1;
        height = maxY - minY + 1;
    }

    public int[] getRect() {
        int[] rectt = new int[4];
        rectt[0] = minX;
        rectt[1] = minY;
        rectt[2] = height;
        rectt[3] = width;
        return rectt;
    }

    public float getArea() {
        setRect();

        // System.out.println(height + "*" + width + " = " + height * width);
        return (height * width);
    }

    public void increaseCountOfBlackPixels() {
        countOfBlackPixels++;
    }

    public void setValues(int x, int y) {
        if (minX == MAX_COMP && maxX == 0 && minY == MAX_COMP && maxY == 0) {
            componentSet = true;
        }
        // Comparing and setting X-axis limits for components
        if (x < minX) {
            // System.out.println("hi maathi bata" + minX + " x:" + x);
            minX = x;
            // System.out.println("hi" + minX+" x:"+x);
        }

        if (x > maxX) {
            maxX = x;
            // System.out.println("hello" + maxX + " maxx:" + x);
        }

        // Comparing and setting Y-axis limits for components
        if (y < minY) {
            // System.out.println("y = " + y + " minY= " + minY);
            minY = y;
        }

        if (y > maxY)
            maxY = y;
    }

    // Getter functions to get co-ordnate values
    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getCountOfBlackPixels() {
        return countOfBlackPixels;
    }

    // Merges a part of a twin system, the object itself, to another part i.e the
    // passed parameter c
    public void mergeComp(Component c) {
        // minX is of this object and maxX is of the object c
        minX = c.getMinX() < minX ? c.getMinX() : minX;
        minY = c.getMinY() < minY ? c.getMinY() : minY;

        maxX = c.getMaxX() > maxX ? c.getMaxX() : maxX;
        maxY = c.getMaxY() > maxY ? c.getMaxY() : maxY;

        countOfBlackPixels += c.getCountOfBlackPixels();
    }

    public void showValues(int i) {
        System.out.println(i + ":" + minX + " " + minY + " " + maxX + " " + maxY);
    }

    public void setImage(Image img) {

        if (componentSet) {
            setRect();
            selfImage = new Image(width, height, Image.TYPE.BIN);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    selfImage.pixel[i][j] = img.pixel[i + minX][j + minY];
                }
            }
        }
    }

    public Image getImage() {
        return selfImage;
    }

    public void addComponentOnImage(Image img) {
        if (componentSet) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    img.pixel[i + minX][j + minY] = selfImage.pixel[i][j];
                }
            }
        }
    }

}
