
public class Image
{
    //only supports 8 bit depth by default
    public int[][] pixel;
    protected int sizeX; //width
    protected int sizeY; //height
    public enum TYPE {GRAY, RGB, BIN, COMP};
    protected TYPE type;
    
    private void initialize(int _sizex,int _sizey, TYPE _type)
    {
        sizeX = _sizex;
        sizeY = _sizey;
        pixel = new int[sizeX][sizeY];
        type = _type;
    }
    
    public Image(Image _image)
    {
        initialize(_image.getWidth(), _image.getHeight(), _image.getType());
        for (int i = 0; i < sizeX; i++) 
            for (int j = 0; j < sizeY; j++)
                pixel[i][j] = _image.pixel[i][j];
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
    
    public void logicalAnd(Image image)
    {//perform pixelwise subtraction
        if(image.getType() != TYPE.BIN || this.type != TYPE.BIN )
        {
            System.out.println("Cannot logical-and unbinarized Image!");
            return;
        }
        for (int i = 0; i < sizeX; i++) 
            for (int j = 0; j < sizeY; j++)
                pixel[i][j] = pixel[i][j] & image.pixel[i][j];
    }
    
    public void subtract(Image image)
    {
        subtract(image, 1.0f);
    }
    public void subtract(Image image, float reducer)
    {//perform pixelwise subtraction
        if(image.getWidth() != getWidth() || image.getHeight() != getHeight() )
        {
            System.out.println("Cannot subtract images of different size!");
            return;
        }
        if(image.getType() != getType())
        {
            System.out.println("Cannot subtract images of different types!");
            return;
        }
        for (int i = 0; i < sizeX; i++) 
            for (int j = 0; j < sizeY; j++)
                pixel[i][j] = Math.max(pixel[i][j] - (int)(image.pixel[i][j] * reducer), 0);
    }
}