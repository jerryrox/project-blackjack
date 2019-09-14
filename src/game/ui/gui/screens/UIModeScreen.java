/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.entities.UserEntity;
import game.graphics.ColorPreset;
import game.rulesets.GameModes;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.objects.customized.UIModeButton;
import game.ui.gui.objects.customized.UIRoundBoxButton;

/**
 * Mode selection screen.
 * @author jerrykim
 */
public class UIModeScreen extends UIScreen {
    
    private UIModeButton survivalButton;
    private UIModeButton casualButton;
    
    private UILabel survivalRoundLabel;
    
    private boolean isGoingToGame;
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private UIScreenController screens;
    
    
    public UIModeScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        final float buttonPosY = -50;
        uiObject.AddChild(survivalButton = new UIModeButton());
        {
            survivalButton.SetupMode(true);
            survivalButton.GetTransform().SetLocalPosition(-250, buttonPosY);
            survivalButton.Clicked.Add((args) -> {
                OnSurvivalButton();
            });
            
            survivalRoundLabel = survivalButton.CreateChild().AddComponent(new UILabel());
            {
                survivalRoundLabel.SetFontSize(16);
                survivalRoundLabel.GetTransform().SetLocalPosition(0, 280);
            }
        }
        uiObject.AddChild(casualButton = new UIModeButton());
        {
            casualButton.SetupMode(false);
            casualButton.GetTransform().SetLocalPosition(250, buttonPosY);
            casualButton.Clicked.Add((args) -> {
                OnCasualButton();
            });
        }
        
        UIRoundBoxButton backButton = uiObject.AddChild(new UIRoundBoxButton());
        {
            backButton.SetBgColor(colors.Warning);
            backButton.SetTextColor(colors.Dark);
            backButton.SetLabel("Back to main");
            backButton.SetWidth(300);
            backButton.GetTransform().SetLocalPosition(0, 300);
            backButton.Clicked.Add((arg) -> {
                OnBackButton();
            });
        }
    }
    
    protected @Override void OnPreShowView()
    {
        super.OnPreShowView();
        
        isGoingToGame = false;
        survivalButton.ResetState();
        casualButton.ResetState();
        
        survivalRoundLabel.SetText("Current round: " + user.SurvivalRound.GetValue());
    }
    
    private void OnSurvivalButton()
    {
        if(isGoingToGame)
            return;
        isGoingToGame = true;
        
        survivalButton.SelectMode(true, () -> {
            UIGameScreen game = screens.ShowView(UIGameScreen.class);
            game.SetGameMode(GameModes.Survival);
            game.StartGame(); 
        });
        casualButton.SelectMode(false, null);
    }
    
    private void OnCasualButton()
    {
        if(isGoingToGame)
            return;
        isGoingToGame = true;
        
        casualButton.SelectMode(true, () -> {
            UIGameScreen game = screens.ShowView(UIGameScreen.class);
            game.SetGameMode(GameModes.Casual);
            game.StartGame();
        });
        survivalButton.SelectMode(false, null);
    }
    
    private void OnBackButton()
    {
        if(isGoingToGame)
            return;
        
        screens.ShowView(UIMainScreen.class);
    }
}
