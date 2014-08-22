/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author markburton
 */
public class ClipStore {
    
    private HashMap clipStore;
    
    public ClipStore() {
        clipStore = new HashMap<>();
    }
    
    public Clip getClip(String name) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        
        Clip toReturn = null;
        
        
        
        if(clipStore.get(name) != null) {
            //Clip located
            toReturn = ((Clip) clipStore.get(name));
        }else {
            // Load Clip
            URL url = this.getClass().getClassLoader().getResource(name);
            AudioInputStream  stream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            toReturn = clip;
            stream.close();
        }
        return toReturn;
    }
    
}
