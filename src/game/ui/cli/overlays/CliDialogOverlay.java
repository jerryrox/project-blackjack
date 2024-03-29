/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.overlays;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliOverlayController;
import game.ui.cli.CliView;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.components.CliFrame;

/**
 * Overlay for displaying dialogs.
 * @author jerrykim
 */
public class CliDialogOverlay extends CliView {
    
    private String message;
    
    private CliFrame frame;
    
    @ReceivesDependency
    private CliOverlayController overlays;
    
    
    public CliDialogOverlay()
    {
        commands.SetEnable(true);
        commands.SetPropagate(false);
    }
    
    @InitWithDependency
    private void Init()
    {
        AddChild(frame = new CliFrame());
    }
    
    public void SetDialog(String message, CommandInfo... commands)
    {
        this.message = message;
        for(CommandInfo cmd : commands)
        {
            final CommandInfo.CallbackHandler callback = cmd.GetCallback();
            
            // Hook dialog close action on the command.
            cmd.SetCallback((args) -> {
                if(callback != null)
                    callback.Invoke(args);
                cmd.GetCallback();
                overlays.HideView(this);
            });
            this.commands.AddCommand(cmd);
        }
    }
    
    public @Override int GetDepth() { return 9000; }
    
    public @Override void OnDisable()
    {
        this.commands.ClearCommands();
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int lines = commands.GetCount() + 2;
        int halfWidth = buffer.GetHalfWidth();
        int halfHeight = buffer.GetHalfHeight();
        
        int lastX = buffer.GetLastX();
        int lastY = buffer.GetLastY();
        
        // Clear underlying views
        for(int c=1; c<lastX; c++)
        {
            for(int r=1; r<lastY; r++)
                buffer.SetBuffer(' ', c, r);
        }
        // Draw frames
        frame.SetRect(1, 1, lastX-1, lastY-1);
        
        // Calculate the Y position where the message and commands will be listed from.
        int y = halfHeight - lines/2;
        
        // Display message.
        buffer.SetBuffer(message, halfWidth, y, Pivot.Center);
        // Display commands.
        y += 2;
        for(CommandInfo cmd : commands.GetCommands())
        {
            buffer.SetBuffer(String.format("[ %s ]", cmd.GetName()), halfWidth, y, Pivot.Center);
            y ++;
        }
        
        super.Render(buffer);
    }
}
