/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class YieldableTest {
    
    public YieldableTest() {
    }

    /**
     * Test of iterator method, of class Yieldable.
     */
    @Test
    public void testIterator() {
        Iterable<Integer> testIterable = new Yieldable<Integer>(yield -> {
            yield.Return(0);
            yield.Return(1);
            yield.Return(2);
            yield.Return(3);
            yield.Return(4);
            yield.Return(5);
            yield.Break();
            yield.Return(6);
        });
        
        int returned = 0;
        for(Integer i : testIterable)
        {
            System.out.println("Returned value: " + i.toString());
            assertNotEquals(i.intValue(), 6);
            assertEquals(i.intValue(), returned++);
        }
    }
    
}
