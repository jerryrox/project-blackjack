/*
 * Jerry Kim (18015036), 2019
 */
package game.utils;

/**
 * Utility class for simple maths.
 * @author jerrykim
 */
public class MathUtils {
    
    /**
     * Returns the clamped value between 0 and 1.
     */
    public static float Clamp01(float value)
    {
        if(value < 0)
            return 0;
        else if(value > 1)
            return 1;
        return value;
    }
    
    /**
     * Returns the clamped value between specified min and max.
     */
    public static float Clamp01(float value, float min, float max)
    {
        if(value < min)
            return min;
        else if(value > max)
            return max;
        return value;
    }
    public static int Clamp(int value, int min, int max)
    {
        if(value < min)
            return min;
        else if(value > max)
            return max;
        return value;
    }
}
