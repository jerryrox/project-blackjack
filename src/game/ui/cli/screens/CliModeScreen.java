/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.entities.User;
import game.rulesets.GameModes;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.commands.ArgumentTypes;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.components.CliButton;

/**
 * Game mode selection.
 * @author jerrykim
 */
public class CliModeScreen extends CliView {
    
    private CliButton homeButton;
    
    @ReceivesDependency
    private CliScreenController screens;
    
    @ReceivesDependency
    private User user;
    
    
    @InitWithDependency
    private void Init()
    {
        int buttonY = 16;
        homeButton = (CliButton)AddChild(new CliButton(16, 3, "Home"));
        {
            CommandInfo home = new CommandInfo("home", (args) -> {
                screens.ShowView(CliMainScreen.class);
            });
            home.SetDescription("Navigates back to home screen.");
            homeButton.BindCommand(home);
            homeButton.SetPosition(3, buttonY);
        }
        
        CommandInfo survival = new CommandInfo("survival", (args) -> {
            CliGameScreen game = screens.ShowView(CliGameScreen.class);
            game.SetGameMode(GameModes.Survival);
            game.StartGame();
        });
        survival.SetDescription("Initiates the game on survival mode from the last round you played.");
        
        CommandInfo casual = new CommandInfo("casual", (args) -> {
            CliGameScreen game = screens.ShowView(CliGameScreen.class);
            game.SetGameMode(GameModes.Casual);
            game.StartGame();
        });
        casual.SetDescription("Initiates the game on casual mode.");
        
        commands.AddCommand(survival);
        commands.AddCommand(casual);
        commands.SetPropagate(false);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int halfWidth = buffer.GetHalfWidth();
        
        buffer.SetBuffer("[ Select a game mode. ]", halfWidth, 4, Pivot.Center);
        
        buffer.SetBuffer("(Survival)", halfWidth, 6, Pivot.Center);
        buffer.SetBuffer("Endless battle against AIs which get stronger every next round.", halfWidth, 7, Pivot.Center);
        buffer.SetBuffer("Your best round: " + user.GetSurvivalRound(), halfWidth, 8, Pivot.Center);
        
        buffer.SetBuffer("(Casual)", halfWidth, 10, Pivot.Center);
        buffer.SetBuffer("A single battle against AI with similar stats of yours.", halfWidth, 11, Pivot.Center);
        
        super.Render(buffer);
    }
}
