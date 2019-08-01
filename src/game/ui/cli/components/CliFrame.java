/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.components;

import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;

/**
 * Frame displayer.
 * @author jerrykim
 */
public class CliFrame extends CliDisplayer {
    
    private int x;
    private int y;
    private int z;
    private int w;
    
    
    public void SetRect(int leftX, int topY, int rightX, int botY)
    {
        x = leftX;
        y = topY;
        z = rightX;
        w = botY;
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        for(int i=x; i<=z; i++) buffer.SetBuffer('▓', i, 1);
        for(int i=x+1; i<=z-1; i++) buffer.SetBuffer('▒', i, 2);
        for(int i=x+2; i<=z-2; i++) buffer.SetBuffer('░', i, 3);
        for(int i=x; i<=z; i++) buffer.SetBuffer('▓', i, w);
        for(int i=x+1; i<=z-1; i++) buffer.SetBuffer('▒', i, w-1);
        for(int i=x+2; i<=z-2; i++) buffer.SetBuffer('░', i, w-2);
        for(int i=y; i<=w; i++) buffer.SetBuffer('▓', 1, i);
        for(int i=y+1; i<=w-1; i++) buffer.SetBuffer('▒', 2, i);
        for(int i=y+2; i<=w-2; i++) buffer.SetBuffer('░', 3, i);
        for(int i=y; i<=w; i++) buffer.SetBuffer('▓', z, i);
        for(int i=y+1; i<=w-1; i++) buffer.SetBuffer('▒', z-1, i);
        for(int i=y+2; i<=w-2; i++) buffer.SetBuffer('░', z-2, i);
    }
}
