/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.ui;

import game.rulesets.BaseRuleset;
import game.rulesets.IGameSession;
import game.ui.cli.IDisplayer;

/**
 * Provides a general interface for a drawable ruleset object.
 * @author jerrykim
 */
public interface IDrawableRuleset<T extends BaseRuleset> extends IGameSession {
    
    /**
     * Sets the ruleset instance.
     */
    void SetRuleset(T ruleset);
}
