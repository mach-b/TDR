package topdownracer;

public class AnimatedSprite extends Sprite
{
    // Ex003.5: Add a field, the container for frame coordinates.
    
    private float m_frameSpeed;
    private int m_frameWidth;
    
    private float m_timeElapsed;
    private int m_currentFrame;
    
    private boolean m_paused;
    private boolean m_loop;
    private boolean m_animating;
    
    private int[] coordinatesContainer;
        
    public AnimatedSprite(String filename)
    {
        super(filename);
        
        m_frameWidth = 64;
        m_frameSpeed = 0.07f;
        
        m_loop = false;
        m_paused = false;
        m_animating = true;
        
        startAnimating();
        
        // Ex003.5: Instantiate the frame coordinates container.
        coordinatesContainer = new int[]{0, 64, 128, 192, 256};
    }
    
    public void addFrame(int x)
    {
        // Ex003.5: Add the x coordinate to the frame coordinate container.
    }
    
    public void process(float dt)
    {
        // Ex003.5: If not paused...
        if (!m_paused) {
            // Ex003.5: Count the time elapsed.
            m_timeElapsed += dt;
            // Ex003.5: If the time elapsed is greater than the frame speed.
            if (m_timeElapsed > m_frameSpeed) {
                // Ex003.5: Move to the next frame.
                m_currentFrame++;
                // Ex003.5: Reset the time elapsed counter.
                m_timeElapsed = 0;
                // Ex003.5: If the current frame is greater than the number 
                //          of frame in this animation...
                        if (m_currentFrame > coordinatesContainer.length-1) {
                    // Ex003.5: Reset to the first frame.
                            m_currentFrame = 0;
                            if (!m_loop) {
                                m_animating = false;
                            }
                        }
                    // Ex003.5: Stop the animation if it is not looping...
            }
        }
                       
    }
    
    public void draw(BackBuffer b, int x, int y)
    {
        // Ex003.5: Draw the particular frame into the backbuffer.
        b.drawSpriteFrame(x, y, coordinatesContainer[m_currentFrame], 0,
                m_frameWidth, m_frameWidth, m_image);
        //          What is the current frame's x coordinate?
        //          What is the frame width?
        //          See BackBuffer's DrawSpriteFrame method.
    }
    
    public void setFrameSpeed(float f)
    {
        m_frameSpeed = f;
    }
    
    public void setFrameWidth(int w)
    {
        m_frameWidth = w;
    }
    
    public void pause()
    {
        m_paused = !m_paused;
    }
       
    public boolean isPaused()
    {
        return (m_paused);
    }
    
    public boolean isAnimating()
    {
        return (m_animating);
    }
        
    public void startAnimating()
    {
        m_animating = true;
        
        m_timeElapsed = 0;
        m_currentFrame = 0;
    }
    
    public boolean isLooping()
    {
        return (m_loop);
    }

    public void setLooping(boolean b)
    {
        m_loop = b;
    }
}
