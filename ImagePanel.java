import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    Image img;
    BufferedImage bimg;

    public void setImg(Image _img) {
        img = _img;
        setBuffImg();
    }
    private void setBuffImg() {
        bimg = ImageUtility.getBufferedImage(img);
    }
    public BufferedImage getBufferedImage()
    {
        return bimg;
    }
    
    public void paint(Graphics g) {
        if (img != null)
            g.drawImage(bimg, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public void setBuffImgComp(Image _img) {
        img = _img;
        bimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        int pix;
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                if (img.pixel[i][j] != 0) {
                    pix = (img.pixel[i][j]) | 0xff800000;
                    bimg.setRGB(i, j, pix);
                } else {
                    bimg.setRGB(i, j, 0xffffffff);
                }
            }
        }
    }

    public void setBuffBoundingOnBinarized(Image _img, Component[] listedComp) {
        img = _img;
        bimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                if (img.pixel[i][j] == 0)
                    bimg.setRGB(i, j, 0xffffffff);
                else
                    bimg.setRGB(i, j, 0x000000);
            }
        }
        for (int i = 0; i < listedComp.length - 1; i++) {
            if (listedComp[i] != null) {

                for (int j = listedComp[i].getMinX(); j < listedComp[i].getMaxX(); j++) {
                    bimg.setRGB(j, listedComp[i].getMinY(), 0xffff0000);
                    // System.out.println("fa");
                    bimg.setRGB(j, listedComp[i].getMaxY(), 0xffff0000);

                }

                for (int j = listedComp[i].getMinY(); j < listedComp[i].getMaxY(); j++) {
                    bimg.setRGB(listedComp[i].getMinX(), j, 0xffff0000);
                    bimg.setRGB(listedComp[i].getMaxX(), j, 0xffff0000);

                }
            }
        }
    }
}
