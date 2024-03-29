/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.overlays;

import game.allocation.ReceivesDependency;
import game.entities.UserEntity;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliView;

/**
 *
 * @author jerrykim
 */
public class CliTopMenuOverlay extends CliView {
    
    @ReceivesDependency
    private UserEntity user;
    
    public @Override void Render(CliBuffer buffer)
    {
        int lastX = buffer.GetLastX();
        
        buffer.SetBuffer('│', 1, 1);
        buffer.SetBuffer('│', lastX-1, 1);
        buffer.SetBuffer('└', 1, 2);
        buffer.SetBuffer('┘', lastX-1, 2);
        for(int i=2; i<lastX-1; i++)
            buffer.SetBuffer('─', i, 2);
        
        buffer.SetBuffer("Profile: " + user.Username.GetValue(), 3, 1);
        buffer.SetBuffer("Gold: " + user.Gold.GetValue(), lastX-3, 1, Pivot.Right);
    }
}
