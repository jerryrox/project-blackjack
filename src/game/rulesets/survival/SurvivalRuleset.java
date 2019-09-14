/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.survival;

import game.Application;
import game.allocation.ReceivesDependency;
import game.debug.Debug;
import game.entities.UserEntity;
import game.rulesets.BaseRuleset;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameModes;
import game.rulesets.ui.IDrawableRuleset;
import game.rulesets.ui.cli.CliSurvivalRuleset;
import game.rulesets.ui.gui.GuiRuleset;
import game.ui.cli.IDisplayer;

/**
 * Ruleset implementation for Survival game mode.
 * @author jerrykim
 */
public class SurvivalRuleset extends BaseRuleset {
    
    @ReceivesDependency
    private UserEntity user;
    
    
    public @Override void OnStartSession()
    {
        // In survival ruleset, the AI's difficulty depends on user's best survival level value.
        GameAIPlayer ai = gameProcessor.GetAIPlayer();
        ai.SetDifficulty(user.SurvivalRound.GetValue() + 1);
        
        super.OnStartSession();
    }
    
    protected @Override IDrawableRuleset CreateDrawableRuleset()
    {
        switch(Application.Runtime)
        {
        case Console: return new CliSurvivalRuleset();
        case Gui: return new GuiRuleset();
        }
        return null;
    }
    
    public @Override GameModes GetGameMode() { return GameModes.Survival; }
}
