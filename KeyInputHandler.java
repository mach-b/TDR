package topdownracer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInputHandler extends KeyAdapter
{
    private boolean turningRight, turningLeft, accelerating, braking, reversing;
    
    public KeyInputHandler()
    {

    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            GameLogger.getInstance().logMessage(LogLevel.INFO, this.toString(), "Turning Left");
            turningLeft = true;
            Game.getInstance().turningLeft();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            GameLogger.getInstance().logMessage(LogLevel.INFO, this.toString(), "Turning Right");
            turningRight = true;
            Game.getInstance().turningRight();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_A)
        {
            GameLogger.getInstance().logMessage(LogLevel.INFO, this.toString(), "Accelerating");
            accelerating = true;
            Game.getInstance().accelerate();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_Q)
        {
            GameLogger.getInstance().logMessage(LogLevel.INFO, this.toString(), "Accelerating");
            reversing = true;
            Game.getInstance().reverse();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_Z)
        {
            braking = true;
            Game.getInstance().brake();
        }
        
        if (e.getKeyCode() == KeyEvent.VK_D)
        {
            Game.getInstance().ToggleDebugInfo();
        }
        e.consume();
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            turningLeft = false;
            if(turningRight == false) {
                Game.getInstance().stopTurning();
            }else {
                Game.getInstance().turningRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            turningRight = false;
            if(turningLeft == false) {
                Game.getInstance().stopTurning();
            }else {
                Game.getInstance().turningLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_A)
        {
            accelerating = false;
            if(braking == true) {
                Game.getInstance().brake();
            }else if(reversing == true){
                Game.getInstance().reverse();
            }else {
                Game.getInstance().neutral();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_Q)
        {
            reversing = false;
            if(braking == true) {
                Game.getInstance().brake();
            }else if(accelerating == true){
                Game.getInstance().accelerate();
            }else {
                Game.getInstance().neutral();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_Z)
        {
            braking = false;
            if(accelerating == true) {
                Game.getInstance().accelerate();
            }else if(reversing == true){
                Game.getInstance().reverse();
            }else {
                Game.getInstance().neutral();
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        
        
        
    }
}
