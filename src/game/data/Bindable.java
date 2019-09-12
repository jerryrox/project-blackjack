/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 * Data wrapper for event binding feature.
 * @author jerrykim
 */
public class Bindable<T extends Object> {
    
    public final Events<T> Changed = new Events<>();
    
    protected T value;
    
    
    public Bindable(T value)
    {
        this.value = value;
    }
    
    public T GetValue() { return value; }
    
    public void SetValue(T value)
    {
        this.value = value;
        Changed.Invoke(value);
    }
}
