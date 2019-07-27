/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.allocation.IDependencyContainer;
import game.debug.ILogger;
import java.util.Scanner;

/**
 * Manages the shared modules that can be used between any implementation of game,
 * such as console or GUI, etc.
 * @author jerrykim
 */
public class BaseGame {
    
    protected ILogger logger;
    
    protected IDependencyContainer dependencies;
    
    
    protected BaseGame(ILogger logger)
    {
        this.logger = logger;
    }
    
    /**
     * Starts the game logic.
     */
    public void Start()
    {
        Initialize();
        PostInitialize();
    }
    
    /**
     * Performs initialization on any modules to be used, caching them to dependency container as necessary.
     */
    protected void Initialize()
    {
        dependencies.CacheAs(ILogger.class, logger);
        dependencies.CacheAs(IDependencyContainer.class, dependencies);
    }
    
    /**
     * Performs any processes after initialization process.
     */
    protected void PostInitialize()
    {
    }
}
