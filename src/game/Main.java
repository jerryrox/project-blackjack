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
        ConsoleGame game = new ConsoleGame(logger);
        
    }
}
