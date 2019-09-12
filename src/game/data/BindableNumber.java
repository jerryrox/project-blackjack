/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 * Bindable data for numbers.
 * @author jerrykim
 */
public abstract class BindableNumber<T extends Number> extends Bindable<T> {
    
    protected T minValue;
    protected T maxValue;
    
    
    protected BindableNumber(T value, T minT, T maxT)
    {
        super(value);
        minValue = minT;
        maxValue = maxT;
    }
    
    public abstract void SetMin(T min);
    
    public abstract void SetMax(T max);
}
