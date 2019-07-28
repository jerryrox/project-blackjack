/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

/**
 * Provides general signatures for accessing the deck across different rulesets.
 * @author jerrykim
 */
public interface IDeck<T extends ICard> {
    
    /**
     * Shuffles all cards in the deck.
     */
    void Shuffle();
    
    /**
     * Returns the number of card sets which the deck was initialized with.
     */
    int GetSetCount();
    
    /**
     * Returns the number of cards remaining in the pile.
     */
    int GetCardCount();
    
    /**
     * Adds specified card to the pile and returns whether it was added.
     * @param card
     */
    boolean AddCard(T card);
    
    /**
     * Takes a card from the deck, removing it from the pile.
     */
    T PopCard();
    
    /**
     * Checks the next card to be returned from the pile, if exists.
     */
    T PeekCard();
    
    /**
     * Returns whether there is a card in the pile.
     */
    boolean HasCard();
}
