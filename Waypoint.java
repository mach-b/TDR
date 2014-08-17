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
public class Waypoint extends Point {
    
    protected float radius;

    public Waypoint(int x, int y, float radius) {
        super(x, y);
        this.radius = radius;
    }
    
}
