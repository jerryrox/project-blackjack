/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.allocation.DependencyContainer;
import game.allocation.IDependencyContainer;
import game.debug.Debug;
import game.debug.ILogger;
import game.io.storage.UserFileStorage;
import game.io.store.UserStore;
import game.rulesets.ItemDefinitions;

/**
 * Manages the shared modules that can be used between any implementation of game,
 * such as console or GUI, etc.
 * @author jerrykim
 */
public class BaseGame {
    
    protected ILogger logger;
    
    protected IDependencyContainer dependencies;
    
    protected UserStore userStore;
    
    protected ItemDefinitions itemDefinitions;
    
    
    protected BaseGame(ILogger logger)
    {
        this.logger = logger;
        dependencies = new DependencyContainer(logger);
    }
    
    /**
     * Starts the game logic.
     */
    public void Start()
    {
        Initialize();
        PostInitialize();
        OnStart();
    }
    
    /**
     * Performs initialization on any modules to be used, caching them to dependency container as necessary.
     */
    protected void Initialize()
    {
        Debug.Initialize(logger);
        
        // Core
        dependencies.CacheAs(ILogger.class, logger);
        dependencies.CacheAs(IDependencyContainer.class, dependencies);
        
        // Cache user store and user instance
        dependencies.Cache(userStore = new UserStore(new UserFileStorage()));
        dependencies.Cache(userStore.Load());
        
        dependencies.Cache(itemDefinitions = new ItemDefinitions());
    }
    
    /**
     * Performs any processes after initialization process.
     */
    protected void PostInitialize()
    {
    }
    
    /**
     * Performs any initial process to start the actual game.
     */
    protected void OnStart()
    {
    }
}
