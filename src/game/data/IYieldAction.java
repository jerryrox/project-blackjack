/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 * Delegate for defining a Yieldable's iteration process.
 * @author jerrykim
 */
public interface IYieldAction<T> {

    void Invoke(IYieldHandler<T> yield);
}
