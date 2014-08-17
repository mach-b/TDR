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
public class Vector2D extends Vector2d {
    
    public Vector2D(double x, double y) {
        super(x, y);
    }
    
    public void rotate(double n) {
        double rx = (this.x * Math.cos(n)) - (this.y * Math.sin(n));
        double ry = (this.x * Math.sin(n)) + (this.y * Math.cos(n));
        x = rx;
        y = ry;
    }
    
    public Vector2D combineVectors(Vector2d v1, Vector2d v2) {
        return new Vector2D(v1.x + v2.x, v1.y + v2.y);
    }
    
    public Vector2D increaseVectorLength(Vector2d v, double d) {
        double newLength = v.length() + d;
        return new Vector2D((v.x * (newLength/v.length())), (v.y * (newLength/v.length())));
    }
}
