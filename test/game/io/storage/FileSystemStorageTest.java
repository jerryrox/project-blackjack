/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.entities.IEntity;
import game.io.storage.FileSystemStorage;
import game.io.IKeyValueSerializable;
import game.io.serializers.KeyValueSerializer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class FileSystemStorageTest {
    
    private final String TestFileName = "test.data";
    
    
    public FileSystemStorageTest() {
    }

    /**
     * Test of Initialize method, of class FileSystemStore.
     */
    @Test
    public void testInitialize() {
        FileSystemStorage instance = new FileSystemStorage(TestFileName, () -> new TestData());
        instance.Initialize();
    }

    /**
     * Test of Save & Load methods, of class FileSystemStore.
     */
    @Test
    public void testSave_Load() {
        FileSystemStorage<TestData> instance = new FileSystemStorage<TestData>(TestFileName, () -> new TestData());
        instance.Initialize();
        TestData testData = new TestData(1, true, "Test");
        instance.Set("Data0", testData);
        assertEquals(instance.Get("Data0"), testData);
        instance.Save();
        
        // Reinitialize to reload data
        instance.Initialize();
        assertEquals(instance.ContainsId("Data0"), true);
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
        FileSystemStorage<TestData> instance = new FileSystemStorage<TestData>(TestFileName, () -> new TestData());
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
        FileSystemStorage<TestData> instance = new FileSystemStorage<TestData>(TestFileName, () -> new TestData());
        instance.Initialize();
        
        TestData testData = new TestData(1, true, "Test");
        instance.Set("Data0", testData);
        instance.Remove("Data0");
        assertEquals(instance.ContainsId("Data0"), false);
        assertEquals(instance.Get("Data0"), null);
    }

    /**
     * Test of GetAllNames method, of class FileSystemStore.
     */
    @Test
    public void testGetAllNames() {
        FileSystemStorage<TestData> instance = new FileSystemStorage<TestData>(TestFileName, () -> new TestData());
        instance.Initialize();
        instance.Clear();
        instance.Set("Data0", new TestData(10, true, "A"));
        instance.Set("Data1", new TestData(11, true, "B"));
        instance.Set("Data2", new TestData(12, true, "C"));
        instance.Set("Data3", new TestData(13, true, "D"));
        instance.Set("Data4", new TestData(14, true, "E"));
        instance.Set("Data0", null);
        instance.Remove("Data3");
        
        for(String name : instance.GetAllId())
        {
            System.out.println("Name: " + name);
            assertTrue(name.equals("Data1") || name.equals("Data2") || name.equals("Data4"));
        }
    }
    
    @Test
    public void TestAdd()
    {
        FileSystemStorage<TestData> instance = new FileSystemStorage<TestData>(TestFileName, () -> new TestData());
        instance.Initialize();
        
        TestData data = new TestData(20, true, "bbb");
        instance.Add(data);
        
        System.out.println("Added with id: " + data.GetId());
        
        assertTrue(instance.ContainsId(data.GetId()));
    }
    
    
    private class TestData implements IKeyValueSerializable, IEntity {
        
        public String id;
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
        
        public @Override void SetId(String id) { this.id = id; }

        public @Override String GetId() { return id; }

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
