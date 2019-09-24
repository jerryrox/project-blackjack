/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class DeckIT {
    
    /**
     * Testing deck shuffling.
     */
    @Test
    public void TestShuffle()
    {
        Deck deck = new Deck(1);
        assertTrue(deck.HasCards());
        
        int i = 1;
        for(Card card : deck.GetAllCards())
            assertEquals(card.GetNumber(), i++);
        
        deck.Shuffle();
        boolean isOutOfOrder = false;
        i = 1;
        for(Card card : deck.GetAllCards())
        {
            if(card.GetNumber() != i++)
            {
                isOutOfOrder = true;
                break;
            }
        }
        assertTrue(isOutOfOrder);
    }
    
    /**
     * Testing card interaction (pop, peek, add)
     */
    @Test
    public void TestCardInteraction()
    {
        Deck deck = new Deck(1);
        assertTrue(deck.HasCards());
        deck.Shuffle();
        
        Card card = deck.PeekCard();
        assertEquals(card, deck.PopCard());
        assertNotEquals(card, deck.PeekCard());
        
        deck.AddCard(card);
        assertEquals(card, deck.PeekCard());
    }
}
