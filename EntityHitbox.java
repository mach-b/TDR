/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topdownracer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.vecmath.Vector2d;

/**
 * EntityHitbox provides the mechanism for collision checking between entities
 * and Bitmaps. Bounds of EntityHitbox allows the checking of other Entities,
 * and Bitmaps, within it's location only, not the whole canvas. Uses 2D Lines
 * to represent sprite bounds, which can be checked for intersection with
 * overlapping entities.
 *
 * @author markburton
 */
public class EntityHitbox {

    protected Graphics2D g2d;
    protected int xPos, yPos, xOffset, yOffset, entityX, entityY, boundingDimension;  // Bounds are Square
    // Line2D : Line segments to represent the bounds of the Entity as a rectangle.
    // Update Lines based on position and rotation of Vehicle/Entity
    protected final Line2D.Double frontLine, rightLine, backLine, leftLine;
    // workingLineArray to maintain integrity of lines over time.
    protected Line2D.Double[] workingLineArray;
    protected Point2D rectangleOrigin, rotationPoint;
    protected Rectangle rectangle;
    protected double currentRotationRadians;
    protected int numIntersections;
    protected ArrayList<Point2D> intersectionPoints;
    //protected AffineTransform transformer;

    public EntityHitbox(int entityyX, int entityY, int entityWidth, int entityHeight,
            Rectangle rectangle, Point2D rectangleOrigin, Point2D rotationPoint) {
        // The entity game coordinates.
        this.entityX = entityyX;
        this.entityY = entityY;
        // Get bounding Dimension, the larger value, width or height.
        // Rectangle MUST fit within these bounds!!!
        boundingDimension = (int) Math.ceil(Math.sqrt((entityHeight*entityHeight) + (entityWidth*entityWidth)));
        currentRotationRadians = 0;
            this.rectangle = rectangle;  // Not Really needed, REFACTOR WHEN WORKING.
        // The EntityHitbox game coordinates relative to the Entity
        this.xOffset = -((boundingDimension - entityWidth) / 2);
        this.yOffset = -((boundingDimension - entityHeight) / 2);
        this.rectangleOrigin = rectangleOrigin;
        // Translate rectangleOrigin to context of EntityHitbox (previously relative to Entity)
        this.rectangleOrigin.setLocation(-xOffset + rectangleOrigin.getX(),
                -yOffset + rectangleOrigin.getY());
        this.rotationPoint = rotationPoint;
        this.rotationPoint.setLocation(rotationPoint.getX()+(-xOffset), rotationPoint.getY()+(-yOffset));
        // Create Line Segments, actual collision area bounds. (CARE makes assumtions about Rectangle, e.g. not rotated)
        this.frontLine = new Line2D.Double(rectangleOrigin,
                new Point2D.Double(rectangle.width + rectangleOrigin.getX(), rectangleOrigin.getY()));

        this.rightLine = new Line2D.Double(new Point2D.Double(rectangle.width + rectangleOrigin.getX(), rectangleOrigin.getY()),
                new Point2D.Double(rectangle.width + rectangleOrigin.getX(), rectangleOrigin.getY() + rectangle.height));

        this.backLine = new Line2D.Double(new Point2D.Double(rectangle.width + rectangleOrigin.getX(), rectangleOrigin.getY() + rectangle.height),
                new Point2D.Double(rectangleOrigin.getX(), rectangleOrigin.getY() + rectangle.height));

        this.leftLine = new Line2D.Double(new Point2D.Double(rectangleOrigin.getX(), rectangleOrigin.getY() + rectangle.height),
                rectangleOrigin);
        // Set up workingLineArray
        workingLineArray = new Line2D.Double[4];
        setWorkingLinesRotation(currentRotationRadians);
        numIntersections = 0;  // Hope this is correct, try and handle anyway if not. REFACTOR???
        intersectionPoints = new ArrayList<>();
    }

    public synchronized void getEntityVsEntityIntersections(EntityHitbox eA, EntityHitbox eB) {
        for(Line2D lineA : eA.workingLineArray) {
            for(Line2D lineB : eB.workingLineArray) {
                if(checkForIntersection(lineA, lineB)) {
                    intersectionPoints.add(getIntersectionPoint(lineA, lineB));
                }
            }
        }
    }
    
    // Check for intersection of line segments
    /**
     * Checks if two Line2D.Double lines intersect
     * @param lineA
     * @param lineB
     * @return true if intersecting
     */
    public boolean checkForIntersection(Line2D lineA, Line2D lineB) {
        return lineA.intersectsLine(lineB);
    }
    
    /**
     * Align with map based on Entities x and y
     * @param x
     * @param y 
     */
    public void alignWithEntity(int x, int y) {
        entityX = x;
        entityY = y;
        xPos = x + xOffset;
        yPos = y + yOffset;
        System.out.println("xPos="+(xPos)+", yPos="+(yPos));
    }
    
    // Code modified from online source, author not known,
    /**
     * Returns intersection point of two Line2D.Double lines segments
     * @param lineA
     * @param lineB
     * @return intersection Point2D, or null if not intersecting.
     */
    public Point2D getIntersectionPoint(Line2D lineA, Line2D lineB) {
        if (!lineA.intersectsLine(lineB)) {
            return null;
        }
        double px = lineA.getX1(),
                py = lineA.getY1(),
                rx = lineA.getX2() - px,
                ry = lineA.getY2() - py;
        double qx = lineB.getX1(),
                qy = lineB.getY1(),
                sx = lineB.getX2() - qx,
                sy = lineB.getY2() - qy;

        double det = sx * ry - sy * rx;
        if (det == 0) {
            return null;
        } else {
            double z = (sx * (qy - py) + sy * (px - qx)) / det;
            if (z == 0 || z == 1) {
                return null;
            }  // intersection at end point!
            return new Point2D.Double((px + z * rx), (py + z * ry));
        }
    }
    
    /**
     * Sets workingLineArray to current Entity Rotation.
     * @param theta Radians to rotate from the origin
     */
    public void setWorkingLinesRotation(double theta) {
        currentRotationRadians = theta;  // Will be helpful in resolving collision.
        workingLineArray[0] = getRotatedLine(frontLine, theta);
        workingLineArray[1] = getRotatedLine(rightLine, theta);
        workingLineArray[2] = getRotatedLine(backLine, theta);
        workingLineArray[3] = getRotatedLine(leftLine, theta);
    }
    
    /**
     * Rotates a Line2D.Double around the rotationPoint
     * @param line 
     * @param theta Radians to rotate 
     * @return the rotated Line2D.Double
     */
    public Line2D.Double getRotatedLine(Line2D.Double line, double theta) {
        Point2D p1 = line.getP1();
        Point2D p2 = line.getP2();
        AffineTransform transformer = new AffineTransform();
        transformer.rotate(theta, rotationPoint.getX(), rotationPoint.getY());
        transformer.transform(p1, p1);  // CHECK is this OK !!!
        transformer.transform(p2, p2);
        return new Line2D.Double(p1, p2);
    }
    
    public Vector2d getCollisionResolutionVector() {
        
        return null;
    }
    
    
////// DO LINES INTERSECT /////////////
//Line2D line1 = new Line2D.Float(100, 100, 200, 200);
//Line2D line2 = new Line2D.Float(150, 150, 150, 200);
//boolean result = line2.intersectsLine(line1);
//System.out.println(result); // => true
//
//// Also check out linesIntersect() if you do not need to construct the line objects
//// It will probably be faster due to putting less pressure on the garbage collector
//// if running it in a loop
//System.out.println(Line2D.linesIntersect(100,100,200,200,150,150,150,200));

//////// WHAT IS INTERSECTION POINT /////////////    
//public Point2D.Float getIntersectionPoint(Line2D.Float line1, Line2D.Float line2) {
//    if (! line1.intersectsLine(line2) ) return null;
//      double px = line.getX1(),
//            py = line.getY1(),
//            rx = line.getX2()-px,
//            ry = line.getY2()-py;
//      double qx = line2.getX1(),
//            qy = line2.getY1(),
//            sx = line2.getX2()-qx,
//            sy = line2.getY2()-qy;
//
//      double det = sx*ry - sy*rx;
//      if (det == 0) {
//        return null;
//      } else {
//        double z = (sx*(qy-py)+sy*(px-qx))/det;
//        if (z==0 ||  z==1) return null;  // intersection at end point!
//        return new Point2D.Float(
//          (float)(px+z*rx), (float)(py+z*ry));
//      }
// } // end intersection line-line  
//    Sorry, of course it should be
//
//double px = line1.getX1(),
//py = line1.getY1(),
//rx = line1.getX2()-px,
//ry = line1.getY2()-py;
  
////// ROTATE LINE ///////////////////
//        transformer = new AffineTransform();
//        transformer.rotate(theta, rotationPoint.getX(), rotationPoint.getY()); //Math.toRadians(angle)
//        //m_graphics.transform(transformer);
//        g2d.transform(transformer);
//        System.out.println("Rotation = "+Math.toDegrees(theta)+"degrees\n"+
//                "x="+workingLineArray[0].x1+" y="+workingLineArray[0].y1);
//        
//        Point2D result = new Point2D.Double();
//    AffineTransform rotation = new AffineTransform();
//    double angleInRadians = (angle * Math.PI / 180);
//    rotation.rotate(angleInRadians, pivot.getX(), pivot.getY());
//    rotation.transform(point, result);
//    return result;    
}
