/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.components;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.rulesets.BaseRuleset;
import game.rulesets.Deck;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameModes;
import game.rulesets.GamePlayer;
import game.rulesets.GameProcessor;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;
import game.ui.cli.CliOverlayController;
import game.ui.cli.CliScreenController;

/**
 * Game info panel displayed on the right side of the game screen.
 * @author jerrykim
 */
public class CliGameInfoPanel extends CliDisplayer {
    
    private BaseRuleset ruleset;
    private GameProcessor gameProcessor;
    
    private CliCard deckDisplay;
    
    private boolean peekDeck;
    
    @ReceivesDependency
    private CliOverlayController overlays;
    
    @ReceivesDependency
    private CliScreenController screens;
    
    
    public CliGameInfoPanel(BaseRuleset ruleset)
    {
        this.ruleset = ruleset;
        this.gameProcessor = ruleset.GetGameProcessor();
    }
    
    @InitWithDependency
    private void Init()
    {
        // Deck card count displayer.
        deckDisplay = new CliCard();
        AddChild(deckDisplay);
    }
    
    /**
     * Sets whether deck's next card should be peekable.
     */
    public void SetPeekDeck(boolean peekDeck) { this.peekDeck = peekDeck; }
    
    /**
     * Returns the left-most occupied position of the panel.
     */
    public int GetLeft() { return 84; }
    
    public @Override void OnEnable()
    {
        SetPeekDeck(false);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int lastX = buffer.GetLastX();
        int lastY = buffer.GetLastY();
        int left = GetLeft();
        int centerX = (left + lastX) / 2;
        
        GamePlayer player = gameProcessor.GetHumanPlayer();
        GameAIPlayer aiPlayer = gameProcessor.GetAIPlayer();
        
        // Border
        for(int i=1; i<lastY; i++)
            buffer.SetBuffer('│', left, i);
        
        // Display game mode
        buffer.SetBuffer(ruleset.GetGameMode().toString(), centerX, 1, Pivot.Center);
        if(ruleset.GetGameMode() == GameModes.Survival)
            buffer.SetBuffer("Round: " + aiPlayer.GetDifficulty(), centerX, 2, Pivot.Center);
        else if(ruleset.GetGameMode() == GameModes.Casual)
            buffer.SetBuffer("Diff: " + aiPlayer.GetDifficulty(), centerX, 2, Pivot.Center);
        
        // Display phase
        buffer.SetBuffer("Phase: " + gameProcessor.GetPhase(), centerX, 4, Pivot.Center);
        
        // Display deck
        Deck deck = gameProcessor.GetDeck();
        buffer.SetBuffer("Deck", centerX, 6, Pivot.Center);
        deckDisplay.SetValue(deck.GetCardCount());
        deckDisplay.SetPosition(centerX-1, 7);
        if(peekDeck && deck.PeekCard() != null)
            buffer.SetBuffer("Next: " + deck.PeekCard().GetNumber(), centerX, 10, Pivot.Center);
        else
            buffer.SetBuffer("Next: ??", centerX, 10, Pivot.Center);
        
        // Display menus
        buffer.SetBuffer("Actions:", centerX, 12, Pivot.Center);
        if(!gameProcessor.IsFinished())
        {
            if(player.GetHand().GetTotalCardValue() < 21)
            {
                buffer.SetBuffer("[Draw]", centerX, 13, Pivot.Center);
            }
            buffer.SetBuffer("[Skip]", centerX, 14, Pivot.Center);
            buffer.SetBuffer("[Item]", centerX, 15, Pivot.Center);
            buffer.SetBuffer("[Exit]", centerX, 16, Pivot.Center);
        }
        else
        {
            buffer.SetBuffer("[End]", centerX, 13, Pivot.Center);
        }
        
        super.Render(buffer);
    }
}
