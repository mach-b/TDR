package topdownracer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BackBuffer 
{
    protected int m_windowWidth = 1000;
    protected int m_windowHeight = 700;
        
    private BufferStrategy m_strategy;
    private Canvas m_canvas;
    private Graphics2D m_graphics;
    private AffineTransform transformer;
    
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
        
        createWindow();
        
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
        m_graphics.drawImage(i, x, y, null);
    }
    
    public void drawSpriteFrame(int x, int y, int frameX, int frameY, int w, int h, Image i)
    {
        m_graphics.drawImage(i, x, y, x + w, y + h,
                             frameX, frameY, frameX + w, frameY + h, null);
    }
    
    public void drawRotatedImage(int x, int y, Image i, float angle, Point point) {
        //ROTATION???
//    Graphics2D g2d = ...
//AffineTransform backup = g2d.getTransform();
//AffineTransform trans = new AffineTransform();
//trans.rotate( sprite.angle, sprite.x, sprite.y ); // the points to rotate around (the center in my example, your left side for your problem)
//
//g2d.transformer( trans );
//g2d.drawImage( image, sprite.x, sprite.y );  // the actual location of the sprite
//
//g2d.setTransform( backup );
        
        transformer = new AffineTransform();
        transformer.rotate(angle, point.getX(), point.getY()); //Math.toRadians(angle)
        m_graphics.transform(transformer);
        m_graphics.drawImage(i, x, y, m_canvas); //
        m_graphics.setColor(Color.red);
        m_graphics.drawRect(x+1, y-1, 3, 3);
        m_graphics = (Graphics2D) m_strategy.getDrawGraphics();
        m_graphics.setColor(Color.blue);
        m_graphics.drawRect(x+1, y-1, 3, 3);
    }
    
    private void createWindow()
    {
        JFrame containerFrame = new JFrame("");
        
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


}
