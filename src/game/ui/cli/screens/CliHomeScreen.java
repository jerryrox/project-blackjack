/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.entities.User;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliOverlayController;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.ICliEngine;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.overlays.CliDialogOverlay;

/**
 * Home screen.
 * @author jerrykim
 */
public class CliHomeScreen extends CliView {
    
    
    @InitWithDependency
    private void Init(CliScreenController screens, CliOverlayController overlays, User user, ICliEngine engine)
    {
        CommandInfo start = new CommandInfo("start", (args) -> {
            if(user.IsEmptyData())
                screens.ShowView(CliRegisterScreen.class);
            else
                screens.ShowView(CliMainScreen.class);
        });
        start.SetDescription("Starts the game");
        
        CommandInfo quit = new CommandInfo("quit", (args) -> {
            CliDialogOverlay overlay = overlays.ShowView(CliDialogOverlay.class);
            overlay.SetDialog(
                "Are you sure you want to quit?",
                new CommandInfo("yes", (arguments) -> {
                    engine.StopUpdate();
                }),
                new CommandInfo("no", null)
            );
        });
        
        commands.AddCommand(start);
        commands.AddCommand(quit);
        
        commands.SetEnable(true);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int halfWidth = buffer.GetHalfWidth();
        
        buffer.SetBuffer("______ _            _    _            _      _   __      _       _     _       ", halfWidth, 3, Pivot.Center);
        buffer.SetBuffer("| ___ | |          | |  (_)          | |    | | / /     (_)     | |   | |      ", halfWidth, 4, Pivot.Center);
        buffer.SetBuffer("| |_/ | | __ _  ___| | ___  __ _  ___| | __ | |/ / _ __  _  __ _| |__ | |_ ___ ", halfWidth, 5, Pivot.Center);
        buffer.SetBuffer("| ___ | |/ _` |/ __| |/ | |/ _` |/ __| |/ / |    \\| '_ \\| |/ _` | '_ \\| __/ __|", halfWidth, 6, Pivot.Center);
        buffer.SetBuffer("| |_/ | | (_| | (__|   <| | (_| | (__|   <  | |\\  | | | | | (_| | | | | |_\\__ \\", halfWidth, 7, Pivot.Center);
        buffer.SetBuffer("\\____/|_|\\__,_|\\___|_|\\_| |\\__,_|\\___|_|\\_\\ \\_| \\_|_| |_|_|\\__, |_| |_|\\__|___/", halfWidth, 8, Pivot.Center);
        buffer.SetBuffer("                       _/ |                                 __/ |              ", halfWidth, 9, Pivot.Center);
        buffer.SetBuffer("                      |__/                                 |___/               ", halfWidth, 10, Pivot.Center);
        
        buffer.SetBuffer("Enter a command to start", halfWidth, 15, Pivot.Center);
        
        super.Render(buffer);
    }
}
