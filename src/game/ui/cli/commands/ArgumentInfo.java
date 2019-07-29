/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.commands;

/**
 * Information of an argument which is to be bound to a specific command.
 * @author jerrykim
 */
public class ArgumentInfo {
    
    /**
     * Name of the argument.
     */
    private String name;
    
    /**
     * Type of expected value.
     */
    private ArgumentTypes type;
    
    /**
     * The value which has been validated to desired argument type and stored.
     * Just a temporary variable since Java doesn't support ref or out keyword like C# does.
     */
    private String value;
    
    
    public ArgumentInfo(String name, ArgumentTypes type)
    {
        this.name = name;
        this.type = type;
    }
    
    /**
     * Returns the name of the argument.
     */
    public String GetName() { return name; }
    
    /**
     * Returns the expected type of the argument.
     */
    public ArgumentTypes GetType() { return type; }
    
    /**
     * Returns the parsed value from evaluating TryParse.
     */
    public String GetParsed()
    {
        String val = value;
        value = null;
        return val;
    }
    
    /**
     * Tries parsing the specified string value according to the desired argument type.
     * Returns a non-null string value which represents the error message if the parsing has failed.
     */
    public String TryParse(String value)
    {
        switch(type)
        {
        case String:
            this.value = value;
            return null;
        case Int:
            try
            {
                int val = Integer.parseInt(value);
                this.value = value;
                return null;
            }
            catch(Exception e) {}
            return "You must provide an integer value!";
        case Float:
            try
            {
                float val = Float.parseFloat(value);
                this.value = value;
                return null;
            }
            catch(Exception e) {}
            return "You must provide a float value!";
        case Boolean:
            if(value.equalsIgnoreCase("y") || value.equalsIgnoreCase("true"))
                this.value = "true";
            else if(value.equalsIgnoreCase("n") || value.equalsIgnoreCase("false"))
                this.value = "false";
            else
                return "You must provide a boolean (y/n) value!";
            return null;
        }
        // Unknown type!
        return "An unknown argument type was evaluated!";
    }
}
