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
    
    public void spawnExplosion(int x, int y) {
        explosions.add(new Explosion(x, y));
    }
}
