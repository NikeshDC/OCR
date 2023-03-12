public class RectangularBound<T>
{
    public T minX;  //starting coord along x-axis
    public T maxX;  //ending coord along x-axis
    public T minY;  //starting coord along y-axis
    public T maxY;  //ending coord along y-axis
    private boolean validity = true;
    
    public boolean isValid() {return validity;}
    public void invalidate() { validity = false;}
}
