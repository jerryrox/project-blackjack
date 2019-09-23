/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.allocation.DependencyContainer;
import game.allocation.IDependencyContainer;
import game.database.DatabaseConnection;
import game.debug.ConsoleLogger;
import game.debug.Debug;
import game.debug.ILogger;
import game.io.storage.ItemDatabaseStorage;
import game.io.storage.ItemFileStorage;
import game.io.storage.UserDatabaseStorage;
import game.io.storage.UserFileStorage;
import game.io.store.ItemStore;
import game.io.store.UserStore;
import game.rulesets.items.ItemDefinitions;

/**
 * Manages the shared modules that can be used between any implementation of game,
 * such as console or GUI, etc.
 * @author jerrykim
 */
public abstract class BaseGame implements IGame {
    
    protected ILogger logger;
    
    protected IDependencyContainer dependencies;
    
    protected ItemDefinitions itemDefinitions;
    
    protected DatabaseConnection dbConnection;
    protected UserStore userStore;
    protected ItemStore itemStore;
    
    protected GameArguments gameArgs;
    
    
    protected BaseGame(GameArguments args)
    {
        gameArgs = args;
        logger = args.Logger == null ? new ConsoleLogger() : args.Logger;
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
        // Setup a global debugger instance.
        Debug.Initialize(logger);
        
        // Core
        dependencies.CacheAs(IGame.class, this);
        dependencies.CacheAs(ILogger.class, logger);
        dependencies.CacheAs(IDependencyContainer.class, dependencies);
        
        // Items
        dependencies.Cache(itemDefinitions = new ItemDefinitions());
        
        // Stores
        boolean useDatabase = gameArgs.UseDatabaseStorage;
        // Init database connection instance if using database.
        if(useDatabase)
            dependencies.Cache(dbConnection = new DatabaseConnection("ProjectBlackjackDB", logger));
        // Init stores.
        dependencies.Cache(userStore = new UserStore(useDatabase ? new UserDatabaseStorage(dbConnection) : new UserFileStorage()));
        dependencies.Cache(userStore.Load());
        dependencies.Cache(itemStore = new ItemStore(itemDefinitions, useDatabase ? new ItemDatabaseStorage(dbConnection, itemDefinitions) : new ItemFileStorage(itemDefinitions)));
    }
    
    /**
     * Performs any processes after initialization process.
     */
    protected void PostInitialize() {}
    
    /**
     * Performs any initial process to start the actual game.
     */
    protected abstract void OnStart();
}
