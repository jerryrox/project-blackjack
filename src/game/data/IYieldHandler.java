/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 *
 * @author jerrykim
 */
public interface IYieldHandler<T> {

    /**
     * Returns the specified value to output it.
     */
    void Return(T value);

    /**
     * Breaks the iteration.
     */
    void Break();
}
