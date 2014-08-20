package topdownracer;

import java.awt.FontFormatException;
import java.util.HashMap;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;

public class SpriteStore
{
    //Member data:
    private HashMap m_spriteStoreContainer;
    
    //Member functions:
    public SpriteStore()
    {
        m_spriteStoreContainer = new HashMap();
        
        // TODO: Log State Change: "SpriteStore Created"
    }
    
    public Sprite getSprite(String filename) 
    {
        Sprite toReturn = null;

        if (m_spriteStoreContainer.get(filename) != null)
        {    
            // Sprite already loaded...
            System.out.println("Fetching preloaded image");
            toReturn = ((Sprite) m_spriteStoreContainer.get(filename));
        }
        else
        {
            // New sprite to load...
            System.out.println("Loading image");
            toReturn = new Sprite(filename);

            m_spriteStoreContainer.put(filename, toReturn);
            
            // TODO: Log State Change: "Sprite Loaded"
        }
        
        return (toReturn);
    }
}
