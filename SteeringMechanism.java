/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topdownracer;

import javax.vecmath.Vector2d;

/**
 *
 * @author markburton
 */
public class SteeringMechanism {

    // Center wheels
    //protected boolean centered;
    // Turn radius values, maxTurnRadius set as to not extend to a very, very, very, big number.

    protected float minTurnRadius, currentTurnRadius, maxTurnRadus, stepValue;
    protected int numSteps; // The number of steps from max to min turn radius.
    protected TurnState turnState;

    public SteeringMechanism(float minRadius, float maxRadius, int numSteps) {
        this.minTurnRadius = minRadius;
        this.maxTurnRadus = maxRadius;
        this.numSteps = numSteps;
        this.stepValue = (maxRadius - minRadius) / numSteps;
        turnState = TurnState.NOT_TURNING;
    }

    // Returns vector to new position relative to current turn radius
    public Vector2d steeringAdjustedPositionVector(float distance) {
        if (turnState == TurnState.NOT_TURNING) {
            return new Vector2d(0, distance);
        }
        if (distance > 0) {
            // Calculate arc of turn.
            float theta = distance / currentTurnRadius;
            // Calculate magnitude of vector that represents the start and end of arc.
            //float magnitude = 2*(float)((Math.sin(Math.toDegrees(theta)/2) * currentTurnRadius));
            float magnitude = 2*(float)((Math.sin(theta/2) * currentTurnRadius));
            //float magnitude = distance;
            // calculate angle of vector, isosceles :)
            float angle = (float) ((Math.PI - theta) / 2);
            // Calculate and return vector
            System.out.println("Distance=" + distance + " Angle=" +
                    Math.toDegrees(angle) + " Magnitude=" + magnitude + " Theta=" + theta);
            if (turnState == TurnState.TURNING_RIGHT) {
                return new Vector2d((float) (Math.cos(Math.toDegrees(angle)) * magnitude),
                        (float) (Math.sin(Math.toDegrees(angle)) * magnitude));
            } else {
                return new Vector2d((float) -(Math.cos(Math.toDegrees(angle)) * magnitude),
                        (float) (Math.sin(Math.toDegrees(angle)) * magnitude));
            }
        }
        return new Vector2d(0, distance);
    }

    // Returns number of Radians to rotate vehicle by to correctly corospond to position (perpendicular to the turning circle center)
    public float steeringVehicleRotationRadians(float distance) {
        if (turnState == TurnState.TURNING_RIGHT) {
            return distance / currentTurnRadius;
        } else if (turnState == TurnState.TURNING_LEFT) {
            return (float) (2 * Math.PI) - distance / currentTurnRadius;
        } else {
            return 0;
        }
    }

    public void turnRight() {
        if (turnState == TurnState.NOT_TURNING) { // Heading straight, start right turn
            currentTurnRadius = maxTurnRadus;
            turnState = TurnState.TURNING_RIGHT;
        } else if (turnState == TurnState.TURNING_RIGHT) {  // check turning right, keep turning
            currentTurnRadius += stepValue;
            if (currentTurnRadius < minTurnRadius) {
                currentTurnRadius = minTurnRadius;
            }
        } else { // Turning left, cancel left turn.
            turnState = TurnState.NOT_TURNING;
        }
    }

    public void turnLeft() {
        if (turnState == TurnState.NOT_TURNING) { // Heading straight, start left turn
            currentTurnRadius = maxTurnRadus;
            turnState = TurnState.TURNING_LEFT;
        } else if (turnState == TurnState.TURNING_LEFT) {  // check turning left, keep turning
            currentTurnRadius += stepValue;
            if (currentTurnRadius < minTurnRadius) {
                currentTurnRadius = minTurnRadius;
            }
        } else { // Turning right, cancel right turn.
            turnState = TurnState.NOT_TURNING;
        }
    }

    public void stopTurning() {
        turnState = TurnState.NOT_TURNING;
    }

    public float getMinTurnRadius() {
        return minTurnRadius;
    }

    public float getMaxTurnRadus() {
        return maxTurnRadus;
    }

    public float getCurrentTurnRadius() {
        return currentTurnRadius;
    }

    public void setMinTurnRadius(float minTurnRadius) {
        this.minTurnRadius = minTurnRadius;
    }

    public void setCurrentTurnRadius(float currentTurnRadius) {
        this.currentTurnRadius = currentTurnRadius;
    }

    public void setMaxTurnRadus(float maxTurnRadus) {
        this.maxTurnRadus = maxTurnRadus;
    }

}
