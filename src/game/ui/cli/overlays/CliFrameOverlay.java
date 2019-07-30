/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.overlays;

import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliView;

/**
 * Frame displayer overlay.
 * @author jerrykim
 */
public class CliFrameOverlay extends CliView {
    
    
    public @Override int GetDepth() { return 10000; }
    
    public @Override void Render(CliBuffer buffer)
    {
        int width = buffer.GetWidth();
        int height = buffer.GetHeight();
        int lastX = buffer.GetLastX();
        int lastY = buffer.GetLastY();
        
        for(int i=0; i<width; i++)
            buffer.SetBuffer('═', i, 0);
        for(int i=0; i<width; i++)
            buffer.SetBuffer('═', i, lastY);
        for(int i=1; i<height-1; i++)
        {
            buffer.SetBuffer('║', 0, i);
            buffer.SetBuffer('║', lastX, i);
        }
        buffer.SetBuffer('╔', 0, 0);
        buffer.SetBuffer('╗', lastX, 0);
        buffer.SetBuffer('╚', 0, lastY);
        buffer.SetBuffer('╝', lastX, lastY);
        
        buffer.SetBuffer(" Blackjack Knights ", width/2, 0, Pivot.Center);
    }
}
