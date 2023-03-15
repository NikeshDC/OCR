import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class TopDownSegmenter
{
    float whitespaceThresholdFactor = 0.001f; //1% of the height or width
    int segmentationThresholdWhitespaceLength = 10; //segment upto 20 whitespacings of layout
    private Image image;
    private ProjectionProfile pp;
    private BinaryTree<RectangularBound<Integer>> layoutTree;
    private ArrayList<RectangularBound<Integer>> segments;
    private static int DEFAULT_SEGMENT_COLOR = 0xff0000ff; //blue color
    
    public TopDownSegmenter(Image image)
    {
        this.image = image;
        segmentationThresholdWhitespaceLength = (int)Math.min(30, image.getWidth() * 0.01);
        pp = new ProjectionProfile(this.image);
    }
    
    public ArrayList<RectangularBound<Integer>> getSegments()
    {
        return segments;
    }
    
    public void segment()
    {//consider the whole image as base layout
        RectangularBound<Integer> rootLayout = new RectangularBound<Integer>();
        rootLayout.minX = 0;
        rootLayout.maxX = image.getMaxX();
        rootLayout.minY = 0;
        rootLayout.maxY = image.getMaxY();
        segment(rootLayout);
        segments = new ArrayList<RectangularBound<Integer>>();
        findSegments();
    }
    public void segment(RectangularBound<Integer> rootLayout)
    {
        layoutTree = new BinaryTree<RectangularBound<Integer>>(rootLayout);
        segment(layoutTree.getRoot());
    }
    
    private void segment(BinaryTree<RectangularBound<Integer>>.Node<RectangularBound<Integer>> node)
    {
       RectangularBound<Integer> bound = node.item;
       int[] hp = pp.getHorizantalProfile(bound.minX, bound.maxX, bound.minY, bound.maxY);
       int[] vp = pp.getVerticalProfile(bound.minX, bound.maxX, bound.minY, bound.maxY);
       //most likely the bound is single pixel thick
       if (hp == null || vp == null){ bound.invalidate(); return; }
       filterBorderWhitespace(bound, hp, vp);
       //calculate new pp after changing bounds
       hp = pp.getHorizantalProfile(bound.minX, bound.maxX, bound.minY, bound.maxY);
       vp = pp.getVerticalProfile(bound.minX, bound.maxX, bound.minY, bound.maxY);
       //most probably bound contains noise only
       if (hp == null || vp == null){ bound.invalidate(); return; }
       
       Range<Integer> horizantal_max_whitespace = new Range<Integer>(0, 0, 0);
       Range<Integer> horizantal_cur_whitespace = new Range<Integer>(0, 0, 0);
       //find max whitespace in horizantal profile
       int horizantalWhitespaceThreshold = (int)(whitespaceThresholdFactor * hp.length);
       for(int i=0; i< hp.length;)
       {
           while(i < hp.length && hp[i] > horizantalWhitespaceThreshold)
                i++;
           horizantal_cur_whitespace.start = bound.minY + i;
           while(i < hp.length && hp[i] <= horizantalWhitespaceThreshold)
                i++;
           horizantal_cur_whitespace.end = bound.minY + i;
           horizantal_cur_whitespace.value = horizantal_cur_whitespace.end - horizantal_cur_whitespace.start + 1;
           if(horizantal_cur_whitespace.value > horizantal_max_whitespace.value)
           {
               horizantal_max_whitespace.setTo(horizantal_cur_whitespace);
           }
       }
       
       
       Range<Integer> vertical_max_whitespace = new Range<Integer>(0, 0, 0);
       Range<Integer> vertical_cur_whitespace = new Range<Integer>(0, 0, 0);
       //find max whitespace in vertical profile
       int verticalWhitespaceThreshold = (int)(whitespaceThresholdFactor * vp.length);
       for(int i=0; i< vp.length;)
       {
           while(i < vp.length && vp[i] > verticalWhitespaceThreshold)
                i++;
           vertical_cur_whitespace.start = bound.minX + i;
           while(i < vp.length && vp[i] <= verticalWhitespaceThreshold)
                i++;
           vertical_cur_whitespace.end = bound.minX + i;
           vertical_cur_whitespace.value = vertical_cur_whitespace.end - vertical_cur_whitespace.start + 1;
           if(vertical_cur_whitespace.value > vertical_max_whitespace.value)
           {
               vertical_max_whitespace.setTo(vertical_cur_whitespace);
           }
       }
       
       //if max-whitespaces are less than T pixels this is the end
       if(vertical_max_whitespace.value < segmentationThresholdWhitespaceLength &&
                horizantal_max_whitespace.value < segmentationThresholdWhitespaceLength)
                return;
                
       //select maximum whitespace from horizantal and vertical profile
       //and then divide the bound along that whitespace
       RectangularBound<Integer> firstBound = new RectangularBound<Integer>();
       RectangularBound<Integer> secondBound = new RectangularBound<Integer>();
       if(horizantal_max_whitespace.value > vertical_max_whitespace.value)
       {
           firstBound.minX = bound.minX;
           firstBound.maxX = bound.maxX;
           firstBound.minY = bound.minY;
           firstBound.maxY = horizantal_max_whitespace.start;
           
           secondBound.minX = bound.minX;
           secondBound.maxX = bound.maxX;
           secondBound.minY = horizantal_max_whitespace.end;
           secondBound.maxY = bound.maxY;
       }
       else
       {
           firstBound.minX = bound.minX;
           firstBound.maxX = vertical_max_whitespace.start;
           firstBound.minY = bound.minY;
           firstBound.maxY = bound.maxY;
           
           secondBound.minX = vertical_max_whitespace.end;
           secondBound.maxX = bound.maxX;
           secondBound.minY = bound.minY;
           secondBound.maxY = bound.maxY;
       }
       //add new nodes of bound to left and right according to reading order
       node.insertToLeft(firstBound);
       node.insertToRight(secondBound);
       //call segment in each of the bounds
       segment(node.getLeftChild());
       segment(node.getRightChild());
    }
    
    public void filterBorderWhitespace(RectangularBound<Integer> bound, int[] hp, int vp[])
    {
       int horizantalWhitespaceThreshold = (int)(whitespaceThresholdFactor * hp.length);
       int hfi = 0;
       while(hfi < hp.length && hp[hfi] <= horizantalWhitespaceThreshold)  //find the first value which is not a whitespace
            hfi++;
       int hli = hp.length - 1;
       while(hli >=0 && hp[hli] <= horizantalWhitespaceThreshold)  //find the last value which is not a whitespace
            hli--;
            
       int verticalWhitespaceThreshold = (int)(whitespaceThresholdFactor * vp.length);
       int vfi = 0;
       while(vfi < vp.length && vp[vfi] <= verticalWhitespaceThreshold)  //find the first value which is not a whitespace
            vfi++;
       int vli = vp.length - 1;
       while(vli >= 0 && vp[vli] <= verticalWhitespaceThreshold)  //find the last value which is not a whitespace
            vli--;
       
       if(vfi > vli || hfi > hli)
       {//most likely to be noise inside the bounds i.e. less than whitespace threshold everywhere
           bound.invalidate();
           return;
       }
           
       bound.maxX = bound.minX + vli;
       bound.maxY = bound.minY + hli;
       
       bound.minX += vfi;
       bound.minY += hfi;
    }
    
    private void findSegments()
    {//call after creating the tree of layout analysis
        segments.clear();  //clear previous segments
        addSegmentsAtLeaf(layoutTree.getRoot());
    }
    
    private void addSegmentsAtLeaf(BinaryTree<RectangularBound<Integer>>.Node<RectangularBound<Integer>> node)
    {
        if (node.isLeaf())
        {
            if(node.item != null && node.item.isValid())
                segments.add(node.item);
            return;
        }
        //else traverse in inorder
        addSegmentsAtLeaf(node.getLeftChild());
        addSegmentsAtLeaf(node.getRightChild());
    }
    
    public void addSegmentsToBufferedImage(BufferedImage bufImage, int color)
    {
        for(RectangularBound<Integer> segment : segments)
        {
            for(int ycoord = segment.minY; ycoord <= segment.maxY; ycoord ++) 
            { 
                 bufImage.setRGB(segment.minX, ycoord, color);//draw minX line
                 bufImage.setRGB(segment.maxX, ycoord, color);//draw maxX line
            }
           
            for(int xcoord = segment.minX; xcoord <= segment.maxX; xcoord ++) 
            { 
                bufImage.setRGB(xcoord, segment.minY, color);//draw minY line
                bufImage.setRGB(xcoord, segment.maxY, color);//draw maxY line
            }
        }
    }
    public void addSegmentsToBufferedImage(BufferedImage bufImage)
    {
        addSegmentsToBufferedImage(bufImage, DEFAULT_SEGMENT_COLOR);
    }
}