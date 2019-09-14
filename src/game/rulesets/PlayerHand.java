/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.data.Yieldable;
import java.util.ArrayList;

/**
 * Representation of player's hand.
 * @author jerrykim
 */
public class PlayerHand {
    
    /**
     * Player that owns this hand.
     */
    private GamePlayer player;
    
    /**
     * List of cards held in hand.
     */
    private ArrayList<Card> hand = new ArrayList<Card>();
    
    
    public PlayerHand(GamePlayer player)
    {
        this.player = player;
    }
    
    /**
     * Clones last drawn card and adds to the hand.
     */
    public void CloneLastCard()
    {
        hand.add(new Card(hand.get(hand.size() - 1)));
    }
    
    /**
     * Draws card from the deck.
     */
    public Card DrawCard(Deck deck)
    {
        Card card = deck.PopCard();
        hand.add(card);
        return card;
    }
    
    /**
     * Returns the last card in hand to the deck.
     * @param deck 
     */
    public void ReturnCard(Deck deck)
    {
        if(hand.size() > 0)
            deck.AddCard(hand.remove(hand.size() - 1));
    }
    
    /**
     * Returns all cards in hand to deck.
     * @param deck 
     */
    public void ReturnAllCards(Deck deck)
    {
        for(int i=0; i<hand.size(); i++)
            deck.AddCard(hand.get(i));
        hand.clear();
    }
    
    /**
     * Returns the number of cards in hand.
     */
    public int GetCardCount() { return hand.size(); }
    
    /**
     * Clears card remnants remaining from stopping the game.
     */
    public void ClearCards() { hand.clear(); }
    
    /**
     * Returns all cards in hand.
     */
    public Iterable<Card> GetAllCards() { return hand; }
    
    /**
     * Returns the card at specified index.
     */
    public Card GetCard(int index)
    {
        if(index < 0 || index >= hand.size())
            return null;
        return hand.get(index);
    }
    
    /**
     * Returns all cards in hand except the first one.
     */
    public Iterable<Card> GetVisibleCards()
    {
        return new Yieldable<Card>(yield -> {
            for(int i=1; i<hand.size(); i++)
                yield.Return(hand.get(i));
        });
    }
    
    /**
     * Returns the total value of all cards in hand.
     */
    public int GetTotalCardValue()
    {
        int val = 0;
        for(int i=0; i<hand.size(); i++)
            val += hand.get(i).GetNumber();
        return val;
    }
    
    /**
     * Returns the total value of all visible cards in hand.
     */
    public int GetTotalVisibleValue()
    {
        int val = 0;
        for(int i=1; i<hand.size(); i++)
            val += hand.get(i).GetNumber();
        return val;
    }
}
