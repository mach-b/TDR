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
public class VectorCalculator {
    
    
    /**
     * vector should not be (0, 0) :)
     * @param v The vector
     * @param d 
     * @return 
     */
    public Vector2d rotateVector(Vector2d v, double theta) {
//        double[] vect = new double[] {v.x, v.y};
//        double[] rotMatrix = new double[] {Math.cos(f), -Math.sin(f), Math.sin(f), Math.cos(f)};
//        double x = (vect[0]*rotMatrix[0]+(vect[1]*rotMatrix[1]));
//        double y = (vect[0]*rotMatrix[2])+(vect[1]*rotMatrix[3]);
        double x = (v.x * Math.cos(theta)) - (v.y * Math.sin(theta));
        double y = (v.x * Math.sin(theta)) + (v.y * Math.cos(theta));
//        GameLogger.getInstance().logMessage(LogLevel.INFO, this.toString(), "Rotating by "
//                + Math.toDegrees(theta)+"degrees.");
        return new Vector2d(x, y);  // Added Negatives
    }
    
    public Vector2d combineVectors(Vector2d v1, Vector2d v2) {
        return new Vector2d(v1.x+v2.x, (v1.y+v2.y));
    }
     
    /**
     * Increase length of vector by specified value
     * @param d The length to increase by
     * @return 
     */
    public Vector2d increaseVectorLength(Vector2d v, double d) {
        //System.out.println("increase by: "+d);
        double newLength = v.length() + d;
        //System.out.println("newLength: "+newLength);
        //System.out.println("x="+(v.x*(newLength/v.length())));
        return new Vector2d((v.x*(newLength/v.length())), (v.y*(newLength/v.length())));
    }
}
