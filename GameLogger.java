/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

/**
 *
 * @author markburton
 */
public class GameLogger {
    
    public static GameLogger instance;
    
    public void logMessage(LogLevel level, String module, String message) {
        System.out.println("LogLevel:"+level.toString()+" Module:"+module+" Message:"+message);
    }
    
    public static GameLogger getInstance()
    {
        if (instance == null)
        {
            instance = new GameLogger();
        }
        return instance;
    }
}
