/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

import game.debug.ConsoleLogger;
import game.debug.Debug;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            System.out.println("Returning 0");
            yield.Return(1);
            System.out.println("Returning 1");
            yield.Return(2);
            System.out.println("Returning 2");
            yield.Return(3);
            System.out.println("Returning 3");
            yield.Return(4);
            System.out.println("Returning 4");
            yield.Return(5);
            System.out.println("Returning 5");
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
    
    @Test
    public void TestGC()
    {
        Debug.Initialize(new ConsoleLogger());
        
        Iterable<Integer> yieldable = new Yieldable<>(yield -> {
            yield.Return(1);
            yield.Return(2);
            yield.Return(3);
        });
        
        // Case 1 - Breaking out of loop during foreach loop on the yieldable.
//        for(Integer i : yieldable)
//        {
//            System.out.println("Breaking");
//            break;
//        }

        // Case 2 - De-referencing iterator after use, but not till the end.
        yieldable.iterator().hasNext();
        
        // Remove yieldable reference to dispose on GC.
        yieldable = null;
        
        int waitCount = 0;
        while(waitCount < 20)
        {
            System.out.println("Waiting: " + waitCount);
            waitCount ++;
            System.gc();
            try 
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException ex) {}
        }
    }
    
    private class TestClass {
        
        public TestClass()
        {
            System.out.println("Created test class");
        }
    }
}
