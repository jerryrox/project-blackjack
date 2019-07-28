/*
 * Jerry Kim (18015036), 2019
 */
package game.io.store;

import game.io.IKeyValueSerializable;
import game.io.serializers.KeyValueSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class FileSystemStoreTest {
    
    private final String TestFileName = "test.data";
    
    
    public FileSystemStoreTest() {
    }

    /**
     * Test of Initialize method, of class FileSystemStore.
     */
    @Test
    public void testInitialize() {
        FileSystemStore instance = new FileSystemStore(TestFileName, () -> new TestData());
        instance.Initialize();
    }

    /**
     * Test of Save & Load methods, of class FileSystemStore.
     */
    @Test
    public void testSave_Load() {
        FileSystemStore<TestData> instance = new FileSystemStore<TestData>(TestFileName, () -> new TestData());
        instance.Initialize();
        TestData testData = new TestData(1, true, "Test");
        instance.Set("Data0", testData);
        assertEquals(instance.Get("Data0"), testData);
        instance.Save();
        
        // Reinitialize to reload data
        instance.Initialize();
        assertEquals(instance.ContainsName("Data0"), true);
        TestData loadedData = instance.Get("Data0");
        assertNotNull(loadedData);
        assertEquals(loadedData.A, testData.A);
        assertEquals(loadedData.B, testData.B);
        assertEquals(loadedData.C, testData.C);
    }

    /**
     * Test of Set method, of class FileSystemStore.
     */
    @Test
    public void testSet() {
        FileSystemStore<TestData> instance = new FileSystemStore<TestData>(TestFileName, () -> new TestData());
        instance.Initialize();
        
        TestData testData = new TestData(1, true, "Test");
        instance.Set("Data0", testData);
        assertEquals(instance.Get("Data0"), testData);
    }

    /**
     * Test of Remove method, of class FileSystemStore.
     */
    @Test
    public void testRemove() {
        FileSystemStore<TestData> instance = new FileSystemStore<TestData>(TestFileName, () -> new TestData());
        instance.Initialize();
        
        TestData testData = new TestData(1, true, "Test");
        instance.Set("Data0", testData);
        instance.Remove("Data0");
        assertEquals(instance.ContainsName("Data0"), false);
        assertEquals(instance.Get("Data0"), null);
    }

    /**
     * Test of GetAllNames method, of class FileSystemStore.
     */
    @Test
    public void testGetAllNames() {
        FileSystemStore<TestData> instance = new FileSystemStore<TestData>(TestFileName, () -> new TestData());
        instance.Initialize();
        instance.Clear();
        instance.Set("Data0", new TestData(10, true, "A"));
        instance.Set("Data1", new TestData(11, true, "B"));
        instance.Set("Data2", new TestData(12, true, "C"));
        instance.Set("Data3", new TestData(13, true, "D"));
        instance.Set("Data4", new TestData(14, true, "E"));
        instance.Set("Data0", null);
        instance.Remove("Data3");
        
        for(String name : instance.GetAllNames())
        {
            System.out.println("Name: " + name);
            assertTrue(name.equals("Data1") || name.equals("Data2") || name.equals("Data4"));
        }
    }
    
    
    private class TestData implements IKeyValueSerializable {
        
        public int A;
        public boolean B;
        public String C;
        
        public TestData()
        {
            A = 0;
            B = false;
            C = "";
        }
        
        public TestData(int a, boolean b, String c)
        {
            A = a;
            B = b;
            C = c;
        }

        public @Override void Serialize(KeyValueSerializer serializer)
        {
            serializer.Set("A", A);
            serializer.Set("B", B);
            serializer.Set("C", C);
        }

        public @Override void Deserialize(KeyValueSerializer serializer)
        {
            A = serializer.GetInt("A");
            B = serializer.GetBool("B");
            C = serializer.Get("C");
        }
    }
}
