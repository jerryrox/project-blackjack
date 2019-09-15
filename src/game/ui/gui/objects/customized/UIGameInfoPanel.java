/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.animations.Anime;
import game.animations.Easing;
import game.data.Events;
import game.graphics.ColorPreset;
import game.rulesets.BaseRuleset;
import game.rulesets.Card;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameItem;
import game.rulesets.GameModes;
import game.rulesets.GamePlayer;
import game.rulesets.GameProcessor;
import game.rulesets.PhaseResults;
import game.rulesets.ui.gui.GuiRuleset;
import game.rulesets.ui.gui.IGameStateListener;
import game.ui.Pivot;
import game.ui.gui.UIOverlayController;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.UITransform;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIObject;
import game.ui.gui.overlays.UIDialogOverlay;
import java.awt.Font;

/**
 * Game info displayer panel.
 * @author jerrykim
 */
public class UIGameInfoPanel extends UIObject implements IGameStateListener{
    
    private GuiRuleset rulesetDisplayer;
    private BaseRuleset ruleset;
    private GameProcessor gameProcessor;
    
    private UISprite modeIcon;
    private UILabel difficultyLabel;
    private UILabel phaseLabel;
    private UIGameDeck deckDisplay;
    private UIGameActionButton drawButton;
    private UIGameActionButton skipButton;
    private UIRoundBoxButton itemButton;
    private UIRoundBoxButton quitButton;
    
    private Anime phaseChangeAni;
    private Events phaseChangeEvent;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    
    public UIGameInfoPanel(GuiRuleset rulesetDisplayer, BaseRuleset ruleset)
    {
        super();
        this.rulesetDisplayer = rulesetDisplayer;
        this.ruleset = ruleset;
        this.gameProcessor = ruleset.GetGameProcessor();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        UISprite bg = CreateChild().AddComponent(new UISprite());
        {
            bg.SetSpritename("info-panel-bg");
            bg.ResetSize();
            bg.SetPivot(Pivot.Right);
            bg.GetTransform().SetLocalPosition(640, 0);
        }
        
        final float centerX = 515;
        
        // Mode icon
        modeIcon = CreateChild().AddComponent(new UISprite());
        {
            if(ruleset.GetGameMode() == GameModes.Survival)
                modeIcon.SetSpritename("survival-title");
            else
                modeIcon.SetSpritename("casual-title");
            modeIcon.ResetSize();
            modeIcon.GetTransform().SetLocalScale(0.75f, 0.75f);
            modeIcon.GetTransform().SetLocalPosition(centerX, -326);
        }
        
        // Difficulty
        difficultyLabel = CreateChild().AddComponent(new UILabel());
        {
            difficultyLabel.GetTransform().SetLocalPosition(centerX, -288);
        }
        
        // Phase
        phaseLabel = CreateChild().AddComponent(new UILabel());
        {
            phaseLabel.SetColor(colors.Phase);
            phaseLabel.SetFontSize(36);
            phaseLabel.SetFontStyle(Font.BOLD);
            phaseLabel.GetTransform().SetLocalPosition(centerX, -206);
        }
        
        // Deck
        AddChild(deckDisplay = new UIGameDeck());
        {
            deckDisplay.GetTransform().SetLocalPosition(centerX, -124);
            deckDisplay.SetGameProcessor(gameProcessor);
        }
        
        // Actions
        final float actionsX = 350;
        AddChild(drawButton = new UIGameActionButton());
        {
            drawButton.Setup("action-draw-bg", "icon-arrow-left", "Draw");
            drawButton.GetTransform().SetLocalPosition(actionsX, -70);
        }
        AddChild(skipButton = new UIGameActionButton());
        {
            skipButton.Setup("action-skip-bg", "icon-cross", "Skip");
            skipButton.GetTransform().SetLocalPosition(actionsX, 70);
        }
        
        // Menu
        AddChild(itemButton = new UIRoundBoxButton());
        {
            itemButton.SetBgColor(colors.Warning);
            itemButton.SetTextColor(colors.Dark);
            itemButton.SetLabel("Items");
            itemButton.SetWidth(160);
            itemButton.GetTransform().SetLocalPosition(centerX, 260);
        }
        AddChild(quitButton = new UIRoundBoxButton());
        {
            quitButton.SetBgColor(colors.Negative);
            quitButton.SetLabel("Quit");
            quitButton.SetWidth(160);
            quitButton.GetTransform().SetLocalPosition(centerX, 320);
        }
        
        UIAnimator animator = AddComponent(new UIAnimator());
        phaseChangeAni = animator.CreateAnime("phasechange");
        {
            UITransform phaseTransform = phaseLabel.GetTransform();
            phaseChangeAni.AddSection(0, 25, (progress) -> {
                float scale = Easing.BackEaseOut(progress, 1, 0.2f, 0.5f);
                phaseTransform.SetLocalScale(scale, scale);
            });
            phaseChangeAni.AddSection(30, 45, (progress) -> {
                float scale = Easing.QuadEaseIn(progress, 1.2f, -0.2f, 0);
                phaseTransform.SetLocalScale(scale, scale);
            });
            phaseChangeAni.AddEvent(45, () -> {
                if(phaseChangeEvent != null)
                    phaseChangeEvent.Invoke(null);
            });
        }
    }
    
    public @Override void OnInactive()
    {
        super.OnInactive();
        
        phaseChangeAni.Stop();
    }
    
    /**
     * Refreshes some displays that should be executed on session start.
     */
    public void RefreshInfo()
    {
        // Set difficulty label.
        int diff = gameProcessor.GetAIPlayer().GetDifficulty();
        if(ruleset.GetGameMode() == GameModes.Survival)
            difficultyLabel.SetText("Round: " + diff);
        else
            difficultyLabel.SetText("Difficulty: " + diff);
        
        // Set phase label.
        RefreshPhase();
    }
    
    public UIGameDeck GetDeck() { return deckDisplay; }
    public UIGameActionButton GetDrawButton() { return drawButton; }
    public UIGameActionButton GetSkipButton() { return skipButton; }
    public UIRoundBoxButton GetItemButton() { return itemButton; }
    public UIRoundBoxButton GetQuitButton() { return quitButton; }

    public @Override Events OnSetTurnState(GamePlayer player) { return null; }

    public @Override Events OnTurnEndState(GamePlayer player) { return null; }

    public @Override Events OnNewPhaseState()
    {
        RefreshPhase();
        
        phaseChangeEvent = new Events();
        phaseChangeAni.PlayAt(0);
        return phaseChangeEvent;
    }

    public @Override Events OnPhaseEndState() { return null; }

    public @Override Events OnEvaluatedState(PhaseResults result, int humanDmg, int aiDmg) { return null; }

    public @Override Events OnDrawState(GamePlayer player, Card card) { return null; }

    public @Override Events OnSkipState(GamePlayer player) { return null; }

    public @Override Events OnItemUseState(GamePlayer player, GameItem item) { return null; }
    
    /**
     * Refreshes the phase label.
     */
    private void RefreshPhase()
    {
        phaseLabel.SetText("Phase " + gameProcessor.GetPhase());
    }
}
