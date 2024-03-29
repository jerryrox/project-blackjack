/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.debug.Debug;
import game.entities.IEntity;
import game.io.IKeyValueSerializable;
import game.utils.PathUtils;
import java.io.File;
import java.util.HashMap;
import game.io.serializers.KeyValueSerializer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import game.io.IStorage;
import java.util.UUID;

/**
 * Data provider using the file system.
 * @author jerrykim
 */
public class FileSystemStorage<T extends IEntity & IKeyValueSerializable> implements IStorage<T> {
    
    /**
     * A reserved key to be mapped to KeyValueSerializer upon setting T value to this store.
     * This will be used during initialization phase, when the deserialized objects need to be remapped to the HashMap.
     */
    private static final String NameKey = "__key__";
    
    /**
     * Table of data loaded and cached from file.
     */
    private HashMap<String, T> cached = new HashMap<String, T>();
    
    /**
     * Information of the target file which the data will be stored to.
     */
    private File file;
    
    /**
     * Creates a new instance of object T.
     */
    private ICreateHandler<T> createHandler;
    
    
    public FileSystemStorage(String fileName, ICreateHandler<T> createHandler)
    {
        this.createHandler = createHandler;
        file = new File(PathUtils.GetDataPath() + fileName);
        Debug.Log("FileSystemStorage - Initializing storage at: " + file.getAbsolutePath());
        
        // Make sure the file exists.
        if(!file.exists())
        {
            try
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            catch(Exception e)
            {
                // TODO: Handle exception
            }
        }
    }
    
    public @Override void Initialize()
    {
        // Start fresh
        cached.clear();
        try
        {
            // Read data from destination file.
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null)
            {
                // Load raw string data into serializer.
                KeyValueSerializer serializer = new KeyValueSerializer();
                serializer.FromString(line);
                
                // Make sure the key exists.
                String id = serializer.Get(NameKey);
                if(id != null && id.length() > 0)
                {
                    // Create the container object.
                    T obj = createHandler.Invoke();
                    if(obj != null)
                    {
                        // Parse data from serializer.
                        obj.Deserialize(serializer);
                        obj.SetId(id);
                        // Store it in the cache.
                        Set(id, obj);
                    }
                }
            }
            reader.close();
        }
        catch(Exception e)
        {
            // TODO: Handle exception
        }
    }
    
    public @Override void Dispose() {}

    public @Override void Save()
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            KeyValueSerializer serializer = new KeyValueSerializer();
            // Serialize all data
            cached.entrySet().forEach(pair -> {
                // Serialize the value first
                pair.getValue().Serialize(serializer);
                // Append name for identification.
                serializer.Set(NameKey, pair.getKey());
                
                if(sb.length() > 0)
                    sb.append("\r\n");
                sb.append(serializer.toString());
                serializer.Clear();
            });
            
            // Write to file
            FileWriter writer = new FileWriter(file);
            writer.write(sb.toString());
            writer.close();
        }
        catch(Exception e)
        {
            // TODO: Handle exception
        }
    }

    public @Override void Add(T value)
    {
        if(value == null)
            return;
        String existingId = value.GetId();
        if(existingId == null || existingId.length() == 0)
            value.SetId(existingId = UUID.randomUUID().toString());
        
        Set(existingId, value);
    }

    public @Override void Set(String id, T value)
    {
        if(id == null || id.length() == 0)
            return;
        if(value == null)
            Remove(id);
        else
            cached.put(id, value);
    }
    
    public @Override void Remove(String id) { cached.remove(id); }

    public @Override void Clear() { cached.clear(); }
    
    public @Override T Get(String id) { return cached.get(id); }

    public @Override Iterable<String> GetAllId() { return cached.keySet(); }
    
    public @Override Iterable<T> GetAll() { return cached.values(); }
    
    public @Override boolean ContainsId(String id) { return cached.containsKey(id); }
    
    
    /**
     * Delegate which handles creation of T object.
     */
    public interface ICreateHandler<T> {
        
        T Invoke();
    }
}
