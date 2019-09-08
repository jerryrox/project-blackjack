/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.data.Vector2;

/**
 * Provides a general signature for an element which has a position.
 * @author jerrykim
 */
public interface IHasPosition {
    
    /**
     * Sets the X, Y position of the displayer.
     * @param x
     * @param y 
     */
    void SetPosition(int x, int y);
    
    /**
     * Sets the X position of the displayer.
     * @param x 
     */
    void SetPositionX(int x);
    
    /**
     * Sets the Y position of the displayer.
     * @param y 
     */
    void SetPositionY(int y);
    
    /**
     * Returns a new position instance of this object.
     */
    Vector2 GetPosition();
}
