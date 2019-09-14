/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.entities.UserEntity;
import game.graphics.ColorPreset;
import game.io.store.UserStore;
import game.rulesets.Card;
import game.rulesets.GameModes;
import game.rulesets.GameResult;
import game.rulesets.GameResultTypes;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.UITransform;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.customized.UICardDisplayer;
import game.ui.gui.objects.customized.UIRoundBoxButton;
import java.awt.Font;

/**
 * Result displayer screen.
 * @author jerrykim
 */
public class UIResultScreen extends UIScreen {
    
    /**
     * Result of last game to be displayed.
     */
    private GameResult result;
    
    private UISprite modeTitle;
    private UISprite modeIcon;
    private UILabel resultLabel;
    private UISprite rewardIcon;
    private UILabel rewardLabel;
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private UserStore userStore;
    
    
    public UIResultScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors, UIScreenController screens)
    {
        final float titlePosY = -100;
        UILabel title = uiObject.CreateChild().AddComponent(new UILabel());
        {
            title.SetText("Result");
            title.SetFont(Font.BOLD | Font.ITALIC, 32);
            title.GetTransform().SetLocalPosition(0, titlePosY);
        }
        UICardDisplayer leftCard = uiObject.AddChild(new UICardDisplayer());
        {
            leftCard.SetCard(new Card(1));
            leftCard.SetScale(0.25f);
            leftCard.GetTransform().SetLocalPosition(-150, titlePosY);
            leftCard.SetSide(true, false);
        }
        UICardDisplayer rightCard = uiObject.AddChild(new UICardDisplayer());
        {
            rightCard.SetCard(new Card(11));
            rightCard.SetScale(0.25f);
            rightCard.GetTransform().SetLocalPosition(150, titlePosY);
            rightCard.SetSide(true, false);
        }
        
        modeTitle = uiObject.CreateChild().AddComponent(new UISprite());
        {
            modeTitle.GetTransform().SetLocalScale(0.75f, 0.75f);
            modeTitle.GetTransform().SetLocalPosition(0, -20);
        }
        modeIcon = uiObject.CreateChild().AddComponent(new UISprite());
        {
            modeIcon.GetTransform().SetLocalScale(5f, 5f);
            modeIcon.GetObject().SetDepth(-1);
            modeIcon.SetAlpha(0.1f);
        }
        
        resultLabel = uiObject.CreateChild().AddComponent(new UILabel());
        {
            resultLabel.GetTransform().SetLocalPosition(0, 70);
            resultLabel.SetFontSize(22);
        }
        
        rewardIcon = uiObject.CreateChild().AddComponent(new UISprite());
        {
            rewardIcon.GetTransform().SetLocalPosition(0, 113);
            rewardIcon.SetSpritename("icon-coin");
            rewardIcon.GetTransform().SetLocalScale(0.4f, 0.4f);
        }
        rewardLabel = uiObject.CreateChild().AddComponent(new UILabel());
        {
            rewardLabel.GetTransform().SetLocalPosition(20, 110);
            rewardLabel.SetFontSize(20);
            rewardLabel.SetColor(colors.Gold);
        }
        
        UIRoundBoxButton homeButton = uiObject.AddChild(new UIRoundBoxButton());
        {
            homeButton.GetTransform().SetLocalPosition(-180, 280);
            homeButton.SetBgColor(colors.Positive);
            homeButton.SetWidth(300);
            homeButton.SetLabel("Main menu");
            homeButton.Clicked.Add((arg) -> {
                screens.ShowView(UIMainScreen.class);
            });
        }
        UIRoundBoxButton continueButton = uiObject.AddChild(new UIRoundBoxButton());
        {
            continueButton.GetTransform().SetLocalPosition(180, 280);
            continueButton.SetBgColor(colors.Neutral);
            continueButton.SetWidth(300);
            continueButton.SetLabel("Continue playing");
            continueButton.Clicked.Add((arg) -> {
                UIGameScreen game = screens.ShowView(UIGameScreen.class);
                game.SetGameMode(result.Mode);
                game.StartGame();
            });
        }
    }
    
    /**
     * Sets the result data to be displayed on screen.
     */
    public void SetResult(GameResult result)
    {
        this.result = result;
        
        // Add gold
        user.Gold.SetValue(user.Gold.GetValue() + GetFinalRewards());
        // Add round number.
        if(result.Mode == GameModes.Survival && result.ResultType == GameResultTypes.Win)
            user.SurvivalRound.SetValue(Math.max(result.Difficulty, user.SurvivalRound.GetValue()));
        // Save user data.
        userStore.Save();
        
        // Set gold rewards.
        rewardLabel.SetText(String.valueOf(GetFinalRewards()));
        
        // Set mode icon and title.
        if(result.Mode == GameModes.Casual)
        {
            modeTitle.SetSpritename("casual-title");
            modeIcon.SetSpritename("casual-icon");
        }
        else
        {
            modeTitle.SetSpritename("survival-title");
            modeIcon.SetSpritename("survival-icon");
        }
        modeTitle.ResetSize();
        modeIcon.ResetSize();
        
        // Set result.
        switch(result.ResultType)
        {
        case Win:
            resultLabel.SetText("You win!");
            break;
        case Lose:
            resultLabel.SetText("You lose!");
            break;
        case Draw:
            resultLabel.SetText("Draw!");
            break;
        }
    }
    
    public @Override void Update(float deltaTime)
    {
        super.Update(deltaTime);
        
        UITransform transform = rewardIcon.GetTransform();
        transform.SetLocalPosition(-15 - rewardLabel.GetWidth() / 2, 113);
    }
    
    /**
     * Returns the final reward amount after applying the user's Fortune stat.
     */
    private int GetFinalRewards()
    {
        return (int)(result.Rewards * user.GetStats().Fortune.GetValue());
    }
}
