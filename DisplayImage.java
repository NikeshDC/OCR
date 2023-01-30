import javax.swing.JFrame;

public class DisplayImage
{
   public static int DEFAULT_HEIGHT = 600;
   
   static JFrame window;
   static ImagePanel imagePanel;
   
   public static void display(Image image, int width, int height)
   {
       if(window == null)
       {
           window = new JFrame();
           window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       }
       if(imagePanel == null)
       {
           imagePanel = new ImagePanel();
           window.getContentPane().add(imagePanel);
       }
       imagePanel.setImg(image);
       imagePanel.setBounds(0,0,width, height);
       window.setSize(width, height);
       window.setVisible(true);
   }
   public static void display(Image image)
   {
       int height = DEFAULT_HEIGHT;
       int width = (int)(height * (float)image.getWidth() / image.getHeight());
       display(image, width, height);
   }
   public static void closeDisplay()
   {
       window = null;
       imagePanel = null;
   }
   public static void hideDisplay()
   {
       window.setVisible(false);
   }
   
   public static void displayNew(Image image, int width, int height)
   {//height and width represent the window's height and width and not of image
       JFrame window = new JFrame();
       ImagePanel myimgpanel = new ImagePanel();
       myimgpanel.setImg(image);
       myimgpanel.setBounds(0,0,width, height);
       window.getContentPane().add(myimgpanel);
       window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       window.setSize(width, height);
       window.setVisible(true);
   }
   
   public static void displayNew(Image image)
   {
       int height = DEFAULT_HEIGHT;
       int width = (int)(height * (float)image.getWidth() / image.getHeight());
       displayNew(image, width, height);
   }
   
   
   
   public static void displayNew(ImagePanel imagePanel, int width, int height)
   {//height and width represent the window's height and width and not of image
       JFrame window = new JFrame();
       imagePanel.setBounds(0,0,width, height);
       window.getContentPane().add(imagePanel);
       window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       window.setSize(width, height);
       window.setVisible(true);
   }
   public static void displayNew(ImagePanel imagePanel)
   {//height and width represent the window's height and width and not of image
       int height = DEFAULT_HEIGHT;
       int width = (int)(height * (float)imagePanel.getBufferedImage().getWidth() / imagePanel.getBufferedImage().getHeight());
       displayNew(imagePanel, width, height);
   }
}
