package topdownracer;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

public class Game 
{
    // Singleton Design Pattern:
    private static Game sm_game = null;
    
    // Framework:
    protected BackBuffer m_backBuffer;
    private SpriteStore m_spriteStore;
    
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
    
    // Game Entities...
    
    // Ex003.2: Add a PlayerShip field.  
    //private PlayerShip playerShip;
    // Ex003.3: Add an alien enemy container field.
    //private Enemy[][] enemyContainer;
    //private ArrayList<Enemy> enemyContainer;
    // Ex003.4: Add a bullet container field.
    //private ArrayList<Bullet> bulletContainer;
    // Ex003.5: Add an explosion container field.
    //private ArrayList<Explosion> explosionContainer;
    
//    private VehicleVeryBadATM playerVehicle;
    private Vehicle testVehicleA, testVehicleB, playerVehicle;
    private ArrayList<VehicleVeryBadATM> opponentContainer;
    private Track track;
    
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
    
        // Setup game framework.
        m_backBuffer = new BackBuffer();
        m_spriteStore = new SpriteStore();
        m_backBuffer.initialise(1000, 700); // Frame width, height.
        //camera = new Camera(5000, 5000, new Point2D.Double(1000, 1000));
        //camera.initialise(1000, 700);


        keyInputHandler = new KeyInputHandler();
        vCalc = new VectorCalculator();

        
        m_drawDebugInfo = true;
        // Create player vehicle
//        playerVehicle = new VehicleVeryBadATM(m_spriteStore.getSprite("assets/BMW_small_Prototype.png"), 300, 400);
          playerVehicle = new Vehicle(m_spriteStore.getSprite("assets/BMW_55x114.png"), 300, 400);
//        testVehicleA = new Vehicle(m_spriteStore.getSprite("assets/BMW_small_Prototype.png"), 300, 400);
        
        // Create track
        track = new Track(m_spriteStore.getSprite("assets/LoopTrack.jpg"), 0, 0);
        
        
        //populateEnemyContainer();
        
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
            
        }
       
    }
    
    public void draw()
    {
        ++m_frameCount;
        
        // Prepare to draw a new frame:
        m_backBuffer.clear();
        //m_backBuffer.camera.shiftCamera(playerVehicle, m_backBuffer);
        //camera.clear();
        drawTrack();
        drawPlayerVehicle();
        
        //Draw Test Text:
        if (m_drawDebugInfo)
        {
            m_backBuffer.drawText(10, 30, "FPS: " + String.valueOf(m_FPS));
            m_backBuffer.drawText(10, 45, "Rotation: " +Math.toDegrees(playerVehicle.m_rotationAngle)+" degrees");
            m_backBuffer.drawText(10, 60, "Velocity: " + playerVehicle.speed);
            m_backBuffer.drawText(10, 75, "Acceleration: " + playerVehicle.acceleration);
            m_backBuffer.drawText(10, 90, "X Position: " + playerVehicle.m_positionX);
            m_backBuffer.drawText(10, 105, "Y Position: " + playerVehicle.m_positionY);
//            camera.drawText(10, 30, "FPS: " + String.valueOf(m_FPS));
//            camera.drawText(10, 45, "Rotation: " +Math.toDegrees(playerVehicle.m_rotationAngle)+" degrees");
//            camera.drawText(10, 60, "Velocity: " + playerVehicle.speed);
//            camera.drawText(10, 75, "Acceleration: " + playerVehicle.acceleration);
//            camera.drawText(10, 90, "X Position: " + playerVehicle.m_positionX);
//            camera.drawText(10, 105, "Y Position: " + playerVehicle.m_positionY);
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
}

