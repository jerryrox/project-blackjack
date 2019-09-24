/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.survival;

import game.IGame;
import game.allocation.ReceivesDependency;
import game.debug.Debug;
import game.entities.UserEntity;
import game.rulesets.BaseRuleset;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameModes;
import game.rulesets.ui.IDrawableRuleset;
import game.rulesets.ui.cli.CliSurvivalRuleset;
import game.rulesets.ui.gui.GuiRuleset;

/**
 * Ruleset implementation for Survival game mode.
 * @author jerrykim
 */
public class SurvivalRuleset extends BaseRuleset {
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private IGame game;
    
    
    public @Override void OnStartSession()
    {
        // In survival ruleset, the AI's difficulty depends on user's best survival level value.
        GameAIPlayer ai = gameProcessor.GetAIPlayer();
        ai.SetDifficulty(user.SurvivalRound.GetValue() + 1);
        
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
    
    public @Override GameModes GetGameMode() { return GameModes.Survival; }
}
