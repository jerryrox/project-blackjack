/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.entities.UserEntity;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class PlayerHandIT {
    
    /**
     * Testing initialization of player hand.
     */
    @Test
    public void TestInit()
    {
        PlayerHand hand = new PlayerHand(new GamePlayer(new UserEntity()));
        assertEquals(hand.GetCardCount(), 0);
        assertEquals(hand.GetTotalCardValue(), 0);
        assertEquals(hand.GetTotalVisibleValue(), 0);
    }
    
    /**
     * Testing basic card add/remove functions.
     */
    @Test
    public void TestCardAddRemove()
    {
        Deck deck = new Deck(1);
        Card firstCard = deck.PeekCard();
        PlayerHand hand = new PlayerHand(new GamePlayer(new UserEntity()));
        
        // Drawing card
        hand.DrawCard(deck);
        assertEquals(firstCard, hand.GetCard(0));
        assertNotEquals(hand.GetCard(0), deck.PeekCard());
        
        // Returning card
        hand.ReturnCard(deck);
        assertEquals(firstCard, deck.PeekCard());
        assertEquals(hand.GetCardCount(), 0);
        
        // Draw some more cards
        for(int i=0; i<4; i++)
        {
            Card nextCard = deck.PeekCard();
            hand.DrawCard(deck);
            assertEquals(hand.GetCard(i), nextCard);
        }
        
        // Return all cards
        hand.ReturnAllCards(deck);
        assertEquals(deck.GetCardCount(), Deck.CardsPerSet);
        assertEquals(hand.GetCardCount(), 0);
    }
    
    /**
     * Testing values of all cards held in hand.
     */
    @Test
    public void TestCardValue()
    {
        Deck deck = new Deck(1);
        deck.Shuffle();
        Card firstCard = deck.PeekCard();
        PlayerHand hand = new PlayerHand(new GamePlayer(new UserEntity()));
        
        int sum = 0;
        for(int i=0; i<3; i++)
        {
            Card nextCard = deck.PeekCard();
            sum += nextCard.GetNumber();
            hand.DrawCard(deck);
            assertEquals(nextCard, hand.GetCard(i));
        }
        
        assertEquals(hand.GetTotalCardValue(), sum);
        assertEquals(hand.GetTotalVisibleValue(), sum - firstCard.GetNumber());
    }
}
