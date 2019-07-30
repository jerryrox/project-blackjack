/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.commands;

import java.util.HashMap;


/**
 * Container for types of commands supported by a particular displayer.
 * @author jerrykim
 */
public class CommandContext {
    
    /**
     * Whether the inputted command should further propagate when none of the registered
     * command has been selected.
     */
    private boolean isPropagate = true;
    
    /**
     * Whether the context is enabled for receiving commands.
     */
    private boolean isEnabled = true;
    
    /**
     * List of command infos this context provides.
     */
    private HashMap<String, CommandInfo> commands = new HashMap<String, CommandInfo>();
    
    
    /**
     * Sets whether command should propagate to other contexts when an inputted command
     * does not exist in this context.
     * @param isPropagate 
     */
    public void SetPropagate(boolean isPropagate) { this.isPropagate = isPropagate; }
    
    /**
     * Whether this context allows command propagation.
     */
    public boolean IsPropagate() { return isPropagate; }
    
    /**
     * Sets the enable state of the context.
     * @param isEnabled 
     */
    public void SetEnable(boolean isEnabled) { this.isEnabled = isEnabled; }
    
    /**
     * Returns whether the context is currently enabled.
     */
    public boolean IsEnabled() { return isEnabled; }
    
    /**
     * Adds specified command to commands list.
     * @param command 
     */
    public void AddCommand(CommandInfo command) { commands.put(command.GetName(), command); }
    
    /**
     * Clears all commands in the table.
     */
    public void ClearCommands() { commands.clear(); }
    
    /**
     * Returns the number of commands in the context.
     */
    public int GetCount() { return commands.size(); }
    
    /**
     * Returns the command information that matches the specified name.
     * @param name
     */
    public CommandInfo GetCommand(String name) { return commands.get(name); }
    
    /**
     * Returns all supported commands in this context.
     */
    public Iterable<CommandInfo> GetCommands() { return commands.values(); }
}
