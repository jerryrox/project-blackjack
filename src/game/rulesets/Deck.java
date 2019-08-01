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
public class Deck implements IDeck<Card> {
    
    private ArrayList<Card> cards = new ArrayList<Card>();
    
    private int setCount;
    
    
    public Deck(int setCount)
    {
        this.setCount = setCount;
        
        // Create sets of cards.
        for(int s=0; s<setCount; s++)
        {
            for(int i=0; i<11; i++)
                cards.add(new Card(i+1));
        }
    }
    
    public @Override void Shuffle()
    {
        for(int i=0; i<cards.size(); i++)
        {
            int targetInx = Random.Range(0, cards.size());
            Card card = cards.get(i);
            cards.set(i, cards.get(targetInx));
            cards.set(targetInx, card);
        }
    }
    
    public @Override int GetSetCount() { return setCount; }
    
    public @Override int GetCardCount() { return cards.size(); }
    
    public @Override boolean AddCard(Card card)
    {
        if(card.IsCloned())
            return false;
        if(cards.contains(card))
            return false;
        cards.add(card);
        return true;
    }
    
    public @Override Card PopCard()
    {
        if(cards.size() == 0)
            return null;
        return cards.remove(cards.size()-1);
    }
    
    public @Override Card PeekCard()
    {
        if(cards.size() == 0)
            return null;
        return cards.get(cards.size()-1);
    }
    
    public @Override boolean HasCard() { return cards.size() > 0; }
    
    public @Override Iterable<Card> GetAllCards() { return cards; }
}
