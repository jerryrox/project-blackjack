/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class EventsTest {
    
    /**
     * General test case.
     */
    @Test
    public void Test()
    {
        Events<Integer> event = new Events<>();
        DummyObject dummy = new DummyObject();
        
        ActionT<Integer> callback = (i) -> {
            dummy.I = i;
            dummy.Callbacked = true;
        };
        
        // Invoke without listening to events.
        event.Invoke(5);
        assertEquals(dummy.I, 0);
        assertFalse(dummy.Callbacked);
        
        // Listen to invocations
        event.Add(callback);
        event.Invoke(3);
        assertEquals(dummy.I, 3);
        assertTrue(dummy.Callbacked);
        
        // Reset
        dummy.I = 0;
        dummy.Callbacked = false;
        
        // Remove callback
        event.Remove(callback);
        event.Invoke(8);
        assertEquals(dummy.I, 0);
        assertFalse(dummy.Callbacked);
        
        // Add callback and clear events
        event.Add(callback);
        event.Clear();
        event.Invoke(10);
        assertEquals(dummy.I, 0);
        assertFalse(dummy.Callbacked);
    }
    
    private class DummyObject {
        public int I = 0;
        public boolean Callbacked = false;
    }
}
