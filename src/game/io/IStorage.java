/*
 * Jerry Kim (18015036), 2019
 */
package game.io;

import game.data.IDisposable;
import game.entities.IEntity;

/**
 * Provides a general signature for accessing data from different storage types.
 * Example: Filesystem, Database, etc.
 * @author jerrykim
 */
public interface IStorage<T extends IEntity> extends IDisposable {
    
    /**
     * Initializes this storage provider instance.
     */
    void Initialize();
    
    /**
     * Disposes any used resources.
     */
    @Override void Dispose();
    
    /**
     * Stores any unsaved data to storage.
     */
    void Save();
    
    /**
     * Adds the specified value as a new entry, using a randomized GUID.
     */
    void Add(T value);
    
    /**
     * Sets the specified value associated with an id to the storage.
     * If presented value is null, the entry will be removed instead.
     * @param id
     * @param value 
     */
    void Set(String id, T value);
    
    /**
     * Removes the entry mapped to specified id, if exists.
     * @param id 
     */
    void Remove(String id);
    
    /**
     * Removes all data in the storage.
     */
    void Clear();
    
    /**
     * Returns the value which corresponds to the specified id.
     * @param id
     * @return 
     */
    T Get(String id);
    
    /**
     * Returns all names which can be used to retrieve data from this storage.
     */
    Iterable<String> GetAllId();
    
    /**
     * Returns all entries in the storage.
     */
    Iterable<T> GetAll();
    
    /**
     * Returns whether an entry with specified id exists.
     * @param id
     */
    boolean ContainsId(String id);
}
