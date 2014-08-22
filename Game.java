package topdownracer;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Game 
{
    // Singleton Design Pattern:
    private static Game sm_game = null;
    
    // Framework:
    protected BackBuffer m_backBuffer;
    protected SpriteStore m_spriteStore;
    //private FontStore fontStore;
    protected ClipStore clipStore;
    protected CheckpointHandler checkpointHandler;
    protected WaypointHandler waypointHandler;
    
    // Simulation Counters:
    private float m_elapsedSeconds;
    private float m_executionTime;
    private int m_frameCount;
    private int m_FPS;
    private int m_numUpdates;
    private boolean m_drawDebugInfo;
    private KeyInputHandler keyInputHandler;
    protected GameLogger gamelogger;
    protected VectorCalculator vCalc;
    protected Font boldItalic;
    protected AIVehicle aiV;
    protected HeadsUpDisplay hud;
    protected boolean drawHUD = true;
    // Game Entities...

    private Vehicle testVehicleA, testVehicleB, playerVehicle;
    private ArrayList<AIVehicle> opponentContainer;
    protected Track track;
    
    
    
    protected ExplosionEmitter explosionEmmiter;
    
    // Back particle emitter
    //private ParticleEmitter m_smallStarEmitter;
    //private ParticleEmitter m_biggerStarEmitter;
    
    
    // Singleton Design Pattern:
    public synchronized static Game getInstance()
    {
        if (sm_game == null)
        {
            sm_game = new Game();
        }
        
        return (sm_game);
    }
    
    private Game()
    {
        gamelogger = GameLogger.getInstance();
        m_executionTime = 0;
        m_elapsedSeconds = 0;
        m_frameCount = 0;
        m_FPS = 0;
        m_numUpdates = 0;
        
        //checkpointHandler = new CheckpointHandler();
        waypointHandler = new WaypointHandler();
        
        // Setup game framework.
        m_backBuffer = new BackBuffer();
        m_spriteStore = new SpriteStore();
        m_backBuffer.initialise(1000, 700); // Frame width, height.

        clipStore = new ClipStore();
        
        hud = new HeadsUpDisplay();
        populateHUD();

        explosionEmmiter = new ExplosionEmitter("assets/Explosion.png");
        keyInputHandler = new KeyInputHandler();
        vCalc = new VectorCalculator();

        opponentContainer = new ArrayList<>();
        aiV = new AIVehicle(m_spriteStore.getSprite("assets/BMW_55x114.png"), 900, 3000);
        opponentContainer.add(aiV);
        
        m_drawDebugInfo = true;
        // Create player vehicle
//        playerVehicle = new VehicleVeryBadATM(m_spriteStore.getSprite("assets/BMW_small_Prototype.png"), 300, 400);
          playerVehicle = new Vehicle(m_spriteStore.getSprite("assets/Camaro_55x118.png"), 1200, 3000);
//        testVehicleA = new Vehicle(m_spriteStore.getSprite("assets/BMW_small_Prototype.png"), 300, 400);
        
        // Create track
        track = new Track(m_spriteStore.getSprite("assets/LoopTrack.jpg"), 0, 0);
        track.initialiseBitmap(new Sprite("assets/LoopBitmap.jpg"));
        //setPlayerClips();
        try {
            playerVehicle.setClip(clipStore.getClip("assets/EngineNoise4.wav"));
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            playerVehicle.setTyreSound(clipStore.getClip("assets/TyreSound.wav"));
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        playerVehicle.playClip();

    }
    
    public void gameLoop()
    {
        final float stepSize = 16.6666666f;

        long lastTime = System.currentTimeMillis();
        long lag = 0;
                        
        while (true)
        {
            long current = System.currentTimeMillis();
            long deltaTime = current - lastTime;
            lastTime = current;
            
            m_executionTime += deltaTime;
            
            lag += deltaTime;
    
            while (lag >= stepSize)
            {
                process(stepSize / 1000.0f);
                
                lag -= stepSize;
                
                ++m_numUpdates;
            }
            
            draw();
        }
    }
    
    public void process(float deltaTime)
    {
        // Count total simulation time elapsed:
        m_elapsedSeconds += deltaTime;
        
        // Frame Counter:
        if (m_elapsedSeconds > 1)
        {
            m_elapsedSeconds -= 1;
            m_FPS = m_frameCount;
            m_frameCount = 0;
        }
        if(deltaTime > 0) {
            playerVehicle.process(deltaTime);
            opponentContainer.stream().forEach((ai) -> {
                ai.process(deltaTime);
            });
            explosionEmmiter.process(deltaTime);
        }
       
    }
    
    public void draw()
    {
        ++m_frameCount;
        
        // Prepare to draw a new frame:
        m_backBuffer.clear();
        //m_backBuffer.camera.shiftCamera(opponentContainer.get(0), m_backBuffer);
        m_backBuffer.camera.shiftCamera(playerVehicle, m_backBuffer);
        drawTrack();
        explosionEmmiter.drawExplosions(m_backBuffer);
        drawPlayerVehicle();
        drawAIPlayers();
        drawHUD();
        
        //Draw Test Text:
        if (m_drawDebugInfo)
        {
            m_backBuffer.drawText(10, 30, "FPS: " + String.valueOf(m_FPS));
            m_backBuffer.drawText(10, 45, "Rotation: " +Math.toDegrees(playerVehicle.rotationRadians)+" degrees");
            m_backBuffer.drawText(10, 60, "Speed: " + playerVehicle.speed);
            m_backBuffer.drawText(10, 75, "Acceleration: " + playerVehicle.acceleration);
            m_backBuffer.drawText(10, 90, "X Position: " + playerVehicle.m_positionX);
            m_backBuffer.drawText(10, 105, "Y Position: " + playerVehicle.m_positionY);

        }
        
        
        // Flip frame buffers:
        m_backBuffer.present();
    }
    
    public void turningRight() {
        playerVehicle.turnState = TurnState.TURNING_RIGHT;
    }
    
    public void turningLeft() {
        playerVehicle.turnState = TurnState.TURNING_LEFT;
    }
    
    public void stopTurning() {
        playerVehicle.turnState = TurnState.NOT_TURNING;
    }

    public void accelerate() {
        playerVehicle.momentumState = MomentumState.ACCELERATING;
    }
    
    public void reverse() {
        playerVehicle.momentumState = MomentumState.REVERSING;
    }
    
    /**
     * Set vehicle to neutral, e.g no accelerationVector, or braking forces.
     */
    public void neutral() {
        playerVehicle.momentumState = MomentumState.NEUTRAL;
    }
    
    public void brake() {
        playerVehicle.momentumState = MomentumState.BRAKING;
    }
    
    public void ToggleDebugInfo()
    {
        m_drawDebugInfo = !m_drawDebugInfo;
    }
    
    // Application Entrypoint:
    public static void main(String[] args)
    {
        Game.getInstance().gameLoop();
    }


    
    private synchronized void drawTrack() {
        track.draw(m_backBuffer);
    }
    
    private synchronized void drawPlayerVehicle() {
        playerVehicle.draw(m_backBuffer);
    }

    private void drawAIPlayers() {
        opponentContainer.stream().forEach((ai) -> {
            ai.draw(m_backBuffer);
        });
    }

    private void populateHUD() {
        hud.setSpeedTextSprite(m_spriteStore.getSprite("assets/bigSpeed.png"));
        for(int i = 0; i<10; i++) {
            hud.addSpeedNumbersSprites(m_spriteStore.getSprite("assets/big"+i+".png"));
        }
    }

    private void drawHUD() {
        hud.drawSpeed((int)playerVehicle.speed, m_backBuffer);
    }

    private void setPlayerClips() {
        try {
            playerVehicle.setClip(clipStore.getClip("assets/EngineNoise4.wav"));
            playerVehicle.setTyreSound(clipStore.getClip("assetes/TyreSound.wav"));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }throw new UnsupportedOperationException("Not supported yet."); 
        
       
        
    }

    public ArrayList<AIVehicle> getOpponentContainer() {
        return opponentContainer;
    }

    public Entity getPlayer() {
        return playerVehicle;
    }
    
    public void spawnExplosion(int x, int y) {
        if(explosionEmmiter!=null)
        explosionEmmiter.spawnExplosion(x, y);
    }
    
}

