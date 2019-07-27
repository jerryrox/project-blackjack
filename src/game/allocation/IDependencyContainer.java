/*
 * Jerry Kim (18015036), 2019
 */
package game.allocation;

/**
 * Provides the general signatures for any implementation of dependency container.
 * @author jerrykim
 */
public interface IDependencyContainer {
    
    /**
     * Caches specified object with its own type.
     * @param obj 
     */
    void Cache(Object obj);
    
    /**
     * Caches specified object as a specific type.
     * @param obj 
     */
    void CacheAs(Class type, Object obj);
    
    /**
     * Returns a dependency instance of specified type T.
     * @param <T>
     */
    <T> T Get(Class<T> type);
    
    /**
     * Injects dependencies into specified object.
     * @param <T>
     * @param obj 
     */
    <T> void Inject(T obj);
}
