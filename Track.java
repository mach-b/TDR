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
class Track extends Entity {
    
    protected Bitmap bitmap;
    
    public Track(Sprite sprite, int x, int y) {
        super(sprite, x, y);
        // Create Bitmap, scaled to represent each pixel '1'.
        bitmap = new Bitmap(sprite, 1);
    }
    
    public Track(Sprite sprite, int x, int y, String bitmapURL) {
        super(sprite, x, y);
        // Create Bitmap, scaled to represent each pixel '1'.
        bitmap = new Bitmap(bitmapURL);
    }
    
    public void initialiseBitmap(String s) {
        bitmap = new Bitmap(s);
    }

    @Override
    public void collidedWith(Entity e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
