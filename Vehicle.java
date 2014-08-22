/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topdownracer;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.vecmath.Vector2d;

/**
 *
 * @author markburton
 */
public class Vehicle extends Entity {

    //protected EntityHitbox entityHitbox;
    protected Point2D.Double rotationPoint;
    protected Rectangle collisionRectangle;
    protected double rotationRadians, speed, acceleration,
            accelerationIncrement, turnIncrement, maxAcceleration, maxSpeed;
    protected Point2D.Double previousPosition, currentPosition;

    protected TurnState turnState;
    protected MomentumState momentumState;
    protected Vector2d movementVector;
    protected boolean drawLines;
    protected double previousRotation;
    protected Clip clip;
    protected Clip tyreSound;
    private boolean speedMax;

    public Vehicle(Sprite sprite, int x, int y) {
        super(sprite, x, y);
        // Set Default Values
        this.rotationRadians = 0;
        this.speed = 0;
        this.acceleration = 0;
        this.accelerationIncrement = 3;
        this.maxAcceleration = 10;
        this.maxSpeed = 20;
        this.rotationPoint = new Point2D.Double(sprite.getWidth() / 2, sprite.getHeight() / 2);
        // TEMP FIX
        this.collisionRectangle = new Rectangle(8, 8, 38, 99);
        // Create EntityHitbox
        this.entityHitbox = new EntityHitbox(x, y, sprite.getWidth(), sprite.getHeight(),
                collisionRectangle, new Point2D.Double(collisionRectangle.getX(), collisionRectangle.getY()),
                rotationPoint);
        this.drawLines = false;
        this.momentumState = MomentumState.NEUTRAL;
        this.turnState = TurnState.NOT_TURNING;
        this.currentPosition = new Point2D.Double(x, y);
        this.previousPosition = currentPosition;
        this.entityHitbox.alignWithEntity((int) currentPosition.x, (int) currentPosition.y);

    }


    @Override
    public void process(float dt) {
        if (dt > 0) {
            moveVehicle(dt);
        }
    }

    public void setClip(Clip c) {
        this.clip = c;
    }

    public void setTyreSound(Clip c) {
        tyreSound = c;
    }

    public void playClip() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            //clip.setFramePosition(0);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.start();
        }
    }

    private void moveVehicle(float dt) {
        previousPosition = (Point2D.Double) currentPosition.clone();
        previousRotation = rotationRadians;

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
    public synchronized boolean isCollidingWith(Entity e) {
        boolean colliding = false;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(entityHitbox.workingLineArray[i].intersectsLine(e.entityHitbox.workingLineArray[j])) {
                    colliding = true;
                }
            }
        }
        return colliding;
    }
    @Override
    public void collidedWith(Entity e) {
        
        speed = speed*0.5;
        currentPosition = previousPosition;
    }

    private void accelerate(float dt) {
        speedMax = (speed == maxSpeed);
        acceleration += accelerationIncrement * dt;
        if (acceleration > maxAcceleration) {
            acceleration = maxAcceleration;
        }
        speed += acceleration * dt; // ****************** ?????????????????????
        if (speed > maxSpeed) {
            if(!speedMax) {
                spawnExplosion();
            }
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
        acceleration = 0;
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
        if (speed == maxSpeed && tyreSound != null && !tyreSound.isRunning()) {
            tyreSound.start();
        }
    }

    private void steerRight(float dt) {
        if (speed != 0) {
            rotationRadians += ((Math.PI) / 2) * dt;
            if (rotationRadians > (Math.PI * 2)) {
                rotationRadians -= (Math.PI * 2);
            }
        }
        if (speed == maxSpeed && tyreSound != null && !tyreSound.isRunning()) {
            tyreSound.start();
        }
    }

    private void steerStraight(float dt) {
        if (tyreSound != null && tyreSound.isRunning()) {
            tyreSound.stop();
            tyreSound.setFramePosition(0);
        }
    }

    public void updatePosition() {
        previousPosition = (Point2D.Double) currentPosition.clone();
        movementVector = new Vector2d(0, speed);
        // Check this method may be problem with Vehicle class too !!!!!!!!
        if (speed != 0) {
            movementVector = Game.getInstance().vCalc.rotateVector(movementVector, rotationRadians);
        }
        currentPosition.setLocation(currentPosition.getX() - movementVector.x,
                currentPosition.getY() - movementVector.y);
//        System.out.println("CurrentX=" + currentPosition.getX() + ", CurrentY=" + currentPosition.getY());
        this.entityHitbox.alignWithEntity((int) currentPosition.getX(), (int) currentPosition.getY());
        this.entityHitbox.setWorkingLinesRotation(rotationRadians);

        // Validate Position
        if (!onTrack()) {
//            System.out.println("Off Track!");
            currentPosition = previousPosition;

            rotationRadians = previousRotation;

            speed = speed*0.5;
            //bounce(movementVector);
//            if (!onTrack()) {
//                currentPosition = previousPosition;
//            }
        }
        checkCollisionAgainstOpponents();
    }

    @Override
    public void draw(BackBuffer b) {
        if (!m_dead) {
            if (rotationRadians > Math.PI * 2) {
                rotationRadians -= Math.PI * 2;
            }
            m_EntitySprite.draw(b, (int) currentPosition.x, (int) currentPosition.y, (float) rotationRadians,
                    new Point2D.Double((int) currentPosition.x + m_EntitySprite.getWidth() / 2,
                            (int) currentPosition.y + m_EntitySprite.getHeight() / 2));
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

    protected boolean onTrack() {
        for (int i = 0; i < 4; i++) {
            if (Game.getInstance().track.bitmap.getBufferedImageBit(entityHitbox.workingPointArray[i]).getRGB()
                  != (Color.BLACK.getRGB())) {
                return false;
            }
        }
        return true;
    }

    public void bounce(Vector2d v) {
        // Rotate invalid movement by PI Radians
        movementVector = Game.getInstance().vCalc.rotateVector(v, Math.PI);
        // Bounce player back a proportion of rotated Vector
        currentPosition.setLocation(currentPosition.getX() - movementVector.x * 0.2,
                currentPosition.getY() - movementVector.y * 0.2);
        // Set Hitbox
        entityHitbox.alignWithEntity((int) currentPosition.x, (int) currentPosition.y);
        entityHitbox.setWorkingLinesRotation(rotationRadians);
    }


    public synchronized void checkCollisionAgainstOpponents() {
        ArrayList<AIVehicle> temp = Game.getInstance().getOpponentContainer();
        for(AIVehicle ai : temp) {
            if(Game.getInstance().getPlayer().isCollidingWith(ai)) {
                collidedWith(ai);
            }
        }
    }

    private void spawnExplosion() {
        Game.getInstance().spawnExplosion((int)(currentPosition.x+rotationPoint.x),
                (int)(currentPosition.y+rotationPoint.y));
    }
}
