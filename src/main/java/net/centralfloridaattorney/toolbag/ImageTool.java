package net.centralfloridaattorney.toolbag;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTool {

    public static void main(String[] args) {
        ImageTool imageTool = new ImageTool();
    }

    public void saveHTML(){

    }

    public void saveMPG(){

    }

    public void saveImage(){

    }

    public static BufferedImage getBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bufferedImage.getGraphics();
        bg.drawImage(image, 0, 0, null);
        bg.dispose();
        return bufferedImage;
    }

    public static BufferedImage getBufferedImage(String filePath){

        return null;
    }

    public static Image getImage(String filePath){
        Image image = null;
        filePath = FileTool.getFullFilePath(filePath);
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
