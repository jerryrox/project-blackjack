/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

import game.utils.MathUtils;

/**
 * Bindable integer.
 * @author jerrykim
 */
public class BindableInt extends BindableNumber<Integer> {
    
    public BindableInt(int value)
    {
        super(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    public BindableInt(int value, int min, int max)
    {
        super(value, Integer.MIN_VALUE, Integer.MAX_VALUE);
        SetMin(min);
        SetMax(max);
    }
    
    public @Override void SetValue(Integer value)
    {
        super.SetValue(MathUtils.Clamp(value, minValue, maxValue));
    }
    
    public @Override void SetMin(Integer min)
    {
        minValue = Math.min(min, maxValue);
        if(minValue > value)
            SetValue(minValue);
    }
    
    public @Override void SetMax(Integer max)
    {
        maxValue = Math.max(max, minValue);
        if(maxValue < value)
            SetValue(maxValue);
    }
}
