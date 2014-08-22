package topdownracer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.vecmath.Vector2d;

public abstract class Entity
{
    protected float m_positionX;
    protected float m_positionY;
    
    protected float m_velocityX;
    protected float m_velocityY;
    protected Vector2d velocityVector;
    protected Vector2d position;
    protected Vector2d accelerationVector;
    protected Vector2d rotationVector;
    protected Vector2d brakeVector;
    protected EntityHitbox entityHitbox;
    
    protected float maxVelocity, maxVelocityReverse;
    
    protected float m_scale;
    protected float m_rotationAngle;
    
    protected Sprite m_EntitySprite;
    
    protected boolean m_dead;
    
    public Entity(Sprite sprite, int x, int y)
    {
        m_EntitySprite = sprite;
        
        m_positionX = x;
        m_positionY = y;
        
        rotationVector = new Vector2d(0,-1);
        accelerationVector = new Vector2d(0, 0);
        velocityVector = new Vector2d(0, 0);
        m_dead = false;
    }
    
   
    
    public void process(float deltatime)
    {
         
    }
    
    public void draw(BackBuffer b)
    {
        if (!m_dead)
        {
            m_EntitySprite.draw(b, (int)m_positionX, (int)m_positionY);
        }
    }
   
    public float getPositionX()
    {
        return (m_positionX);
    }
    
    public float getPositionY()
    {
        return (m_positionY);
    }

    public float getHorizontalVelocity()
    {
        return (m_velocityX);
    }
    
    public float getVerticalVelocity()
    {
        return (m_velocityY);
    }
    
    public void setHorizontalVelocity(float x)
    {
        m_velocityX = x;
    }
    
    public void setVerticalVelocity(float y)
    {
        m_velocityY = y;
    }
    
    public boolean isDead()
    {
        return (m_dead);
    }
    
    public void setDead(boolean b)
    {
        m_dead = b;
    }
    
    public boolean isCollidingWith(Entity e)
    {

//        // Ex003.4: Generic Entity Collision routine.
//        
//        // Ex003.4: Does this object collide with the e object?
//        // Ex003.4: Creat a Java Rectangle for each entity (this and e).
//        Rectangle thisBounds = new Rectangle((int)m_positionX, (int)m_positionY, 
//                m_EntitySprite.getWidth(), m_EntitySprite.getHeight());
//        Rectangle eBounds = new Rectangle((int)e.m_positionX, (int)e.m_positionY, 
//                e.m_EntitySprite.getWidth(), e.m_EntitySprite.getHeight());
//        // Ex003.4: Set bounds for each entity.
//
//        // Ex003.4: Call intersects method.
//        if (thisBounds.intersects(eBounds)) {
//            return true;
//        }
//        // Ex003.4: Return result of collision.
//        return (false); // TODO: Change return value
        return false;
    }
    
    public abstract void collidedWith(Entity e);
}
