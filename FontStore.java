/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 *
 * @author markburton
 */
public class FontStore {
    private HashMap fontContainer;
    
    public FontStore()
    {
        fontContainer = new HashMap();
    }
    
    public Font getFont(String filename) throws FontFormatException, IOException 
    {
        Font toReturn = null;

        if (fontContainer.get(filename) != null)
        {    
            // Font already loaded...
            System.out.println("Fetching preloaded image");
            toReturn = ((Font) fontContainer.get(filename));
        }
        else
        {
            // New font to load...
            URL url = this.getClass().getClassLoader().getResource(filename);
            System.out.println("Loading font");
            toReturn = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            toReturn.deriveFont(Font.PLAIN, 100);
            GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();

            ge.registerFont(toReturn);

            
            fontContainer.put(filename, toReturn);
        }
        return (toReturn);
    }
}
