/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.allocation.IDependencyContainer;
import game.debug.Debug;
import game.rulesets.casual.CasualRuleset;
import game.rulesets.survival.SurvivalRuleset;
import java.util.HashMap;

/**
 * Static ruleset provider factory.
 * @author jerrykim
 */
public final class Rulesets {
    
    /**
     * Table of rulesets which have been cached.
     */
    private static HashMap<GameModes, BaseRuleset> CachedRulesets = new HashMap<GameModes, BaseRuleset>();
    
    /**
     * Returns a ruleset instance for specified game mode.
     * @param mode
     * @param dependencies
     */
    public static BaseRuleset GetRuleset(GameModes mode, IDependencyContainer dependencies)
    {
        BaseRuleset ruleset = CachedRulesets.get(mode);
        if(ruleset != null)
            return ruleset;
        
        // Create if not already cached.
        switch(mode)
        {
        case Survival:
            CachedRulesets.put(GameModes.Survival, ruleset = new SurvivalRuleset());
            break;
        case Casual:
            CachedRulesets.put(GameModes.Casual, ruleset = new CasualRuleset());
            break;
        default:
            Debug.LogError("Could not create ruleset for specified mode: " + mode);
            return null;
        }
        
        dependencies.Inject(ruleset);
        return ruleset;
    }
}
