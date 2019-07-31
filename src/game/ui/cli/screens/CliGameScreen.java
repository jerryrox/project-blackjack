/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.IDependencyContainer;
import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.debug.Debug;
import game.rulesets.BaseRuleset;
import game.rulesets.GameModes;
import game.rulesets.Rulesets;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;
import game.ui.cli.CliOverlayController;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.overlays.CliDialogOverlay;
import game.ui.cli.overlays.CliDialogPresets;
import game.ui.cli.overlays.CliTopMenuOverlay;

/**
 * Game view container screen.
 * The actual game process should be handled through each particular ruleset instance.
 * @author jerrykim
 */
public class CliGameScreen extends CliView {
    
    /**
     * Current game mode in play.
     */
    private GameModes mode;
    
    /**
     * Current ruleset instance in use.
     */
    private BaseRuleset currentRuleset;
    
    @ReceivesDependency
    private CliOverlayController overlays;
    
    @ReceivesDependency
    private CliScreenController screens;
    
    @ReceivesDependency
    private IDependencyContainer dependencies;
    
    
    @InitWithDependency
    private void Init()
    {
        CommandInfo exit = new CommandInfo("exit", (args) -> {
            CliDialogOverlay overlay = overlays.ShowView(CliDialogOverlay.class);
            CliDialogPresets.SetExitGame(overlay, (a) -> ExitGame(CliMainScreen.class), null);
        });
        exit.SetDescription("Exits to home screen. Progress will be lost!");
        
        commands.AddCommand(exit);
        commands.SetPropagate(false);
    }
    
    public @Override void OnEnable()
    {
        overlays.HideView(CliTopMenuOverlay.class);
    }
    
    public @Override void OnDisable()
    {
        overlays.ShowView(CliTopMenuOverlay.class);
    }
    
    /**
     * Sets current game mode.
     * @param mode 
     */
    public void SetGameMode(GameModes mode)
    {
        this.mode = mode;
        currentRuleset = Rulesets.GetRuleset(mode, dependencies);
    }
    
    /**
     * Starts game.
     */
    public void StartGame()
    {
        AddChild((CliDisplayer)currentRuleset.GetDrawableRuleset());
        currentRuleset.OnStartSession();
    }
    
    /**
     * Stops the game and exits to specified screen.
     * @param <T>
     * @param screen 
     */
    public <T extends CliView> void ExitGame(Class<T> screen)
    {
        StopGame();
        screens.ShowView(screen);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        super.Render(buffer);
    }
    
    /**
     * Stops game.
     */
    private void StopGame()
    {
        currentRuleset.OnStopSession();
        RemoveChild((CliDisplayer)currentRuleset.GetDrawableRuleset());
    }
}
