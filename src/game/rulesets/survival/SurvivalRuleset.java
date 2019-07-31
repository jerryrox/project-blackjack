/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.survival;

import game.Application;
import game.debug.Debug;
import game.rulesets.BaseRuleset;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameModes;
import game.rulesets.ui.IDrawableRuleset;
import game.rulesets.ui.cli.CliSurvivalRuleset;
import game.ui.IDisplayer;

/**
 * Ruleset implementation for Survival game mode.
 * @author jerrykim
 */
public class SurvivalRuleset extends BaseRuleset {
    
    public @Override void OnStartSession()
    {
        // In survival ruleset, the AI's difficulty depends on user's best survival level value.
        GameAIPlayer ai = (GameAIPlayer) players[1];
        ai.SetDifficulty(user.GetSurvivalRound() + 1);
        
        super.OnStartSession();
    }
    
    protected @Override IDrawableRuleset CreateDrawableRuleset()
    {
        switch(Application.Runtime)
        {
        case Console: return new CliSurvivalRuleset();
        }
        return null;
    }
    
    public @Override GameModes GetGameMode() { return GameModes.Survival; }
}
