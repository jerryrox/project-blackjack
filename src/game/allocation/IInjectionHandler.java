/*
 * Jerry Kim (18015036), 2019
 */
package game.allocation;

/**
 * Delegate interface which invokes injection on objects.
 * @author jerrykim
 */
public interface IInjectionHandler {
    
    void Invoke(Object obj, IDependencyContainer container);
}
