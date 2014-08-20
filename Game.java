package topdownracer;

import java.awt.Font;
import java.util.ArrayList;

public class Game 
{
    // Singleton Design Pattern:
    private static Game sm_game = null;
    
    // Framework:
    protected BackBuffer m_backBuffer;
    private SpriteStore m_spriteStore;
    private FontStore fontStore;
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
    // Game Entities...

    private Vehicle testVehicleA, testVehicleB, playerVehicle;
    private ArrayList<AIVehicle> opponentContainer;
    protected Track track;
    
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
        //camera = new Camera(5000, 5000, new Point2D.Double(1000, 1000));
        //camera.initialise(1000, 700);


        keyInputHandler = new KeyInputHandler();
        vCalc = new VectorCalculator();

        opponentContainer = new ArrayList<>();
        aiV = new AIVehicle(m_spriteStore.getSprite("assets/BMW_55x114.png"), 1000, 3000);
        System.out.println("AIVehicle created");
        opponentContainer.add(aiV);
        
        m_drawDebugInfo = true;
        // Create player vehicle
//        playerVehicle = new VehicleVeryBadATM(m_spriteStore.getSprite("assets/BMW_small_Prototype.png"), 300, 400);
          playerVehicle = new Vehicle(m_spriteStore.getSprite("assets/BMW_55x114.png"), 1200, 3000);
//        testVehicleA = new Vehicle(m_spriteStore.getSprite("assets/BMW_small_Prototype.png"), 300, 400);
        
        // Create track
        track = new Track(m_spriteStore.getSprite("assets/LoopTrack.jpg"), 0, 0);
        track.initialiseBitmap(new Sprite("assets/LoopBitmap.jpg"));
        

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
            System.out.println("OpponentContainerSize="+opponentContainer.size());
            opponentContainer.stream().forEach((ai) -> {
                ai.process(deltaTime);
            });
        }
       
    }
    
    public void draw()
    {
        ++m_frameCount;
        
        // Prepare to draw a new frame:
        m_backBuffer.clear();
        m_backBuffer.camera.shiftCamera(playerVehicle, m_backBuffer);
        //camera.clear();
        drawTrack();
        drawPlayerVehicle();
        drawAIPlayers();
        
        //Draw Test Text:
        if (m_drawDebugInfo)
        {
            m_backBuffer.drawText(10, 30, "FPS: " + String.valueOf(m_FPS));
            m_backBuffer.drawText(10, 45, "Rotation: " +Math.toDegrees(playerVehicle.rotationRadians)+" degrees");
            m_backBuffer.drawText(10, 60, "Speed: " + playerVehicle.speed);
            m_backBuffer.drawText(10, 75, "Acceleration: " + playerVehicle.acceleration);
            m_backBuffer.drawText(10, 90, "X Position: " + playerVehicle.m_positionX);
            m_backBuffer.drawText(10, 105, "Y Position: " + playerVehicle.m_positionY);
            
//            m_backBuffer.m_graphics.setFont(boldItalic);
//            m_backBuffer.m_graphics.setColor(Color.red);
//            m_backBuffer.drawText(100, 200, "TESTING");
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
}

