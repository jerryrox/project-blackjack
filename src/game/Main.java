/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.debug.ConsoleLogger;
import game.debug.ILogger;
import game.tests.TestConsoleGame;

/**
 * The entry point of the application.
 * @author jerrykim
 */
public class Main {
    
    public static void main(String[] args)
    {
        ILogger logger = new ConsoleLogger();
        
        BaseGame game = null;
        switch(Application.Runtime)
        {
        case Console: game = new ConsoleGame(logger); break;
        case Gui: break;
        default:
            logger.LogWarning("Unknown application runtime! Check Application class to see if it's setup correctly!");
            return;
        }
        
        // Start game.
        game.Start();
    }
}
