package topdownracer;

import java.awt.Rectangle;
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
    
    private Vehicle playerVehicle;
    private Vehicle2 testVehicleA, testVehicleB;
    private ArrayList<Vehicle> opponentContainer;
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

        // Ex003.2: Load the player ship sprite. 
        //Sprite playerShipSprite = new Sprite("assets/playership.png");
        // Ex003.2: Create the player ship instance.
        //playerShip = new PlayerShip(playerShipSprite, 400, 500);
        keyInputHandler = new KeyInputHandler();
        vCalc = new VectorCalculator();
        // Ex003.3: Create empty enemy alien container.
        //enemyContainer = new Enemy[14][4];
        //enemyContainer = new ArrayList<>();
        // Ex003.4: Create empty bullet container.
        //bulletContainer = new ArrayList<>();
        // Ex003.5: Create empty explosion container.
        //explosionContainer = new ArrayList<>();
        // Ex003.3: Spawn four rows of 14 alien enemies.
        
        m_drawDebugInfo = true;
        // Create player vehicle
        playerVehicle = new Vehicle(m_spriteStore.getSprite("assets/BMW_small_Prototype.png"), 300, 400);
        testVehicleA = new Vehicle2(m_spriteStore.getSprite("assets/BMW_small_Prototype.png"), 300, 400,
            new Rectangle(20, 20, 50, 100));
        testVehicleA = new Vehicle2(m_spriteStore.getSprite("assets/BMW_small_Prototype.png"), 300, 400,
            new Rectangle(20, 20, 50, 100));
        
        // Create track
        track = new Track(m_spriteStore.getSprite("assets/Road_1000x700.png"), 0, 0);
        
        
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
       
        drawTrack();
        drawPlayerVehicle();
        
        //Draw Test Text:
        if (m_drawDebugInfo)
        {
            m_backBuffer.drawText(10, 30, "FPS: " + String.valueOf(m_FPS));
            m_backBuffer.drawText(10, 45, "Rotation: " +Math.toDegrees(playerVehicle.m_rotationAngle)+" degrees");
            m_backBuffer.drawText(10, 60, "Velocity: " + playerVehicle.velocity);
            m_backBuffer.drawText(10, 75, "Acceleration: " + playerVehicle.accel);
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
}

