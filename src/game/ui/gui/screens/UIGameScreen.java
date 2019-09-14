/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.IDependencyContainer;
import game.allocation.ReceivesDependency;
import game.rulesets.BaseRuleset;
import game.rulesets.GameModes;
import game.rulesets.GameResult;
import game.rulesets.Rulesets;
import game.rulesets.ui.gui.GuiRuleset;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.overlays.UITopMenuOverlay;

/**
 * Gameplay screen.
 * @author jerrykim
 */
public class UIGameScreen extends UIScreen {
    
    private GameModes mode;
    
    private BaseRuleset currentRuleset;
    
    @ReceivesDependency
    private UIScreenController screens;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    @ReceivesDependency
    private IDependencyContainer dependencies;
    
    
    public UIGameScreen()
    {
        super();
    }
    
    protected @Override void OnPreShowView()
    {
        super.OnPreShowView();
        
        overlays.HideView(UITopMenuOverlay.class);
    }
    
    protected @Override void OnPreHideView()
    {
        super.OnPreHideView();
        
        overlays.ShowView(UITopMenuOverlay.class);
    }
    
    /**
     * Sets current game mode.
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
        GuiRuleset obj = (GuiRuleset)currentRuleset.GetDrawableRuleset();
        if(uiObject.ContainsChild(obj))
            obj.SetActive(true);
        else
            uiObject.AddChild(obj);
        currentRuleset.OnStartSession();
    }
    
    /**
     * Stops the game and exits to specified screen.
     */
    public <T extends UIScreen> void ExitGame(Class<T> screen)
    {
        GameResult result = currentRuleset.GetResult();
        
        StopGame();
        T view = screens.ShowView(screen);
        if(screen == UIResultScreen.class)
        {
            UIResultScreen resultScreen = (UIResultScreen)view;
            if(resultScreen != null)
                resultScreen.SetResult(result);
        }
    }
    
    private void StopGame()
    {
        currentRuleset.OnStopSession();
        
        GuiRuleset obj = (GuiRuleset)currentRuleset.GetDrawableRuleset();
        if(obj != null)
            obj.SetActive(false);
    }
}
