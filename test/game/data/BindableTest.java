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
public class BindableTest {
    
    /**
     * Testing initialization.
     */
    @Test
    public void TestInit()
    {
        Bindable<String> bindable = new Bindable<>(null);
        assertNull(bindable.GetValue());
        
        bindable = new Bindable<>("HEYEYEYEYEY");
        assertEquals(bindable.GetValue(), "HEYEYEYEYEY");
    }
    
    /**
     * Testing value setting.
     */
    @Test
    public void TestSetValue()
    {
        Bindable<String> bindable = new Bindable<>(null);
        assertNull(bindable.GetValue());
        
        bindable.SetValue("My new string");
        assertEquals(bindable.GetValue(), "My new string");
    }
    
    /**
     * Testing callback on value assign.
     * This method is an integration test with Events class.
     */
    @Test
    public void TestCallback()
    {
        Bindable<String> bindable = new Bindable<>("");
        DummyObject dummy = new DummyObject();
        ActionT<String> callback = (newStr) -> {
            dummy.NewStr = newStr;
            dummy.Callbacked = true;
        };
        
        // Test with callback
        bindable.Changed.Add(callback);
        
        bindable.SetValue("Lol");
        assertEquals(bindable.GetValue(), "Lol");
        assertEquals(dummy.NewStr, bindable.GetValue());
        assertTrue(dummy.Callbacked);
        
        // Test without callback.
        bindable.Changed.Remove(callback);
        dummy.Callbacked = false;
        
        bindable.SetValue("lolz");
        assertEquals(bindable.GetValue(), "lolz");
        assertNotEquals(dummy.NewStr, bindable.GetValue());
        assertFalse(dummy.Callbacked);
    }
    
    private class DummyObject {
        public boolean Callbacked;
        public String NewStr;
        
    }
}
