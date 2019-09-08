/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.IDependencyContainer;

/**
 * Provides a general signature for any displayable ui element.
 * @author jerrykim
 */
public interface IDisplayer<T> extends IHasPosition {
    
    /**
     * Returns the depth of the display.
     */
    int GetDepth();
    
    /**
     * Sets the active state of the displayer.
     * @param isActive 
     */
    void SetActive(boolean isActive);
    
    /**
     * Event called when the displayer has become active.
     */
    void OnEnable();
    
    /**
     * Event called when the displayer has become inactive.
     */
    void OnDisable();
    
    /**
     * Returns whether the displayer is active and should be rendered.
     */
    boolean IsActive();
    
    /**
     * Adds specified child under this displayer.
     * @param child 
     */
    <TDisplayer extends T> TDisplayer AddChild(TDisplayer child);
    
    /**
     * Adds specified children under this displayer.
     * @param children 
     */
    void AddChildren(T... children);
    
    /**
     * Removes the specified child from children list.
     * @param child 
     */
    void RemoveChild(T child);
    
    /**
     * Returns all children nested directly under this displayer.
     * @param reverse
     */
    Iterable<T> GetChildren(boolean reverse);
}
