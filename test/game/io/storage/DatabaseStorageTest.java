/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.database.DatabaseConnection;
import game.debug.ConsoleLogger;
import game.entities.IEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Performs integration test on DatabaseStorage implementation using a dummy storage and entity.
 * @author jerrykim
 */
public class DatabaseStorageTest {
    
    /**
     * Initialization test
     */
    @Test
    public void InitTest()
    {
        DummyStorage storage = new DummyStorage();
        storage.Initialize();
        storage.Dispose();
        assertNull(storage.GetAll());
    }
    
    @Test
    public void AddTest() throws Exception
    {
        DummyStorage storage = new DummyStorage();
        storage.Initialize();
        
        DummyEntity entity = AddNewEntity(storage);
        
        DummyEntity sameEntity = storage.Get(entity.Id);
        assertNotNull(sameEntity);
        assertEquals(sameEntity.Id, entity.Id);
        assertEquals(sameEntity.MyInt, entity.MyInt);
        assertEquals(sameEntity.MyFloat, entity.MyFloat, 0.000000001);
        
        ClearStorage(storage);
        storage.Dispose();
    }
    
    @Test
    public void ReplaceTest() throws Exception
    {
        DummyStorage storage = new DummyStorage();
        storage.Initialize();
        
        DummyEntity entity = AddNewEntity(storage);
        
        DummyEntity replacingEntity = new DummyEntity(entity.Id, 1000, 200);
        assertEquals(entity.Id, replacingEntity.Id);
        assertNotEquals(entity.MyInt, replacingEntity.MyInt);
        assertNotEquals(entity.MyFloat, replacingEntity.MyFloat);
        storage.Set(replacingEntity.Id, replacingEntity);
        
        DummyEntity loadedEntity = storage.Get(entity.Id);
        assertEquals(loadedEntity.Id, replacingEntity.Id);
        assertEquals(loadedEntity.MyInt, replacingEntity.MyInt);
        assertEquals(loadedEntity.MyFloat, replacingEntity.MyFloat, 0.00000001);
        
        ClearStorage(storage);
        storage.Dispose();
    }
    
    @Test
    public void RemoveTest() throws Exception
    {
        DummyStorage storage = new DummyStorage();
        storage.Initialize();
        
        DummyEntity entity = AddNewEntity(storage);
        
        storage.Remove(entity.Id);
        assertNull(storage.Get(entity.Id));
        
        ClearStorage(storage);
        storage.Dispose();
    }
    
    @Test
    public void GetAllIdTest() throws Exception
    {
        DummyStorage storage = new DummyStorage();
        storage.Initialize();
        
        AddNewEntity(storage);
        AddNewEntity(storage);
        AddNewEntity(storage);
        
        Iterable<String> ids = storage.GetAllId();
        for(String id : ids)
        {
            System.out.println("Retrieved id: " + id);
            assertNotNull(id);
        }
        
        ClearStorage(storage);
        storage.Dispose();
    }
    
    @Test
    public void GetAllTest() throws Exception
    {
        DummyStorage storage = new DummyStorage();
        storage.Initialize();
        
        AddNewEntity(storage);
        AddNewEntity(storage);
        AddNewEntity(storage);
        
        Iterable<DummyEntity> values = storage.GetAll();
        for(DummyEntity value : values)
        {
            System.out.println("Retrieved entity: " + value.Id);
            assertNotNull(value);
        }
        
        ClearStorage(storage);
        storage.Dispose();
    }
    
    @Test
    public void ContainsTest() throws Exception
    {
        DummyStorage storage = new DummyStorage();
        storage.Initialize();
        
        DummyEntity entity = AddNewEntity(storage);
        assertTrue(storage.ContainsId(entity.Id));
        
        ClearStorage(storage);
        storage.Dispose();
    }
    
    private DummyEntity AddNewEntity(DummyStorage storage)
    {
        DummyEntity entity = new DummyEntity(null, 100, 1);
        storage.Add(entity);
        assertNotNull(entity.Id);
        System.out.println("Entity id: " + entity.Id);
        return entity;
    }
    
    private void ClearStorage(DummyStorage storage) throws Exception
    {
        storage.Clear();
        Iterable<DummyEntity> all = storage.GetAll();
        assertNotNull(all);
        for(DummyEntity e : all)
            throw new Exception("There mustn't be any entity!");
    }
    
    
    
    private class DummyEntity implements IEntity {
        
        public String Id;
        public int MyInt;
        public float MyFloat;

        
        public DummyEntity()
        {
        }
        
        public DummyEntity(String id, int i, float f)
        {
            Id = id;
            MyInt = i;
            MyFloat = f;
        }
        
        public @Override void SetId(String id) { Id = id; }

        public @Override String GetId() { return Id; }
    }
    
    private class DummyStorage extends DatabaseStorage<DummyEntity> {
        
        public DummyStorage()
        {
            super(new DatabaseConnection("DummyStorageDB", new ConsoleLogger()), "dummytable");
        }

        @Override
        protected String GetTableStructureString()
        {
            return "id varchar(50) not null unique, myint int, myfloat float";
        }

        @Override
        protected String GetInsertValueString(DummyEntity value)
        {
            return String.format("'%s', %d, %f", value.Id, value.MyInt, value.MyFloat);
        }

        @Override
        protected String GetUpdateValueString(DummyEntity value)
        {
            return String.format("myint=%d, myfloat=%f", value.MyInt, value.MyFloat);
        }

        @Override
        protected DummyEntity ParseEntity(ResultSet result) throws SQLException
        {
            return new DummyEntity(result.getString("id"), result.getInt("myint"), result.getFloat("myfloat"));
        }
        
    }
}
