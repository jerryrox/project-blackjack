/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.commands;

import game.io.serializers.KeyValueSerializer;
import java.util.HashMap;

/**
 * Information of a single command supported by a displayer.
 * @author jerrykim
 */
public class CommandInfo {
    
    /**
     * The command name which the user must type to initiate callbacks.
     */
    private String name;
    
    /**
     * Description of the command to be displayed to the user.
     */
    private String description;
    
    /**
     * The callback method to be invoked on initiating this command.
     */
    private CallbackHandler callback;
    
    /**
     * The condition which determines the command's availability.
     */
    private ConditionHandler condition;
    
    /**
     * List of argument informations which this command suports.
     */
    private HashMap<String, ArgumentInfo> arguments = new HashMap<String, ArgumentInfo>();
    
    
    public CommandInfo(String name, CallbackHandler callback)
    {
        this(name, callback, () -> true);
    }
    
    public CommandInfo(String name, CallbackHandler callback, ConditionHandler condition)
    {
        this.name = name;
        this.callback = callback;
        this.condition = condition;
    }
    
    /**
     * Returns all argument informations set on this command.
     */
    public Iterable<ArgumentInfo> GetArgumentInfos() { return arguments.values(); }
    
    /**
     * Sets the description of the command.
     */
    public CommandInfo SetDescription(String description)
    {
        this.description = description;
        return this;
    }
    
    /**
     * Returns the description of this command.
     */
    public String GetDescription() { return description; }
    
    /**
     * Sets an argument to indicate that this command takes certain arguments.
     */
    public CommandInfo SetArgument(String name, ArgumentTypes type)
    {
        arguments.put(name, new ArgumentInfo(name, type));
        return this;
    }
    
    /**
     * Returns the name of the command.
     */
    public String GetName() { return name; }
    
    /**
     * Returns whether this command is currently available for use.
     */
    public boolean IsAvailable() { return condition.Invoke(); }
    
    /**
     * Evaluates the command using specified arguments.
     */
    public void Evaluate(KeyValueSerializer arguments)
    {
        callback.Invoke(arguments);
    }
    
    /**
     * Delegate for checking whether this command should be available on a certain game state.
     */
    public interface ConditionHandler {
        
        boolean Invoke();
    }
    
    /**
     * Delegate for handling callbacks when this command has been initiated by user.
     */
    public interface CallbackHandler {
        
        void Invoke(KeyValueSerializer arguments);
    }
}
