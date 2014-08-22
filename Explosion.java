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
class Explosion {
    
    protected int x, y;
    protected AnimatedSprite aSprite;
    
    public Explosion(int x, int y, AnimatedSprite aSprite) {
        this.x = x;
        this.y = y;
        this.aSprite = aSprite;
    }
    
    public void process(float dt) {
        aSprite.process(dt);
    }
}
