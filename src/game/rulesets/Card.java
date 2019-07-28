/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

/**
 * A card used in the game.
 * @author jerrykim
 */
public class Card implements ICard {
    
    /**
     * Number value of the card.
     */
    private int number = 0;
    
    /**
     * Whether the card is a cloned instance created from item effect.
     */
    private boolean isCloned = false;
    
    
    public Card(int number)
    {
        this.number = number;
    }
    
    public Card(Card card)
    {
        number = card.number;
        isCloned = true;
    }
    
    public @Override int GetNumber() { return number; }
    
    public @Override boolean IsCloned() { return isCloned; }
}
