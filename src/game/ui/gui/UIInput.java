/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui;

import game.data.Vector2;
import game.debug.Debug;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 * Input management for GUI system.
 * @author jerrykim
 */
public class UIInput implements MouseInputListener, KeyListener {
    
    /**
     * Current mouse position at current frame.
     */
    private Vector2 mousePos = new Vector2();
    
    private boolean isMouseClicked = false;
    private boolean isMouseDown = false;
    private boolean isMouseHold = false;
    private boolean isMouseUp = false;
    
    /**
     * Buffer which holds all characters typed within a single game loop.
     */
    private StringBuilder typedBuffer = new StringBuilder();
    
    /**
     * List of all keys pressed down during a game loop.
     */
    private ArrayList<Integer> downKeys = new ArrayList<>();
    
    /**
     * List of all keys being held in current game loop.
     */
    private ArrayList<Integer> holdKeys = new ArrayList<>();
    
    /**
     * List of all keys released during this game loop.
     */
    private ArrayList<Integer> upKeys = new ArrayList<>();
    
    
    public UIInput()
    {
    }
    
    /**
     * Binds the context of input receiver to specified panel.
     */
    public void Bind(JPanel panel)
    {
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.addKeyListener(this);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }
    
    /**
     * Returns current mouse position.
     */
    public Vector2 GetMousePosition() { return mousePos; }
    
    public boolean IsMouseClicked() { return isMouseClicked; }
    public boolean IsMouseDown() { return isMouseDown; }
    public boolean IsMouseHold() { return isMouseHold; }
    public boolean IsMouseUp() { return isMouseUp; }
    
    /**
     * Returns the string value of all characters entered during this frame.
     */
    public String GetInputString() { return typedBuffer.toString(); }
    
    public boolean IsKeyDown(int keyCode) { return downKeys.contains(keyCode); }
    public boolean IsKeyHold(int keyCode) { return holdKeys.contains(keyCode); }
    public boolean IsKeyUp(int keyCode) { return upKeys.contains(keyCode); }
    
    /**
     * Handles input synchronization with the GUI system.
     * Should be called after object hierarchy update.
     */
    public void Update()
    {
        // Process mouse event update.
        isMouseClicked = false;
        isMouseDown = false;
        isMouseUp = false;
        
        // Process keyboard event update.
        downKeys.clear();
        upKeys.clear();
        typedBuffer.setLength(0);
    }
    
    public @Override void mouseClicked(MouseEvent e)
    {
        isMouseClicked = true;
    }

    public @Override void mousePressed(MouseEvent e)
    {
        isMouseDown = true;
        isMouseHold = true;
    }

    public @Override void mouseReleased(MouseEvent e)
    {
        isMouseHold = false;
        isMouseUp = true;
    }

    public @Override void mouseEntered(MouseEvent e) {}

    public @Override void mouseExited(MouseEvent e) {}

    public @Override void mouseDragged(MouseEvent e)
    {
        mousePos.X = e.getX();
        mousePos.Y = e.getY();
    }

    public @Override void mouseMoved(MouseEvent e)
    {
        mousePos.X = e.getX();
        mousePos.Y = e.getY();
    }

    public @Override void keyTyped(KeyEvent e)
    {
        char ch = e.getKeyChar();
        // Preprocess certain characters before appending.
        if(ch == '\b')
            typedBuffer.append("\\b");
        else
            typedBuffer.append(ch);
    }

    public @Override void keyPressed(KeyEvent e)
    {
        int keycode = e.getKeyCode();
        if(holdKeys.contains(keycode))
            return;
        downKeys.add(keycode);
        holdKeys.add(keycode);
    }

    public @Override void keyReleased(KeyEvent e)
    {
        int keycode = e.getKeyCode();
        upKeys.add(keycode);
        
        if(holdKeys.contains(keycode))
            holdKeys.remove((Integer)keycode);
    }
}
