/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.utils.MathUtils;
import game.utils.Random;

/**
 * A card used in the game.
 * @author jerrykim
 */
public class Card {
    
    /**
     * Maximum value of a card.
     */
    public static final int MaxValue = 11;
    
    /**
     * Minimum value of a card.
     */
    public static final int MinValue = 1;
    
    /**
     * Number value of the card.
     */
    private int number = MinValue;
    
    /**
     * Whether the card is a cloned instance created from item effect.
     */
    private boolean isCloned = false;
    
    
    public Card()
    {
        this(Random.Range(MinValue, MaxValue + 1));
    }
    
    public Card(int number)
    {
        this.number = MathUtils.Clamp(number, MinValue, MaxValue);
    }
    
    public Card(Card card)
    {
        number = card.number;
        isCloned = true;
    }
    
    public int GetNumber() { return number; }
    
    public boolean IsCloned() { return isCloned; }
}
