/*
 * Jerry Kim (18015036), 2019
 */
package game.allocation;

import game.debug.ILogger;
import game.debug.LogLevels;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles activation on objects for dependency injection.
 * @author jerrykim
 */
public class DependencyActivator {
    
    /**
     * Table of activators cached for use again.
     */
    private static HashMap<Class, DependencyActivator> CachedActivators = new HashMap<Class, DependencyActivator>();
    
    /**
     * Debug logger.
     */
    private static ILogger Logger;
    
    /**
     * List of handlers that perform the actual injection.
     */
    private ArrayList<IInjectionHandler> injectionHandlers = new ArrayList<IInjectionHandler>();
    
    /**
     * Activator for the parent type, if exists.
     */
    private DependencyActivator parentActivator;
    
    
    private DependencyActivator(Class type)
    {
        injectionHandlers.add(CreateHandlerForMethod(type));
        injectionHandlers.add(CreateHandlerForFields(type));
        
        Class superType = type.getSuperclass();
        if(superType != null && superType != Object.class)
            parentActivator = GetActivator(superType);
    }
    
    /**
     * Performs injection on specified object using given container, while caching the activator if necessary.
     * @param obj
     * @param container 
     */
    public static void Activate(Object obj, IDependencyContainer container)
    {
        GetActivator(obj.getClass()).ActivateInternal(obj, container);
    }
    
    /**
     * Returns the activator instance for specified type.
     * @param type
     */
    public static DependencyActivator GetActivator(Class type)
    {
        DependencyActivator activator = CachedActivators.get(type);
        if(activator == null)
            CachedActivators.put(type, activator = new DependencyActivator(type));
        return activator;
    }
    
    /**
     * Sets the logger instance to use for logging.
     * @param logger 
     */
    public static void SetLogger(ILogger logger)
    {
        Logger = logger; 
    }
    
    /**
     * Performs injection on specified object using container.
     * @param obj
     * @param container 
     */
    private void ActivateInternal(Object obj, IDependencyContainer container)
    {
        // Make sure the base type is injected first.
        if(parentActivator != null)
            parentActivator.ActivateInternal(obj, container);
        
        // Injection.
        for(int i=0; i<injectionHandlers.size(); i++)
            injectionHandlers.get(i).Invoke(obj, container);
    }
    
    /**
     * Creates an injection handler for InitWithDependency attribute.
     * @param type
     */
    private IInjectionHandler CreateHandlerForMethod(Class type)
    {
        Method[] methods = type.getMethods();
        for(int i=0; i<methods.length; i++)
        {
            Method method = methods[i];
            // The method must have the InitWithDependency annotation to be invoked.
            InitWithDependency attribute = method.getAnnotation(InitWithDependency.class);
            if(attribute == null)
                continue;
            
            // Make sure the method is accessible.
            if(!method.isAccessible())
            {
                try { method.setAccessible(true); }
                catch(SecurityException e)
                {
                    Logger.LogErrorFormat("Method (%s) is not accessible!", method.getName());
                    continue;
                }
            }
            
            boolean allowNulls = attribute.AllowNulls();
            List<ParamGetter> paramGetters = Arrays.stream(method.getParameters())
                    .map(p -> GetParamGetter((Class<?>)p.getParameterizedType(), type, allowNulls))
                    .collect(Collectors.toList());
            
            // We should not support injection through multiple methods.
            return (Object obj, IDependencyContainer container) -> {
                try
                {
                    method.invoke(obj, paramGetters.stream().map(d -> d.Invoke(container)).toArray());
                }
                catch(Exception e)
                {
                    Logger.LogErrorFormat("An exception was thrown while invoking the method (%s) of type (%s) with dependencies.", method.getName(), type.getName());
                }
            };
        }
        return (obj, container) -> {};
    }
    
    /**
     * Creates an injection handler for ReceivesDependency attribute.
     * @param type
     */
    private IInjectionHandler CreateHandlerForFields(Class type)
    {
        ArrayList<IInjectionHandler> handlers = new ArrayList<IInjectionHandler>();
        Field[] fields = type.getFields();
        for(int i=0; i<fields.length; i++)
        {
            Field field = fields[i];
            // Field receiving dependency must have the ReceivesDependency annotation.
            ReceivesDependency attribute = field.getAnnotation(ReceivesDependency.class);
            if(attribute == null)
                continue;
            
            // Make sure the field is accessible.
            if(!field.isAccessible())
            {
                try { field.setAccessible(true); }
                catch(SecurityException e)
                {
                    Logger.LogErrorFormat("Method (%s) is not accessible!", field.getName());
                    continue;
                }
            }
            
            boolean allowNulls = attribute.AllowNulls();
            Class fieldType = (Class<?>)field.getGenericType();
            ParamGetter getParam = GetParamGetter(fieldType, type, allowNulls);
            
            // Add the handler to list.
            handlers.add((Object obj, IDependencyContainer container) -> {
                try
                {
                    Object value = getParam.Invoke(container);
                    field.set(obj, value);
                }
                catch(Exception e)
                {
                    Logger.LogErrorFormat("Failed to inject dependency into field (%s) for type (%s)!", field.getName(), type.getName());
                }
            });
        }
        
        // Returns a single injection handler which invokes the list of handlers for individual field.
        return (obj, container) -> {
            for(int i=0; i<handlers.size(); i++)
                handlers.get(i).Invoke(obj, container);
        };
    }
    
    /**
     * Returns a parameter getter delegate which extracts dependency instance from specified container.
     * @param type
     * @param requestingType
     * @param allowNulls
     */
    private ParamGetter GetParamGetter(Class type, Class requestingType, boolean allowNulls)
    {
        return (IDependencyContainer container) -> {
            Object param = container.Get(type);
            if(param == null && !allowNulls)
            {
                Logger.LogErrorFormat("Dependency instance not cached for type (%s). Requester type is (%s)", type.getName(), requestingType.getName());
            }
            return param;
        };
    }
    

    /**
     * Delegate which receives a dependency container and desired type to be extracted from it.
     */
    private interface ParamGetter {
        
        Object Invoke(IDependencyContainer container);
    }
}
