/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

/**
 * A structure representing a rect value in GUI space.
 * @author jerrykim
 */
public class Rect implements IStruct<Rect> {

    public int X;
    public int Y;
    public int Z;
    public int W;
    
    
    public Rect() { this(0, 0, 0, 0); }
    
    public Rect(Rect other) { this(other.X, other.Y, other.Z, other.W); }
    
    public Rect(int x, int y, int z, int w)
    {
        X = x;
        Y = y;
        Z = z;
        W = w;
    }
    
    /**
     * Returns whether the rect represents an absolute zero value.
     */
    public boolean IsZero() { return (X | Y | Z | W) == 0; }
    
    public @Override Rect Clone() { return new Rect(this); }
}
