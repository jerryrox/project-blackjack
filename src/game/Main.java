/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.debug.ConsoleLogger;
import game.debug.ILogger;

/**
 * The entry point of the application.
 * @author jerrykim
 */
public class Main {
    
    public static void main(String[] args)
    {
        ILogger logger = new ConsoleLogger();
        
        // Build arguments to pass to the game.
        GameArguments gameArgs = new GameArguments();
        gameArgs.Logger = logger;
        gameArgs.UseDatabaseStorage = false;
        
        BaseGame game = null;
        switch(Application.Runtime)
        {
        case Console: game = new ConsoleGame(gameArgs); break;
        case Gui: game = new GuiGame(gameArgs); break;
        default:
            logger.LogWarning("Unknown application runtime! Check Application class to see if it's setup correctly!");
            return;
        }
        
        // Start game.
        if(game != null)
            game.Start();
    }
}
