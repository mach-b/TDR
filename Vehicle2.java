/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import javax.vecmath.Vector2d;

/**
 *
 * @author markburton
 */
public class Vehicle2 extends Entity {
    
    protected EntityHitbox entityHitbox;
    protected Point2D rotationPoint;
    protected Rectangle collisionRectangle;
    protected double rotationRadians, speed, acceleration, 
            accelerationIncrement, turnIncrement, maxAcceleration;
    protected Point2D previousPosition, currentPosition;
    
    protected TurnState turnState;
    protected MomentumState momentumState;
    protected Vector2D movementVector;
    protected boolean drawLines;

//    public Vehicle2(Sprite sprite, int x, int y) {
//        super(sprite, x, y);
//    }
    
    public Vehicle2(Sprite sprite, int x, int y, Rectangle rectangle) {
        super(sprite, x, y);
        // Set Default Values
        this.rotationRadians = 0;
        this.speed = 0;
        this.acceleration = 0;
        this.accelerationIncrement = 50;
        this.rotationPoint = new Point2D.Double(sprite.getWidth()/2, sprite.getHeight()/2);
        this.collisionRectangle = rectangle;
        // TEMP FIX
        this.collisionRectangle = new Rectangle(10, 10, 53, 131);
        // Create EntityHitbox
        this.entityHitbox = new EntityHitbox(x, y, sprite.getWidth(), sprite.getHeight(),
                rectangle, new Point2D.Double(rectangle.getX(), rectangle.getY()),
                rotationPoint);
        this.drawLines = true;
    }
    
    @Override
    public void process(float dt) {
        if (dt > 0) {
            moveVehicle(dt);
        }
    }

    private void moveVehicle(float dt) {

        previousPosition = (Point2D) currentPosition.clone();
        
        switch (momentumState) {
            case ACCELERATING:
                accelerate(dt);
                break;
            case BRAKING:
                brake(dt);
                break;
            case REVERSING:
                reverse(dt);
                break;
            case NEUTRAL:
                coast(dt);
                break;
        }

        switch (turnState) {
            case TURNING_LEFT:
                steerLeft(dt);
                break;
            case TURNING_RIGHT:
                steerRight(dt);
                break;
            case NOT_TURNING:
                steerStraight(dt);
                break;
        }
        
        updatePosition();

    }
    
    
    @Override
    public void collidedWith(Entity e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void accelerate(float dt) {
        acceleration += accelerationIncrement*dt;
        if(acceleration > maxAcceleration) {
            acceleration = maxAcceleration;
        }
        speed += acceleration;
    }

    private void brake(float dt) {
        if(speed > 0) {
            speed -= (speed * 0.5)*dt;
        }
    }

    private void reverse(float dt) {
        speed = (-50)*dt;
    }

    private void coast(float dt) {
        speed = speed * 0.9;
    }

    private void steerLeft(float dt) {
        rotationRadians -= ((Math.PI)/12)*dt;
        if(rotationRadians < 0.0) {
            rotationRadians += (Math.PI*2);
        }
    }

    private void steerRight(float dt) {
        rotationRadians += ((Math.PI)/12)*dt;
        if(rotationRadians > (Math.PI*2)) {
            rotationRadians -= (Math.PI*2);
        }
    }

    private void steerStraight(float dt) {
        // Nothing to do
    }

    private void updatePosition() {
        movementVector= new Vector2D(0, speed);
        // Check this method may be problem with Vehicle class too !!!!!!!!
        Game.getInstance().vCalc.rotateVector(movementVector, rotationRadians);
        // ????????????????
        currentPosition.setLocation(currentPosition.getX()+movementVector.x,
                currentPosition.getY()-movementVector.y);
        entityHitbox.alignWithMap((int)currentPosition.getX(), (int)currentPosition.getY());
        entityHitbox.setWorkingLinesRotation(rotationRadians);
    }
    
    @Override
    public void draw(BackBuffer b) {
        if (!m_dead) {
            if(m_rotationAngle>Math.PI*2) {
                m_rotationAngle -= Math.PI*2;
            }
            m_EntitySprite.draw(b, (int) m_positionX, (int) m_positionY, m_rotationAngle, //m_rotationAngle (float) (Math.PI/2)
                    new Point((int) m_positionX + m_EntitySprite.getWidth()/2, 
                            (int) m_positionY + m_EntitySprite.getHeight()/2));
        }
        
        if (drawLines) {
            Game.getInstance().m_backBuffer.draw(entityHitbox.workingLineArray);
        }
    }
    
}
