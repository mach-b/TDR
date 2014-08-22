/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topdownracer;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.BitSet;

/**
 *
 * @author markburton
 */
public class Bitmap extends BitSet {

    
    protected int x;  // the width of the map
    protected int y;  // the height of the map
    protected int scale;  // the factor to scale the map by
    protected Sprite sprite;
    protected BufferedImage bufferedImage;

    public Bitmap(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Bitmap(Sprite sprite)  {
        this.x = sprite.getWidth();
        this.y = sprite.getHeight();
        this.scale = 1;
        this.sprite = sprite;
        this.bufferedImage = (BufferedImage) sprite.m_image;
        //populateFromSprite();
        //outPutAsZerosandOnes();
    }
    
    public Bitmap(Sprite sprite, int scale)  {
        this.x = sprite.getWidth();
        this.y = sprite.getHeight();
        this.scale = scale;
        this.sprite = sprite;
        this.bufferedImage = (BufferedImage) sprite.m_image;
        //populateFromSprite();
        //outPutAsZerosandOnes();
    }
    
    /**
     * Get serialized Bitmap
     * @param fileName 
     */
    public Bitmap(String fileName) {
        // get file
        // de-serialize
    }

    /**
     * Return bit at index Point p
     *
     * @param p a point in 2d space
     * @return boolean
     */
    public boolean getBit(Point2D.Double p) {
//        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//        System.out.println("Getting Point x="+p.x+ ", y="+p.y);
//        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        return get((int) ((int)x * ((int)p.y - 1) + (int)p.y));
        
    }

    /**
     * Populate Bitmap based on image
     * @param sprite 
     */
    private void populateFromSprite()  {
        long start = System.currentTimeMillis();
        int bitSetIndex = 0;
        BufferedImage bImage = (BufferedImage) sprite.m_image;
        //BufferedImage img = ImageIO.read(new File("assets/LoopBitmap.bmp"));
        int color;
        // Loop through image according to scale
        for(int i = 0; i < sprite.getWidth(); i+=scale) {
            for(int j = 0; j < sprite.getHeight(); j+= scale) {
                // Get color at pixel i, j, if black set Bitmap index to true.
                color = bImage.getRGB(i, j);
                if(color == Color.BLACK.getRGB()) { //tempColor.equals(Color.black)) {
                    this.set(bitSetIndex, true);
                    //System.out.println("'BLACK' Color = "+color + " i="+ i + ", j="+j);
                }
                bitSetIndex++;
            }
        }
        long end = System.currentTimeMillis();
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        System.out.println("BITMAP DONE :)");
//        System.out.println("Time to build = "+(end-start)+"ms");
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
    
    /**
     * Serialize and Output Bitmap to File to save re-processing each time
     * @param fileName 
     * @throws java.io.FileNotFoundException 
     */
    public void saveBitmapToDisk(String fileName) throws FileNotFoundException, IOException {
        // serialize
        // save to disk
        FileOutputStream fout = new FileOutputStream("assets/"+fileName+".bitmap");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(this);
    }

    Color getBufferedImageBit(Point2D.Double aDouble) {
        return new Color(bufferedImage.getRGB((int)aDouble.x, (int)aDouble.y));
    }
    
    public void outPutAsZerosandOnes() {
        for(int i = 0; i < this.size(); i++) {
            if(i%x == 0) {
                System.out.print("\n");
            }
            if(this.get(i)==false) {
                System.out.print("0");
            }else {
                System.out.print("1");
            }
        }
    }
}
