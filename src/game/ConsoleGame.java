/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.debug.Debug;
import game.debug.ILogger;
import game.ui.cli.CliEngine;
import game.ui.cli.CliOverlayController;
import game.ui.cli.CliRoot;
import game.ui.cli.CliScreenController;
import game.ui.cli.ICliEngine;
import game.ui.cli.overlays.CliFrameOverlay;
import game.ui.cli.overlays.CliTestCommandOverlay;
import game.ui.cli.screens.CliHomeScreen;

/**
 * Game implementation for console application build.
 * @author jerrykim
 */
public class ConsoleGame extends BaseGame {
    
    private final boolean EnableTestCommands = true;
    
    protected CliRoot rootDisplayer;
    
    protected CliEngine cliEngine;
    
    protected CliScreenController screenController;
    protected CliOverlayController overlayController;
    
    
    public ConsoleGame(GameArguments args)
    {
        super(args);
    }
    
    protected @Override void Initialize()
    {
        super.Initialize();
        
        // Instantiate the root displayer object.
        rootDisplayer = new CliRoot();
        
        // Create the CLI engine.
        dependencies.CacheAs(ICliEngine.class, cliEngine = new CliEngine(rootDisplayer, dependencies));
        
        // Cache view controllers.
        dependencies.Cache(screenController = new CliScreenController(rootDisplayer, dependencies));
        dependencies.Cache(overlayController = new CliOverlayController(rootDisplayer, dependencies));
    }
    
    protected @Override void PostInitialize()
    {
        // Initialize root displayer by injecting dependencies.
        dependencies.Inject(rootDisplayer);
    }
    
    protected @Override void OnStart()
    {
        // Start from home screen.
        screenController.ShowView(CliHomeScreen.class);
        // Show frame overlay.
        overlayController.ShowView(CliFrameOverlay.class);
        
        // Test commands.
        if(EnableTestCommands)
            overlayController.ShowView(CliTestCommandOverlay.class);
        
        // Start the engine.
        cliEngine.StartUpdate();
    }

    public @Override String GetVersion() { return ""; }

    public @Override void ForceQuit() {}

    public @Override void Quit() {}
}
