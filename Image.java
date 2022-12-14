
public class Image
{
    //only supports 8 bit depth by default
    public int[][] pixel;
    protected int sizeX; //width
    protected int sizeY; //height
    public enum TYPE {GRAY, RGB, BIN, UND, COMP};
    protected TYPE type;
    
    private void initialize(int _sizex,int _sizey, TYPE _type)
    {
        sizeX = _sizex;
        sizeY = _sizey;
        pixel = new int[sizeX][sizeY];
        type = _type;
    }
    
    public Image(int _sizex,int _sizey, TYPE _type)
    { 
        initialize(_sizex, _sizey, _type); 
    }
    
    public Image(int _sizex,int _sizey)
    { 
        initialize(_sizex, _sizey, TYPE.GRAY); 
    }
    
    public void setType(TYPE _type)
    { type = _type; }
    
    public TYPE getType()
    { return type; }
    
    public int getWidth()
    { return sizeX; }
    
    public int getMaxX()
    {  return sizeX -1;}
    
    public int getHeight()
    { return sizeY; }
    
    public int getMaxY()
    { return sizeY -1; }
}