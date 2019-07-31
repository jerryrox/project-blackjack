/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.components;

import game.allocation.InitWithDependency;
import game.rulesets.BaseRuleset;
import game.rulesets.GameModes;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;

/**
 * Game info panel displayed on the right side of the game screen.
 * @author jerrykim
 */
public class CliGameInfoPanel extends CliDisplayer {
    
    private BaseRuleset ruleset;
    
    private CliCard deckDisplay;
    
    
    public CliGameInfoPanel(BaseRuleset ruleset)
    {
        this.ruleset = ruleset;
    }
    
    @InitWithDependency
    private void Init()
    {
        deckDisplay = new CliCard();
        AddChild(deckDisplay);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int lastX = buffer.GetLastX();
        int lastY = buffer.GetLastY();
        int left = lastX - 15;
        int centerX = (left + lastX) / 2;
        
        // Border
        for(int i=1; i<lastY; i++)
            buffer.SetBuffer('│', left, i);
        
        // Display game mode
        buffer.SetBuffer(ruleset.GetGameMode().toString(), centerX, 1, Pivot.Center);
        
        // Display phase
        buffer.SetBuffer("Phase: " + ruleset.GetPhase(), centerX, 3, Pivot.Center);
        
        // Display deck
        buffer.SetBuffer("Deck", centerX, 5, Pivot.Center);
        deckDisplay.SetValue(ruleset.GetDeck().GetCardCount());
        deckDisplay.SetPosition(centerX-1, 6);
        
        super.Render(buffer);
    }
}
