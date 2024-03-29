/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.casual;

import game.IGame;
import game.allocation.ReceivesDependency;
import game.debug.Debug;
import game.entities.UserEntity;
import game.entities.UserStats;
import game.rulesets.BaseRuleset;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameModes;
import game.rulesets.ui.IDrawableRuleset;
import game.rulesets.ui.cli.CliSurvivalRuleset;
import game.rulesets.ui.gui.GuiRuleset;
import game.utils.Random;

/**
 * Ruleset implementation for Casual game mode.
 * @author jerrykim
 */
public class CasualRuleset extends BaseRuleset {
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private IGame game;
    
    
    public @Override void OnStartSession()
    {
        // In casual ruleset, the enemy ai's difficulty should be adjusted
        // somewhere near player's level.
        UserStats stats = user.GetStats();
        int difficulty = (stats.Power.Level.GetValue() + stats.Armor.Level.GetValue() + stats.Endurance.Level.GetValue() + stats.Luck.Level.GetValue()) / 2;
        
        GameAIPlayer ai = gameProcessor.GetAIPlayer();
        ai.SetDifficulty(Math.max(Random.Range(difficulty-2, difficulty+5), 0));
        
        super.OnStartSession();
    }
    
    protected @Override IDrawableRuleset CreateDrawableRuleset()
    {
        switch(game.GetRuntime())
        {
        case Cli: return new CliSurvivalRuleset();
        case Gui: return new GuiRuleset();
        }
        return null;
    }
    
    public @Override GameModes GetGameMode() { return GameModes.Casual; }
}
