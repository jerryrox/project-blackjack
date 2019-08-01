/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.ui.cli;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.rulesets.AiBehavior;
import game.rulesets.BaseRuleset;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameItem;
import game.rulesets.GamePlayer;
import game.rulesets.GameProcessor;
import game.rulesets.PhaseResults;
import game.rulesets.ui.IDrawableRuleset;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;
import game.ui.cli.CliOverlayController;
import game.ui.cli.CliScreenController;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.components.CliGameInfoPanel;
import game.ui.cli.components.CliHand;
import game.ui.cli.components.CliItemPanel;
import game.ui.cli.components.CliStatusBar;
import game.ui.cli.overlays.CliDialogOverlay;
import game.ui.cli.overlays.CliDialogPresets;
import game.ui.cli.screens.CliGameScreen;
import game.ui.cli.screens.CliMainScreen;
import game.ui.cli.screens.CliResultScreen;

/**
 * Base ruleset displayer implementation.
 * @author jerrykim
 */
public class CliRuleset<T extends BaseRuleset> extends CliDisplayer implements IDrawableRuleset<T> {

    /**
     * Ruleset instance which this displayer represents.
     */
    protected T ruleset;
    protected GameProcessor gameProcessor;
    
    private CliGameInfoPanel infoPanel;
    private CliStatusBar humanStatusBar;
    private CliStatusBar aiStatusBar;
    private CliHand humanHandDisplay;
    private CliHand aiHandDisplay;
    private CliItemPanel itemPanel;
    
    private AiBehavior aiBehavior;
    
    private String statusMessage;
    
    @ReceivesDependency
    private CliScreenController screens;
    
    @ReceivesDependency
    private CliOverlayController overlays;
    
    
    @InitWithDependency
    private void Init()
    {
        AddChild(infoPanel = new CliGameInfoPanel(ruleset));
        AddChild(humanStatusBar = new CliStatusBar());
        AddChild(aiStatusBar = new CliStatusBar());
        AddChild(humanHandDisplay = new CliHand());
        AddChild(aiHandDisplay = new CliHand());
        AddChild(itemPanel = new CliItemPanel());
        itemPanel.SetActive(false);
        
        GamePlayer humanPlayer = gameProcessor.GetHumanPlayer();
        
        CommandInfo draw = new CommandInfo("draw", (args) -> {
            DoDraw();
        }, () -> {
            return humanPlayer.GetHand().GetTotalCardValue() < 21 && !gameProcessor.IsFinished();
        });
        draw.SetDescription("Draws a card from the deck.");
        
        CommandInfo skip = new CommandInfo("skip", (args) -> {
            DoSkip();
        }, () -> {
            return !gameProcessor.IsFinished();
        });
        skip.SetDescription("Skips the draw.");
        
        CommandInfo item = new CommandInfo("item", (args) -> {
            DoItem();
        }, () -> {
            return !gameProcessor.IsFinished();
        });
        item.SetDescription("Use an item from your inventory.");
        
        CommandInfo exit = new CommandInfo("exit", (args) -> {
            DoExit();
        }, () -> {
            return !gameProcessor.IsFinished();
        });
        exit.SetDescription("Exits to home screen. Progress will be lost!");
        
        // End command is only available when the game has finished.
        CommandInfo end = new CommandInfo("end", (args) -> {
            screens.GetView(CliGameScreen.class).ExitGame(CliResultScreen.class);
        }, () -> {
            return gameProcessor.IsFinished();
        });
        
        commands.AddCommand(draw);
        commands.AddCommand(skip);
        commands.AddCommand(item);
        commands.AddCommand(exit);
        commands.AddCommand(end);
    }
    
    public @Override void OnStartSession()
    {
        // Initialize AI behavior handler
        aiBehavior = new AiBehavior(
            gameProcessor.GetAIPlayer(),
            gameProcessor.GetHumanPlayer(),
            gameProcessor.GetDeck()
        );
        
        GamePlayer humanPlayer = gameProcessor.GetHumanPlayer();
        GameAIPlayer aiPlayer = gameProcessor.GetAIPlayer();
        
        SetStatus(null);
        humanStatusBar.SetPlayer(humanPlayer);
        aiStatusBar.SetPlayer(aiPlayer);
        humanHandDisplay.SetPlayer(humanPlayer, true);
        aiHandDisplay.SetPlayer(aiPlayer, false);
    }

    public @Override void OnStopSession()
    {
    }
    
    public @Override void SetRuleset(T ruleset)
    {
        this.ruleset = ruleset;
        this.gameProcessor = ruleset.GetGameProcessor();
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        GamePlayer humanPlayer = gameProcessor.GetHumanPlayer();
        GameAIPlayer aiPlayer = gameProcessor.GetAIPlayer();
        int halfHeight = buffer.GetHalfHeight();
        
        // Draw status
        if(statusMessage != null && statusMessage.length() > 0)
            buffer.SetBuffer(String.format("[ %s ]", statusMessage), 2, 1);
        
        // Draw both players
        if(aiPlayer.IsRevealing())
            buffer.SetBuffer(String.format("Total: %d", aiPlayer.GetHand().GetTotalCardValue()), 2, halfHeight-7);
        else
            buffer.SetBuffer(String.format("Total: %d + ??", aiPlayer.GetHand().GetTotalVisibleValue()), 2, halfHeight-7);
        aiHandDisplay.SetPosition(2, halfHeight-6);
        aiStatusBar.SetPosition(2, halfHeight-2);
        buffer.SetBuffer("(AI)", 2, halfHeight-1);
        buffer.SetBuffer("vs.", 2, halfHeight);
        buffer.SetBuffer("(You)", 2, halfHeight+1);
        humanStatusBar.SetPosition(2, halfHeight+2);
        humanHandDisplay.SetPosition(2, halfHeight+4);
        buffer.SetBuffer(String.format("Total: %d", humanPlayer.GetHand().GetTotalCardValue()), 2, halfHeight+7);
        DrawUsingItems(buffer, humanPlayer, 2, halfHeight+8);
        
        super.Render(buffer);
    }
    
    /**
     * Sets status message string.
     * @param message 
     */
    private void SetStatus(String message)
    {
        statusMessage = message;
    }

    /**
     * Draws all using items for specified player.
     */
    private void DrawUsingItems(CliBuffer buffer, GamePlayer player, int x, int y)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Items: ");
        
        boolean isFirst = true;
        for(GameItem item : player.GetUsingItems())
        {
            if(!isFirst)
                sb.append('|');
            isFirst = false;
            sb.append(item.Info.Name);
        }
        
        final int maxLength = 80;
        if(sb.length() > maxLength)
        {
            sb.setLength(76);
            sb.append(" ...");
        }
        buffer.SetBuffer(sb.toString(), x, y);
    }
    
    /**
     * Performs draw command action.
     */
    private void DoDraw()
    {
        SetStatus("Drew a card.");
        
        GamePlayer player = gameProcessor.GetHumanPlayer();
        player.GetHand().DrawCard(gameProcessor.GetDeck());
        player.SetDrawn(true);
        
        // Next deck card shouldn't be peekable.
        infoPanel.SetPeekDeck(false);
        
        // Draw a card for AI
        HandleAiDraw();
        
        // Eval result
        EvaluateResult();
    }
    
    /**
     * Performs skip command action.
     */
    private void DoSkip()
    {
        SetStatus("Skipped turn.");
        
        GamePlayer player = gameProcessor.GetHumanPlayer();
        player.SetDrawn(false);
        
        // Next deck card shouldn't be peekable.
        infoPanel.SetPeekDeck(false);
        
        // Draw a card for AI
        HandleAiDraw();
        
        // Eval result
        EvaluateResult();
    }
    
    /**
     * Performs item command action.
     */
    private void DoItem()
    {
        itemPanel.SetContext(infoPanel, ruleset);
        itemPanel.SetActive(true);
    }
    
    /**
     * Performs exit command action.
     */
    private void DoExit()
    {
        CliGameScreen gameScreen = screens.GetView(CliGameScreen.class);
        CliDialogOverlay overlay = overlays.ShowView(CliDialogOverlay.class);
        CliDialogPresets.SetExitGame(overlay, (a) -> gameScreen.ExitGame(CliMainScreen.class), null);
    }
    
    /**
     * Handles AI drawing process.
     */
    private void HandleAiDraw()
    {
        // In CLI, instantly switch turn to ai.
        gameProcessor.SwitchTurn();
        
        GamePlayer ai = gameProcessor.GetAIPlayer();
        if(aiBehavior.WillDraw())
        {
            ai.GetHand().DrawCard(gameProcessor.GetDeck());
            ai.SetDrawn(true);
        }
        else
        {
            ai.SetDrawn(false);
        }
        gameProcessor.SwitchTurn();
    }
    
    /**
     * Evaluates phase result.
     */
    private void EvaluateResult()
    {
        if(!gameProcessor.ShouldEvalPhase())
            return;
        
        GamePlayer humanPlayer = gameProcessor.GetHumanPlayer();
        GameAIPlayer aiPlayer = gameProcessor.GetAIPlayer();
        
        PhaseResults phaseResult = gameProcessor.GetPhaseResult();
        int aiDmg = 0;
        int humanDmg = 0;
        switch(phaseResult)
        {
        case Draw:
            aiDmg = humanPlayer.ApplyDamage(aiPlayer.GetDamage(), aiPlayer.GetCriticalChance());
            humanDmg = aiPlayer.ApplyDamage(humanPlayer.GetDamage(), humanPlayer.GetCriticalChance());
            SetStatus(String.format(
                "It's a draw | Total (%d:%d) | You took %d dmg, AI took %d dmg.",
                humanPlayer.GetHand().GetTotalCardValue(),
                aiPlayer.GetHand().GetTotalCardValue(),
                aiDmg,
                humanDmg
            ));
            break;
        case AiWin:
            aiDmg = humanPlayer.ApplyDamage(aiPlayer.GetDamage(), aiPlayer.GetCriticalChance());
            SetStatus(String.format(
                "AI wins | Total (%d:%d) | You took %d dmg.",
                humanPlayer.GetHand().GetTotalCardValue(),
                aiPlayer.GetHand().GetTotalCardValue(),
                aiDmg
            ));
            break;
        case HumanWin:
            humanDmg = aiPlayer.ApplyDamage(humanPlayer.GetDamage(), humanPlayer.GetCriticalChance());
            SetStatus("You win | AI took " + humanDmg + " dmg");
            SetStatus(String.format(
                "You win | Total (%d:%d) | AI took %d dmg.",
                humanPlayer.GetHand().GetTotalCardValue(),
                aiPlayer.GetHand().GetTotalCardValue(),
                humanDmg
            ));
            break;
        }
        
        // Set end phase.
        gameProcessor.SetPhaseEnd();
    }
}
