/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.ui.gui;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Action;
import game.data.FuncTT;
import game.data.Vector2;
import game.debug.Debug;
import game.rulesets.AiBehavior;
import game.rulesets.BaseRuleset;
import game.rulesets.Card;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameItem;
import game.rulesets.GamePlayer;
import game.rulesets.GameProcessor;
import game.rulesets.PhaseResults;
import game.rulesets.items.CloneItemInfo;
import game.rulesets.items.ItemInfo;
import game.rulesets.items.ReturnItemInfo;
import game.rulesets.items.RevealItemInfo;
import game.rulesets.ui.IDrawableRuleset;
import game.rulesets.ui.gui.controller.GuiGameAiController;
import game.rulesets.ui.gui.controller.GuiGameHumanController;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIObject;
import game.ui.gui.objects.customized.UIGameInfoPanel;
import game.ui.gui.objects.customized.UIGamePlayArea;
import game.ui.gui.objects.customized.UIInputBlocker;
import game.ui.gui.overlays.UIDialogOverlay;
import game.ui.gui.screens.UIGameScreen;
import game.ui.gui.screens.UIMainScreen;
import game.ui.gui.screens.UIResultScreen;

/**
 * GUI representation of the ruleset.
 * @author jerrykim
 */
public class GuiRuleset extends UIObject implements IDrawableRuleset<BaseRuleset> {
    
    /**
     * Ruleset which this GUI represents.
     */
    private BaseRuleset ruleset;
    
    /**
     * Game logic processor.
     */
    private GameProcessor gameProcessor;
    
    private UIInputBlocker inputBlocker;
    private UIGameInfoPanel infoPanel;
    private UIGamePlayArea playerArea;
    private UIGamePlayArea aiArea;
    
    private GuiGameHumanController humanController;
    private GuiGameAiController aiController;
    
    private GuiGameStateHandler stateHandler;
    
    private boolean isInitialized = false;
    
    @ReceivesDependency
    private UIScreenController screens;
    
    @ReceivesDependency
    private UIOverlayController overlays;

    
    public GuiRuleset()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        if(isInitialized)
            return;
        isInitialized = true;
        
        AddChild(inputBlocker = new UIInputBlocker());
        {
            inputBlocker.SetActive(false);
        }
        
        final float playAreaX = -165;
        UISprite line = CreateChild().AddComponent(new UISprite());
        {
            line.SetSpritename("box");
            line.SetSize(860, 2);
            line.SetAlpha(0.25f);
            line.GetTransform().SetLocalPosition(playAreaX, 0);
        }
        
        AddChild(infoPanel = new UIGameInfoPanel(this, ruleset));
        AddChild(playerArea = new UIGamePlayArea());
        {
            playerArea.GetTransform().SetLocalPosition(playAreaX, 0);
        }
        AddChild(aiArea = new UIGamePlayArea());
        {
            aiArea.GetTransform().SetLocalPosition(playAreaX, 0);
        }
        
        // Add all game state listeners.
        stateHandler = new GuiGameStateHandler();
        stateHandler.AddListener(playerArea);
        stateHandler.AddListener(playerArea.GetHealthbar());
        stateHandler.AddListener(playerArea.GetHand());
        stateHandler.AddListener(playerArea.GetPhaseResulter());
        stateHandler.AddListener(playerArea.GetItemList());
        stateHandler.AddListener(aiArea);
        stateHandler.AddListener(aiArea.GetHealthbar());
        stateHandler.AddListener(aiArea.GetHand());
        stateHandler.AddListener(aiArea.GetPhaseResulter());
        stateHandler.AddListener(aiArea.GetItemList());
        stateHandler.AddListener(infoPanel.GetDeck());
        stateHandler.AddListener(infoPanel);
    }
    
    public @Override void SetRuleset(BaseRuleset ruleset)
    {
        this.ruleset = ruleset;
        this.gameProcessor = ruleset.GetGameProcessor();
    }

    public @Override void OnStartSession()
    {
        // Initialize state handler.
        stateHandler.Initialize();
        
        // Initialize controllers.
        humanController = new GuiGameHumanController(this, gameProcessor.GetHumanPlayer(), infoPanel, overlays);
        aiController = new GuiGameAiController(this, gameProcessor.GetAIPlayer(), gameProcessor.GetHumanPlayer(), gameProcessor.GetDeck());
        
        // Initialize game play areas with their corresponding players.
        playerArea.SetPlayer(gameProcessor.GetHumanPlayer());
        aiArea.SetPlayer(gameProcessor.GetAIPlayer());
        
        // Initialize phase result displayers
        playerArea.GetPhaseResulter().SetGameProcessor(gameProcessor);
        aiArea.GetPhaseResulter().SetGameProcessor(gameProcessor);
        
        // Refresh info panel.
        infoPanel.RefreshInfo();
        
        // Link deck position to player and AI's hand objects.
        Vector2 deckCardPos = infoPanel.GetDeck().GetCardPosition();
        playerArea.GetHand().SetDeckPosition(deckCardPos);
        playerArea.GetHand().PrecookCards();
        aiArea.GetHand().SetDeckPosition(deckCardPos);
        aiArea.GetHand().PrecookCards();
        
        // Start new phase.
        SetNewPhase();
    }

    public @Override void OnStopSession()
    {
        stateHandler.Dispose();
        
        playerArea.Dispose();
        aiArea.Dispose();
        
        if(humanController != null)
            humanController.Dispose();
        humanController = null;
        
        if(aiController != null)
            aiController.Dispose();
        aiController = null;
    }
    
    /**
     * Quits game.
     */
    public void QuitGame()
    {
        UIGameScreen gameScreen = screens.GetView(UIGameScreen.class);
        UIDialogOverlay dialog = overlays.ShowView(UIDialogOverlay.class);
        dialog.SetYesNoMode(
            "Are you sure you want to exit the game play? Your progress will not be saved!",
            () -> {
                gameScreen.ExitGame(UIMainScreen.class);
            },
            null
        );
    }
    
    /**
     * Performs evaluation of phase results.
     */
    public void SetEvaluation()
    {
        // Evaluate result
        PhaseResults result = gameProcessor.GetPhaseResult();
        GameAIPlayer aiPlayer = gameProcessor.GetAIPlayer();
        GamePlayer humanPlayer = gameProcessor.GetHumanPlayer();
        
        // Apply result on players.
        int humanDmg = 0;
        int aiDmg = 0;
        switch(result)
        {
        case Draw:
            humanDmg = aiPlayer.ApplyDamage(humanPlayer.GetDamage(), humanPlayer.GetCriticalChance());
            aiDmg = humanPlayer.ApplyDamage(aiPlayer.GetDamage(), aiPlayer.GetCriticalChance());
            break;
        case AiWin:
            aiDmg = humanPlayer.ApplyDamage(aiPlayer.GetDamage(), aiPlayer.GetCriticalChance());
            break;
        case HumanWin:
            humanDmg = aiPlayer.ApplyDamage(humanPlayer.GetDamage(), humanPlayer.GetCriticalChance());
            break;
        }
        
        // Java limitations
        final int finalHumanDmg = humanDmg;
        final int finalAiDmg = aiDmg;
        
        SetState(
            (handler, action) -> handler.InvokeEvaluated(result, finalHumanDmg, finalAiDmg, action),
            () -> {
                // End turn.
                SetPhaseEnd();
            }
        );
    }
    
    /**
     * Performs turn end action on current player.
     */
    public void SetTurnEnd(GamePlayer player, boolean hasDrawn)
    {
        // There is no specific "turn end" process in game processor,
        // but we allow listening to such a conceptual event.
        SetState(
            (handler, action) -> handler.InvokeTurnEnd(player, action),
            () -> {
                if(hasDrawn)
                    SetSwitchTurn();
                else
                {
                    // Change turn if human aplayer.
                    if(player.IsHuman())
                        SetSwitchTurn();
                    else
                    {
                        // If evaluation required, evaluate.
                        if(gameProcessor.ShouldEvalPhase())
                            SetEvaluation();
                        // Else, change turn.
                        else
                            SetSwitchTurn();
                    }
                }
            }
        );
    }
    
    /**
     * Performs drawing of card on specified player.
     */
    public void SetDraw(GamePlayer player)
    {
        // Draw card.
        Card card = player.GetHand().DrawCard(gameProcessor.GetDeck());
        player.SetDrawn(true);
        SetState(
            (handler, action) -> handler.InvokeDraw(player, card, action),
            () -> {
                // End turn.
                SetTurnEnd(player, true);
            }
        );
    }
    
    /**
     * Performs skipping of turn on specified player.
     */
    public void SetSkip(GamePlayer player)
    {
        // Skip turn.
        player.SetDrawn(false);
        SetState(
            (handler, action) -> handler.InvokeSkip(player, action),
            () -> {
                // End turn.
                SetTurnEnd(player, false);
            }
        );
    }
    
    /**
     * Performs using item on specified player.
     */
    public void SetItemUse(GamePlayer player, ItemInfo item, Action callback)
    {
        // Apply item
        GameItem gameItem = player.ApplyItem(item);
        if(item instanceof ReturnItemInfo)
        {
            player.GetHand().ReturnCard(gameProcessor.GetDeck());
            gameProcessor.GetDeck().Shuffle();
        }
        else if(item instanceof CloneItemInfo)
            player.GetHand().CloneLastCard();
        else if(item instanceof RevealItemInfo)
            gameProcessor.GetPlayerNotOnTurn().SetReveal(true);
        
        SetState(
            (handler, action) -> handler.InvokeItemUse(player, gameItem, action),
            callback
        );
    }
    
    /**
     * Performs setting turn to human player.
     */
    public void SetHumanPlayerTurn()
    {
        // Set turn
        gameProcessor.SetHumanTurn(true);
        // Set turn on controllers.
        GamePlayer player = gameProcessor.GetPlayerOnTurn();
        humanController.SetTurn(player.IsHuman());
        aiController.SetTurn(!player.IsHuman());
        
        SetState(
            (handler, action) -> handler.InvokeSetTurn(gameProcessor.GetPlayerOnTurn(), action),
            null
        );
    }
    
    /**
     * Performs turn switching.
     */
    public void SetSwitchTurn()
    {
        // Switch turn.
        gameProcessor.SwitchTurn();
        // Set turn on controllers.
        GamePlayer player = gameProcessor.GetPlayerOnTurn();
        humanController.SetTurn(player.IsHuman());
        aiController.SetTurn(!player.IsHuman());
                
        SetState(
            (handler, action) -> handler.InvokeSetTurn(player, action),
            null
        );
    }
    
    /**
     * Performs new phase action.
     */
    public void SetNewPhase()
    {
        // Set new phase.
        gameProcessor.NewPhase();
        SetState(
            (handler, action) -> handler.InvokeNewPhase(action),
            () -> {
                // Set to human's turn.
                SetHumanPlayerTurn();
            }
        );
    }
    
    /**
     * Performs phase end action.
     */
    public void SetPhaseEnd()
    {
        // End phase
        gameProcessor.SetPhaseEnd();
        SetState(
            (handler, action) -> handler.InvokePhaseEnd(action),
            () -> {
                // If game ended, finish.
                if(gameProcessor.IsFinished())
                    SetGameFinish();
                // Else if not finished, set new phase.
                else
                    SetNewPhase();
            }
        );
    }
    
    /**
     * Performs game finish action.
     */
    public void SetGameFinish()
    {
        screens.GetView(UIGameScreen.class).ExitGame(UIResultScreen.class);
    }
    
    /**
     * Handles repeated state change invocation processes.
     */
    private void SetState(FuncTT<GuiGameStateHandler, Action, Boolean> action, Action callback)
    {
        // Check whether invocation returns that a time-consuming process has started.
        boolean handle = action.Invoke(stateHandler, () -> {
            // Action finished.
            inputBlocker.SetActive(false);
            if(callback != null)
                callback.Invoke();
        });
        // If true, block inputs while whatever animation or etc are being performed.
        if(handle)
            inputBlocker.SetActive(true);
        // Otherwise, invoke callback.
        else if(callback != null)
            callback.Invoke();
    }
}
