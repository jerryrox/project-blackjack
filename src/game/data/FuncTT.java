/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 * Function which returns a value with two arguments.
 * @author jerrykim
 */
public interface FuncTT<T1, T2, TResult> {
    
    TResult Invoke(T1 value1, T2 value2);
}
