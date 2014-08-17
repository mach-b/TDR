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
public class ParticleEmitter {
    
    protected ArrayList<Particle> particleContainer;
    private Sprite sprite;
    
    public ParticleEmitter(Sprite sprite) {
        this.sprite = sprite;
        particleContainer = new ArrayList<>();
    }
    
    
    
    public void spawnNewParticle(Particle p) {
        particleContainer.add(p);
    }
    
    public void process() {
        
    }
    
    public void draw() {
        
    }
}
