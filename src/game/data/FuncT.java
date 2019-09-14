/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 * Function which returns a value with one argument.
 * @author jerrykim
 */
public interface FuncT<T, TResult> {
    
    TResult Invoke(T value);
}
