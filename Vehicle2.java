/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.awt.Rectangle;
import java.awt.geom.Point2D;

/**
 *
 * @author markburton
 */
public class Vehicle2 extends Entity {
    
    protected EntityHitbox entityHitbox;
    protected Point2D rotationPoint;
    protected Rectangle collisionRectangle;
    protected double rotationRadians, speed, acceleration;

//    public Vehicle2(Sprite sprite, int x, int y) {
//        super(sprite, x, y);
//    }
    
    public Vehicle2(Sprite sprite, int x, int y, Rectangle rectangle) {
        super(sprite, x, y);
        // Set Default Values
        rotationRadians = 0;
        speed = 0;
        acceleration = 0;
        rotationPoint = new Point2D.Double(sprite.getWidth()/2, sprite.getHeight()/2);
        collisionRectangle = rectangle;
        // Create EntityHitbox
        entityHitbox = new EntityHitbox(x, y, sprite.getWidth(), sprite.getHeight(),
                rectangle, new Point2D.Double(rectangle.getX(), rectangle.getY()),
                rotationPoint);
        
    }

    
    
    @Override
    public void collidedWith(Entity e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
