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
public class Camera extends BackBuffer {
    
    protected int trackWidth, trackHeight;  // The size of the track
    protected Point position;  // The center of the camera's view
    
    /**
     * Construct camera instance with width and height of map defined.
     * @param trackWidth The track width
     * @param trackHeight The track height
     */
    public Camera(int trackWidth, int trackHeight, Point position) {
        super();
        this.trackWidth = trackWidth;
        this.trackHeight = trackHeight;
        // Position camera at center of track.
        this.position = position;
    }
    
    /**
     * Relocate camera to a point.
     * @param p the new point to center the camera on.
     */
    public void relocateCamera(Point p) {
        position = p;
        validateCameraPosition();
    }
    
    /**
     * Relocate camera to coordinates
     * @param x Camera center x value.
     * @param y Camera center y value
     */
    public void relocateCamera(int x, int y) {
        position.x = x;
        position.y = y;
        validateCameraPosition();
    }

    /**
     * Checks camera within bounds of track, and relocates if needed.
     */
    private void validateCameraPosition() {
        position.x = (position.x-(m_windowWidth/2)<0 ? m_windowWidth/2 : position.x);
        position.x = (position.x+(m_windowWidth/2)>trackWidth ? trackWidth-m_windowWidth/2 : position.x);
        position.y = (position.y-(m_windowHeight/2)<0 ? m_windowHeight/2 : position.y);
        position.y = (position.y+(m_windowHeight/2)>trackHeight ? trackHeight-m_windowHeight/2 : position.y);
    }
    
}
