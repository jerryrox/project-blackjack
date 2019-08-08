/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.debug.Debug;
import game.entities.UserEntity;
import game.io.store.UserStore;
import game.rulesets.GameModes;
import game.rulesets.GameResult;
import game.rulesets.GameResultTypes;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.components.CliButton;

/**
 * Result screen.
 * @author jerrykim
 */
public class CliResultScreen extends CliView {
    
    /**
     * Current result object to be displayed.
     */
    private GameResult result;
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private UserStore userStore;
    
    
    @InitWithDependency
    private void Init(CliScreenController screens)
    {
        int buttonY = 16;
        CliButton homeButton = (CliButton)AddChild(new CliButton(15, 3, "Home"));
        {
            CommandInfo home = new CommandInfo("home", (args) -> {
                screens.ShowView(CliMainScreen.class);
            });
            home.SetDescription("Navigates back to home screen.");
            homeButton.BindCommand(home);
            homeButton.SetPosition(3, buttonY);
        }
        CliButton continueButton = (CliButton)AddChild(new CliButton(15, 3, "Continue"));
        {
            CommandInfo cont = new CommandInfo("continue", (args) -> {
                CliGameScreen game = screens.ShowView(CliGameScreen.class);
                game.SetGameMode(result.Mode);
                game.StartGame();
            });
            cont.SetDescription("Continues playing within same game mode.");
            continueButton.BindCommand(cont);
            continueButton.SetPosition(20, buttonY);
        }
    }
    
    /**
     * Sets result information.
     */
    public void SetResult(GameResult result)
    {
        this.result = result;
        
        // Refresh user status.
        user.SetGold(user.GetGold() + GetFinalRewards());
        if(result.Mode == GameModes.Survival && result.ResultType == GameResultTypes.Win)
            user.SetSurvivalRound(Math.max(result.Difficulty, user.GetSurvivalRound()));
        
        // Save user data.
        userStore.Save();
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int halfWidth = buffer.GetHalfWidth();
        
        // Title
        buffer.SetBuffer("______               _ _   ", halfWidth, 3, Pivot.Center);
        buffer.SetBuffer("| ___ \\             | | |  ", halfWidth, 4, Pivot.Center);
        buffer.SetBuffer("| |_/ /___ ___ _   _| | |_ ", halfWidth, 5, Pivot.Center);
        buffer.SetBuffer("|    // _ / __| | | | | __|", halfWidth, 6, Pivot.Center);
        buffer.SetBuffer("| |\\ |  __\\__ | |_| | | |_ ", halfWidth, 7, Pivot.Center);
        buffer.SetBuffer("\\_| \\_\\___|___/\\__,_|_|\\__|", halfWidth, 8, Pivot.Center);
        
        // Game mode
        buffer.SetBuffer("["+result.Mode+"]", halfWidth, 10, Pivot.Center);
        
        // Result type
        switch(result.ResultType)
        {
        case Win:
            buffer.SetBuffer("You win!", halfWidth, 11, Pivot.Center);
            break;
        case Lose:
            buffer.SetBuffer("You lose!", halfWidth, 11, Pivot.Center);
            break;
        case Draw:
            buffer.SetBuffer("Draw!", halfWidth, 11, Pivot.Center);
            break;
        }
        
        // Rewards
        buffer.SetBuffer("Rewards:", halfWidth, 12, Pivot.Center);
        buffer.SetBuffer(GetRewardString(), halfWidth, 13, Pivot.Center);
        
        super.Render(buffer);
    }
    
    /**
     * Returns the final reward amount after applying the user's Fortune stat.
     */
    private int GetFinalRewards()
    {
        return (int)(result.Rewards * user.GetStats().Fortune.GetValue());
    }
    
    /**
     * Returns the displayed reward string.
     */
    private String GetRewardString()
    {
        return String.format("%d gold (%d x %.2f)", GetFinalRewards(), result.Rewards, user.GetStats().Fortune.GetValue());
    }
}
