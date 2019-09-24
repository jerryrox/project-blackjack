/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.utils.Random;
import java.util.ArrayList;

/**
 * Holds piles of cards to be used in game.
 * @author jerrykim
 */
public class Deck {
    
    public static final int CardsPerSet = 11;
    
    private ArrayList<Card> cards = new ArrayList<>();
    
    private int setCount;
    
    
    public Deck(int setCount)
    {
        this.setCount = setCount;
        
        // Create sets of cards.
        for(int i=0; i<setCount; i++)
            AddSetTo(cards);
    }
    
    public void Shuffle()
    {
        for(int i=0; i<cards.size(); i++)
        {
            int targetInx = Random.Range(0, cards.size());
            Card card = cards.get(i);
            cards.set(i, cards.get(targetInx));
            cards.set(targetInx, card);
        }
    }
    
    public int GetSetCount() { return setCount; }
    
    public int GetCardCount() { return cards.size(); }
    
    public boolean AddCard(Card card)
    {
        if(card.IsCloned())
            return false;
        if(cards.contains(card))
            return false;
        cards.add(card);
        return true;
    }
    
    public void AddSetTo(ArrayList<Card> list)
    {
        for(int i=1; i<12; i++)
            list.add(new Card(i));
    }
    
    public Card PopCard()
    {
        if(cards.size() == 0)
            return null;
        return cards.remove(cards.size()-1);
    }
    
    public Card PeekCard()
    {
        if(cards.size() == 0)
            return null;
        return cards.get(cards.size()-1);
    }
    
    public boolean HasCards() { return cards.size() > 0; }
    
    public Iterable<Card> GetAllCards() { return cards; }
}
