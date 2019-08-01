/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.debug.Debug;
import game.utils.Random;
import java.util.ArrayList;

/**
 * General behavior of an AI player.
 * @author jerrykim
 */
public class AiBehavior {
    
    private GameAIPlayer ai;
    private GamePlayer player;
    private Deck deck;
    
    private ArrayList<Card> seenCards = new ArrayList<Card>();
    private ArrayList<Card> availableCards = new ArrayList<Card>();
    
    
    public AiBehavior(GameAIPlayer ai, GamePlayer player, Deck deck)
    {
        this.ai = ai;
        this.player = player;
        this.deck = deck;
    }
    
    /**
     * Returns whether the AI will draw card.
     */
    public boolean WillDraw()
    {
        PlayerHand hand = ai.GetHand();
        PlayerHand humanHand = player.GetHand();
        
        // If player's visible hands have already exceeded 21, just skip.
        if(humanHand.GetTotalVisibleValue() > 21)
            return false;
        // Get my total
        int total = hand.GetTotalCardValue();
        if(total >= 21)
            return false;
        
        // Get all cards that have been seen by AI.
        seenCards.clear();
        for(Card card : hand.GetAllCards())
            seenCards.add(card);
        for(Card card : humanHand.GetVisibleCards())
            seenCards.add(card);
        
        // Determine all potentially available cards.
        availableCards.clear();
        for(int d=0; d<deck.GetSetCount(); d++)
        {
            for(int i=1; i<12; i++)
                availableCards.add(new Card(i));
        }
        // Caculate probability of drawing a right card.
        int cardsWithinRange = 0;
        for(int i=availableCards.size()-1; i>=0; i--)
        {
            Card card = availableCards.get(i);
            for(int c=seenCards.size()-1; c>=0; c--)
            {
                if(seenCards.get(c).GetNumber() == card.GetNumber())
                {
                    card = null;
                    availableCards.remove(i);
                    seenCards.remove(c);
                    break;
                }
            }
            
            if(card != null && total + card.GetNumber() <= 21)
                cardsWithinRange ++;
        }
        
        // Get probability.
        float rangeProbability = (float)cardsWithinRange / (float)availableCards.size();
        // Apply randomness.
        // TODO: Improve
        rangeProbability *= Random.Range(1f, 1.15f);
        
        // Return whether ai should draw.
        return Random.Range(0f, 1f) < rangeProbability;
    }
}
