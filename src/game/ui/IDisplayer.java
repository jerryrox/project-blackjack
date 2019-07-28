/*
 * Jerry Kim (18015036), 2019
 */
package game.ui;

import game.allocation.IDependencyContainer;

/**
 * Provides a general signature for any displayable ui element.
 * @author jerrykim
 */
public interface IDisplayer<T extends IDisplayer> extends Comparable<T> {
    
    /**
     * Returns the depth of the display.
     */
    int GetDepth();
    
    /**
     * Adds specified child under this displayer.
     * @param child 
     */
    void AddChild(T child);
    
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
     * Sets the dependency container instance on the displayer.
     */
    void SetDependencyContainer(IDependencyContainer container);
}
