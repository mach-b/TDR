/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topdownracer;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import javax.vecmath.Vector2d;

/**
 *
 * @author markburton
 */
public class Vehicle extends Entity {

    protected EntityHitbox entityHitbox;
    protected Point2D rotationPoint;
    protected Rectangle collisionRectangle;
    protected double rotationRadians, speed, acceleration,
            accelerationIncrement, turnIncrement, maxAcceleration, maxSpeed;
    protected Point2D previousPosition, currentPosition;

    protected TurnState turnState;
    protected MomentumState momentumState;
    protected Vector2d movementVector;
    protected boolean drawLines;

    public Vehicle(Sprite sprite, int x, int y) {
        super(sprite, x, y);
        // Set Default Values
        this.rotationRadians = 0;
        this.speed = 0;
        this.acceleration = 0;
        this.accelerationIncrement = 1;
        this.maxAcceleration = 5;
        this.maxSpeed = 25;
        this.rotationPoint = new Point2D.Double(sprite.getWidth() / 2, sprite.getHeight() / 2);
        // TEMP FIX
        this.collisionRectangle = new Rectangle(8, 8, 38, 99);
        // Create EntityHitbox
        this.entityHitbox = new EntityHitbox(x, y, sprite.getWidth(), sprite.getHeight(),
                collisionRectangle, new Point2D.Double(collisionRectangle.getX(), collisionRectangle.getY()),
                rotationPoint);
        this.drawLines = true;
        this.momentumState = MomentumState.NEUTRAL;
        this.turnState = TurnState.NOT_TURNING;
        currentPosition = new Point2D.Double(x, y);
        previousPosition = currentPosition;
        entityHitbox.alignWithEntity((int) currentPosition.getX(), (int) currentPosition.getY());

    }

    public Vehicle(Sprite sprite, int x, int y, Rectangle rectangle) {
        super(sprite, x, y);
        // Set Default Values
        this.rotationRadians = 0;
        this.speed = 0;
        this.acceleration = 0;
        this.accelerationIncrement = 2;
        this.maxAcceleration = 10;
        this.maxSpeed = 40;
        this.rotationPoint = new Point2D.Double(sprite.getWidth() / 2, sprite.getHeight() / 2);
        this.collisionRectangle = rectangle;
        // TEMP FIX
        this.collisionRectangle = new Rectangle(8, 8, 40, 100);
        // Create EntityHitbox
        this.entityHitbox = new EntityHitbox(x, y, sprite.getWidth(), sprite.getHeight(),
                rectangle, new Point2D.Double(rectangle.getX(), rectangle.getY()),
                rotationPoint);
        this.drawLines = true;
        this.momentumState = MomentumState.NEUTRAL;
        this.turnState = TurnState.NOT_TURNING;
        currentPosition = new Point2D.Double(x, y);
        previousPosition = currentPosition;
        entityHitbox.alignWithEntity((int) currentPosition.getX(), (int) currentPosition.getY());

    }

    @Override
    public void process(float dt) {
        if (dt > 0) {
            moveVehicle(dt);
        }
    }

    private void moveVehicle(float dt) {

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
        acceleration += accelerationIncrement * dt;
        if (acceleration > maxAcceleration) {
            acceleration = maxAcceleration;
        }
        speed += acceleration;
        if (speed > maxSpeed) {
            speed = maxSpeed;
        }
    }

    private void brake(float dt) {
        if (speed != 0) {
            speed -= (speed * 0.8) * dt;
        }
        if (speed > 0 && speed < 1) {
            speed = 0;
        }
    }

    private void reverse(float dt) {
        if (speed > 0) {
            brake(dt);
        }
        speed -= accelerationIncrement * dt;
        if (speed < -30) {
            speed = -30;
        }
    }

    private void coast(float dt) {
        if (speed > 0) {
            speed -= (speed * 0.3) * dt;
        }

    }

    private void steerLeft(float dt) {
        if (speed != 0) {
            rotationRadians -= ((Math.PI) / 2) * dt;
            if (rotationRadians < 0.0) {
                rotationRadians += (Math.PI * 2);
            }
        }
    }

    private void steerRight(float dt) {
        if (speed != 0) {
            rotationRadians += ((Math.PI) / 2) * dt;
            if (rotationRadians > (Math.PI * 2)) {
                rotationRadians -= (Math.PI * 2);
            }
        }
    }

    private void steerStraight(float dt) {
        // Nothing to do
    }

    private void updatePosition() {
        previousPosition = (Point2D) currentPosition.clone();
        movementVector = new Vector2d(0, speed);
        // Check this method may be problem with Vehicle class too !!!!!!!!
        if (speed != 0) {
            movementVector = Game.getInstance().vCalc.rotateVector(movementVector, rotationRadians);
        }
        currentPosition.setLocation(currentPosition.getX() - movementVector.x,
                currentPosition.getY() - movementVector.y);
        System.out.println("CurrentX=" + currentPosition.getX() + ", CurrentY=" + currentPosition.getY());
        entityHitbox.alignWithEntity((int) currentPosition.getX(), (int) currentPosition.getY());
        entityHitbox.setWorkingLinesRotation(rotationRadians);

        // Validate Position
        if (!onTrack()) {
            System.out.println("Off Track!");
            currentPosition = previousPosition;
        }
    }

    @Override
    public void draw(BackBuffer b) {
        if (!m_dead) {

            if (m_rotationAngle > Math.PI * 2) {
                m_rotationAngle -= Math.PI * 2;
            }
            m_EntitySprite.draw(b, (int) currentPosition.getX(), (int) currentPosition.getY(), (float) rotationRadians, //m_rotationAngle (float) (Math.PI/2)
                    new Point2D.Double((int) currentPosition.getX() + m_EntitySprite.getWidth() / 2,
                            (int) currentPosition.getY() + m_EntitySprite.getHeight() / 2));
        }

        if (drawLines) {
            Game.getInstance().m_backBuffer.draw(entityHitbox);
        }
    }

    private void setUpCamera() {
        Game.getInstance().m_backBuffer.camera.setX(rotationPoint.getX()
                - (Game.getInstance().m_backBuffer.m_windowWidth / 2));
        Game.getInstance().m_backBuffer.camera.setY(rotationPoint.getY()
                - (Game.getInstance().m_backBuffer.m_windowHeight / 2));
    }

    private boolean onTrack() {
        for (int i = 0; i < 4; i++) {
            //if(Game.getInstance().track.bitmap.getBit(entityHitbox.workingPointArray[i])==false) {
//            System.out.println("Color="+Game.getInstance().track.bitmap.getBufferedImageBit(entityHitbox.workingPointArray[i]).toString());
//            System.out.println("Black.toString()="+Color.BLACK.toString());
//            System.out.println("Color int="+Game.getInstance().track.bitmap.getBufferedImageBit(entityHitbox.workingPointArray[i]).getRGB());
//            System.out.println("Black.getRGB()="+Color.BLACK.getRGB());
            if (Game.getInstance().track.bitmap.getBufferedImageBit(entityHitbox.workingPointArray[i]).getRGB()
                    != (Color.BLACK.getRGB())) {
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                System.out.println("i=" + i + ", collision detected.");
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                return false;
            }
        }
        return true;
    }

//    private boolean onTrack() {
//        for(int i = 0; i<4; i++) {
//            //if(Game.getInstance().track.bitmap.getBit(entityHitbox.workingPointArray[i])==false) {
//            System.out.println("Color="+Game.getInstance().track.bitmap.getBufferedImageBit(entityHitbox.workingPointArray[i]).toString());
//            System.out.println("Black.toString()="+Color.BLACK.toString());
//            System.out.println("Color int="+Game.getInstance().track.bitmap.getBufferedImageBit(entityHitbox.workingPointArray[i]).getRGB());
//            System.out.println("Black.getRGB()="+Color.BLACK.getRGB());
//            if(Game.getInstance().track.bitmap.getBufferedImageBit(entityHitbox.workingPointArray[i]).getRGB()
//                    !=(Color.BLACK.getRGB())) {
//                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//                System.out.println("i="+i+", collision detected.");
//                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//                return false;
//            }
//        }
//        return true;
//    }
}
