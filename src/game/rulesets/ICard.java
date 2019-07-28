/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import java.util.Collection;

/**
 * Provides a general signature for cards across different implementation in rulesets.
 * @author jerrykim
 */
public interface ICard {
    
    /**
     * Returns the numeric value on the card.
     */
    int GetNumber();
    
    /**
     * Returns whether the card is a cloned instance.
     */
    boolean IsCloned();
}
