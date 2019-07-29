/*
 * Jerry Kim (18015036), 2019
 */
package game.debug;

/**
 * Static debug logger service.
 * @author jerrykim
 */
public final class Debug {
    
    private static ILogger Logger;
    
    
    /**
     * Initializes the logger service with specified log method.
     * @param logger 
     */
    public static void Initialize(ILogger logger)
    {
        Logger = logger;
    }
    
    /**
     * Outputs a log message at specified level.
     * @param level
     * @param message 
     */
    public static void Log(LogLevels level, Object message)
    {
        if(Logger != null)
            Logger.Log(level, message);
    }
    
    /**
     * Outputs a Info level message.
     * @param message 
     */
    public static void LogInfo(Object message)
    {
        if(Logger != null)
            Logger.LogInfo(message);
    }
    
    /**
     * Outputs a Info level formatted message.
     * Does not support null-safe toString on params.
     * @param format
     * @param params 
     */
    public static void LogInfoFormat(String format, Object... params)
    {
        if(Logger != null)
            Logger.LogInfoFormat(format, params);
    }
    
    /**
     * Outputs a Warning level message.
     * @param message 
     */
    public static void LogWarning(Object message)
    {
        if(Logger != null)
            Logger.LogWarning(message);
    }
    
    /**
     * Outputs a Warning level formatted message.
     * Does not support null-safe toString on params.
     * @param format
     * @param params 
     */
    public static void LogWarningFormat(String format, Object... params)
    {
        if(Logger != null)
            Logger.LogWarningFormat(format, params);
    }
    
    /**
     * Outputs a Error level message.
     * @param messsage 
     */
    public static void LogError(Object message)
    {
        if(Logger != null)
            Logger.LogError(message);
    }
    
    /**
     * Outputs a Error level formatted message.
     * Does not support null-safe toString on params.
     * @param format
     * @param params 
     */
    public static void LogErrorFormat(String format, Object... params)
    {
        if(Logger != null)
            Logger.LogErrorFormat(format, params);
    }
}
