/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package topdownracer;

import java.util.ArrayList;

/**
 *
 * @author markburton
 */
public class ExplosionEmitter {
    
    protected AnimatedSprite aSprite;
    protected Explosion explosion;
    protected ArrayList<Explosion> explosions;
    
    public ExplosionEmitter(String filename) {
        explosions = new ArrayList<>();
        aSprite = new AnimatedSprite(filename);
        
    }
    
    public synchronized void spawnExplosion(int x, int y) {
        explosions.add(new Explosion(x, y, aSprite));
    }
    
    public synchronized void process(float dt) {
//        for(Explosion e : explosions) {
//            e.aSprite.process(dt);
//            if(e.aSprite.isAnimating()==false) {
//                explosions.remove(e);
//            }
//        }
        for (int i = (explosions.size()-1); i >= 0; i--) {
            explosions.get(i).process(dt);
            if(explosions.get(i).aSprite.isAnimating()==false) {
                explosions.remove(i);
            }
        }
        
    }
    
    public synchronized void drawExplosions(BackBuffer b) {
        for (Explosion e : explosions) {
            aSprite.draw(b, e.x, e.y);
        }
    }
}
