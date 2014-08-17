/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topdownracer;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import javax.vecmath.Vector2d;

/**
 *
 * @author markburton
 */
public class VehicleVeryBadATM extends Entity {

    // protected boolean turningRight, turningLeft, accelerating, braking;
    protected Point2D.Double centerOfSprite, rearRightWheel, rearLeftWheel;
    protected SteeringMechanism steeringMechanism;
    protected TurnState turnState;
    protected MomentumState momentumState;
    protected float accelerationIncrement = 10;
    protected float velocity;
    protected float accel, maxAccel, brakeValue;
    protected SoundEffect se1, se2;

    // Inertia, Mass ???
    public VehicleVeryBadATM(Sprite sprite, int x, int y) {
        super(sprite, x, y);
        centerOfSprite = new Point2D.Double((double)sprite.getWidth() / 2,
                (double)sprite.getHeight() / 2);
        steeringMechanism = new SteeringMechanism(50, 100, 5);
        brakeVector = new Vector2d(0, -10);
        momentumState = MomentumState.NEUTRAL;
        turnState = TurnState.NOT_TURNING;
        m_rotationAngle = 0;  // Up = 0
        velocityVector = new Vector2d(0, -1);  // 0, 0 vector not working...
        accelerationVector = new Vector2d(0, 10);
        velocity = 0;
        accel = 0;
        maxAccel = 50;
        brakeValue = 50;
        se1 = new SoundEffect("assets/EngineNoise4.wav");
        se1.play();
     
    }

    @Override
    public void collidedWith(Entity e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void process(float dt) {
        if (dt > 0) {
            moveVehicle(dt);
        }
    }

    public void steerLeft(float dt) {
        steeringMechanism.turnLeft();
        Vector2d steeringV2d = steeringMechanism.steeringAdjustedPositionVector((velocity * dt));
        float rotation = steeringMechanism.steeringVehicleRotationRadians((velocity * dt));
        steeringV2d = Game.getInstance().vCalc.rotateVector(steeringV2d, m_rotationAngle);
        m_rotationAngle += rotation;
        m_positionX -= steeringV2d.x;
        m_positionY -= steeringV2d.y;
    }

    public void steerRight(float dt) {
        steeringMechanism.turnRight();
        Vector2d steeringV2d = steeringMechanism.steeringAdjustedPositionVector(velocity * dt);
        float rotation = steeringMechanism.steeringVehicleRotationRadians(velocity * dt);
        steeringV2d = Game.getInstance().vCalc.rotateVector(steeringV2d, m_rotationAngle);
        m_rotationAngle += rotation;
        m_positionX -= steeringV2d.x;
        m_positionY -= steeringV2d.y;
    }

    public void steerStraight(float dt) {
        steeringMechanism.stopTurning();
        Vector2d steeringV2d = steeringMechanism.steeringAdjustedPositionVector(velocity * dt);
        float rotation = steeringMechanism.steeringVehicleRotationRadians(velocity * dt);
        steeringV2d = Game.getInstance().vCalc.rotateVector(steeringV2d, m_rotationAngle);
        m_rotationAngle += rotation;
        m_positionX -= steeringV2d.x;
        m_positionY -= steeringV2d.y;
   
    }

//    public void accelerate(float dt) {
//        System.out.println("Velocity x = "+velocityVector.x+", y = "+velocityVector.y);
//        accelerationVector = Game.getInstance().vCalc.increaseVectorLength(accelerationVector, accelerationIncrement*dt);
//        velocityVector = Game.getInstance().vCalc.increaseVectorLength(velocityVector, accelerationVector.length()*dt);
//    }
    public void accelerate(float dt) {
        accel += accelerationIncrement;
        if (accel > maxAccel) {
            accel = maxAccel;
        }
        velocity += accel * dt;
    }

    public void brake(float dt) {
        velocity -= brakeValue * dt;
        if (velocity < 0) {
            velocity = 0;
        }
    }

    public void reverse(float dt) {

    }

    public void coast(float dt) {

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

    }

}
