package topdownracer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BackBuffer 
{
    protected int m_windowWidth = 1000;
    protected int m_windowHeight = 700;
        
    private BufferStrategy m_strategy;
    private Canvas m_canvas;
    protected Graphics2D m_graphics;
    private AffineTransform transformer;
    protected Camera camera;
    
    public BackBuffer()
    {
    
    }

    public int getM_windowWidth() {
        return m_windowWidth;
    }

    public int getM_windowHeight() {
        return m_windowHeight;
    }
    
    public void initialise(int width, int height)
    {
        m_canvas = new Canvas();
        
        m_windowWidth = width;
        m_windowHeight = height;
        camera = new Camera();
        
        createWindow();
        
        camera = new Camera(0, 0);
        // TODO: Log State Change: "Backbuffer Initialised"
    }
        
    public void clear()
    {
        m_graphics = (Graphics2D) m_strategy.getDrawGraphics();
        m_graphics.setColor(Color.black);
        m_graphics.fillRect(0, 0, m_windowWidth, m_windowHeight);
    }
    
    public void present()
    {
        m_graphics.dispose();
        m_strategy.show();
    }
    
    public void drawText(int x, int y, String s)
    {
        m_graphics.setColor(Color.white);
        m_graphics.drawString(s, x, y);
    }
    
    public void drawImage(int x, int y, Image i)
    {
        m_graphics.drawImage(i, x+((int)camera.x), y+((int)camera.y), null);
    }
    
    public void drawSpriteFrame(int x, int y, int frameX, int frameY, int w, int h, Image i)
    {
        m_graphics.drawImage(i, x+((int)camera.x), y+((int)camera.y), x+((int)camera.x) + w, y+((int)camera.y) + h ,
                             frameX, frameY, frameX + w, frameY + h, null);
    }
    
    public void drawRotatedImage(int x, int y, Image i, float angle, Point2D.Double point) {
        
        transformer = new AffineTransform();
        transformer.rotate(angle, point.getX()+((int)camera.x), point.getY()+((int)camera.y));
        m_graphics.transform(transformer);
        m_graphics.drawImage(i, x+((int)camera.x), y+((int)camera.y), m_canvas); //
//        m_graphics.setColor(Color.red);
//        m_graphics.drawRect(x+1+((int)camera.x), y-1+((int)camera.y), 3, 3);
        m_graphics = (Graphics2D) m_strategy.getDrawGraphics();
//        m_graphics.setColor(Color.blue);
//        m_graphics.drawRect(x+1+((int)camera.x), y-1+((int)camera.y), 3, 3);
    }
    
    private void createWindow()
    {
        JFrame containerFrame = new JFrame("");
        containerFrame.setLocationRelativeTo(null);
        JPanel panel = (JPanel) containerFrame.getContentPane();
        
        panel.setPreferredSize(new Dimension(m_windowWidth, m_windowHeight));
        panel.setLayout(null);
        
        m_canvas.setBounds(0, 0, m_windowWidth, m_windowHeight);
        panel.add(m_canvas);
        
        m_canvas.setIgnoreRepaint(true);
        
        containerFrame.pack();
        containerFrame.setResizable(false);
        containerFrame.setVisible(true);
        
        containerFrame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        
        m_canvas.addKeyListener(new KeyInputHandler());
    
        m_canvas.requestFocus();
    
        m_canvas.createBufferStrategy(2);
        
        m_strategy = m_canvas.getBufferStrategy();
    }

    void draw(Line2D.Double[] workingLineArray) {
        m_graphics.setColor(Color.red);
        for(Line2D line : workingLineArray) {
            m_graphics.drawLine((int)line.getX1()+((int)camera.x), (int)line.getY1()+((int)camera.y),
                    (int)line.getX2()+((int)camera.x), (int)line.getY2()+((int)camera.y));
            m_graphics.setColor(Color.blue);
        }
    }

    public synchronized void draw(EntityHitbox entityHitbox) {
        m_graphics.setColor(Color.PINK);
        m_graphics.drawRect(entityHitbox.entityX+((int)camera.x), entityHitbox.entityY+((int)camera.y), 5, 5);
        m_graphics.setColor(Color.red);
        //System.out.println("*** EHB location: x="+ entityHitbox.xPos+", y="+entityHitbox.yPos);
        for(Line2D line : entityHitbox.workingLineArray) {
            m_graphics.drawLine((int)(line.getX1()+entityHitbox.xPos+((int)camera.x)),
                    (int)(line.getY1()+entityHitbox.yPos+((int)camera.y)),
                    (int)(line.getX2()+entityHitbox.xPos+((int)camera.x)),
                    (int)(line.getY2()+entityHitbox.yPos+((int)camera.y)));
            m_graphics.setColor(Color.blue);
        }
    }

//    void draw(EntityHitbox entityHitbox) {
//        m_graphics.setColor(Color.PINK);
//        m_graphics.drawRect(entityHitbox.entityX, entityHitbox.entityY, 5, 5);
//        m_graphics.setColor(Color.red);
//        for(Line2D line : entityHitbox.workingLineArray) {
//            m_graphics.drawLine((int)(line.getX1()+entityHitbox.xPos), (int)(line.getY1()+entityHitbox.yPos),
//                    (int)(line.getX2()+entityHitbox.xPos), (int)(line.getY2()+entityHitbox.yPos));
//            m_graphics.setColor(Color.blue);
//            System.out.println("Line point x="+(line.getX1()+entityHitbox.xPos)+", y"+(line.getY1()+entityHitbox.yPos));
//        }
//    }
    
        //ROTATION???
//      Graphics2D g2d = ...
//      AffineTransform backup = g2d.getTransform();
//      AffineTransform trans = new AffineTransform();
//      trans.rotate( sprite.angle, sprite.x, sprite.y ); // the points to rotate around (the center in my example, your left side for your problem)
//
//      g2d.transformer( trans );
//      g2d.drawImage( image, sprite.x, sprite.y );  // the actual location of the sprite
//
//      g2d.setTransform( backup );

}
