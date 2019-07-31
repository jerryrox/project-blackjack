/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.ui.cli;

import game.allocation.InitWithDependency;
import game.rulesets.BaseRuleset;
import game.rulesets.ui.IDrawableRuleset;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;
import game.ui.cli.components.CliCard;
import game.ui.cli.components.CliGameInfoPanel;

/**
 * Base ruleset displayer implementation.
 * @author jerrykim
 */
public class CliRuleset<T extends BaseRuleset> extends CliDisplayer implements IDrawableRuleset<T> {

    /**
     * Ruleset instance which this displayer represents.
     */
    protected T ruleset;
    
    
    @InitWithDependency
    private void Init()
    {
        AddChild(new CliGameInfoPanel(ruleset));
    }
    
    public @Override void OnStartSession()
    {
    }

    public @Override void OnStopSession()
    {
    }
    
    public @Override void SetRuleset(T ruleset)
    {
        this.ruleset = ruleset;
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        buffer.SetBuffer("Test string rendering", buffer.GetHalfWidth(), buffer.GetHalfHeight(), Pivot.Center);
        buffer.SetBuffer("Current game mode: " + ruleset.GetGameMode().toString(), buffer.GetHalfWidth(), buffer.GetHalfHeight()+1, Pivot.Center);
        
        super.Render(buffer);
    }

}
