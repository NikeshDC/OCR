import java.awt.image.BufferedImage;

public class ProjectionProfile
{
    Image image;
    int DEFAULT_PROJECTION_COLOR = 0xffff0000;
    
    void setImage(Image image)
    {
        if(image.getType() != Image.TYPE.BIN)
        {
            System.out.println("Image not in binarized form. <ProjectionProfile>");
        }
        this.image = image;
    }
    
    public ProjectionProfile(Image image)
    {
        setImage(image);
    }
    
    
    public int[] getHorizantalProfile(int xs, int xe, int ys, int ye)
    {//get horizantal profile within given bounds of the image
        if (xs > image.getMaxX() || xe > image.getMaxX() || ys > image.getMaxY() || ye > image.getMaxY())
        {
            System.out.println("Given bound exceeds Image bound. <ProjectionProfile>");
            return null;
        }
        if ( xs < 0 || xe < 0 || ys <0 || ye < 0)
        {
            System.out.println("Given bounds not appropriate. Some bound less than zero. <ProjectionProfile>");
            return null;
        }
        if (xs >= xe || ys >= ye)
        {
            System.out.println("Given bounds not appropriate. smaller bound greater or equal to larger bound. <ProjectionProfile>");
            return null;
        }
        int[] horizantalProfile = new int[ye -  ys + 1];
        for(int i=0; i< horizantalProfile.length ; i++)
        {
            int ycoord = i + ys;
            for(int xcoord = xs; xcoord <= xe; xcoord++)
                horizantalProfile[i] += image.pixel[xcoord][ycoord];
        }
        return horizantalProfile;
    }
    public int[] getHorizantalProfile()
    {
        return getHorizantalProfile(0, image.getMaxX(), 0, image.getMaxY());
    }
    
    
    public int[] getVerticalProfile(int xs, int xe, int ys, int ye)
    {//get horizantal profile within given bounds of the image
        if (xs > image.getMaxX() || xe > image.getMaxX() || ys > image.getMaxY() || ye > image.getMaxY())
        {
            System.out.println("Given bound exceeds Image bound. <ProjectionProfile>");
            return null;
        }
        if ( xs < 0 || xe < 0 || ys <0 || ye < 0)
        {
            System.out.println("Given bounds not appropriate. Some bound less than zero. <ProjectionProfile>");
            return null;
        }
        if (xs >= xe || ys >= ye)
        {
            System.out.println("Given bounds not appropriate. smaller bound greater or equal to larger bound. <ProjectionProfile>");
            return null;
        }
        int[] verticalProfile = new int[xe -  xs + 1];
        for(int i=0; i< verticalProfile.length ; i++)
        {
            int xcoord = i + xs;
            for(int ycoord = ys; ycoord <= ye; ycoord++)
                verticalProfile[i] += image.pixel[xcoord][ycoord];
        }
        return verticalProfile;
    }
    public int[] getVerticalProfile()
    {
        return getVerticalProfile(0, image.getMaxX(), 0, image.getMaxY());
    }
    
    
    public void addHorizantalProfileToBufferedImage(BufferedImage bufImage, int color)
    {
        int[] horizantalProfile = getHorizantalProfile();
        if(bufImage.getHeight() != image.getHeight() || bufImage.getHeight() != image.getHeight())
        {
            System.out.println("BufferedImage and Image dont have same size!<ProjectionProfile>");
            return;
        }
        for(int ycoord = 0; ycoord < image.getHeight(); ycoord++)
        {
            int xcoord = image.getWidth() - horizantalProfile[ycoord];
            for(; xcoord < image.getWidth(); xcoord++)
            {
                bufImage.setRGB(xcoord, ycoord, color);
            }
        }
    }
    public void addHorizantalProfileToBufferedImage(BufferedImage bufImage)
    {
        addHorizantalProfileToBufferedImage(bufImage, DEFAULT_PROJECTION_COLOR);
    }
    
    public void addVerticalProfileToBufferedImage(BufferedImage bufImage, int color)
    {
        int[] verticalProfile = getVerticalProfile();
        if(bufImage.getHeight() != image.getHeight() || bufImage.getHeight() != image.getHeight())
        {
            System.out.println("BufferedImage and Image dont have same size!<ProjectionProfile>");
            return;
        }
        for(int xcoord = 0; xcoord < image.getWidth(); xcoord++)
        {
            int ycoord = image.getHeight() - verticalProfile[xcoord];
            for(; ycoord < image.getHeight(); ycoord++)
            {
                bufImage.setRGB(xcoord, ycoord, color);
            }
        }
    }
    public void addVerticalProfileToBufferedImage(BufferedImage bufImage)
    {
        addVerticalProfileToBufferedImage(bufImage, DEFAULT_PROJECTION_COLOR);
    }
    
    public void addProjectionProfileToBufferedImage(BufferedImage bufImage, int color)
    {
        addHorizantalProfileToBufferedImage(bufImage, color);
        addVerticalProfileToBufferedImage(bufImage, color);
    }
    public void addProjectionProfileToBufferedImage(BufferedImage bufImage)
    {
        addHorizantalProfileToBufferedImage(bufImage);
        addVerticalProfileToBufferedImage(bufImage);
    }
}
