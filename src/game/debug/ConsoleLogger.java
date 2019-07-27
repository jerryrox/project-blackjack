/*
 * Jerry Kim (18015036), 2019
 */
package game.debug;

/**
 * ILogger implementation which logs messages to the console.
 * @author jerrykim
 */
public class ConsoleLogger implements ILogger {

    public @Override void Log(LogLevels level, Object message)
    {
        System.out.println(String.format("%s - %s", String.valueOf(level), Stringify(message)));
    }

    public @Override void LogInfo(Object message)
    {
        Log(LogLevels.Info, message);
    }

    public @Override void LogInfoFormat(String format, Object... params)
    {
        Log(LogLevels.Info, String.format(format, params));
    }

    public @Override void LogWarning(Object message)
    {
        Log(LogLevels.Warning, message);
    }

    public @Override void LogWarningFormat(String format, Object... params)
    {
        Log(LogLevels.Warning, String.format(format, params));
    }

    public @Override void LogError(Object message)
    {
        Log(LogLevels.Error, message);
    }

    public @Override void LogErrorFormat(String format, Object... params)
    {
        Log(LogLevels.Error, String.format(format, params));
    }
    
    /**
     * Returns a null-safe string representation of specified object.
     * @param object
     */
    private String Stringify(Object object)
    {
        return object == null ? "null" : object.toString();
    }
}
