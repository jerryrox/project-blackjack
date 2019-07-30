/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 * Since Java doesn't have a struct type which C# does, this interface will hopefully
 * try to simulate some significant behaviors of a C# struct through methods.
 * @author jerrykim
 */
public interface IStruct<T> {
    
    /**
     * Clones this instance with derived values.
     */
    T Clone();
}
