/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author markburton
 */
public class HeadsUpDisplay {
    
    
    private Point2D.Double speedPoint;
    private ArrayList<Sprite> speedNums;
    private Sprite speedText;
    
    public HeadsUpDisplay() {
        speedNums = new ArrayList<>();
        speedPoint = new Point2D.Double(700, 30);
    }
    
    public void setSpeedTextSprite(Sprite s) {
        speedText = s;
    }
    
    public void addSpeedNumbersSprites(Sprite s) {
        speedNums.add(s);
    }
    
    
    // SHOULD SCALE SPEED - REFACTOR!!!
    public void drawSpeed(int speed, BackBuffer b) {
        String speedString = Integer.toString(speed);
        //  NEEDS REFACTOR MESSY
        speedText.draw(b, (int)(speedPoint.x-b.camera.x), (int)(speedPoint.y-b.camera.y));
        int offset = speedText.getWidth();
        for(int i = 0; i<speedString.length(); i++) {
            char c = speedString.charAt(i);
            int index = Character.getNumericValue(c);
            Sprite toDraw = speedNums.get(index);
            toDraw.draw(b, (int)(speedPoint.x+offset-b.camera.x), (int)(speedPoint.y-b.camera.y));
            offset += toDraw.getWidth();
        }
    }
    
}
