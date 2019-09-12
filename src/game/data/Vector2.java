/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 * A structure that represents a 2d vector or a point.
 * @author jerrykim
 */
public class Vector2 implements IStruct<Vector2> {
    
    public float X;
    public float Y;
    
    
    public Vector2() { this(0, 0); }
    
    public Vector2(float x, float y)
    {
        X = x;
        Y = y;
    }
    
    public Vector2(Vector2 vec)
    {
        X = vec.X;
        Y = vec.Y;
    }
    
    /**
     * Copies values from specified vector.
     */
    public void CopyFrom(Vector2 vec)
    {
        X = vec.X;
        Y = vec.Y;
    }
    
    /**
     * Clones this instance's values to a new Vector2 object.
     */
    public @Override Vector2 Clone() { return new Vector2(X, Y); }
    
    public @Override String toString() { return String.format("(%.2f, %.2f)", X, Y); }
}
