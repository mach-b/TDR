/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Checkpoint a location on the track that must be past through to register progress and laps.
 * Represented by a circle, the 
 * @author markburton
 */
public class Checkpoint {
    
    protected Point2D.Double p1, p2;  // Location of checkpoint
    protected Line2D.Double line; // Radius of Checkpoint
    protected String name;
    protected int id;


    public Checkpoint(double x1, double y1, double x2, double y2, String name, int id) {
        this.p1 = new Point2D.Double(x1, y1);
        this.p2 = new Point2D.Double(x2, y2);
        this.name = name;
        this.id = id;
    }
    
    
}
