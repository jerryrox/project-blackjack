/*
 * Jerry Kim (18015036), 2019
 */
package game.debug;

/**
 * Provides a general signature of a logger implementation.
 * @author jerrykim
 */
public interface ILogger {
    
    /**
     * Outputs a log message at specified level.
     * @param level
     * @param message 
     */
    void Log(LogLevels level, Object message);
    
    /**
     * Outputs a Info level message.
     * @param message 
     */
    void LogInfo(Object message);
    
    /**
     * Outputs a Info level formatted message.
     * Does not support null-safe toString on params.
     * @param format
     * @param params 
     */
    void LogInfoFormat(String format, Object... params);
    
    /**
     * Outputs a Warning level message.
     * @param message 
     */
    void LogWarning(Object message);
    
    /**
     * Outputs a Warning level formatted message.
     * Does not support null-safe toString on params.
     * @param format
     * @param params 
     */
    void LogWarningFormat(String format, Object... params);
    
    /**
     * Outputs a Error level message.
     * @param messsage 
     */
    void LogError(Object message);
    
    /**
     * Outputs a Error level formatted message.
     * Does not support null-safe toString on params.
     * @param format
     * @param params 
     */
    void LogErrorFormat(String format, Object... params);
}
