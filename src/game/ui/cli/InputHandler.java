/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.data.Yieldable;
import game.debug.Debug;
import game.io.serializers.KeyValueSerializer;
import game.ui.cli.commands.ArgumentInfo;
import game.ui.cli.commands.CommandContext;
import game.ui.cli.commands.CommandInfo;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Input manager system for CLI engine.
 * @author jerrykim
 */
public class InputHandler {
    
    /**
     * The root displayer from which the entered command should propagate.
     */
    private CliDisplayer inputRoot;
    
    /**
     * String builder used for displaying commands list.
     */
    private StringBuilder sb = new StringBuilder();
    
    /**
     * Scanner instance to receive inputs from.
     */
    private Scanner scanner;
    
    /**
     * The last inputted value retrieved from scanner.
     */
    private String lastInput;
    
    /**
     * The current command information being used.
     */
    private CommandInfo curCommand;
    
    
    public InputHandler()
    {
        scanner = new Scanner(System.in);
    }
    
    /**
     * Sets the command propagation root.
     * @param root 
     */
    public void SetRoot(CliRoot root) { this.inputRoot = root; }
    
    /**
     * Outputs a list of commands that the user can type.
     */
    public void PromptCommands()
    {
        // Reset to index 0
        sb.setLength(0);
        
        sb.append("===== Commands =====\n");
        
        // Start iterating through non-duplicate commands from the highest depth.
        HashSet<String> commands = new HashSet<String>();
        IterateCommands(inputRoot, command -> {
            String name = command.GetName();
            String description = command.GetDescription();
            // If the command name has already been used by the higher-depth context, skip it.
            if(commands.contains(name))
                return true;

            // We should print this command
            sb.append('"').append(name).append('"');
            if(description != null)
                sb.append(" - ").append(description);
            sb.append('\n');

            // Mark this command name as 'displayed' so it won't show again on lower-depth contexts.
            commands.add(command.GetName());
            return true;
        });
        
        // Output commands
        System.out.print(sb.toString());
    }
    
    /**
     * Listens to commands.
     */
    public void ListenToCommand()
    {
        while(true)
        {
            // Ask the user to enter a command.
            System.out.println("Enter command.");
            // Receive command and check if the definition exists.
            lastInput = GetInputString();
            if(lastInput == null || lastInput.length() == 0)
                continue;
            
            // Find command info with given input.
            IterateCommands(inputRoot, command -> {
                if(command.GetName().equalsIgnoreCase(lastInput))
                {
                    curCommand = command;
                    return false;
                }
                return true;
            });
            
            // If command not found, just continue.
            if(curCommand == null)
            {
                System.out.println("Unknown command: " + lastInput);
                continue;
            }
            break;
        }
        
        // Ensure the command exists
        if(curCommand != null)
        {
            KeyValueSerializer arguments = new KeyValueSerializer();
            for(ArgumentInfo arg : curCommand.GetArgumentInfos())
            {
                while(true)
                {
                    System.out.println(String.format(
                        "Enter value for argument (%s) of type (%s)",
                        arg.GetName(),
                        arg.GetType().toString()
                    ));
                    
                    // Receive argument string
                    String input = GetInputString();
                    if(input == null || input.length() == 0)
                        continue;
                    
                    // Try parsing the input as argument value.
                    String error = arg.TryParse(input);
                    // Show error if exists.
                    if(error != null)
                    {
                        System.out.println(error);
                        continue;
                    }
                    
                    arguments.Set(arg.GetName(), arg.GetParsed());
                    break;
                }
            }
            // Execute the command.
            curCommand.Evaluate(arguments);
        }
        
        lastInput = null;
        curCommand = null;
    }
    
    /**
     * Iterates through all available commands that can be executed, performing custom processes.
     */
    private boolean IterateCommands(CliDisplayer displayer, CommandHandler handler)
    {
        // If displayer is somehow null or is inactive, commands shouldn't be processed.
        if(displayer == null || !displayer.IsActive())
            return true;
        
        // Propagate through children first.
        for(Object child : displayer.GetChildren(true))
        {
            // If stop propagation, just return.
            if(!IterateCommands((CliDisplayer)child, handler))
            {
                return false;
            }
        }

        CommandContext commands = displayer.GetCommands();
        // Context must be enabled to receive commands or to block propagation.
        if(!commands.IsEnabled())
        {
            return true;
        }
        
        // Invoke handler.
        for(CommandInfo command : commands.GetCommands())
        {
            if(!command.IsAvailable())
                continue;
            if(!handler.Invoke(command))
                return false;
        }
        // Return whether propagation should continue
        return commands.IsPropagate();
    }
    
    /**
     * Returns the inputted string value on the scanner.
     */
    private String GetInputString()
    {
        String val = scanner.nextLine();
        if(val != null)
            val = val.trim();
        return val;
    }
    
    /**
     * Delegate for handling custom actions with the given command info.
     * Returns whether iteration should continue.
     */
    private interface CommandHandler {
        
        boolean Invoke(CommandInfo command);
    }
}
