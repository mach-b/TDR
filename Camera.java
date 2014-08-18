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
public class Camera {
    
    double x, y;
    
    public Camera (double x, double y) {
        this.x = x;
        this.y = y;
    }

    Camera() {
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public void shiftCamera(Vehicle vehicle, BackBuffer bB) {
        x = -((vehicle.currentPosition.getX() + vehicle.rotationPoint.getX()) - (bB.m_windowWidth/2));
        y = -((vehicle.currentPosition.getY() +vehicle.rotationPoint.getY()) - (bB.m_windowHeight/2));
        bB.m_graphics.translate(x, y);
        
    }
    
}
