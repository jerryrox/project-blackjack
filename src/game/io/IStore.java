/*
 * Jerry Kim (18015036), 2019
 */
package game.io;

/**
 * Provides a general signature for accessing data from different storage types.
 * Example: Filesystem, Database, etc.
 * @author jerrykim
 */
public interface IStore<T> {
    
    
    /**
     * Initializes this storage provider instance.
     */
    void Initialize();
    
    /**
     * Stores any unsaved data to storage.
     */
    void Save();
    
    /**
     * Sets the specified value associated with a name to the storage.
     * If presented value is null, the entry will be removed instead.
     * @param name
     * @param value 
     */
    void Set(String name, T value);
    
    /**
     * Removes the entry mapped to specified name, if exists.
     * @param name 
     */
    void Remove(String name);
    
    /**
     * Removes all data in the storage.
     */
    void Clear();
    
    /**
     * Returns the value which corresponds to the specified name.
     * @param name
     * @return 
     */
    T Get(String name);
    
    /**
     * Returns all names which can be used to retrieve data from this storage.
     */
    Iterable<String> GetAllNames();
    
    /**
     * Returns whether an entry with specified name exists.
     * @param name
     */
    boolean ContainsName(String name);
}
