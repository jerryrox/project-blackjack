/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.casual;

import game.Application;
import game.debug.Debug;
import game.entities.UserStats;
import game.rulesets.BaseRuleset;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameModes;
import game.rulesets.ui.IDrawableRuleset;
import game.rulesets.ui.cli.CliSurvivalRuleset;
import game.utils.Random;

/**
 * Ruleset implementation for Casual game mode.
 * @author jerrykim
 */
public class CasualRuleset extends BaseRuleset {
    
    public @Override void OnStartSession()
    {
        // In casual ruleset, the enemy ai's difficulty should be adjusted
        // somewhere near player's level.
        UserStats stats = players[0].GetUser().GetStats();
        int difficulty = (stats.Power.GetLevel() + stats.Armor.GetLevel() + stats.Endurance.GetLevel() + stats.Luck.GetLevel()) / 8;
        
        GameAIPlayer ai = (GameAIPlayer)players[1];
        ai.SetDifficulty(Random.Range(difficulty-2, difficulty+2));
        
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
    
    public @Override GameModes GetGameMode() { return GameModes.Casual; }
}
