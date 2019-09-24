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
public class CardTest {
   
    /**
     * Testing general processes
     */
    @Test
    public void Test()
    {
        Card card = new Card();
        assertTrue(card.GetNumber() >= Card.MinValue && card.GetNumber() <= Card.MaxValue);
        assertFalse(card.IsCloned());
        
        Card cloned = new Card(card);
        assertTrue(cloned.IsCloned());
        assertEquals(cloned.GetNumber(), card.GetNumber());
        
        Card outOfBoundCard = new Card(-100);
        assertEquals(outOfBoundCard.GetNumber(), Card.MinValue);
    }
}
