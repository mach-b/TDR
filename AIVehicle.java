/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.vecmath.Vector2d;

/**
 *
 * @author markburton
 */
public class AIVehicle extends Vehicle {

    protected Waypoint nextWaypoint;
    protected boolean atWaypoint;
    protected double distanceToNextPoint, angleToNextPointRadians;
    protected int waypointIndex;
    
    public AIVehicle(Sprite sprite, int x, int y) {
        super(sprite, x, y);
        waypointIndex = 0;
        setNextWaypoint();
        this.entityHitbox.alignWithEntity(x, y);
        this.maxSpeed = 18;

    }
    
    @Override
    public void process(float dt) {
        driveToNextWaypoint(dt);
        updatePosition();
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//        System.out.println("%% AI xPos:"+m_positionX+", yPos:"+m_positionY);
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    private void driveToNextWaypoint(float dt) {
        distanceToNextPoint = currentPosition.distance(nextWaypoint);
        calculateAngleToNextPoint();
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
//        System.out.println("%% ANGLE TO NEXT:"+angleToNextPointRadians);
//        System.out.println("%% Current Rotation:"+rotationRadians);
//        System.out.println("%% Distance to Waypoint:"+distanceToNextPoint);
//        System.out.println("%% Speed:"+(int)speed);
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        steerTowardsNextWaypoint(dt);
        adjustMomentum(dt);
    }
    
    public boolean checkArrivalAtWaypoint() {
        if(rotationPoint.distance(nextWaypoint)<20) {
            waypointIndex++;
            if(waypointIndex == 12) {
                waypointIndex = 0;
            }
        }
        return rotationPoint.distance(nextWaypoint)<20;
    }
    
    private void setNextWaypoint() {
        nextWaypoint = WaypointHandler.getInstance().getGoodWaypoint(waypointIndex);
    }
    

    private void calculateAngleToNextPoint() {
        double a = rotationPoint.x - nextWaypoint.x;
        double b = rotationPoint.y - nextWaypoint.y;
        
        angleToNextPointRadians = Math.atan2(a,-b); // ????????
        
    }
    
        private void steerTowardsNextWaypoint(float dt) {
        double a = angleToNextPointRadians;
        double b = normalizeAngle(a);
        double c = rotationRadians;
        double d = b-c;
        double e = c-b;
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("Dir to waypoint 'a'="+angleToNextPointRadians);
        System.out.println("b="+b);
        System.out.println("c="+c);
        System.out.println("d="+d);
        System.out.println("e="+e);
        System.out.println("Current Dir="+rotationRadians);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        if(d < 0.03 || d < ((Math.PI*2)-0.03)) {
            if(b - c > 0) {  // Obtuse or acute?
                // rotate one way???
                rotationRadians -= ((Math.PI) / 2) * dt;
            }else {
                // rotate the other???
                rotationRadians += ((Math.PI) / 2) * dt;
            }
        }
        if (rotationRadians>Math.PI*2) {
            rotationRadians -= (Math.PI*2);
        }
        if (rotationRadians<0) {
            rotationRadians = (Math.PI*2)+rotationRadians;
        }
        //rotationRadians = angleToNextPointRadians;
    }

//    private void steerTowardsNextWaypoint(float dt) {
//        double a = rotationRadians - angleToNextPointRadians;
//        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//        System.out.println("Dir to waypoint="+angleToNextPointRadians);
//        System.out.println("Current Dir="+rotationRadians);
//        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//        if(a > 0.03 || a < -0.03) {
//            if(a < 0) {
//                // rotate one way???
//                rotationRadians -= ((Math.PI) / 2) * dt;
//            }else {
//                // rotate the other???
//                rotationRadians += ((Math.PI) / 2) * dt;
//            }
//        }
//        if (rotationRadians>Math.PI*2) {
//            rotationRadians -= (Math.PI*2);
//        }
//        if (rotationRadians<0) {
//            rotationRadians = (Math.PI*2)+rotationRadians;
//        }
//        //rotationRadians = angleToNextPointRadians;
//    }

    private void adjustMomentum(float dt) {
        acceleration += accelerationIncrement*dt;
        speed += acceleration*dt;
        if(speed > maxSpeed) {
            speed = maxSpeed;
        }
    }
    
    
    @Override
    public void updatePosition() {
        previousPosition = (Point2D.Double) currentPosition.clone();
        movementVector = new Vector2d(0, speed);
        // Check this method may be problem with Vehicle class too !!!!!!!!
        if (speed != 0) {
            movementVector = Game.getInstance().vCalc.rotateVector(movementVector, rotationRadians);
        }
        currentPosition.setLocation(currentPosition.getX() + movementVector.x,
                currentPosition.getY() - movementVector.y);
//        System.out.println("CurrentX=" + currentPosition.getX() + ", CurrentY=" + currentPosition.getY());
        this.entityHitbox.alignWithEntity((int) currentPosition.getX(), (int) currentPosition.getY());
        this.entityHitbox.setWorkingLinesRotation(rotationRadians);
        
        // Validate Position
        if (!onTrack()) {
//            System.out.println("Off Track!");
            currentPosition = previousPosition;
            
            rotationRadians = previousRotation;
//            System.out.println("*********************************");
//            System.out.println("AI off Track");
//            System.out.println("Point1:"+entityHitbox.workingPointArray[0].toString());
//            System.out.println("AI x="+m_positionX+", y="+m_positionY);
//            System.out.println("*********************************");
            speed = 0;
//            bounce(movementVector);
//            if(!onTrack()) {
//                currentPosition = previousPosition;
//            }
        }
        checkCollisionAgainstOpponents();
    }

    private double normalizeAngle(double a) {
        double toReturn = 0.0;
        if (a > Math.PI*2) {
            toReturn = a - (Math.PI*2);
        }
        if (a < 0) {
            toReturn = (Math.PI*2)+a;
        }
        return toReturn;
    }
    
   
    public synchronized void checkCollisionAgainstOpponents() {
        ArrayList<AIVehicle> temp = Game.getInstance().getOpponentContainer();
//        temp.stream().filter((e) -> (this != e && isCollidingWith(e))).forEach((e) -> {
//            collidedWith(e);
//            System.out.println("Collided with other Opponent");
//        });
//        for(AIVehicle ai : temp) {
//            if(Game.getInstance().getPlayer().isCollidingWith(ai)) {
//                collidedWith(ai);
//            }
//        }
        if (this.isCollidingWith(Game.getInstance().getPlayer())) {
            collidedWith(Game.getInstance().getPlayer());
            System.out.println("Collided with Player");
        }
    }
    
}
