/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class SoundEffect 
{
    private Clip m_clip;
    
    public SoundEffect(String filename)
    {
        // LOG SFX Loading...
        GameLogger.getInstance().logMessage(LogLevel.INFO, this.toString(), "Loading SFX");
        try
        {
            URL url = this.getClass().getClassLoader().getResource(filename);
            
            AudioInputStream stream = AudioSystem.getAudioInputStream(url);
            
            m_clip = AudioSystem.getClip();
            
            m_clip.open(stream);
        }
        catch (Exception e)
        {
            // LOG Loading Error...
            GameLogger.getInstance().logMessage(LogLevel.INFO, this.toString(), "Error:"+e);
        }
    }
    
    public void play()
    {
        if (m_clip.isRunning())
        {
            m_clip.stop();
        }
        m_clip.loop(Clip.LOOP_CONTINUOUSLY);
        m_clip.setFramePosition(0);
        m_clip.start();
    }
    
//    import javax.sound.sampled.*;
//
//AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
//    new File("some_file.wav"));
//Clip clip = AudioSystem.getClip();
//clip.open(audioInputStream);
//FloatControl gainControl = 
//    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
//clip.start();
    
}


