/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.debug.Debug;
import game.debug.ILogger;
import game.ui.cli.CliEngine;
import game.ui.cli.CliRoot;
import game.ui.cli.ICliEngine;

/**
 * Game implementation for console application build.
 * @author jerrykim
 */
public class ConsoleGame extends BaseGame {
    
    protected CliRoot rootDisplayer;
    
    protected CliEngine cliEngine;
    
    
    public ConsoleGame(ILogger logger)
    {
        super(logger);
    }
    
    protected @Override void Initialize()
    {
        super.Initialize();
        
        // Instantiate the root displayer object.
        rootDisplayer = new CliRoot();
        
        // Create the CLI engine.
        dependencies.CacheAs(ICliEngine.class, cliEngine = new CliEngine(rootDisplayer, dependencies));
    }
    
    protected @Override void PostInitialize()
    {
        // Initialize root displayer by injecting dependencies.
        dependencies.Inject(rootDisplayer);
    }
}
