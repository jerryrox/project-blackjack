/*
 * Jerry Kim (18015036), 2019
 */
package game.io.serializers;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class KeyValueSerializerTest {
    
    public KeyValueSerializerTest() {
    }

    /**
     * Test of Clear method, of class KeyValueSerializer.
     */
    @Test
    public void testClear() {
        KeyValueSerializer instance = new KeyValueSerializer();
        assertEquals(instance.GetCount(), 0);
        instance.Set("a", "5");
        assertEquals(instance.GetCount(), 1);
        instance.Clear();
        assertEquals(instance.GetCount(), 0);
    }

    /**
     * Test of Set method, of class KeyValueSerializer.
     */
    @Test
    public void testSet() {
        KeyValueSerializer instance = new KeyValueSerializer();
        assertEquals(instance.GetCount(), 0);
        instance.Set("asdf", "55");
        assertEquals(instance.GetCount(), 1);
        instance.Set("asdf", 56);
        assertEquals(instance.GetCount(), 1);
        assertEquals(instance.Get("asdf"), "56");
        instance.Set(null, "");
        assertEquals(instance.GetCount(), 1);
    }

    /**
     * Test of ContainsKey method, of class KeyValueSerializer.
     */
    @Test
    public void testContainsKey() {
        KeyValueSerializer instance = new KeyValueSerializer();
        assertEquals(instance.ContainsKey("aa"), false);
        instance.Set("aa", "bbb");
        assertEquals(instance.ContainsKey("aa"), true);
    }

    /**
     * Test of Get method, of class KeyValueSerializer.
     */
    @Test
    public void testGet_String() {
        KeyValueSerializer instance = new KeyValueSerializer();
        instance.Set("a", "1");
        assertEquals(instance.Get("a"), "1");
    }

    /**
     * Test of GetInt method, of class KeyValueSerializer.
     */
    @Test
    public void testGetInt_String() {
        KeyValueSerializer instance = new KeyValueSerializer();
        instance.Set("a", "1");
        assertEquals(instance.GetInt("a"), 1);
    }

    /**
     * Test of GetFloat method, of class KeyValueSerializer.
     */
    @Test
    public void testGetFloat_String() {
        KeyValueSerializer instance = new KeyValueSerializer();
        instance.Set("a", "1.5");
        assertEquals(instance.GetFloat("a"), 1.5f, 0.001f);
    }

    /**
     * Test of GetBool method, of class KeyValueSerializer.
     */
    @Test
    public void testGetBool_String() {
        KeyValueSerializer instance = new KeyValueSerializer();
        instance.Set("a", "true");
        assertEquals(instance.GetBool("a"), true);
        instance.Set("ab", "false");
        assertEquals(instance.GetBool("ab"), false);
        assertEquals(instance.GetBool("abc", true), true);
    }

    /**
     * Test of FromString method, of class KeyValueSerializer.
     */
    @Test
    public void testFromString() {
        KeyValueSerializer instance = new KeyValueSerializer();
        instance.FromString("a=1|b=2.5|c=true");
        assertEquals(instance.GetInt("a"), 1);
        assertEquals(instance.GetFloat("b"), 2.5f, 0.001f);
        assertEquals(instance.GetBool("c"), true);
    }

    /**
     * Test of toString method, of class KeyValueSerializer.
     */
    @Test
    public void testToString() {
        KeyValueSerializer instance = new KeyValueSerializer();
        instance.Set("a", "1");
        instance.Set("b", 2);
        instance.Set("c", true);
        assertEquals(instance.toString(), "a=1|b=2|c=true");
    }
    
}
