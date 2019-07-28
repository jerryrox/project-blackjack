/*
 * Jerry Kim (18015036), 2019
 */
package game.utils;

/**
 * A helper class which provides random-related functions.
 * @author jerrykim
 */
public final class Random {
    
    private static java.util.Random RandomGenerator;
    
    
    static
    {
        RandomGenerator = new java.util.Random(System.currentTimeMillis());
    }
    
    /**
     * Sets the seed on random generator.
     * This shouldn't be necessary, as the generator will be initialized using current time as seed.
     * @param seed 
     */
    public static void SetSeed(long seed)
    {
        RandomGenerator.setSeed(seed);
    }
    
    /**
     * Returns a value between min and max values, excluding max.
     * @param min
     * @param max
     */
    public static float Range(float min, float max)
    {
        return (max - min) * RandomGenerator.nextFloat() + min;
    }
    
    /**
     * Returns a value between min and max values, excluding max.
     * @param min
     * @param max
     */
    public static int Range(int min, int max)
    {
        return (int)((max - min) * RandomGenerator.nextFloat() + min);
    }
}
