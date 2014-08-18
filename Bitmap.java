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

    public Bitmap(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Bitmap(Sprite sprite) {
        this.x = sprite.getWidth();
        this.y = sprite.getHeight();
        this.scale = 1;
        populateFromSprite(sprite);
    }
    
    public Bitmap(Sprite sprite, int scale) {
        this.x = sprite.getWidth();
        this.y = sprite.getHeight();
        this.scale = scale;
        populateFromSprite(sprite);
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
        return get((int) (x * (p.y - 1) + p.y));
    }

    /**
     * Populate Bitmap based on image
     * @param sprite 
     */
    private void populateFromSprite(Sprite sprite) {
        int bitSetIndex = 0;
        BufferedImage bImage = (BufferedImage) sprite.m_image;
        Color tempColor;
        // Loop through image according to scale
        for(int i = 0; i < sprite.getWidth(); i+=scale) {
            for(int j = 0; j < sprite.getHeight(); j+= scale) {
                // Get color at pixel i, j, if black set Bitmap index to true.
                tempColor = new Color(bImage.getRGB(i, j));
                if(tempColor.equals(Color.BLACK)) {
                    this.set(bitSetIndex);
                }
                bitSetIndex++;
            }
        }
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
}
