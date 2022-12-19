public class Component {
    private int MAX_COORD = 99999;
    // instance variables - replace the example below with your own
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public Component() {
        minX = MAX_COORD;
        maxX = 0;
        minY = MAX_COORD;
        maxY = 0;
    }

    public int[] getRect() {
        int[] rectt = new int[4];
        int length = maxX - minX;
        int height = maxY - minY;
        rectt[0] = minX;
        rectt[1] = minY;
        rectt[2] = height;
        rectt[3] = length;
        return rectt;
    }

    public void setValues(int x, int y) {
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

    // Merges a part of a twin system, the object itself, to another part i.e the
    // passed parameter c
    public void mergeComp(Component c) {
        // minX is of this object and maxX is of the object c
        minX = c.getMinX() < minX ? c.getMinX() : minX;
        minY = c.getMinY() < minY ? c.getMinY() : minY;

        maxX = c.getMaxX() > maxX ? c.getMaxX() : maxX;
        maxY = c.getMaxY() > maxY ? c.getMaxY() : maxY;

    }

    public void showValues(int i) {
        System.out.println(i + ":" + minX + " " + minY + " " + maxX + " " + maxY);
    }

}
