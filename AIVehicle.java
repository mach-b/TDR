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
public class AIVehicle extends Vehicle {

    protected Waypoint nextWaypoint;
    protected boolean atWaypoint;
    protected double distanceToNextPoint, angleToNextPointRadians;
    protected int waypointIndex;
    
    public AIVehicle(Sprite sprite, int x, int y) {
        super(sprite, x, y);
        waypointIndex = 0;
        setNextWaypoint();
        
    }
    
    @Override
    public void process(float dt) {
        driveToNextWaypoint(dt);
        updatePosition();
    }

    private void driveToNextWaypoint(float dt) {
        distanceToNextPoint = rotationPoint.distance(nextWaypoint);
        calculateAngleToNextPoint();
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
        
        angleToNextPointRadians = Math.tan(b/a);
        
    }

    private void steerTowardsNextWaypoint(float dt) {
        double a = rotationRadians - angleToNextPointRadians;
        if(a > 0.03 || a < -0.03) {
            if(a < 0) {
                // rotate one way???
                rotationRadians += ((Math.PI) / 2) * dt;
            }else {
                // rotate the other???
                rotationRadians -= ((Math.PI) / 2) * dt;
            }
        }
    }

    private void adjustMomentum(float dt) {
        acceleration += accelerationIncrement*dt;
        speed += acceleration*dt;
        if(speed >maxSpeed) {
            speed = maxSpeed;
        }
    }
    
    
    
    
}
