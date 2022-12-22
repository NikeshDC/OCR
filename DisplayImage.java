import javax.swing.JFrame;

public class DisplayImage
{
   public static int DEFAULT_HEIGHT = 600;
   
   public static void display(Image image, int width, int height)
   {//height and width represent the window's height and width and not of image
       JFrame window = new JFrame();
       ImagePanel myimgpanel = new ImagePanel();
       myimgpanel.setImg(image);
       myimgpanel.setBounds(0,0,width, height);
       window.getContentPane().add(myimgpanel);
       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       window.setSize(width, height);
       window.setVisible(true);
   }
   
   public static void display(Image image)
   {
       int height = DEFAULT_HEIGHT;
       int width = (int)(height * (float)image.getWidth() / image.getHeight());
       display(image, width, height);
   }
}
