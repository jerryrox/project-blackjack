/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.debug.ConsoleLogger;
import game.debug.ILogger;
import java.util.Scanner;

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
        gameArgs.UseDatabaseStorage = true;
        gameArgs.RuntimeArguments = args;
        
        // Ask for runtime mode first.
        Scanner scanner = new Scanner(System.in);
        BaseGame game = null;
        boolean selectedRuntime = false;
        while(!selectedRuntime)
        {
            System.out.println("Select runtime mode. (gui / cli / quit)");
            // Receive mode.
            if(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if(line == null)
                    continue;
                
                switch(line.trim().toLowerCase())
                {
                case "quit":
                    selectedRuntime = true;
                    break;
                case "cli":
                    selectedRuntime = true;
                    game = new ConsoleGame(gameArgs);
                    break;
                case "gui":
                    selectedRuntime = true;
                    game = new GuiGame(gameArgs);
                    break;
                default:
                    logger.LogWarning("Invalid command '"+line+"'!");
                    break;
                }
            }
        }
        
        // Start game.
        if(game != null)
            game.Start();
        
        scanner.close();
    }
}
