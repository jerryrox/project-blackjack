/*
 * Jerry Kim (18015036), 2019
 */
package game.allocation;

import game.debug.ILogger;
import java.util.HashMap;

/**
 * Holds cached dependencies in order to be used for dependency injection.
 * @author jerrykim
 */
public class DependencyContainer implements IDependencyContainer {
    
    /**
     * Table of cached dependency instances mapped to specific types.
     */
    private HashMap<Class, Object> dependencies = new HashMap<Class, Object>();
    
    private ILogger logger;
    
    
    
    public DependencyContainer(ILogger logger)
    {
        this.logger = logger;
        DependencyActivator.SetLogger(logger);
    }

    public @Override void Cache(Object obj) { CacheAs(obj.getClass(), obj); }

    public @Override void CacheAs(Class type, Object obj)
    {
        if(dependencies.containsKey(type))
        {
            logger.LogErrorFormat("A duplicate dependency type (%s) already exists!", type.getName());
            return;
        }
        dependencies.put(type, obj);
    }
    
    public @Override void Remove(Class type)
    {
        dependencies.remove(type);
    }

    public @Override <T> T Get(Class<T> type)
    {
        Object obj = dependencies.get(type);
        if(obj == null)
        {
            logger.LogErrorFormat("The dependency instance of type (%s) retrieved from container is null!", type.getName());
            return null;
        }
        T objT = (T)obj;
        if(objT == null)
        {
            logger.LogErrorFormat("The dependency instance could not be casted into type (%s)!", type.getName());
            return null;
        }
        return objT;
    }

    public @Override <T> void Inject(T obj)
    {
        DependencyActivator.Activate(obj, this);
    }
}
