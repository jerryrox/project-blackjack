/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 * A general-purpose delegate to be used like the one in C#.
 * @author jerrykim
 */
public interface ActionT <T> {
    
    void Invoke(T value);
}
